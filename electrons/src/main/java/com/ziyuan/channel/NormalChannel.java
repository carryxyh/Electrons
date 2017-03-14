package com.ziyuan.channel;

import com.ziyuan.events.Electron;

/**
 * NormalChannel
 *
 * @author ziyuan
 * @since 2017-03-10
 */
public class NormalChannel extends AbstractChannel {

    @Override
    public boolean publish(String tag, Electron electron) throws Exception {
        super.publish(tag, electron);
        return true;
    }

    @Override
    public void handle() {

    }
}
