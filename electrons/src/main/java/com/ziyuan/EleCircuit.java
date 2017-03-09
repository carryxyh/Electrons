package com.ziyuan;

import com.google.common.reflect.TypeToken;
import com.ziyuan.events.Electron;
import com.ziyuan.events.ElectronsWrapper;
import com.ziyuan.events.ListenerCollectWrapper;
import com.ziyuan.exceptions.CircuitCongestedException;
import com.ziyuan.util.ClassUtil;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * EleCircuit 电路，相当于事件的入口
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public final class EleCircuit {

    private static Logger logger = LoggerFactory.getLogger(EleCircuit.class);

    /**
     * 分发器
     */
    private Dispatcher dispatcher;

    /**
     * 是否启动中
     */
    private AtomicBoolean started;

    /**
     * 配置
     */
    @Setter
    private Config conf = new Config();

    /**
     * 启动
     *
     * @return
     */
    public synchronized boolean start() {
        return false;
    }

    /**
     * 发布事件（异步）
     *
     * @param tag      tag
     * @param electron 事件
     * @throws CircuitCongestedException 异常
     */
    public void publish(String tag, Electron electron) throws CircuitCongestedException {

    }

    /**
     * 同步调用
     *
     * @param tag      tag
     * @param electron 事件
     * @throws CircuitCongestedException 异常
     */
    public void publishSync(String tag, Electron electron) throws CircuitCongestedException {

    }

    /**
     * 停止
     *
     * @return
     */
    public synchronized boolean stop() {
        return false;
    }

    /**
     * 扫描监听器
     */
    private void scan() {
        // 扫描注解
        Set<Class<?>> clazzSet = ClassUtil.scanPackageByAnnotation(conf.getScanPackage(), conf.isScanJar(), Listener.class);
        if (CollectionUtils.isEmpty(clazzSet)) {
            logger.error("Listener is not found ! Please check the package and jar that u scan !");
        }
        List<Class<? extends ElectronsListener>> allListeners = new ArrayList<>();
        Class superClass = ElectronsListener.class;
        for (Class<?> clazz : clazzSet) {
            if (superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)) {
                allListeners.add((Class<? extends ElectronsListener>) clazz);
            }
        }
        sort(allListeners);
        Map<ElectronsWrapper, ListenerCollectWrapper> wrapperMap = new HashMap<>();
        for (Class<? extends ElectronsListener> clazz : allListeners) {
            TypeToken<Class<? extends ElectronsListener>> token = new TypeToken<Class<? extends ElectronsListener>>() {};
            TypeToken<?> generic = token.resolveType(clazz.getTypeParameters()[0]);
            Type type = generic.getType();
        }
    }

    /**
     * 对监听器进行排序，如果hasAfterlis = false的情况下
     *
     * @param listenerClazz
     */
    public void sort(List<Class<? extends ElectronsListener>> listenerClazz) {
        Collections.sort(listenerClazz, new Comparator<Class<? extends ElectronsListener>>() {

            @Override
            public int compare(Class<? extends ElectronsListener> o1, Class<? extends ElectronsListener> o2) {
                int x = o1.getAnnotation(Listener.class).priority();
                int y = o2.getAnnotation(Listener.class).priority();
                return (x < y) ? -1 : ((x == y) ? 0 : 1);
            }
        });
    }
}
