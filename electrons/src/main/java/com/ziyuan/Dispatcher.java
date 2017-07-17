package com.ziyuan;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.LiteBlockingWaitStrategy;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.ziyuan.chain.ListenerChainBuilderNew;
import com.ziyuan.channel.Channel;
import com.ziyuan.channel.NormalChannel;
import com.ziyuan.channel.SpecChannel;
import com.ziyuan.events.Electron;
import com.ziyuan.events.ElectronsWrapper;
import com.ziyuan.events.HomelessEle;
import com.ziyuan.events.ListenerCollectWrapper;
import com.ziyuan.exceptions.ElecExceptionHandler;
import com.ziyuan.exceptions.OpNotSupportException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.EventCountCircuitBreaker;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Dispatcher 分发器，用来把任务放到各个channel中
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public final class Dispatcher {

    /**
     * 是否已经开始了
     */
    private AtomicBoolean started = new AtomicBoolean(false);

    /**
     * 通道 特殊通道的键：前缀:tag-class.simpleName
     */
    private Map<String, Channel> channelMap = new HashMap<>();

    /**
     * 针对有after逻辑的特殊管道
     */
    private static final String SPEC_CHANNEL_PREFIX = "spec_channel";

    /**
     * normal channel的key
     */
    private static final String NORMAL_CHANNEL_KEY = "normal_channel";

    /**
     * config
     */
    private Config conf;

    /**
     * key 事件的包装类  value 该事件的监听器包装类
     */
    private Map<ElectronsWrapper, ListenerCollectWrapper> wrapperMap;

    /**
     * 线程池，由于disruptor不会停止线程池，所以需要我们保留，并且在关闭的时候关掉
     */
    private List<ExecutorService> pools = new ArrayList<>();

    /**
     * 启动分发器
     */
    public synchronized void start() {
        if (started.get()) {
            return;
        }
        started.set(true);
        if (channelMap.size() != 0) {
            for (Channel c : channelMap.values()) {
                c.open();
            }
        }
    }

    /**
     * 停止分发器
     */
    public synchronized void stop() {
        if (!started.get()) {
            return;
        }
        started.set(false);
        if (channelMap.size() != 0) {
            for (Channel c : channelMap.values()) {
                c.close();
            }
            channelMap.clear();
        }
        if (CollectionUtils.isNotEmpty(pools)) {
            for (ExecutorService p : pools) {
                p.shutdown();
            }
            pools.clear();
        }
        wrapperMap.clear();
    }

    public Dispatcher(Map<ElectronsWrapper, ListenerCollectWrapper> wrapperMap, Config config) {
        this.conf = config;
        if (wrapperMap == null || wrapperMap.size() == 0) {
            throw new NullPointerException("WrapperMap can not be null or contains nothing !");
        }
        this.wrapperMap = wrapperMap;
        //初始化pool
        ExecutorService pool = Executors.newFixedThreadPool(conf.getCircuitNum(), new ThreadFactory() {

            final AtomicInteger cursor = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Electrons Thread : thread" + cursor.incrementAndGet());
            }
        });
        this.pools.add(pool);
        initNormalChannel(pool);
        initSpecChannel(wrapperMap.entrySet());
    }

    /**
     * 初始化特殊通道
     *
     * @param entries 事件和监听器集合
     */
    private void initSpecChannel(Set<Map.Entry<ElectronsWrapper, ListenerCollectWrapper>> entries) {
        for (Map.Entry<ElectronsWrapper, ListenerCollectWrapper> entry : entries) {
            ListenerCollectWrapper lisWrapper = entry.getValue();
            if (lisWrapper.isHasAfter()) {
                //走特殊通道，初始化特殊通道的Disruptor
                ElectronsWrapper eleWrapper = entry.getKey();
                initSpecDisruptor(eleWrapper.getSymbol(), lisWrapper.getElectronsListeners());
            } else {
                //走普通通道
                continue;
            }
        }
    }

    /**
     * 根据config初始化特殊通道
     *
     * @param symbol    事件
     * @param listeners 对应的监听器集合
     */
    private void initSpecDisruptor(String symbol, List<ElectronsListener> listeners) {
        ExecutorService specPool = Executors.newFixedThreadPool(conf.getSpecCircuitNum(), new ThreadFactory() {

            final AtomicInteger cursor = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Electrons Thread (from spec channel) : thread" + cursor.incrementAndGet());
            }
        });
        pools.add(specPool);

        Disruptor<ElectronsHolder> disruptor = new Disruptor<>(new EventFactory<ElectronsHolder>() {

            @Override
            public ElectronsHolder newInstance() {
                return new ElectronsHolder();
            }
        }, conf.getSpecCircuitLen(), specPool, ProducerType.MULTI, new LiteBlockingWaitStrategy());
        disruptor.handleExceptionsWith(new ElecExceptionHandler("Spec Disruptor {" + symbol + "}"));

        //初始化管道并放入集合中
        SpecChannel specChannel = new SpecChannel(disruptor);
        if (conf.isBreaker()) {
            EventCountCircuitBreaker breaker = new EventCountCircuitBreaker(conf.getErrorNum(), conf.getPerUnit(), conf.getUnit(), conf.getCloseThreshold(), conf.getRest(), conf.getRestUnit());
            specChannel.setBreaker(breaker);
        }

        //构建listener顺序
        ListenerChainBuilderNew.buildChain(specChannel, listeners);

        channelMap.put(SPEC_CHANNEL_PREFIX + symbol, specChannel);
    }

    /**
     * 初始化正常管道，任何情况下都会有
     *
     * @param pool 线程池
     */
    private void initNormalChannel(ExecutorService pool) {
        Disruptor<ElectronsHolder> normalDis = new Disruptor<>(new EventFactory<ElectronsHolder>() {

            @Override
            public ElectronsHolder newInstance() {
                return new ElectronsHolder();
            }
        }, conf.getCircuitLen(), pool, ProducerType.MULTI, new LiteBlockingWaitStrategy());
        WorkHandler[] workHandlers = new WorkHandler[conf.getCircuitNum()];
        Arrays.fill(workHandlers, new WorkHandler<ElectronsHolder>() {
            @Override
            public void onEvent(ElectronsHolder electronsHolder) throws Exception {
                electronsHolder.handle();
            }
        });
        normalDis.handleEventsWithWorkerPool(workHandlers);
        normalDis.handleExceptionsWith(new ElecExceptionHandler("Normal Disruptor"));

        //初始化channel
        Channel normalChannel = new NormalChannel(normalDis);
        //配置限流相关
        normalChannel.confLimitRate(conf.isLimitRate(), conf.getPermitsPerSecond(), conf.isWarmup(), conf.getWarmupPeriod(), conf.getWarmPeriodUnit());
        channelMap.put(NORMAL_CHANNEL_KEY, normalChannel);
    }

    /**
     * 根据事件找到一个channel
     *
     * @return 根据wrapper选中的channel
     */
    private Channel selectOne(String tag, Electron electron, boolean hasAfter) {
        if (StringUtils.isBlank(tag) || electron == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        if (hasAfter) {
            builder.append(SPEC_CHANNEL_PREFIX).append(tag).append("-").append(electron.getClass().getSimpleName());
            //特殊管道
        } else {
            builder.append(NORMAL_CHANNEL_KEY);
        }
        return channelMap.get(builder.toString());
    }

    /**
     * 分发
     *
     * @param electron 电子
     * @param tag      tag
     * @param sync     是否同步
     */
    public boolean dispatch(String tag, Electron electron, boolean sync) throws Exception {
        ElectronsWrapper wrapper = new ElectronsWrapper(tag, electron.getClass());
        ListenerCollectWrapper lisWrapper = wrapperMap.get(wrapper);

        //没有找到监听器集合，并且不是HomelessEle，用homelessEle发一次
        if (lisWrapper == null) {
            if (electron.getClass().isAssignableFrom(HomelessEle.class)) {
                //homelessEle还没找到监听器，return false
                return false;
            } else {
                HomelessEle homelessEle = new HomelessEle(electron.getSource());
                return dispatch(tag, homelessEle, true);
            }
        }

        boolean hasAfterLis = lisWrapper.isHasAfter();
        if (sync && hasAfterLis) {
            //如果同步并且还有after逻辑直接抛出异常
            throw new OpNotSupportException();
        }

        //找对应的channel，找不到对应的channel返回false
        Channel channel = selectOne(tag, electron, hasAfterLis);
        if (channel == null) {
            return false;
        }

        ElectronsHolder holder = new ElectronsHolder();
        holder.setElectron(electron);
        holder.setListeners(lisWrapper.getElectronsListeners());
        if (sync) {
            //同步
            return channel.handle(holder);
        } else {
            return channel.publish(holder);
        }
    }
}
