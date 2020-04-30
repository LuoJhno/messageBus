package com.zzb.message.bus.cores;

import lombok.Data;
import org.springframework.util.Assert;

import java.util.ArrayDeque;
import java.util.Queue;

@Data
public class MessageResultQueue {

    public static final String X_QUEUE_MASTER_LOCATOR = "x-queue-master-locator";

    private final String name;

    private final Queue<Object> resultQueue = new ArrayDeque<>();


    /**
     * Construct a new queue, given a name, durability flag, and auto-delete flag, and arguments.
     *
     * @param name the name of the queue - must not be null; set to "" to have the broker generate the name.
     */
    public MessageResultQueue(String name) {
        Assert.notNull(name, "'name' cannot be null");
        this.name = name;
    }

    public void addResult(Object result) {
        this.resultQueue.add(result);
    }

    public void removeResult(Object result) {
        this.resultQueue.remove(result);
    }

}
