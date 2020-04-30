package com.zzb.message.bus.cores;

import lombok.Data;

@Data
public class MetaResult {
    /**
     * 调用的方法元数据
     */
    private MetaMethod metaMethod;
    /**
     * 调用后返回的结果
     */
    private Object result;
    /**
     * 参数
     */
    private Object[] messageParams;
    /**
     * 队列的引用
     */
    private MessageResultQueue queue;
    /**
     * ack用于结果确认，当ack存在时则没有确认，ack为null时即为确认
     */
    private String ack;

    public MetaResult(MetaMethod metaMethod, MessageResultQueue queue, Object[] messageParams, Object result, String ack) {
        this.metaMethod = metaMethod;
        this.queue = queue;
        this.result = result;
        this.messageParams = messageParams;
        this.ack = ack;
    }

    /**
     * 确认使用
     */
    public void basicAck(String ack) {
        if (ack.equals(this.ack)) {
            this.queue.removeResult(this);
        } else {
            throw new RuntimeException("结果确认出错");
        }
    }

    /**
     * 拒绝
     */
    public void basicReject() {

    }

    /**
     * 确认未使用
     */
    public void basicNack() {

    }
}
