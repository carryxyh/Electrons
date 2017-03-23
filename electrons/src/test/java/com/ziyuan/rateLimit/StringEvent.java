package com.ziyuan.rateLimit;

import com.ziyuan.events.Electron;

/**
 * StringEvent
 *
 * @author ziyuan
 * @since 2017-03-23
 */
public class StringEvent extends Electron {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public StringEvent(Object source) {
        super(source);
    }
}
