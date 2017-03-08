package com.ziyuan.delegate;

import com.ziyuan.DelegatePublisher;
import com.ziyuan.EleCircuit;
import com.ziyuan.events.Electron;

/**
 * AbstractDelegatePublisher
 *
 * @author ziyuan
 * @since 2017-03-08
 */
public abstract class AbstractDelegatePublisher implements DelegatePublisher {

    protected AbstractDelegatePublisher(EleCircuit eleCircuit) {
        this.eleCircuit = eleCircuit;
    }

    public abstract void publish(String tag, Electron electron);

    private final EleCircuit eleCircuit;

    protected EleCircuit getEleCircuit() {
        return eleCircuit;
    }
}
