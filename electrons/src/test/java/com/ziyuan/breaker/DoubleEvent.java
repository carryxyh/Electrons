package com.ziyuan.breaker;

import com.ziyuan.events.Electron;

/**
 * DoubleEvent
 *
 * @author ziyuan
 * @since 2017-03-23
 */
public class DoubleEvent extends Electron {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public DoubleEvent(Object source) {
        super(source);
    }
}
