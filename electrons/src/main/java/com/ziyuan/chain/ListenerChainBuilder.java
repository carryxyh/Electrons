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

import java.io.Serializable;
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
        if (CollectionUtils.isEmpty(electronsListeners) || disruptor == null) {
            return;
        }

        /**
         * 第一步，把没有id和没有after的直接运行掉，把有id的放到map中，只有after的继续放在list中
         */
        Map<String, ListenerChain> chainMap = new HashMap<>();
        Iterator<ElectronsListener> iterator = electronsListeners.iterator();
        while (iterator.hasNext()) {
            ElectronsListener listener = iterator.next();
            Listener ann = listener.getClass().getAnnotation(Listener.class);
            String id = ann.id();
            String after = ann.after();
            if (none(id, after)) {
                //没有id没有after,直接handle就行了
                disruptor.handleEventsWith(new ProxyHandler(listener));
                iterator.remove();
            } else if (afterOnly(id, after)) {
                //只有after的 continue
                continue;
            } else {
                chainMap.put(id, new ListenerChain(listener));
                iterator.remove();
            }
        }

        /**
         * 第二步，现在只剩下只有after的，把这些加入到map中有id的节点的后面，清理掉这个集合
         */
        for (ElectronsListener listener : electronsListeners) {
            Listener ann = listener.getClass().getAnnotation(Listener.class);
            String after = ann.after();
            String[] ids = after.split(",");
            for (String id : ids) {
                ListenerChain chain = chainMap.get(id);
                if (chain == null) {
                    continue;
                }
                chain.addAfter(listener);
            }
        }
        electronsListeners.clear();

        /**
         * 第三步，处理map中的元素，一个n方的循环，构建完成链
         */
        Map<String, ListenerChain> finalMap = chainMap;
        for (Map.Entry<String, ListenerChain> entry : chainMap.entrySet()) {
            ListenerChain c = entry.getValue();
            ElectronsListener listener = c.getListener();
            Listener ann = listener.getClass().getAnnotation(Listener.class);
            String after = ann.after();
            String[] ids = after.split(",");
            for (String id : ids) {
                ListenerChain chain = finalMap.get(id);
                if (chain == null) {
                    continue;
                }
                chain.addAfter(c);
            }
        }
        chainMap.clear();

        /**
         * 最后一步，在map中找到只有id的，然后把disruptor传进去构建
         */
        for (Map.Entry<String, ListenerChain> entry : finalMap.entrySet()) {
            ListenerChain chain = entry.getValue();
            ElectronsListener listener = chain.getListener();
            Listener ann = listener.getClass().getAnnotation(Listener.class);
            if (idOnly(ann.id(), ann.after())) {
                //如果只有id 则处理disruptor，否则不处理
                disruptor.handleEventsWith(chain.getProxyHandler());
                finalDeal(chain, disruptor);
            }
        }
    }

    /**
     * 最终构建链
     *
     * @param chain
     * @param disruptor
     */
    private static void finalDeal(ListenerChain chain, Disruptor disruptor) {
        Set<ListenerChain> set = chain.getAfters();
        if (CollectionUtils.isEmpty(set)) {
            return;
        }
        for (ListenerChain ch : set) {
            disruptor.after(chain.getProxyHandler()).handleEventsWith(ch.getProxyHandler());
            finalDeal(ch, disruptor);
        }
    }

    /**
     * 只有after
     *
     * @param id
     * @param after
     * @return
     */
    private static boolean afterOnly(String id, String after) {
        return StringUtils.isBlank(id) && StringUtils.isNotBlank(after);
    }

    /**
     * id after都没有
     *
     * @param id
     * @param after
     * @return
     */
    private static boolean none(String id, String after) {
        return StringUtils.isBlank(id) && StringUtils.isBlank(after);
    }

    /**
     * 只有id
     *
     * @param id
     * @param after
     * @return
     */
    private static boolean idOnly(String id, String after) {
        return StringUtils.isNotBlank(id) && StringUtils.isBlank(after);
    }

    /**
     * 监听器的代理类
     */
    private static class ProxyHandler implements EventHandler {

        private ElectronsListener listener;

        public ProxyHandler(ElectronsListener listener) {
            this.listener = listener;
        }

        @Override
        public void onEvent(Object o, long l, boolean b) throws Exception {
            listener.onEvent((Electron) o);
        }
    }

    private final static class ListenerChain implements Serializable {

        @Getter
        private String id;

        /**
         * 该listener之后执行的listener集合
         */
        @Getter
        private Set<ListenerChain> afters = new HashSet<>();

        @Getter
        private ElectronsListener listener;

        @Getter
        private ProxyHandler proxyHandler;

        private ListenerChain(ElectronsListener lis) {
            this.listener = lis;
            Listener ann = lis.getClass().getAnnotation(Listener.class);
            this.id = ann.id();
            this.proxyHandler = new ProxyHandler(lis);
        }

        public void addAfter(ElectronsListener listener) {
            if (listener == null) {
                return;
            }
            this.afters.add(new ListenerChain(listener));
        }

        public void addAfter(ListenerChain chain) {
            if (listener == null) {
                return;
            }
            this.afters.add(chain);
        }
    }
}
