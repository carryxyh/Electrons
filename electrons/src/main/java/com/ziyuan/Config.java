package com.ziyuan;

import lombok.Data;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

/**
 * Config
 * 配置的内容都在eleconfig.conf中
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
    private boolean eleOpen;

    /**
     * 电路数量
     */
    private int circuitNum;

    /**
     * 特殊电路的数量(如果存在特殊电路，这个值才会生效)
     */
    private int specCircuitNum;

    /**
     * 电路长度，即buffer的size 默认512
     */
    private int circuitLen;

    /**
     * 默认的特殊电路的长度 128
     */
    private int specCircuitLen;

    /**
     * 默认扫所有
     */
    private String scanPackage;

    /**
     * 默认不扫描jar包
     */
    private boolean scanJar;

    /*------------------------------------限流相关---------------------------------------*/

    /**
     * 是否开启限流
     */
    private boolean limitRate;

    /**
     * 是否预热
     */
    private boolean warmup;

    /**
     * 预热1分钟
     */
    private int warmupPeriod;

    /**
     * 默认单位分钟，默认预热1分钟
     */
    private TimeUnit warmPeriodUnit;

    /**
     * 每秒64个令牌
     */
    private double permitsPerSecond;

    /*------------------------------------熔断相关 默认：每两分钟内出现5个错误，就休息3分钟---------------------------------------*/

    /**
     * 是否开启熔断
     */
    private boolean breaker;

    /**
     * 单位时间内错误数
     */
    private int errorNum;

    /**
     * 单位时间内
     */
    private int perUnit;

    /**
     * 单位
     */
    private TimeUnit unit;

    /**
     * 关闭熔断的阈值
     */
    private int closeThreshold;

    /**
     * 休息多久
     */
    private int rest;

    /**
     * 休息的单位
     */
    private TimeUnit restUnit;
}
