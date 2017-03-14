package com.ziyuan.channel;

import com.lmax.disruptor.dsl.Disruptor;
import com.ziyuan.events.Electron;
import com.ziyuan.exceptions.OpNotSupportException;

import javax.xml.ws.Holder;
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
    void open(Disruptor<Holder> disruptor);

    /**
     * 关闭管道
     */
    void close();

    /**
     * 发布事件
     */
    boolean publish(String tag, Electron electron) throws Exception;

    /**
     * 同步处理
     */
    void handle() throws OpNotSupportException;

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
