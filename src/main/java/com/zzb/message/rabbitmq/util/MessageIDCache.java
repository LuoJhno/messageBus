package com.zzb.message.rabbitmq.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageIDCache {

    private static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    public static Map<String, String> getMap() {
        return map;
    }
}
