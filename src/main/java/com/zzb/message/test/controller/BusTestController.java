package com.zzb.message.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.zzb.message.bus.client.IBusClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fuzhengjun
 */
@Api("消息")
@Slf4j
@RestController
@RequestMapping("/api/v1/message/testing")
@AllArgsConstructor
public class BusTestController {
    private IBusClient busClient;

    @GetMapping("")
    @ApiOperation(value = "测试消息总线")
    public boolean testMessage() {

        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);
        integerList.add(3);
        integerList.add(4);
        Map<String, Object> map = new HashMap<>();
        map.put("integerList", integerList);
        map.put("id", 7777L);
        JSONObject json = new JSONObject(map);
        busClient.post(json);

        List<String> stringList = new ArrayList<>();
        stringList.add("10");
        stringList.add("20");
        stringList.add("30");
        stringList.add("40");
        Map<String, Object> map1 = new HashMap<>();
        map1.put("stringList", stringList);
        map1.put("id", 111L);
        JSONObject json1 = new JSONObject(map1);
        busClient.post(json1);

        return true;
    }


}
