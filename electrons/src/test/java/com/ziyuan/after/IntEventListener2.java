package com.ziyuan.after;

import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;

/**
 * IntEventListener1
 *
 * @author ziyuan
 * @since 2017-03-20
 */
@Listener(id = "2", subscribe = "after", after = "1")
//@Listener(subscribe = "after", after = "1")
public class IntEventListener2 implements ElectronsListener<IntEvent> {

    @Override
    public void onEvent(IntEvent electrons) throws Exception {
        System.out.println("middle 2 id 2 after is doing..." + "----------" + electrons.getSource() + "\n");
    }
}
