package com.ziyuan;

import com.ziyuan.events.Electron;
import com.ziyuan.events.ElectronsWrapper;
import com.ziyuan.events.ListenerCollectWrapper;
import com.ziyuan.exceptions.CircuitCongestedException;
import com.ziyuan.util.ClassUtil;
import com.ziyuan.util.TypeSafeUtil;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
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
    private AtomicBoolean started = new AtomicBoolean(false);

    /**
     * 配置
     */
    @Setter
    private Config conf;

    /**
     * 启动
     *
     * @return
     */
    private synchronized boolean start() {
        if (started.get()) {
            logger.error("Circuit is running !");
            return false;
        }
        started.set(true);
        try {
            scan();
        } catch (Exception e) {
            logger.error("Scan crash!", e);
            return false;
        }
        dispatcher.start();
        return true;
    }

    /**
     * 直接准备好一个EleCircuit.
     *
     * @return 电路
     */
    public static synchronized EleCircuit ready() {
        Config config = TypeSafeUtil.getConfig();
        EleCircuit eleCircuit = new EleCircuit();
        eleCircuit.conf = config;
        eleCircuit.start();
        return eleCircuit;
    }

    /**
     * 发布事件（异步）
     *
     * @param tag      tag
     * @param electron 事件
     * @throws CircuitCongestedException 异常
     */
    public boolean publish(String tag, Electron electron) throws Exception {
        if (!started.get()) {
            return false;
        }
        if (electron == null) {
            throw new NullPointerException("Electron can not be null !");
        }
        return dispatcher.dispatch(tag, electron, false);
    }

    /**
     * 同步调用
     *
     * @param tag      tag
     * @param electron 事件
     * @throws CircuitCongestedException 异常
     */
    public boolean publishSync(String tag, Electron electron) throws Exception {
        if (!started.get()) {
            return false;
        }
        if (electron == null) {
            throw new NullPointerException("Electron can not be null !");
        }
        //设置同步
        return dispatcher.dispatch(tag, electron, true);
    }

    /**
     * 停止
     *
     * @return
     */
    public synchronized boolean stop() {
        if (!started.get()) {
            return true;
        }
        started.set(false);
        if (dispatcher != null) {
            dispatcher.stop();
            dispatcher = null;
        }
        return true;
    }

    /**
     * 扫描监听器
     * <p>
     * TODO 需要重新考虑一下监听器加载的过程
     */
    private void scan() throws Exception {
        if (dispatcher != null) {
            dispatcher.stop();
        }
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
            Type type = ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0];

            Listener ann = clazz.getAnnotation(Listener.class);
            String tag = ann.subscribe();

            ElectronsWrapper eleWrapper = new ElectronsWrapper(tag, type);
            ListenerCollectWrapper listenerWrapper = wrapperMap.get(eleWrapper);
            if (listenerWrapper == null) {
                listenerWrapper = new ListenerCollectWrapper();
            }
            listenerWrapper.addListener(clazz.newInstance());
            if (StringUtils.isNotBlank(ann.after())) {
                //如果不为空，有after逻辑
                listenerWrapper.setHasAfter(true);
            }
            wrapperMap.put(eleWrapper, listenerWrapper);
        }
        dispatcher = new Dispatcher(wrapperMap, conf);
    }

    /**
     * 对监听器进行排序
     *
     * @param listenerClazz listener的集合
     */
    public void sort(List<Class<? extends ElectronsListener>> listenerClazz) {
        Collections.sort(listenerClazz, (o1, o2) -> {
            int x = o1.getAnnotation(Listener.class).priority();
            int y = o2.getAnnotation(Listener.class).priority();
            return (x < y) ? 1 : ((x == y) ? 0 : -1);
        });
    }
}
