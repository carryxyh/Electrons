package com.ziyuan.events;

import com.ziyuan.ElectronsListener;

import java.util.ArrayList;
import java.util.List;

/**
 * ListenerCollectWrapper
 *
 * @author ziyuan
 * @since 2017-03-09
 */
public class ListenerCollectWrapper {

    /**
     * 监听器集合
     */
    private List<ElectronsListener> electronsListeners = new ArrayList<>();

    /**
     * 是否存在有after逻辑
     */
    private boolean hasAfter;

    public void addListener(ElectronsListener listener) {
        this.electronsListeners.add(listener);
    }

    public List<ElectronsListener> getElectronsListeners() {
        return electronsListeners;
    }

    public void setElectronsListeners(List<ElectronsListener> electronsListeners) {
        this.electronsListeners = electronsListeners;
    }

    public boolean isHasAfter() {
        return hasAfter;
    }

    public void setHasAfter(boolean hasAfter) {
        this.hasAfter = hasAfter;
    }
}
