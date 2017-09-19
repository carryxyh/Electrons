package com.ziyuan.after;

import com.ziyuan.ElectronsListener;

/**
 * Int1
 *
 * @author ziyuan
 * @since 2017-07-21
 */
//@Listener(subscribe = "after", id = "1")
public class Int1 implements ElectronsListener<IntEvent> {
    @Override
    public void onEvent(IntEvent electrons) throws Exception {
        Thread.sleep(1000);
        System.out.println("1 is doing");
    }
}
