package com.ziyuan.ex;

import com.ziyuan.events.Electron;

/**
 * EleEvent
 *
 * @author ziyuan
 * @since 2017-08-08
 */
public class EleEvent extends Electron {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public EleEvent(Object source) {
        super(source);
    }
}
