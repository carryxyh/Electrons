package com.ziyuan.delegate;

import com.ziyuan.EleCircuit;
import com.ziyuan.events.Electron;
import com.ziyuan.exceptions.CircuitCongestedException;

/**
 * RetryDelegate 重试一次，再失败改同步
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public class RetryDelegate extends AbstractDelegatePublisher {

    protected RetryDelegate(EleCircuit eleCircuit) {
        super(eleCircuit);
    }

    public void publish(String tag, Electron electron) {

        try {
            super.getEleCircuit().publish(tag, electron);
        } catch (CircuitCongestedException e) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                //ignore
            }
            retry(tag, electron);
        }
    }

    private void retry(String tag, Electron electron) {
        try {
            super.getEleCircuit().publish(tag, electron);
        } catch (CircuitCongestedException e) {
            //TODO 再次失败，直接同步
        }
    }
}
