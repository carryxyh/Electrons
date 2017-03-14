package com.ziyuan.channel;

import com.ziyuan.ElectronsHolder;

/**
 * NormalChannel
 *
 * @author ziyuan
 * @since 2017-03-10
 */
public class NormalChannel extends AbstractChannel {

    @Override
    public void handle(ElectronsHolder electronsHolder) {
        try {
            electronsHolder.handle();
        } catch (Exception e) {
            super.LOGGER.error("Sync handle occurs exception !", e);
        }
    }
}
