package com.ziyuan;

import com.ziyuan.events.Electron;

/**
 * ElectronsListener 电子监听器
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public interface ElectronsListener<E extends Electron> {

    /**
     * 处理事件
     *
     * @param electrons 电子
     */
    void onEvent(E electrons);
}
