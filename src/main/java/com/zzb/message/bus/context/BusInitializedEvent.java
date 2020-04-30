package com.zzb.message.bus.context;


import com.zzb.message.bus.cores.MetaMethod;
import org.springframework.context.ApplicationEvent;

public class BusInitializedEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public BusInitializedEvent(MetaMethod source) {
        super(source);
    }

    MetaMethod getServer() {
        return (MetaMethod) getSource();
    }
}
