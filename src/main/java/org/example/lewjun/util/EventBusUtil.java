package org.example.lewjun.util;

import com.google.common.eventbus.EventBus;

/**
 * EventBus 工具类
 *
 * @author LewJun
 */
public class EventBusUtil {
    private static final EventBus EVENTBUS = new EventBus((throwable, context) -> {
    });

    /**
     * 注册
     *
     * @param object
     */
    public static void register(final Object object) {
        EVENTBUS.register(object);
    }

    /**
     * 反注册
     *
     * @param object
     */
    public static void unregister(final Object object) {
        EVENTBUS.unregister(object);
    }

    /**
     * 发消息
     *
     * @param event
     */
    public static void post(final Object event) {
        EVENTBUS.post(event);
    }
}
