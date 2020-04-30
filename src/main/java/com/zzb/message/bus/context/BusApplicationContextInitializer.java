package com.zzb.message.bus.context;


import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * bus Application初始化入口
 *
 * @author fuzhengjun
 */
public class BusApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.addApplicationListener(
                (ApplicationListener<BusInitializedEvent>) event
                        -> BusApplicationContextInitializer.this.onApplicationEvent(applicationContext, event));
    }

    private void onApplicationEvent(ConfigurableApplicationContext applicationContext, BusInitializedEvent event) {
        MutablePropertySources sources = applicationContext.getEnvironment().getPropertySources();
        PropertySource<?> source = sources.get("meta");
        if (source == null) {
            source = new MapPropertySource("meta", new HashMap<>());
            sources.addFirst(source);
        }
        ((Map<String, Object>) source.getSource()).put("meta.method", event.getServer().getInstance());
    }
}
