package com.ziyuan.chain;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.ziyuan.ElectronsHolder;
import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;
import com.ziyuan.events.Electron;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

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
         * 中间又有after 又有Id的 最难处理的部分
         */
        Map<String, ElectronsListener> between = new HashMap<>();

        /**
         * 这里是只有after的，这种只能作为监听器的最后一个部分
         */
        List<ElectronsListener> last = new ArrayList<>();
        for (ElectronsListener listener : electronsListeners) {
            Listener ann = listener.getClass().getAnnotation(Listener.class);
            String id = ann.id();
            String after = ann.after();
            if ((StringUtils.isBlank(id) && StringUtils.isBlank(after)) || (StringUtils.isNotBlank(id) && StringUtils.isBlank(after))) {
                inits.add(listener);
            } else if (StringUtils.isNotBlank(after) && StringUtils.isBlank(id)) {
                last.add(listener);
            } else {
                between.put(id, listener);
            }
        }
    }

    /**
     * 实际上处理链的方法
     *
     * @param inits     开始的
     * @param between   中间的 k:id v:listener
     * @param last      最后的
     * @param disruptor disruptor
     */
    private void actualDeal(List<ElectronsListener> inits, Map<String, ElectronsListener> between, List<ElectronsListener> last, Disruptor disruptor) {

        ProxyHandler[] proxyHandlers = new ProxyHandler[inits.size()];
        for (int i = 0; i < inits.size(); i++) {
            proxyHandlers[i] = new ProxyHandler(inits.get(i));
        }
        disruptor.handleEventsWith(proxyHandlers);
        /*-------------第一部分结束-------------*/


    }

    private class ProxyHandler implements EventHandler {

        private ElectronsListener listener;

        public ProxyHandler(ElectronsListener listener) {
            this.listener = listener;
        }

        @Override
        public void onEvent(Object o, long l, boolean b) throws Exception {
            listener.onEvent((Electron) o);
        }
    }

    private static class ListenerChain {

        @Getter
        private String id;

        @Getter
        private Set<String> afters = new HashSet<>();

        @Getter
        private ElectronsListener listener;

        private ListenerChain(ElectronsListener lis) {
            this.listener = lis;
            Listener ann = lis.getClass().getAnnotation(Listener.class);
            this.id = ann.id();
            String[] afts = ann.after().split(",");
            for (String aft : afts) {
                this.afters.add(aft);
            }
        }
    }
}
