package com.ziyuan.channel;

import com.ziyuan.ElectronsHolder;
import com.ziyuan.exceptions.OpNotSupportException;

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
     * 根据disruptor开启一个通道
     */
    void open();

    /**
     * 关闭管道
     */
    void close();

    /**
     * 发布事件
     */
    boolean publish(ElectronsHolder electronsHolder) throws Exception;

    /**
     * 同步处理
     */
    boolean handle(ElectronsHolder electronsHolder) throws OpNotSupportException;

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
