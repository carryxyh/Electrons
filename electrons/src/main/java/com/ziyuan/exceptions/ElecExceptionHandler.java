package com.ziyuan.exceptions;

import com.lmax.disruptor.ExceptionHandler;
import com.ziyuan.ElectronsHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ElecExceptionHandler
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public class ElecExceptionHandler implements ExceptionHandler<ElectronsHolder> {

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

    @Override
    public void handleEventException(Throwable throwable, long l, ElectronsHolder electronsHolder) {
        logger.error("[{}] Event Exception:{},event:{}", disruptor, throwable, electronsHolder);
//        throw new RuntimeException("ex...");
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        logger.error("[{}] on start Exception:{}", disruptor, throwable);
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        logger.error("[{}] on shutdown Exception :", disruptor, throwable);
    }
}
