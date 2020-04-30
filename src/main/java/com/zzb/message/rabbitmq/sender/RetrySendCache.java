package com.zzb.message.rabbitmq.sender;

import com.zzb.message.rabbitmq.cores.SendStatusMessage;

import java.util.concurrent.ConcurrentHashMap;

public class RetrySendCache {
    private DefaultSender defaultSender;
    private ConcurrentHashMap<String, MessageWithTime> map = new ConcurrentHashMap();
    private String key;

    public RetrySendCache() {
        startRetry();
    }

    private static class MessageWithTime {
        private long time;
        private Object message;

        public MessageWithTime(long time, Object message) {
            this.time = time;
            this.message = message;
        }

        public long getTime() {
            return time;
        }

        public Object getMessage() {
            return message;
        }

    }

    public void setSenderInfo(DefaultSender defaultSender,  String key) {
        this.defaultSender = defaultSender;
        this.key = key;
    }

    public void put(String id, Object message) {
        map.put(id, new MessageWithTime(System.currentTimeMillis(), message));
    }

    public void remove(String id) {
        map.remove(id);
    }

    public void startRetry() {

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (String key : map.keySet()) {
                    long now = System.currentTimeMillis();
                    MessageWithTime messageWithTime = map.get(key);
                    //超过三分钟没有ack直接删除本地缓存的message,重试两次
                    if (messageWithTime.getTime() + 3 * 60 * 1000 <= now) {
                        remove(key);
                    } else if (messageWithTime.getTime() + 60 * 1000 <= now) {
                        SendStatusMessage message = defaultSender.send(messageWithTime.getMessage(), key);
                        if (message.isFlag()) {
                            remove(key);
                        }
                    }
                }
            }
        }).start();
    }
}
