package com.zzb.message.test.service;

import com.zzb.message.bus.annotation.BusParam;
import com.zzb.message.bus.annotation.BusService;
import com.zzb.message.bus.annotation.BusSubscribe;
import com.zzb.message.test.MessageQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
@BusService
public class BusTestService {

    @BusSubscribe(queueName = MessageQueue.QUEUE_NAME, complexMode = true)
    public void testBusListInteger(@BusParam(name = "integerList") List<Integer> list,
                                   @BusParam(name = "id") Long id) {
        list.forEach(System.out::print);
        log.info("List泛型匹配Integer,id:{}", id);
    }

    @BusSubscribe(queueName = MessageQueue.QUEUE_NAME, complexMode = true)
    public void testBusListString(@BusParam(name = "stringList") List<String> list,
                                  @BusParam(name = "id") Long id) {
        list.forEach(System.out::print);
        log.info("List泛型匹配String,id:{}", id);
    }

    @BusSubscribe(queueName = MessageQueue.QUEUE_NAME, complexMode = true)
    public void testBusListSet(@BusParam(name = "integerSet") Set<Long> set) {
        set.forEach(System.out::print);
        log.info("Set泛型匹配Long");
    }


}
