package com.ziyuan.delegate;

import com.ziyuan.EleCircuit;
import com.ziyuan.events.Electron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DropDelegate 满了直接丢弃事件（不建议）
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public class DropDelegate extends AbstractDelegatePublisher {

    private static Logger logger = LoggerFactory.getLogger(DropDelegate.class);

    public DropDelegate(EleCircuit eleCircuit) {
        super(eleCircuit);
    }

    public void publish(String tag, Electron electron) {
        try {
            super.getEleCircuit().publish(tag, electron);
        } catch (Exception e) {
            logger.warn("Publish failed and drop the electron : { tag: " + tag + "electron: " + electron.toString() + "}", e);
        }
    }
}
