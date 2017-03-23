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

    @Override
    public void onEvent(IntEvent electrons) throws Exception {
        System.out.println("id = 1 is doing..." + "----------" + electrons.getSource() + "\n");
    }
}
