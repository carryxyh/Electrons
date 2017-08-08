package com.ziyuan.ex;

import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;

/**
 * ExListenerA
 *
 * @author ziyuan
 * @since 2017-08-08
 */
@Listener(subscribe = "ex", after = "ex1")
public class ExListenerB implements ElectronsListener<EleEvent> {

    @Override
    public void onEvent(EleEvent electrons) throws Exception {
        System.out.println("ele ex after..");
    }
}
