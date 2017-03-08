package com.ziyuan;

import com.lmax.disruptor.dsl.Disruptor;
import com.ziyuan.channel.Channel;
import com.ziyuan.events.Electron;

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
    private Channel channel;

    /**
     * disruptor
     */
    private Disruptor<Electron> disruptor;

    public void dispatch() {

    }
}
