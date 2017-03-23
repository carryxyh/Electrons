package com.ziyuan.after;

import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;

/**
 * IntEventListener5
 *
 * @author ziyuan
 * @since 2017-03-20
 */
@Listener(id = "4", subscribe = "after", after = "2")
public class IntEventListener5 implements ElectronsListener<IntEvent> {

    @Override
    public void onEvent(IntEvent electrons) throws Exception {
        System.out.println("midlle 3 is doing..." + "----------" + electrons.getSource() + "\n");
    }
}
