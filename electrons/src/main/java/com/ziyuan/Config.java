package com.ziyuan;

import lombok.Data;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

/**
 * Config
 *
 * @author ziyuan
 * @since 2017-03-08
 */
@Data
@ToString
public final class Config {

    /**
     * 电路是否打开，目前只支持true，后面总线可能用队列，则关闭时用队列
     */
    private boolean eleOpen = true;

    /**
     * 电路数量
     */
    private int circuitNum = 8;

    /**
     * 特殊电路的数量(如果存在特殊电路，这个值才会生效)
     */
    private int specCircuitNum = 3;

    /**
     * 电路长度，即buffer的size 默认512
     */
    private int circuitLen = 2 << 8;

    /**
     * 默认的特殊电路的长度 128
     */
    private int specCircuitLen = 2 << 6;

    /**
     * 默认扫所有
     */
    private String scanPackage = "";

    /**
     * 默认不扫描jar包
     */
    private boolean scanJar = false;

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
    private double permitsPerSecond = 2 << 5;

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
     * 关闭熔断的阈值
     */
    private int closeThreshold = 5;

    /**
     * 休息多久
     */
    private int rest = 3;

    /**
     * 休息的单位
     */
    private TimeUnit restUnit = TimeUnit.MINUTES;
}
