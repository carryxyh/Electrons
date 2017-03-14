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

    protected volatile boolean opened;

    public abstract void publish(String tag, Electron electron);

    public void confLimitRate(boolean limitRate, double perSecond, boolean warmup, int warmupPeriod, TimeUnit unit) {
        if (limitRate) {
            limitRate = true;
            if (warmup) {
                rateLimiter = RateLimiter.create(perSecond, warmupPeriod, unit);
            } else {
                rateLimiter = RateLimiter.create(perSecond);
            }
        } else {
            limitRate = false;
        }
    }
}