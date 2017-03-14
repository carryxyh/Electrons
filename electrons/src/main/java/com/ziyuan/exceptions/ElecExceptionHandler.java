package com.ziyuan.exceptions;

import com.lmax.disruptor.ExceptionHandler;
import com.ziyuan.events.Electron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ElecExceptionHandler
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public class ElecExceptionHandler<E extends Electron> implements ExceptionHandler<E> {

    private final String disruptor;

    private final static Logger logger = LoggerFactory.getLogger(ElecExceptionHandler.class);

    /**
     * 构造器
     *
     * @param disruptor 分发器
     */
    public ElecExceptionHandler(String disruptor) {
        this.disruptor = disruptor;
    }

    /**
     * handle异常
     *
     * @param ex    异常
     * @param event 事件
     * @since 1.0.0
     */
    @Override
    public void handleEventException(Throwable ex, long sequence, E event) {
        logger.error("[{}] Event Exception:{},event:{}", disruptor, ex, event);
    }

    /**
     * start异常
     *
     * @param ex 异常
     * @since 1.0.0
     */
    @Override
    public void handleOnStartException(Throwable ex) {
        logger.error("[{}] on start Exception:{}", disruptor, ex);
    }

    /**
     * shutdown异常
     *
     * @param ex 异常
     * @since 1.0.0
     */
    @Override
    public void handleOnShutdownException(Throwable ex) {
        logger.error("[{}] on shutdown Exception :", disruptor, ex);
    }
}
