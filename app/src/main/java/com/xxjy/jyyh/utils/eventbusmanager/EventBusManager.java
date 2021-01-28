package com.xxjy.jyyh.utils.eventbusmanager;


import org.greenrobot.eventbus.EventBus;

/**
 * EventBus管理器,主要提供注册和取消注册的方法,方便入口统一
 *
 * @see {https://github.com/greenrobot/EventBus}
 */
public class EventBusManager {

    /**
     * 注册 ,如果没有注册则进行注册,否则不进行注册
     *
     * @param subscriber 注册者,监听者,接收者,用来接收发布者的信息
     */
    public static void regisiter(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    /**
     * 取消 ,如果注册了就取消,否则不取消
     *
     * @param subscriber 注册者,监听者,接收者,用来接收发布者的信息
     */
    public static void unregister(Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    /**
     * 移除一个粘性事件
     *
     * @param event 事件
     */
    public static void removeStickyEvent(Object event) {
        EventBus.getDefault().removeStickyEvent(event);
    }

    /**
     * 移除所有粘性事件
     */
    public static void removeAllStickyEvents() {
        EventBus.getDefault().removeAllStickyEvents();
    }

    /**
     * 发出一个事件
     *
     * @param event
     */
    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发出一个粘性事件
     *
     * @param event
     */
    public static void postSticky(Object event) {
        EventBus.getDefault().postSticky(event);
    }
}
