package com.ziyuan;

import com.ziyuan.events.Electron;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * ElectronsHolder 真正放在容器中的类，封装了electron和要执行他的监听器
 * 这个类将来会重新抽象：
 * 父类包含：
 * electron
 * <p>
 * 子类包含：
 * listeners
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
    public boolean handle() throws Exception {
        if (CollectionUtils.isEmpty(listeners)) {
            return false;
        }
        for (ElectronsListener listener : listeners) {
            listener.onEvent(electron);
        }
        return true;
    }
}
