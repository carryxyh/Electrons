package com.ziyuan.base;

import com.ziyuan.ElectronsListener;

/**
 * LongEventListener
 *
 * @author ziyuan
 * @since 2017-03-20
 */
//@Listener(subscribe = "tag1")
public class LongEventListener implements ElectronsListener<LongEvent> {

    @Override
    public void onEvent(LongEvent electrons) throws Exception {
        System.out.println("ele name : " + electrons.getName() + "Source : " + electrons.getSource());
    }
}
