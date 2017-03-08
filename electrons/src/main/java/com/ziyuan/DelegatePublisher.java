package com.ziyuan;

import com.ziyuan.events.Electron;

/**
 * DelegatePublisher
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public interface DelegatePublisher {

    /**
     * 代理发送
     *
     * @param tag
     * @param electron
     */
    void publish(String tag, Electron electron);
}
