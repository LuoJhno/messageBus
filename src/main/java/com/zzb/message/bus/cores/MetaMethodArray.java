package com.zzb.message.bus.cores;

import java.util.ArrayList;
import java.util.List;

/**
 * 元方法集合
 *
 * @author fuzhengjun
 */
public class MetaMethodArray {

    private static List<MetaMethod> metaMethods = null;

    public static synchronized void add(MetaMethod metaMethod) {
        if (metaMethods == null) {
            metaMethods = new ArrayList<MetaMethod>();
        }
        metaMethods.add(metaMethod);
    }

    /**
     * 当前方法集合的size
     */
    public static int size() {
        return metaMethods.size();
    }

    /**
     * 当前的方法集合
     */
    public static boolean isEmpty() {
        return size() == 0;
    }

    public static List<MetaMethod> getMetaMethods() {
        return metaMethods;
    }

    /**
     * start sort with order(int)
     */
    public static void sort() {
        if (metaMethods != null) {
            metaMethods.sort((o1, o2) -> {
                if (o1.getWeight() == o2.getWeight()) {
                    return 0;
                } else {
                    return o1.getWeight() > o2.getWeight() ? 1 : -1;
                }
            });
        }
    }
}
