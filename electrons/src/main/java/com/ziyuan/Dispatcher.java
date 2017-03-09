package com.ziyuan;

import com.lmax.disruptor.dsl.Disruptor;
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
    private List<Channel> channel;

    /**
     * disruptor
     */
    private Disruptor<Electron> disruptor;

    /**
     * wrapper map
     */
    private Map<ElectronsWrapper, ListenerCollectWrapper> wrapperMap;


    public void start() {

    }

    public void stop() {

    }

    public Dispatcher(Map<ElectronsWrapper, ListenerCollectWrapper> wrapperMap) {
        this.wrapperMap = wrapperMap;
    }

    public void dispatch() {
        //包装一下放到channel中
        Channel channel = selectOne();
    }

    private Channel selectOne() {
        return null;
    }

    public void dispatch(ElectronsWrapper wrapper) {

    }
}
