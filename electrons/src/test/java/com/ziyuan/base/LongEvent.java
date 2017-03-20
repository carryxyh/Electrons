package com.ziyuan.base;

import com.ziyuan.events.Electron;
import lombok.Getter;

/**
 * LongEvent
 *
 * @author ziyuan
 * @since 2017-03-20
 */
public class LongEvent extends Electron {

    @Getter
    private String name;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public LongEvent(Object source) {
        super(source);
        this.name = "LongEvent" + source;
    }
}
