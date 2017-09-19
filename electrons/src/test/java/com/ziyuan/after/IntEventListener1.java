package com.ziyuan.after;

import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;

/**
 * IntEventListener1
 *
 * @author ziyuan
 * @since 2017-03-20
 */
@Listener(subscribe = "after", id = "1")
public class IntEventListener1 implements ElectronsListener<IntEvent> {

    private String id = "1";

    @Override
    public void onEvent(IntEvent electrons) throws Exception {
        System.out.println("after -> 1");
    }
}
