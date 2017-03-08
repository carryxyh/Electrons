package com.ziyuan.events;

import java.util.EventObject;

/**
 * Electron 电子，相当于事件。
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public abstract class Electron extends EventObject {

    /**
     * 发生的时间戳
     */
    private long timestamp;

    /**
     * 该事件的权重，在限流中起作用，默认为1
     */
    private int weight = 1;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public Electron(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }

    public final long getTimestamp() {
        return this.timestamp;
    }

    public final int getWeight() {
        return weight;
    }

    protected final void setWeight(int weight) {
        this.weight = weight;
    }
}
