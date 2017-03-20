package com.ziyuan.after;

import com.ziyuan.events.Electron;
import lombok.Getter;

/**
 * IntEvent
 *
 * @author ziyuan
 * @since 2017-03-20
 */
public class IntEvent extends Electron {

    @Getter
    private String name;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public IntEvent(Object source) {
        super(source);
        this.name = "event " + source;
    }
}
