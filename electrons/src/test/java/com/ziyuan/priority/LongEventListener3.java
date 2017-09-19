package com.ziyuan.priority;

import com.ziyuan.ElectronsListener;

/**
 * LongEventListener3
 *
 * @author ziyuan
 * @since 2017-03-20
 */
//@Listener(subscribe = "tag2", priority = 2)
public class LongEventListener3 implements ElectronsListener<LongEvent2> {

    @Override
    public void onEvent(LongEvent2 electrons) throws Exception {
        System.out.println("priority : " + 2);
        System.out.println("ele name : " + electrons.getName() + "Source : " + electrons.getSource());
    }
}
