package com.ziyuan;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.lmax.disruptor.dsl.Disruptor;
import com.ziyuan.chain.ListenerChain;
import com.ziyuan.chain.ListenerChainBuilder;
import com.ziyuan.channel.Channel;
import com.ziyuan.events.Electron;
import com.ziyuan.events.ElectronsWrapper;
import com.ziyuan.events.ListenerCollectWrapper;

import java.util.List;
import java.util.Map;
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
    private AtomicBoolean started;

    /**
     * 通道
     */
    private Map<String, Channel> channelMap;

    /**
     * disruptor
     */
    private Disruptor<Electron> disruptor;

    /**
     * wrapper map
     */
    private ListMultimap<ElectronsWrapper, ListenerChain> chainMap;

    /**
     * 针对有after逻辑的特殊管道
     */
    private static final String SPEC_CHANNEL_PREFIX = "spec_channel:";

    private Config conf;

    public void start() {
        //根据config中channel的数量，初始化channels
        initChannel(conf);
    }

    public void stop() {

    }

    public Dispatcher(Map<ElectronsWrapper, ListenerCollectWrapper> wrapperMap, Config config) {
        chainMap = ArrayListMultimap.create();
        for (Map.Entry<ElectronsWrapper, ListenerCollectWrapper> entry : wrapperMap.entrySet()) {
            List<ListenerChain> cs = ListenerChainBuilder.build(entry.getValue());
            for (ListenerChain lc : cs) {
                chainMap.put(entry.getKey(), lc);
            }
        }
        this.conf = config;
    }

    private void initChannel(Config config) {

    }

    /**
     * 根据事件找到一个channel，这里用的是hashCode取余数，相同的事件会被放到相同的channel中处理
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
     * @param wrapper 事件的wrapper
     */
    public void dispatch(ElectronsWrapper wrapper) {
        List<ListenerChain> chains = chainMap.get(wrapper);
        //包装一下放到channel中
        Channel channel = selectOne(wrapper);
    }
}
