package com.ziyuan.breaker;

import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;

/**
 * DoubleEventlistener1
 *
 * @author ziyuan
 * @since 2017-03-23
 */
@Listener(subscribe = "breaker", after = "1")
public class DoubleEventlistener1 implements ElectronsListener<DoubleEvent> {

    @Override
    public void onEvent(DoubleEvent electrons) throws Exception {
        System.out.println("after 1    " + electrons.getSource());
        throw new NullPointerException("test");
    }
}
