package com.ziyuan;

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

    public boolean start() {
        return false;
    }
}
