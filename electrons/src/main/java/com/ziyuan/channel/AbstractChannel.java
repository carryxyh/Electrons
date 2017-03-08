package com.ziyuan.channel;

import com.lmax.disruptor.RingBuffer;
import com.ziyuan.events.Electron;

/**
 * AbstractChannel
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public abstract class AbstractChannel implements Channel {

    protected AbstractChannel(RingBuffer<Electron> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public abstract void publish(String source, Electron electron);

    /**
     * 容器
     */
    private final RingBuffer<Electron> ringBuffer;
}
