package com.ziyuan.chain;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.ziyuan.ElectronsHolder;
import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * ListenerChainBuilderNew
 *
 * @author ziyuan
 * @since 2017-03-20
 */
public class ListenerChainBuilderNew {

    public final static void buildChain(Disruptor<ElectronsHolder> disruptor, List<ElectronsListener> electronsListeners) {
        if (CollectionUtils.isEmpty(electronsListeners) || disruptor == null) {
            return;
        }

        List<ListenerChain> chains = new ArrayList<>();
        for (ElectronsListener listener : electronsListeners) {
            chains.add(new ListenerChain(listener));
        }

        /**
         * 第0步，把所有没有id没有after的执行掉
         */
        Iterator<ListenerChain> iterator = chains.iterator();
        while (iterator.hasNext()) {
            ListenerChain chain = iterator.next();
            String id = chain.getId();
            String after = chain.getAfter();
            if (none(id, after)) {
                disruptor.handleEventsWith(chain.getProxyHandler());
                iterator.remove();
            } else if (idOnly(id, after)) {
                disruptor.handleEventsWith(chain.getProxyHandler());
            }
        }

        /**
         * 没了，直接返回
         */
        if (CollectionUtils.isEmpty(chains)) {
            return;
        }

        /**
         * 第一步，list转map k id v listener，然后把所有的只有after的找出来，递归的把所有的节点都跟只有after的关联起来
         */
        Map<String, ListenerChain> listenerMap = new HashMap<>();
        for (ListenerChain chain : chains) {
            String id = chain.getId();
            if (StringUtils.isBlank(id)) {
                continue;
            }
            listenerMap.put(id, chain);
        }

        /**
         * 这里递归处理，所有的尾节点已经处理完毕了
         */
        List<ListenerChain> tails = new ArrayList<>();
        for (ListenerChain chain : chains) {
            String id = chain.getId();
            String after = chain.getAfter();
            if (afterOnly(id, after)) {
                addBeforeFromTail(chain, listenerMap);
                tails.add(chain);
            }
        }

        /**
         * 第二步，根据尾节点再递归的调用，构建disruptor
         */
        for (ListenerChain chain : tails) {
            dealWithDisruptorFromTail(chain, disruptor);
        }
    }

    private static void dealWithDisruptorFromTail(ListenerChain chain, Disruptor<ElectronsHolder> disruptor) {
        if (idOnly(chain.getId(), chain.getAfter())) {
            return;
        }
        List<ListenerChain> befores = chain.getBefores();
        if (CollectionUtils.isEmpty(befores)) {
            return;
        }
        for (ListenerChain c : befores) {
            dealWithDisruptorFromTail(c, disruptor);
        }
        ProxyHandler[] handlers = new ProxyHandler[befores.size()];
        for (int i = 0; i < befores.size(); i++) {
            handlers[i] = befores.get(i).getProxyHandler();
        }
        disruptor.after(handlers).handleEventsWith(chain.getProxyHandler());
    }

    /**
     * 把某一个节点的所有前置节点从map中找出来加到该节点的befores中
     *
     * @param chain       节点
     * @param listenerMap map
     */
    private static void addBeforeFromTail(ListenerChain chain, Map<String, ListenerChain> listenerMap) {
        String id = chain.getId();
        String after = chain.getAfter();
        if (idOnly(id, after)) {
            return;
        }
        String[] ids = after.split(",");
        if (ids.length == 0 || (ids.length == 1 && StringUtils.isBlank(ids[0]))) {
            return;
        }
        for (String beforeId : ids) {
            ListenerChain c = listenerMap.get(beforeId);
            addBeforeFromTail(c, listenerMap);
            chain.addBefore(c);
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
    private static class ProxyHandler implements EventHandler<ElectronsHolder> {

        private ElectronsListener listener;

        public ProxyHandler(ElectronsListener listener) {
            this.listener = listener;
        }

        @Override
        public void onEvent(ElectronsHolder electronsHolder, long l, boolean b) throws Exception {
            listener.onEvent(electronsHolder.getElectron());
        }
    }

    private final static class ListenerChain implements Serializable {

        @Getter
        private String id;

        @Getter
        private String after;

        /**
         * 该listener之后执行的listener集合
         */
        @Getter
        private List<ListenerChain> befores = new ArrayList<>();

        @Getter
        private ElectronsListener listener;

        @Getter
        @Setter
        private int beforeHasHandle = 0;

        @Getter
        private int beforeAll = 0;

        @Getter
        private ProxyHandler proxyHandler;

        private ListenerChain(ElectronsListener lis) {
            this.listener = lis;
            Listener ann = lis.getClass().getAnnotation(Listener.class);
            this.id = ann.id();
            this.after = ann.after();
            this.proxyHandler = new ProxyHandler(lis);
        }

        public void addBefore(ListenerChain listenerChain) {
            if (listenerChain == null) {
                return;
            }
            this.befores.add(listenerChain);
            this.beforeAll++;
        }

        public void addBefore(ElectronsListener listener) {
            if (listener == null) {
                return;
            }
            this.befores.add(new ListenerChain(listener));
            this.beforeAll++;
        }
    }
}
