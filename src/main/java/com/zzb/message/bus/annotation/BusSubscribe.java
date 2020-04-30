package com.zzb.message.bus.annotation;

import com.zzb.message.bus.constance.BusConstance;

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
    /**
     * 复杂模式-参数为BaseComplexBusMessage，将具体的参数放到map中进行解析
     */
    boolean complexMode() default false;

    String queueName();

    //调试模式
    boolean debug() default false;

    /**
     * 权重
     */
    int weight() default BusConstance.ORDER_NORMAL;


}
