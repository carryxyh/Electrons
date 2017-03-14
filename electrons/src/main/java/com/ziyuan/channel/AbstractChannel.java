package com.ziyuan.channel;

import com.google.common.util.concurrent.RateLimiter;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.ziyuan.events.Electron;
import lombok.Getter;

import javax.xml.ws.Holder;
import java.util.concurrent.TimeUnit;

/**
 * AbstractChannel
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public abstract class AbstractChannel implements Channel {

    private RateLimiter rateLimiter;

    /**
     * 是否开启限速
     */
    @Getter
    private boolean limitRate;

    /**
     * 开启
     */
    protected volatile boolean opened;

    /**
     * 真正的电路
     */
    protected RingBuffer<Holder> buffer;

    public boolean publish(String tag, Electron electron) throws Exception {
        if (!opened) {
            return false;
        }
        //如果没有配置限速，直接返回
        if (!limitRate) {
            return false;
        }
        if (this.rateLimiter == null) {
            return false;
        }
        int weight = electron.getWeight();
        //等待
        rateLimiter.acquire(weight);
        return true;
    }

    @Override
    public void open(Disruptor<Holder> disruptor) {
        synchronized (this) {
            buffer = disruptor.start();
        }
    }

    @Override
    public void close() {
        if (!this.opened) {
            return;
        }
        synchronized (this) {
            if (this.opened) {
                this.opened = false;
            }
        }
    }

    @Override
    public void confLimitRate(boolean limitRate, double perSecond, boolean warmup, int warmupPeriod, TimeUnit unit) {
        if (limitRate) {
            this.limitRate = true;
            if (warmup) {
                this.rateLimiter = RateLimiter.create(perSecond, warmupPeriod, unit);
            } else {
                this.rateLimiter = RateLimiter.create(perSecond);
            }
        } else {
            this.limitRate = false;
        }
    }
}
