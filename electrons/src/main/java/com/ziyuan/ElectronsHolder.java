package com.ziyuan;

import com.ziyuan.events.Electron;

import java.util.List;

/**
 * ElectronsHolder 真正放在容器中的类，封装了electron和要执行他的监听器
 *
 * @author ziyuan
 * @since 2017-03-10
 */
public final class ElectronsHolder {

    private Electron electron;

    private List<ElectronsListener> listeners;

    public ElectronsHolder(Electron electron, List<ElectronsListener> listeners) {
        this.electron = electron;
        this.listeners = listeners;
    }
}
