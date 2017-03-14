package com.ziyuan.channel;

import com.ziyuan.ElectronsHolder;
import com.ziyuan.exceptions.OpNotSupportException;

/**
 * SpecChannel 特殊管道，用于处理after逻辑
 *
 * @author ziyuan
 * @since 2017-03-10
 */
public class SpecChannel extends AbstractChannel {

    @Override
    public void handle(ElectronsHolder electronsHolder) throws OpNotSupportException {
        throw new OpNotSupportException();
    }
}
