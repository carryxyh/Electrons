package com.ziyuan.channel;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.ziyuan.ElectronsHolder;
import com.ziyuan.chain.ListenerChain;
import com.ziyuan.events.Electron;

import java.util.Collection;

/**
 * NormalChannel
 *
 * @author ziyuan
 * @since 2017-03-10
 */
public class NormalChannel extends AbstractChannel {

    /**
     * disruptor
     */
    private Disruptor<ElectronsHolder> disruptor;

    /**
     * 正常逻辑的电路
     */
    private RingBuffer<ElectronsHolder> circuit;

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
    public void publish(String tag, Electron electron) {

    }

    @Override
    public void handle() {

    }
}
