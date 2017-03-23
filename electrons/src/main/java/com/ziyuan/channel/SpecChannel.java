package com.ziyuan.channel;

import com.lmax.disruptor.dsl.Disruptor;
import com.ziyuan.ElectronsHolder;
import com.ziyuan.exceptions.OpNotSupportException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.concurrent.EventCountCircuitBreaker;

/**
 * SpecChannel 特殊管道，用于处理after逻辑
 *
 * @author ziyuan
 * @since 2017-03-10
 */
public class SpecChannel extends AbstractChannel {

    public SpecChannel(Disruptor<ElectronsHolder> disruptor) {
        super(disruptor);
    }

    @Setter
    @Getter
    private EventCountCircuitBreaker breaker;

    @Override
    public boolean handle(ElectronsHolder electronsHolder) throws OpNotSupportException {
        throw new OpNotSupportException();
    }
}
