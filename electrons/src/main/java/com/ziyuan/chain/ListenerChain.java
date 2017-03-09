package com.ziyuan.chain;

import com.ziyuan.ElectronsListener;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ListenerChain
 *
 * @author ziyuan
 * @since 2017-03-09
 */
public final class ListenerChain {

    /**
     * 自己
     */
    private ElectronsListener me;

    /**
     * 在这个节点之后执行的listener的集合
     */
    private List<ElectronsListener> befores;

    /**
     * 是否有后置节点
     */
    private boolean hasBefore;

    public ListenerChain(ElectronsListener me) {
        this.me = me;
    }

    public void addBefore(ElectronsListener before) {
        if (before == null) {
            return;
        }
        this.hasBefore = true;
        if (CollectionUtils.isEmpty(befores)) {
            befores = new ArrayList<>();
        }
        befores.add(before);
    }
}
