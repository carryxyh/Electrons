package com.ziyuan.delegate;

import com.lmax.disruptor.InsufficientCapacityException;
import com.ziyuan.EleCircuit;
import com.ziyuan.events.Electron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RetryDelegate 重试一次，再失败改同步
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public class RetryDelegate extends AbstractDelegatePublisher {

    private static Logger logger = LoggerFactory.getLogger(RetryDelegate.class);

    public RetryDelegate(EleCircuit eleCircuit) {
        super(eleCircuit);
    }

    public void publish(String tag, Electron electron) {

        try {
            boolean result = super.getEleCircuit().publish(tag, electron);
            if (!result) {
                retry(tag, electron);
            }
        } catch (Exception e) {
            if (e instanceof InsufficientCapacityException) {
                //满了，睡眠300ms重试
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e1) {
                    //ignore
                }
                retry(tag, electron);
            } else {
                logger.error("Async publish failed electron : { tag: " + tag + "electron: " + electron.toString() + "}", e);
            }
        }
    }

    private void retry(String tag, Electron electron) {
        try {
            super.getEleCircuit().publish(tag, electron);
        } catch (Exception e) {
            try {
                super.getEleCircuit().publishSync(tag, electron);
            } catch (Exception e1) {
                logger.error("Retry failed electron : { tag: " + tag + "electron: " + electron.toString() + "}", e1);
            }
        }
    }
}
