package com.zzb.message.bus.annotation;

import java.lang.annotation.*;

/**
 * 订阅方法的参数注解
 * 只有当BusSubscribe的complexMode = true时才会用到
 *
 * @author fuzhengjun
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BusParam {
    String name();

}
