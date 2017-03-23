package com.ziyuan.exceptions;

/**
 * SpecChannelBreakException 特殊通道熔断的异常
 *
 * @author ziyuan
 * @since 2017-03-23
 */
public class SpecChannelBreakException extends Exception {

    public SpecChannelBreakException(String listenerName) {
        super("Spec channel has breaked . ListenerName is " + listenerName);
    }
}
