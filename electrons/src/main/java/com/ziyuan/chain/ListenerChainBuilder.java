package com.ziyuan.chain;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
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
         * 第一步，这里把只有id的放到map中，把没有id和没有after的直接运行掉
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
            } else if (idOnly(id, after)) {
                //有Id没有after的
                chainMap.put(id, new ListenerChain(listener));
                iterator.remove();
            }
        }
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

    private final static class ListenerChain {

        @Getter
        private String id;

        /**
         * 该listener之后执行的listener集合
         */
        @Getter
        private Set<ElectronsListener> afters = new HashSet<>();

        @Getter
        private ElectronsListener listener;

        private ListenerChain(ElectronsListener lis) {
            this.listener = lis;
            Listener ann = lis.getClass().getAnnotation(Listener.class);
            this.id = ann.id();
        }

        public void addAfter(ElectronsListener listener) {
            this.afters.add(listener);
        }

        public static Map<String, ListenerChain> buildMap(List<ElectronsListener> electronsListeners) {
            List<ListenerChain> chains = new ArrayList<>();
            for (ElectronsListener listener : electronsListeners) {
                chains.add(new ListenerChain(listener));
            }

            return Maps.uniqueIndex(chains, new Function<ListenerChain, String>() {
                @Override
                public String apply(ListenerChain c) {
                    return c.getId();
                }
            });
        }
    }
}
