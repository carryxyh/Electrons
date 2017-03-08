package com.ziyuan.exceptions;

import com.lmax.disruptor.ExceptionHandler;
import com.ziyuan.events.Electron;

/**
 * ElecExceptionHandler
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public class ElecExceptionHandler<E extends Electron> implements ExceptionHandler<E> {

    @Override
    public void handleEventException(Throwable throwable, long l, E e) {

    }

    @Override
    public void handleOnStartException(Throwable throwable) {

    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {

    }
}
