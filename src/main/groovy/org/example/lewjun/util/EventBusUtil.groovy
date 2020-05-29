package org.example.lewjun.util

import com.google.common.eventbus.EventBus

class EventBusUtil {
    private static final EventBus EVENTBUS = new EventBus({ throwable, context ->
    })

    /**
     * 注册
     *
     * @param object
     */
    static void register(final Object object) {
        EVENTBUS.register(object)
    }

    /**
     * 反注册
     *
     * @param object
     */
    static void unregister(final Object object) {
        EVENTBUS.unregister(object)
    }

    /**
     * 发消息
     *
     * @param event
     */
    static void post(final Object event) {
        EVENTBUS.post(event)
    }
}
