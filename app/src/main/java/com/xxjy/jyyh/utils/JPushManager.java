package com.xxjy.jyyh.utils;

import android.app.Application;
import android.content.Context;

import com.xxjy.jyyh.app.App;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送的管理器
 */
public class JPushManager {

    /**
     * 初始化极光推送sdk相关，包括推送的初始化和统计的初始化
     *
     * @param application
     */
    public static void initJPush(Application application) {
        initPush(application);
//        initAnalytics(application);
    }

    //初始化推送
    private static void initPush(Application application) {
        JPushInterface.setDebugMode(App.IS_DEBUG);
        JPushInterface.init(application);
    }

    /**
     * 统计界面开始
     *
     * @param context
     * @param pageName
     */
    public static void onPageStart(Context context, String pageName) {
//        JAnalyticsInterface.onPageStart(context, pageName);
    }

    /**
     * 统计界面结束
     *
     * @param context
     * @param pageName
     */
    public static void onPageEnd(Context context, String pageName) {
//        JAnalyticsInterface.onPageEnd(context, pageName);
    }
}
