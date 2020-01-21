package com.message.bus.autoconfig;

import com.message.bus.annotation.BusService;
import com.message.bus.client.BusClient;
import com.message.bus.entity.MetaMethod;
import com.message.bus.entity.MetaMethodArray;
import com.message.bus.annotation.BusSubscribe;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * Bus客户端信息
 *
 * @author fuzhengjun
 */

@Slf4j
@NoArgsConstructor
@ConditionalOnBean(annotation = BusService.class)
@EnableConfigurationProperties(BusProperties.class)
@Configuration
public class BusInit {

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private BusProperties busProperties;

    @Bean
    @ConditionalOnMissingBean(BusClient.class)
    public BusClient serverBuilderConfigurer() {
        if (!busProperties.isEnable()) {
            log.info("zzbMessage:bus ---------->  Not open the Bus, please check the configuration information.");
        }
        // 获取包含Bean
        String[] beanDefinitionNames = applicationContext.getBeanNamesForAnnotation(BusService.class);
        for (String name : beanDefinitionNames) {
            Object bean = applicationContext.getBean(name);
            Method[] methods = bean.getClass().getMethods();
            for (Method method : methods) {
                // 检查注解信息
                BusSubscribe methodAnnotation = AnnotationUtils.findAnnotation(method, BusSubscribe.class);
                if (methodAnnotation == null) {
                    continue;
                }
                method.setAccessible(true);
                MetaMethod metaMethod = new MetaMethod(bean, method);
                MetaMethodArray.add(metaMethod);
                log.info("zzbMessage:bus ----------> Add a methods {}({}) to Bus.",
                        method.getName(),
                        metaMethod.getParamCount());
            }
        }
        // if order execute is open,and sort metaMethod
        if (busProperties.isSequenceExecute()) {
            MetaMethodArray.sort();
        }
        return new BusClient();
    }
}
