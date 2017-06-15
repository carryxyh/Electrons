package com.ziyuan.typesafe.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * TypeSafeTest
 *
 * @author ziyuan
 * @since 2017-06-15
 */
public class TypeSafeTest {

    public static void main(String[] args) {
        Config config = ConfigFactory.load("ele-config.conf");
        System.out.println(config.root().render());
        boolean b = config.getBoolean("eleconfig.isOpen");
        System.out.println(b);

        int i = config.getInt("eleconfig.circuitNum");
        String name = config.getString("eleconfig.name");
        System.out.println(i);
        System.out.println(name);

        boolean b1 = config.getBoolean("limitrateconfig.isOpen");
        System.out.println(b1);
    }
}
