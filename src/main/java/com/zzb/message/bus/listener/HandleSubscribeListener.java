package com.zzb.message.bus.listener;


import com.zzb.message.bus.cores.MetaMethod;

/**
 * when handle subscribe complete,bus will execute handleSubscribeLister
 *
 * @author fuzhengjun
 */
public interface HandleSubscribeListener {

    /**
     * execute subscribe is success,callback this methods.
     *
     * @param metaMethod    execute subscribe's com.zzb.message
     * @param messageParams execute Subscribe's parameter
     * @param resultObject  execute Subscribe's result
     */
    void onSuccess(MetaMethod metaMethod, Object[] messageParams, Object resultObject);

    /**
     * execute subscribe happen exception @{e},callback onError
     *
     * @param t exception
     */
    boolean onError(Throwable t);
}
