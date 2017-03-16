package com.ziyuan.chain;

import com.lmax.disruptor.dsl.Disruptor;
import com.ziyuan.ElectronsHolder;
import com.ziyuan.ElectronsListener;

import java.util.List;

/**
 * ListenerChainBuilder 构造listener链的辅助类
 *
 * @author ziyuan
 * @since 2017-03-09
 */
public final class ListenerChainBuilder {

    /**
     * 不允许初始化
     */
    private ListenerChainBuilder() {
    }

    public synchronized static void buildChain(Disruptor<ElectronsHolder> disruptor, List<ElectronsListener> electronsListeners) {

    }

}
