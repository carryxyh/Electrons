package com.ziyuan.chain;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.ziyuan.ElectronsHolder;
import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;
import com.ziyuan.channel.SpecChannel;
import com.ziyuan.exceptions.SpecChannelBreakException;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.EventCountCircuitBreaker;

import java.io.Serializable;
import java.util.*;

/**
 * ListenerChainBuilderNew 构建监听器链的工具类，旧版的废弃了，存在bug，思路不对
 *
 * @author ziyuan
 * @since 2017-03-20
 */
public final class ListenerChainBuilderNew {

    /**
     * 不允许初始化
     */
    private ListenerChainBuilderNew() {
    }

    public final static void buildChain(SpecChannel specChannel, List<ElectronsListener> electronsListeners) {
        Disruptor<ElectronsHolder> disruptor = specChannel.getDisruptor();
        if (CollectionUtils.isEmpty(electronsListeners) || disruptor == null) {
            return;
        }

        List<ListenerChain> chains = new ArrayList<>();
        for (ElectronsListener listener : electronsListeners) {
            chains.add(new ListenerChain(listener, specChannel));
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
            if (c == null) {
                //如果找不到直接continue
                continue;
            }
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

        /**
         * 监听器
         */
        private ElectronsListener listener;

        /**
         * 该handler隶属的特殊管道
         */
        private SpecChannel specChannel;

        public ProxyHandler(ElectronsListener listener, SpecChannel specChannel) {
            this.listener = listener;
            this.specChannel = specChannel;
        }

        @Override
        public void onEvent(ElectronsHolder electronsHolder, long l, boolean b) throws Exception {
            if (specChannel.getBreaker() == null) {
                listener.onEvent(electronsHolder.getElectron());
            } else {
                //开启熔断的逻辑
                onEventWithBreaker(electronsHolder);
            }
        }

        /**
         * 熔断
         *
         * @param electronsHolder holder
         * @throws Exception 异常
         */
        private void onEventWithBreaker(ElectronsHolder electronsHolder) throws Exception {
            EventCountCircuitBreaker breaker = specChannel.getBreaker();
            if (breaker.checkState()) {
                try {
                    listener.onEvent(electronsHolder.getElectron());
                } catch (Exception e) {
                    breaker.incrementAndCheckState();
                }
            } else {
                throw new SpecChannelBreakException(listener.getClass().getSimpleName());
            }
        }
    }

    private final static class ListenerChain implements Serializable {

        /**
         * 监听器id
         */
        @Getter
        private String id;

        /**
         * 监听器的after属性
         */
        @Getter
        private String after;

        /**
         * 该listener之后执行的listener集合
         */
        @Getter
        private List<ListenerChain> befores = new ArrayList<>();

        /**
         * 监听器
         */
        @Getter
        private ElectronsListener listener;

        /**
         * 代理handler
         */
        @Getter
        private ProxyHandler proxyHandler;

        private ListenerChain(ElectronsListener lis, SpecChannel specChannel) {
            this.listener = lis;
            Listener ann = lis.getClass().getAnnotation(Listener.class);
            this.id = ann.id();
            this.after = ann.after();
            this.proxyHandler = new ProxyHandler(lis, specChannel);
        }

        /**
         * 在before中加入一个
         *
         * @param listenerChain
         */
        public void addBefore(ListenerChain listenerChain) {
            if (listenerChain == null) {
                return;
            }
            this.befores.add(listenerChain);
        }
    }
}
