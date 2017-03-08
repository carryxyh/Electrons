package com.ziyuan.exceptions;

/**
 * CircuitCongestedException 电路拥挤，ringbuffer满了
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public class CircuitCongestedException extends Exception {

    public CircuitCongestedException() {
        super("Circuit is congested!");
    }
}
