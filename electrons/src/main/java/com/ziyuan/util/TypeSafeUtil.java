package com.ziyuan.util;

import com.typesafe.config.ConfigFactory;
import com.ziyuan.Config;

import java.util.concurrent.TimeUnit;

/**
 * TypeSafeUtil
 * 根据typesafe处理config的工具类
 *
 * @author ziyuan
 * @since 2017-06-15
 */
public final class TypeSafeUtil {

    private static final String CONF_PATH = "eleconfig.conf";

    private static final String M = "m";
    private static final String H = "h";
    private static final String S = "s";

    public static final Config getConfig() {
        Config config = new Config();
        com.typesafe.config.Config fileConf = ConfigFactory.load(CONF_PATH);

        /**
         * 电路相关配置
         */
        config.setEleOpen(fileConf.getBoolean("ele_config.isOpen"));
        config.setCircuitNum(fileConf.getInt("ele_config.circuitNum"));
        config.setSpecCircuitNum(fileConf.getInt("ele_config.specCircuitNum"));
        config.setCircuitLen(fileConf.getInt("ele_config.circuitLen"));
        config.setSpecCircuitLen(fileConf.getInt("ele_config.specCircuitLen"));
        config.setScanJar(fileConf.getBoolean("ele_config.scanJar"));
        config.setScanPackage(fileConf.getString("ele_config.scanPackage"));

        /**
         * 限流相关配置
         */
        config.setLimitRate(fileConf.getBoolean("limiter_config.isOpen"));
        config.setWarmup(fileConf.getBoolean("limiter_config.warmup"));
        config.setWarmupPeriod(fileConf.getInt("limiter_config.warmupPeriod"));
        config.setPermitsPerSecond(fileConf.getInt("limiter_config.permitsPerSecond"));

        String unitLimit = fileConf.getString("limiter_config.unit");
        if (S.equals(unitLimit)) {
            config.setWarmPeriodUnit(TimeUnit.SECONDS);
        } else if (H.equals(unitLimit)) {
            config.setWarmPeriodUnit(TimeUnit.HOURS);
        } else if (M.equals(unitLimit)) {
            config.setWarmPeriodUnit(TimeUnit.MINUTES);
        }

        /**
         * 熔断器相关配置
         */
        config.setBreaker(fileConf.getBoolean("breaker_config.isOpen"));
        config.setErrorNum(fileConf.getInt("breaker_config.errorNum"));
        config.setPerUnit(fileConf.getInt("breaker_config.perUnit"));
        config.setCloseThreshold(fileConf.getInt("breaker_config.closeThreshold"));
        config.setRest(fileConf.getInt("breaker_config.rest"));

        String unitBreaker = fileConf.getString("breaker_config.unit");
        if (S.equals(unitBreaker)) {
            config.setUnit(TimeUnit.SECONDS);
            config.setRestUnit(TimeUnit.SECONDS);
        } else if (H.equals(unitBreaker)) {
            config.setUnit(TimeUnit.HOURS);
            config.setRestUnit(TimeUnit.HOURS);
        } else if (M.equals(unitBreaker)) {
            config.setUnit(TimeUnit.MINUTES);
            config.setRestUnit(TimeUnit.MINUTES);
        }

        return config;
    }
}
