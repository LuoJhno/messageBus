package com.zzb.message.bus.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("bus")
@Getter
@Setter
public class BusProperties {

    /**
     * make bus open default value is open
     */
    private boolean enable = true;

    /**
     * Open Subscribe's Order execute,default value is close
     */
    private boolean sequenceExecute = false;
}
