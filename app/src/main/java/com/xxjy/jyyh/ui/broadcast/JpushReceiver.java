package com.xxjy.jyyh.ui.broadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.WelcomeActivity;
import com.xxjy.jyyh.utils.GsonTool;
import com.xxjy.jyyh.utils.NaviActivityInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by czb365 on 2017/5/27.
 */

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();//cn.jpush.android.intent.NOTIFICATION_OPENED
            LogUtils.iTag(TAG, "intent.getAction()--->" + intent.getAction() + ", extras: " + printBundle(bundle));
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                LogUtils.iTag(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                LogUtils.iTag(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                LogUtils.iTag(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                LogUtils.iTag(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                LogUtils.iTag(TAG, "[MyReceiver] 用户点击打开了通知---->");
                String extraExtra = bundle.getString(JPushInterface.EXTRA_EXTRA);
                LogUtils.iTag(TAG, "extraExtra内容为: " + extraExtra);
                if (TextUtils.isEmpty(extraExtra)) {
                    lunchAppIfNotExist(context);
                } else {
                    try {
                        JPushExtraBean jPushExtraBean = GsonTool.parseJson(extraExtra, JPushExtraBean.class);
                        String link = jPushExtraBean.getLink();
                        if (!TextUtils.isEmpty(link)) {
                            if (AppUtils.isAppForeground() && ActivityUtils.getTopActivity() != null) {
                                Activity topActivity = ActivityUtils.getTopActivity();
                                if (topActivity instanceof BaseActivity) {
                                    BaseActivity baseActivity = (BaseActivity) topActivity;
                                    NaviActivityInfo.disPathIntentFromUrl(baseActivity, link);
                                } else {
                                    lunchAppIfNotExist(context);
                                }
                            } else {
                                if (ActivityUtils.isActivityExistsInStack(MainActivity.class)) {
                                    ActivityUtils.finishToActivity(MainActivity.class, false);
                                    Intent i = new Intent(context, MainActivity.class);  //自定义打开的界面
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.putExtra(MainActivity.TAG_FLAG_INTENT_VALUE_INFO, link);
                                    ActivityUtils.startActivity(i);
                                } else {
                                    Intent i = new Intent(context, WelcomeActivity.class);  //自定义打开的界面
                                    i.putExtra(WelcomeActivity.TYPE_ACT_LINK, link);
                                    ActivityUtils.startActivity(i);
                                }
                            }
                        } else {
                            lunchAppIfNotExist(context);
                        }
                    } catch (Exception e) {
                        lunchAppIfNotExist(context);
                    }
                }
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                LogUtils.iTag(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                LogUtils.iTag(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                LogUtils.iTag(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to ");
            } else {
                LogUtils.iTag(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {
            LogUtils.iTag(TAG, "异常------------》" + intent.getAction());
        }

    }

    private void lunchAppIfNotExist(Context context) {
//        try {
//            Tool.isProcessRunning(context);
//            Intent i = new Intent(context, MessageCenterActivity.class);  //自定义打开的界面
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        } catch (Exception e) {
//            Intent i = new Intent(context, WelcomeActivity.class);  //自定义打开的界面
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.putExtra(WelcomeActivity.TYPE_ACT_LINK, "msg");
//            MainActivity.stateFragment = Tool.LOGIN_TO_MY;
//            context.startActivity(i);
//        }
        if (ActivityUtils.isActivityAlive(context)){
            Intent i = new Intent(context, MainActivity.class);  //自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }else{
            Intent i = new Intent(context, WelcomeActivity.class);  //自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    LogUtils.iTag(TAG,"This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    LogUtils.iTag(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
//
//    //send msg to MainActivity
//    private void processCustomMessage(Context context, Bundle bundle) {
////        if (MainActivity.isForeground) {
////            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
////            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
////            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
////            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
////            if (!ExampleUtil.isEmpty(extras)) {
////                try {
////                    JSONObject extraJson = new JSONObject(extras);
////                    if (extraJson.length() > 0) {
////                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
////                    }
////                } catch (JSONException e) {
////
////                }
////
////            }
////            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
////        }
//    }
}

