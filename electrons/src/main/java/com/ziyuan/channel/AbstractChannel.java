package com.ziyuan.channel;

import com.google.common.util.concurrent.RateLimiter;
import com.ziyuan.events.Electron;
import lombok.Getter;

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
     * 每秒令牌
     */
    private int perSecond;

    /**
     * 是否开启限速
     */
    @Getter
    private boolean limitRate;

    /**
     * 默认愿意等待100毫秒
     */
    private int waitLimit = 100;

    /**
     * 单位
     */
    private TimeUnit waitUnit = TimeUnit.MILLISECONDS;

    protected volatile boolean opened;

    public boolean publish(String tag, Electron electron) {
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
        return rateLimiter.tryAcquire(weight, this.waitLimit, this.waitUnit);
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

    @Override
    public void confLimitRate(boolean limitRate, double perSecond, boolean warmup, int warmupPeriod, TimeUnit unit, int waitLimit, TimeUnit waitUnit) {
        confLimitRate(limitRate, perSecond, warmup, warmupPeriod, unit);
        this.waitLimit = waitLimit;
        this.waitUnit = waitUnit;
    }
}
