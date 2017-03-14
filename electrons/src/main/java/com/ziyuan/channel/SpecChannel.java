package com.ziyuan.channel;

import com.lmax.disruptor.RingBuffer;
import com.ziyuan.events.Electron;
import com.ziyuan.exceptions.OpNotSupportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Holder;

/**
 * SpecChannel 特殊管道，用于处理after逻辑
 *
 * @author ziyuan
 * @since 2017-03-10
 */
public class SpecChannel extends AbstractChannel {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecChannel.class);

    @Override
    public boolean publish(String tag, Electron electron) throws Exception {
        super.publish(tag, electron);
        RingBuffer<Holder> buffer = super.buffer;
        long next = buffer.tryNext();
        //the remaining capacity of the buffer < the size of the buffer * 0.2 日志输出提示告警
        if (buffer.remainingCapacity() < buffer.getBufferSize() * 0.2) {
            LOGGER.warn("commandBus consume warn message, remainingCapacity size:" + buffer.remainingCapacity() + ",conRingBuffer size:" + buffer.getBufferSize());
        }
        Holder holder = buffer.get(next);
        holder.value = electron;
        buffer.publish(next);
        return true;
    }

    @Override
    public void handle() throws OpNotSupportException {
        throw new OpNotSupportException();
    }
}
