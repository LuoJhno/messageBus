package com.message.test.controller;

import com.message.bus.client.BusClient;
import com.message.bus.entity.BaseBusGenericMessage;
import com.message.test.domain.TestChildBusDomain;
import com.message.test.domain.TestBusDomain;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author fuzhengjun
 */
@Api("消息")
@Slf4j
@RestController
@RequestMapping("/api/v1/com.zzb.message/testing")
@AllArgsConstructor
public class BusTestController {
    private BusClient busClient;

    @GetMapping("")
    @ApiOperation(value = "测试消息总线")
    public boolean testMessage() {
        // 测试一般参数及多个参数
        TestChildBusDomain childDomain = new TestChildBusDomain();
        childDomain.setName(UUID.randomUUID().toString());
        childDomain.setIcon(UUID.randomUUID().toString());
        childDomain.setDescription(UUID.randomUUID().toString());
        childDomain.setHomeSorted(1);
        TestBusDomain domain = new TestBusDomain();
        BeanUtils.copyProperties(childDomain, domain);
        busClient.submit(childDomain);
        busClient.submit(10L);
        busClient.submit(10L, childDomain);
        busClient.post(10L, domain);
        busClient.post(10L, childDomain, 11L);
        busClient.post(10L, domain, 11L);

        //测试list和set
        /*List<Long> longList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        Set<Long> longSet = new HashSet<>();
        Set<Integer> integerSet = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            longList.add(random.nextLong());
            intList.add(random.nextInt(100));
            integerSet.add(random.nextInt(100));
            longSet.add(random.nextLong());
        }
        busClient.post(new Class[]{Long.class}, longList);
        busClient.post(intList);
        busClient.post(new Class[]{Long.class}, longSet);
        busClient.post(new Class[]{Integer.class}, integerSet);*/


        //测试泛型
        String test = "111";
        BaseBusGenericMessage<String> testBusGenericDomain = new BaseBusGenericMessage<>(test);
        busClient.post(testBusGenericDomain);
        return true;
    }


}
