package com.ziyuan.delegate;

import com.ziyuan.EleCircuit;
import com.ziyuan.events.Electron;

/**
 * DropDelegate 满了直接丢弃事件（不建议）
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public class DropDelegate extends AbstractDelegatePublisher {

    protected DropDelegate(EleCircuit eleCircuit) {
        super(eleCircuit);
    }

    public void publish(String tag, Electron electron) {
        try {
            super.getEleCircuit().publish(tag, electron);
        } catch (Exception e) {

        }
    }
}
