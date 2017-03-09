package com.ziyuan.chain;

import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;
import com.ziyuan.events.ListenerCollectWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ListenerChainBuilder 构造listener链的辅助类
 *
 * @author ziyuan
 * @since 2017-03-09
 */
public final class ListenerChainBuilder {

    private ListenerChainBuilder() {
    }

    public synchronized static List<ListenerChain> build(ListenerCollectWrapper collectWrapper) {
        List<ElectronsListener> electronsListeners = collectWrapper.getElectronsListeners();
        List<ListenerChain> listenerChains = new ArrayList<>();
        if (CollectionUtils.isEmpty(electronsListeners)) {
            return listenerChains;
        }

        Map<String, ListenerChain> listenerMap = new HashMap<>();
        for (ElectronsListener listener : electronsListeners) {
            Listener ann = listener.getClass().getAnnotation(Listener.class);
            String id = ann.id();
            String after = ann.after();
            if (StringUtils.isBlank(id) && StringUtils.isBlank(after)) {
                //如果没有id，after也没有，那么就是普通节点
                ListenerChain chain = new ListenerChain(listener);
                listenerChains.add(chain);
                continue;
            }
            else if (StringUtils.isNotBlank(id) && StringUtils.isBlank(after)) {
                //id 有 after没有
                ListenerChain chain = new ListenerChain(listener);
                listenerChains.add(chain);
                listenerMap.put(id, chain);
                continue;
            }
            else if (StringUtils.isBlank(id) && StringUtils.isNotBlank(after)) {
                //id 没有 after有
                String[] ids = after.split(",");
                for (String i : ids) {
                    ListenerChain chain = listenerMap.get(i);
                    chain.addBefore(listener);
                }
                continue;
            }
            else if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(after)) {
                //id after都有
                String[] ids = after.split(",");
                for (String i : ids) {
                    ListenerChain chain = listenerMap.get(i);
                    chain.addBefore(listener);
                }
                ListenerChain chain = new ListenerChain(listener);
                listenerChains.add(chain);
                listenerMap.put(id, chain);
                continue;
            }
        }
        return listenerChains;
    }

}
