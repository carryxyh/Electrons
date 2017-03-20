package com.ziyuan.base;

import com.ziyuan.EleCircuit;
import com.ziyuan.after.IntEvent;
import com.ziyuan.priority.LongEvent2;
import junit.framework.TestCase;

/**
 * BaseTest
 *
 * @author ziyuan
 * @since 2017-03-20
 */
public class BaseTest extends TestCase {

    public void testBase() throws Exception {
        EleCircuit eleCircuit = new EleCircuit();
        eleCircuit.start();
        boolean ok1 = eleCircuit.publish("tag1", new LongEvent(1));
        boolean ok2 = eleCircuit.publish("tag1", new LongEvent(2));
        System.out.println(ok1);
        System.out.println(ok2);
        eleCircuit.stop();
    }

    public void testPriority() throws Exception {
        EleCircuit eleCircuit = new EleCircuit();
        eleCircuit.start();
        boolean ok1 = eleCircuit.publish("tag2", new LongEvent2(1));
        boolean ok2 = eleCircuit.publish("tag2", new LongEvent2(2));
        System.out.println(ok1);
        System.out.println(ok2);
        eleCircuit.stop();
    }

    public void testAfter() throws Exception {
        EleCircuit eleCircuit = new EleCircuit();
        eleCircuit.start();
        boolean ok2 = eleCircuit.publish("after", new IntEvent(2));
        System.out.println(ok2);
        eleCircuit.stop();
    }
}
