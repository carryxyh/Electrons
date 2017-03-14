package com.ziyuan;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.LiteBlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.ziyuan.channel.Channel;
import com.ziyuan.channel.NormalChannel;
import com.ziyuan.events.Electron;
import com.ziyuan.events.ElectronsWrapper;
import com.ziyuan.events.ListenerCollectWrapper;
import com.ziyuan.exceptions.ElecExceptionHandler;
import com.ziyuan.exceptions.OpNotSupportException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * 通道
     */
    private Map<String, Channel> channelMap = new HashMap<>();

    /**
     * 针对有after逻辑的特殊管道
     */
    private static final String SPEC_CHANNEL_PREFIX = "spec_channel:";

    /**
     * normal channel的key
     */
    private static final String NORMAL_CHANNEL_KEY = "normal_channel";

    /**
     * config
     */
    private Config conf;

    /**
     * 线程池
     */
    private ExecutorService pool;

    /**
     * key 事件的包装类  value 该事件的监听器包装类
     */
    private Map<ElectronsWrapper, ListenerCollectWrapper> wrapperMap;

    /**
     * 正常通道的disruptor
     */
    private Disruptor<ElectronsHolder> normalDis;

    /**
     * 特殊disruptor集合
     */
    private List<Disruptor<ElectronsHolder>> specDises;

    /**
     * 启动分发器
     */
    public synchronized void start() {
        if (started.get()) {
            return;
        }
        started.set(true);
    }

    /**
     * 停止分发器
     */
    public synchronized void stop() {
        channelMap.clear();
        normalDis.shutdown();
        wrapperMap.clear();
    }

    public Dispatcher(Map<ElectronsWrapper, ListenerCollectWrapper> wrapperMap, Config config) {
        this.conf = config;
        this.wrapperMap = wrapperMap;
        //初始化pool
        pool = Executors.newFixedThreadPool(conf.getCircuitNum(), new ThreadFactory() {

            final AtomicInteger cursor = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Electrons Thread : thread" + cursor.incrementAndGet());
            }
        });
        initNormalChannel();
    }

    /**
     * 初始化正常管道，任何情况下都会有
     */
    private void initNormalChannel() {
        normalDis = new Disruptor<ElectronsHolder>(new EventFactory<ElectronsHolder>() {

            @Override
            public ElectronsHolder newInstance() {
                return new ElectronsHolder();
            }
        }, conf.getCircuitLen(), pool, ProducerType.MULTI, new LiteBlockingWaitStrategy());
        normalDis.handleExceptionsWith(new ElecExceptionHandler("Normal Disruptor"));
        Channel normalChannel = new NormalChannel();
        channelMap.put(NORMAL_CHANNEL_KEY, normalChannel);
    }

    /**
     * 根据事件找到一个channel
     *
     * @return 根据wrapper选中的channel
     */
    private Channel selectOne(String tag, String sourceType) {
        StringBuilder builder = new StringBuilder();
        builder.append(SPEC_CHANNEL_PREFIX).append(tag).append("-").append(sourceType);
        return channelMap.get(builder);
    }

    /**
     * 分发
     *
     * @param electron 电子
     * @param tag      tag
     * @param sync     是否同步
     */
    public void dispatch(String tag, Electron electron, boolean sync) throws Exception {
        ElectronsWrapper wrapper = new ElectronsWrapper(tag, electron.getClass());
        ListenerCollectWrapper lisWrapper = wrapperMap.get(wrapper);
        boolean hasAfterLis = lisWrapper.isHasAfter();
        if (sync && hasAfterLis) {
            //如果同步并且还有after逻辑直接抛出异常
            throw new OpNotSupportException();
        }

        if (sync) {
            //同步
        }
    }
}
