package com.ziyuan.chain;

import com.lmax.disruptor.dsl.Disruptor;
import com.ziyuan.ElectronsHolder;
import com.ziyuan.ElectronsListener;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
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

    /**
     * 构建监听器链
     *
     * @param disruptor          disruptor
     * @param electronsListeners listeners
     */
    public final static void buildChain(Disruptor<ElectronsHolder> disruptor, List<ElectronsListener> electronsListeners) {
        if (CollectionUtils.isEmpty(electronsListeners)) {
            return;
        }

        /**
         * 这是只有Id 或者 没有Id也没有after的监听器，所以可以一开始就直接handle
         */
        List<ElectronsListener> inits = new ArrayList<>();


        /**
         * 这里是只有after的，这种只能作为监听器的最后一个部分
         */
        List<ElectronsListener> last = new ArrayList<>();
        for (ElectronsListener listener : electronsListeners) {

        }
    }
}
