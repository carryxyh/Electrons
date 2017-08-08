package com.ziyuan.ex;

import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;

/**
 * ExListenerA
 *
 * @author ziyuan
 * @since 2017-08-08
 */
@Listener(subscribe = "ex", id = "ex1")
public class ExListenerA implements ElectronsListener<EleEvent> {

    @Override
    public void onEvent(EleEvent electrons) throws Exception {
        System.out.println("ele");
        throw new RuntimeException("ele exe...");
    }
}
