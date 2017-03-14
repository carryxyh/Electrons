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

    /**
     * 电子
     */
    private Electron electron;

    /**
     * 要执行的监听器，这里已经排好序了
     */
    private List<ElectronsListener> listeners;

    public Electron getElectron() {
        return electron;
    }

    public void setElectron(Electron electron) {
        this.electron = electron;
    }

    public List<ElectronsListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<ElectronsListener> listeners) {
        this.listeners = listeners;
    }

    /**
     * 处理
     *
     * @throws Exception 处理过程中的异常，同样抛出去
     */
    public void handle() throws Exception {
        for (ElectronsListener listener : listeners) {
            listener.onEvent(electron);
        }
    }
}
