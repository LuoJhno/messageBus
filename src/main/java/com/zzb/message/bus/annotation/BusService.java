package com.zzb.message.bus.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 订阅类注解
 *
 * @author fuzhengjun
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface BusService {
}
