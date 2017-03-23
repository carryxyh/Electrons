package com.ziyuan.breaker;

import com.ziyuan.ElectronsListener;
import com.ziyuan.Listener;

/**
 * DoubleEventListener
 *
 * @author ziyuan
 * @since 2017-03-23
 */
@Listener(subscribe = "breaker", id = "1")
public class DoubleEventListener implements ElectronsListener<DoubleEvent> {

    @Override
    public void onEvent(DoubleEvent electrons) throws Exception {
    }
}
