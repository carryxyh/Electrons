package com.ziyuan.events;

import com.ziyuan.ElectronsListener;

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
    private List<ElectronsListener> electronsListeners;

    /**
     * 是否存在有after逻辑
     */
    private boolean hasAfterLis;

    public List<ElectronsListener> getElectronsListeners() {
        return electronsListeners;
    }

    public void setElectronsListeners(List<ElectronsListener> electronsListeners) {
        this.electronsListeners = electronsListeners;
    }

    public boolean isHasAfterLis() {
        return hasAfterLis;
    }

    public void setHasAfterLis(boolean hasAfterLis) {
        this.hasAfterLis = hasAfterLis;
    }
}
