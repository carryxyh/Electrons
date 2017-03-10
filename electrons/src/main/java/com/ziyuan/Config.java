package com.ziyuan;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * Config
 *
 * @author ziyuan
 * @since 2017-03-08
 */
@Data
public final class Config {

    /**
     * 电路数量
     */
    private int circuitNum = 8;

    /**
     * 默认异步
     */
    private boolean async = true;

    /**
     * 默认2左移十位 2048
     */
    private int circuitLen = 2 << 8;

    /**
     * 默认扫所有
     */
    private String scanPackage = "";

    /**
     * 默认不扫描jar包
     */
    private boolean scanJar = false;

    /**
     * channel的数量，大于1的时候则类似rocketmq的queue模型
     */
    private int channels = 1;

    /*------------------------------------限流相关---------------------------------------*/

    /**
     * 是否开启限流
     */
    private boolean limitRate = false;

    /**
     * 是否预热
     */
    private boolean warmup = false;

    /**
     * 预热1分钟
     */
    private int warmupPeriod = 1;

    /**
     * 默认单位分钟，默认预热1分钟
     */
    private TimeUnit warmPeriodUnit = TimeUnit.MINUTES;

    /**
     * 每秒64个令牌
     */
    private double threshold = 2 << 5;

    /*------------------------------------熔断相关 默认：每两分钟内出现5个错误，就休息3分钟---------------------------------------*/

    /**
     * 是否开启熔断
     */
    private boolean breaker = false;

    /**
     * 单位时间内错误数
     */
    private int errorNum = 5;

    /**
     * 单位时间内
     */
    private int perUnit = 2;

    /**
     * 单位
     */
    private TimeUnit unit = TimeUnit.MINUTES;

    /**
     * 休息多久
     */
    private int rest = 3;

    /**
     * 休息的单位
     */
    private TimeUnit restUnit = TimeUnit.MINUTES;
}
