package com.ziyuan.after;

import com.ziyuan.ElectronsListener;

/**
 * Int1
 *
 * @author ziyuan
 * @since 2017-07-21
 */
//@Listener(subscribe = "after", id = "2")
public class Int2 implements ElectronsListener<IntEvent> {
    @Override
    public void onEvent(IntEvent electrons) throws Exception {
        System.out.println("2 is doing");
    }
}
