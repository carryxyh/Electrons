package com.ziyuan;

import java.lang.annotation.*;

/**
 * Listener
 * 说明
 * 1.不支持同时存在priority和after，避免出现priority比某个listener大但是却要在这个listener之后执行的情况
 * 如果两个都设置，只生效after
 * 2.listener的async标识优先于全局异步
 *
 * @author ziyuan
 * @since 2017-03-08
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Listener {

    /**
     * 标识一个唯一的listener
     *
     * @return 这个listener的id
     */
    String id() default "";

    /**
     * after标识这个listener在某个listener之后执行，返回一个string，是id的集合，用【,】分割
     *
     * @return 要在该listener之前执行的listener的id的集合
     */
    String after() default "";

    /**
     * 优先级
     *
     * @return listener的优先级
     */
    int priority() default Byte.MIN_VALUE;

    /**
     * 是否同步
     *
     * @return 该listener是否同步执行
     */
    boolean sync() default false;

    /**
     * 订阅的类型
     *
     * @return 订阅的事件类型
     */
    String subscribe() default "";
}
