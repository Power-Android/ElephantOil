package com.xxjy.jyyh.ui.broadcast;

import android.content.Context;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2020/5/29.
 */
public class MyJPushMessageReceiver extends JPushMessageReceiver {

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
    }
}
