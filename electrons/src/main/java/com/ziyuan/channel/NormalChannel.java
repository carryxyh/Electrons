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
    public boolean handle(ElectronsHolder electronsHolder) {
        try {
            return electronsHolder.handle();
        } catch (Exception e) {
            super.LOGGER.error("Sync handle occurs exception !", e);
            return false;
        }
    }
}
