package com.message.bus.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.lang.reflect.Method;

@Slf4j
@ConditionalOnMissingBean(ExceptionAdvice.class)
public class DefaultExceptionAdvice implements ExceptionAdvice {

    @Override
    public void handleException(Method method, RuntimeException e) {
        log.info("zzbMessage:bus ----------> Bus执行事件{}出现错误.", method.getName());
        log.info("zzbMessage:bus ----------> Bus订阅事件执行出现异常.", e);
    }
}
