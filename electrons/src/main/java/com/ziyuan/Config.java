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
    private int circuitLen = 2 << 10;

    /**
     * 消费组的数量
     */
    private int workers = 8;

    /*------------------------------------限流相关---------------------------------------*/

    /**
     * 每秒64个令牌
     */
    private int threshold = 2 << 5;

    /*------------------------------------熔断相关 默认：每两分钟内出现5个错误，就休息3分钟---------------------------------------*/

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
