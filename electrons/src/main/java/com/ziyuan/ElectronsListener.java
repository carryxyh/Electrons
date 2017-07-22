package com.ziyuan;

import com.ziyuan.events.Electron;

import java.io.Serializable;

/**
 * ElectronsListener 电子监听器
 * TODO 应该会移除一个接口。
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public interface ElectronsListener<E extends Electron> extends Serializable {

    /**
     * 处理事件
     *
     * @param electrons 电子
     * @throws Exception 处理过程中的异常，比较建议抛出，方便熔断
     */
    void onEvent(E electrons) throws Exception;
}
