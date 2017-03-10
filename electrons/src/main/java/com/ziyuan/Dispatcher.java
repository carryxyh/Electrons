package com.ziyuan;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.ziyuan.chain.ListenerChain;
import com.ziyuan.chain.ListenerChainBuilder;
import com.ziyuan.channel.Channel;
import com.ziyuan.events.ElectronsWrapper;
import com.ziyuan.events.ListenerCollectWrapper;

import java.util.HashMap;
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

    private Config conf;

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
        for (Map.Entry<ElectronsWrapper, ListenerCollectWrapper> entry : wrapperMap.entrySet()) {
            List<ListenerChain> cs = ListenerChainBuilder.build(entry.getValue());
            for (ListenerChain lc : cs) {
                chainMap.put(entry.getKey(), lc);
            }
        }
        this.conf = config;
    }

    private void initChannel(Config config) {
        for (Map.Entry<ElectronsWrapper, ListenerChain> entry : chainMap.entries()) {
            ListenerChain lc = entry.getValue();
            ElectronsWrapper ew = entry.getKey();
            if (lc.isHasBefore()) {
                //特殊处理的channel
                String key = SPEC_CHANNEL_PREFIX + ew.toString();
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
