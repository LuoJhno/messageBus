package com.message.bus.listener;


import com.message.bus.entity.MetaMethod;

/**
 * when handle subscribe complete,bus will execute handleSubscribeLister
 *
 * @author fuzhengjun
 */
public interface HandleSubscribeListener {

    /**
     * execute subscribe is success,callback this methods.
     *
     * @param metaMethod   execute subscribe's com.zzb.message
     * @param resultObject execute Subscribe's result
     */
    void onSuccess(MetaMethod metaMethod, Object resultObject);

    /**
     * execute subscribe happen exception @{e},callback onError
     *
     * @param t exception
     */
    boolean onError(Throwable t);
}
