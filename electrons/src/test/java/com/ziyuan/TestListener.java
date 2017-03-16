package com.ziyuan;

import com.ziyuan.events.Electron;

/**
 * TestListener
 *
 * @author ziyuan
 * @since 2017-03-16
 */
@Listener(id = "abc")
public class TestListener implements ElectronsListener {

    @Override
    public void onEvent(Electron electrons) throws Exception {

    }
}
