package com.ziyuan.channel;

import com.ziyuan.events.Electron;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Channel
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public interface Channel extends Serializable {

    /**
     * 根据监听器链来打开一个管道
     */
    void open();

    /**
     * 关闭管道
     */
    void close();

    /**
     * 给管道注册监听器
     */
    void regist();

    /**
     * 发布事件
     */
    boolean publish(String tag, Electron electron);

    /**
     * 直接处理
     */
    void handle();

    /**
     * 配置限流
     *
     * @param limitRate
     * @param perSecond
     * @param warmup
     * @param warmupPeriod
     * @param unit
     */
    void confLimitRate(boolean limitRate, double perSecond, boolean warmup, int warmupPeriod, TimeUnit unit);

    /**
     * 配置限流
     *
     * @param limitRate
     * @param perSecond
     * @param warmup
     * @param warmupPeriod
     * @param unit
     */
    void confLimitRate(boolean limitRate, double perSecond, boolean warmup, int warmupPeriod, TimeUnit unit, int waitLimit, TimeUnit waitUnit);
}
