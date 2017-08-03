package com.ziyuan;

import com.ziyuan.delegate.DropDelegate;
import com.ziyuan.events.HomelessEle;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        System.out.println(HomelessEle.class.isAssignableFrom(HomelessEle.class));
        System.out.println(2 << 6);

        ElectronsListener electronsListener = new TestListener();
        Listener ann = electronsListener.getClass().getAnnotation(Listener.class);
        System.out.println(ann.id());

        System.out.println("2".split(",")[0]);
    }

    //com.ziyuan.events.Electron
    public void testMethodParam() {
        Method[] methods = DropDelegate.class.getMethods();
        for (Method m : methods) {
            if (!m.getName().equals("publish")) {
                continue;
            }
            for (Type t : m.getGenericParameterTypes()) {
                System.out.println(t.getTypeName());
            }
        }

//        System.out.println(DropDelegate.class.isAssignableFrom(Object.class));
        System.out.println(Object.class.isAssignableFrom(DropDelegate.class));
    }
}
