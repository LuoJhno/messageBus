package com.message.bus.annotation;

import com.message.bus.constance.BusConstance;

import java.lang.annotation.*;

/**
 * 订阅方法注解
 *
 * @author fuzhengjun
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusSubscribe {
    //调试模式
    boolean debug() default false;

    /**
     * 权重
     */
    int weight() default BusConstance.ORDER_NORMAL;


}
