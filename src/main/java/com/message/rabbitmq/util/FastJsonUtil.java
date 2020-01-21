package com.message.rabbitmq.util;

import com.alibaba.fastjson.JSON;
import com.message.rabbitmq.entity.MessageDetail;
import org.apache.commons.lang3.StringUtils;

public class FastJsonUtil {

    public static String objectToString(Object obj) {
        if (null != obj) {
            return JSON.toJSONString(obj);
        }
        return null;
    }

    public static Object stringToObject(String msg) {
        if (!StringUtils.isBlank(msg)) {
            return JSON.parse(msg);
        }
        return null;
    }

    public static MessageDetail stringToMessage(byte[] msg) {
        if (null != msg && msg.length > 0) {
            return JSON.parseObject(msg, MessageDetail.class);
        }
        return null;
    }
}
