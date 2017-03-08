package com.ziyuan;

import com.ziyuan.channel.Channel;

import java.util.List;

/**
 * Dispatcher 分发器，用来把任务放到各个channel中
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public final class Dispatcher {

    private List<Channel> channels;

    public void dispatch() {

    }

    private Channel selectOne() {
        return null;
    }
}
