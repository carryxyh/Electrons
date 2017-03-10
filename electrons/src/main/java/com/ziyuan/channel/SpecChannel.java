package com.ziyuan.channel;

import com.lmax.disruptor.RingBuffer;
import com.ziyuan.chain.ListenerChain;
import com.ziyuan.events.Electron;

import java.util.Collection;

/**
 * SpecChannel 特殊管道，用于处理after逻辑
 *
 * @author ziyuan
 * @since 2017-03-10
 */
public class SpecChannel extends AbstractChannel {

    protected SpecChannel(RingBuffer<Electron> ringBuffer) {
        super(ringBuffer);
    }

    @Override
    public void open(Collection<ListenerChain> chains) {

    }

    @Override
    public void close() {

    }

    @Override
    public void regist() {

    }

    @Override
    public void handle() {

    }

    @Override
    public void publish(String source, Electron electron) {

    }
}
