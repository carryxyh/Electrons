package com.ziyuan.after;

import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;

/**
 * IntEventListener4
 *
 * @author ziyuan
 * @since 2017-03-20
 */
//@Listener(subscribe = "after", after = "2,3")
@Listener(subscribe = "after", after = "3,4")
public class IntEventListener4 implements ElectronsListener<IntEvent> {

    @Override
    public void onEvent(IntEvent electrons) throws Exception {
        System.out.println("last is running");
    }
}
