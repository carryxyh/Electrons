package com.ziyuan.channel;

import com.ziyuan.events.Electron;

/**
 * AbstractChannel
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public abstract class AbstractChannel implements Channel {

    public abstract void publish(String source, Electron electron);

}
