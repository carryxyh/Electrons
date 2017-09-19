package com.ziyuan.rateLimit;

import com.ziyuan.ElectronsListener;

/**
 * StringEventListener
 *
 * @author ziyuan
 * @since 2017-03-23
 */
//@Listener(subscribe = "rateLimit", id = "1")
public class StringEventListener implements ElectronsListener<StringEvent> {

    @Override
    public void onEvent(StringEvent electrons) throws Exception {
        System.out.println("Source : " + electrons.getSource());
    }
}
