package com.ziyuan.priority;

import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;

/**
 * LongEventListener2
 *
 * @author ziyuan
 * @since 2017-03-20
 */
@Listener(subscribe = "tag2", priority = 1)
public class LongEventListener2 implements ElectronsListener<LongEvent2> {

    @Override
    public void onEvent(LongEvent2 electrons) throws Exception {
        System.out.println("priority : " + 1);
        System.out.println("ele name : " + electrons.getName() + "Source : " + electrons.getSource());
    }
}
