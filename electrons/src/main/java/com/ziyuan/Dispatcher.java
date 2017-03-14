package com.ziyuan;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.LiteBlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.ziyuan.chain.ListenerChain;
import com.ziyuan.channel.Channel;
import com.ziyuan.events.Electron;
import com.ziyuan.events.ElectronsWrapper;
import com.ziyuan.events.ListenerCollectWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

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
     * wrapper map
     */
    private ListMultimap<ElectronsWrapper, ListenerChain> chainMap;

    /**
     * 针对有after逻辑的特殊管道
     */
    private static final String SPEC_CHANNEL_PREFIX = "spec_channel:";

    /**
     * normal channel的key
     */
    private static final String NORMAL_CHANNEL_KEY = "normal_channel:";

    /**
     * config
     */
    private Config conf;

    /**
     * 线程池
     */
    private ExecutorService pool;

    /**
     * 正常通道的disruptor
     */
    private Disruptor<ElectronsHolder> normal;

    /**
     * 特殊通道的disruptor
     */
    private Disruptor<? extends Electron> spec;

    public void start() {
        if (started.get()) {
            return;
        }
        started.set(true);
        //根据config中channel的数量，初始化channels
        initChannel(conf);
    }

    public void stop() {

    }

    public Dispatcher(Map<ElectronsWrapper, ListenerCollectWrapper> wrapperMap, Config config) {
        chainMap = ArrayListMultimap.create();
        this.conf = config;
        for (Map.Entry<ElectronsWrapper, ListenerCollectWrapper> entry : wrapperMap.entrySet()) {

        }
        pool = Executors.newFixedThreadPool(config.getCircuitNum(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return null;
            }
        });
        normal = new Disruptor<ElectronsHolder>(new EventFactory<ElectronsHolder>() {
            @Override
            public ElectronsHolder newInstance() {
                return new ElectronsHolder();
            }
        }, config.getCircuitLen(), pool, ProducerType.MULTI, new LiteBlockingWaitStrategy());

    }

    private void initChannel(Config config) {
        for (Map.Entry<ElectronsWrapper, ListenerChain> entry : chainMap.entries()) {
            ListenerChain lc = entry.getValue();
            ElectronsWrapper ew = entry.getKey();
            if (lc.isHasBefore()) {
                //特殊处理的channel
                String key = SPEC_CHANNEL_PREFIX + ew.getTag();
                Channel c = channelMap.get(key);
                if (c == null) {

                } else {
                    //已经注册过channel了
                    continue;
                }
            } else {
                //正常的channel

            }

        }
    }

    /**
     * 根据事件找到一个channel
     *
     * @param wrapper wrapper
     * @return 根据wrapper选中的channel
     */
    private Channel selectOne(ElectronsWrapper wrapper) {
        return null;
    }

    /**
     * 分发
     *
     * @param electron 电子
     * @param tag      tag
     * @param sync     是否同步
     */
    public void dispatch(String tag, Electron electron, boolean sync) {
        ElectronsWrapper wrapper = new ElectronsWrapper(tag, electron.getClass());
        wrapper.setSync(sync);
        List<ListenerChain> chains = chainMap.get(wrapper);
        //包装一下放到channel中
        Channel channel = selectOne(wrapper);
    }
}
