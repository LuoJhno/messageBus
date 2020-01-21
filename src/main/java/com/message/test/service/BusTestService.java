package com.message.test.service;

import com.message.bus.entity.BaseBusGenericMessage;
import com.message.test.domain.TestBusDomain;
import com.message.bus.annotation.BusService;
import com.message.bus.annotation.BusSubscribe;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
@BusService
public class BusTestService {

    @BusSubscribe
    public boolean testBusByOneDomain(TestBusDomain testBusDomain) {
        log.info(testBusDomain.toString());
        log.info("一个参数CarBrandParameter父类匹配");
        return true;
    }

    @BusSubscribe
    public void testBusByTwoParameter(Long brandId, TestBusDomain testBusDomain) {
        log.info(brandId + testBusDomain.toString());
        log.info("两个参数Long，CarBrandParameter匹配");
    }

    @BusSubscribe
    public void testBusByThreeParameter(Long brandId, TestBusDomain testBusDomain, Long seriesId) {
        log.info(brandId + testBusDomain.toString() + seriesId);
        log.info("三个参数Long，CarBrandParameter，Long匹配");
    }

    @BusSubscribe
    public void testBusListLong(List<Long> list) {
        list.forEach(System.out::print);
        log.info("List泛型匹配Long");
    }

    @BusSubscribe
    public void testBusListInteger(List<Integer> list) {
        list.forEach(System.out::print);
        log.info("List泛型匹配Integer");
    }

    @BusSubscribe
    public void testBusListSet(Set<Long> set) {
        set.forEach(System.out::print);
        log.info("Set泛型匹配Long");
    }

    @BusSubscribe
    public void testBusListIntegerSet(Set<Integer> set) {
        set.forEach(System.out::print);
        log.info("Set泛型匹配Integer");
    }

    @BusSubscribe
    public void testBusByGenericString(BaseBusGenericMessage<String> domain) {
        log.info("泛型String");
    }

    @BusSubscribe
    public void testBusByGenericInteger(BaseBusGenericMessage<Integer> domain) {
        log.info("泛型Integer");
    }


}
