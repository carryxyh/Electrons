package com.ziyuan.after;

import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;

/**
 * IntEventListener1
 *
 * @author ziyuan
 * @since 2017-03-20
 */
//@Listener(id = "2", subscribe = "after", after = "1,3")
@Listener(subscribe = "after", after = "1")
public class IntEventListener2 implements ElectronsListener<IntEvent> {

    private String id = "2";

    @Override
    public void onEvent(IntEvent electrons) throws Exception {
        System.out.println("after -> 2");
    }
}
