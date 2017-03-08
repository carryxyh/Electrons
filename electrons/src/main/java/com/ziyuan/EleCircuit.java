package com.ziyuan;

import com.ziyuan.events.Electron;
import com.ziyuan.exceptions.CircuitCongestedException;
import com.ziyuan.util.ClassUtil;
import lombok.Setter;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * EleCircuit 电路，相当于事件的入口
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public final class EleCircuit {

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

    private void scan() {
        // 扫描注解
        Set<Class<?>> clazzSet = ClassUtil.scanPackageByAnnotation(conf.getScanPackage(), conf.isScanJar(), Listener.class);
    }
}
