package com.ziyuan.after;

import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;

/**
 * Int1
 *
 * @author ziyuan
 * @since 2017-07-21
 */
@Listener(subscribe = "after", after = "1,2")
public class Int3 implements ElectronsListener<IntEvent> {
    @Override
    public void onEvent(IntEvent electrons) throws Exception {
        System.out.println("3 is doing");
    }
}
