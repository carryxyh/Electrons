package com.ziyuan.channel;

import com.ziyuan.chain.ListenerChain;
import com.ziyuan.events.Electron;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Channel
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public interface Channel {

    /**
     * 根据监听器链来打开一个管道
     */
    void open(Collection<ListenerChain> chains);

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
     *
     * @param source
     * @param electron
     */
    void publish(String source, Electron electron);

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
}
