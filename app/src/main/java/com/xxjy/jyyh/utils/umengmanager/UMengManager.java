package com.xxjy.jyyh.utils.umengmanager;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.blankj.utilcode.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.constants.Constants;

/**
 * umeng 管理器
 */

public class UMengManager {

    /**
     * 初始化umeng统计
     *
     * @param context
     */
    public static void init(Context context) {
        //友盟分享
        PlatformConfig.setWeixin(Constants.WX_APP_ID, Constants.WX_APP_SCRIPT);
        PlatformConfig.setWXFileProvider("com.xxjy.jyyh.file.provider");
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, null);

        //使用auto模式不再需要Activity中的代码埋点
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        setDebug(context, Constants.IS_DEBUG);
        setCatchUncaughtExceptions(!Constants.IS_DEBUG);
//        setCatchUncaughtExceptions(true);
    }

    /**
     * 是否调试状态
     */
    private static void setDebug(Context context, boolean isDebug) {
        UMConfigure.setLogEnabled(isDebug);
        if (isDebug) {
            String testDeviceInfo = UmengDebugConfig.getTestDeviceInfo(context);
        }
    }

    public static String getChannelName(Context context){
        if (context == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        //此处这样写的目的是为了在debug模式下也能获取到渠道号，如果用getString的话只能在Release下获取到。
                        channelName = applicationInfo.metaData.get("UMENG_CHANNEL") + "";
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 调用kill或者exit之类的方法杀死进程，请务必在此之前调用onKillProcess(Context context)方法，用来保存统计数据。
     *
     * @param context
     */
    public static void onKillProcess(Context context) {
        MobclickAgent.onKillProcess(context);
    }

    /**
     * 在每个Activity的onResume方法中调用 MobclickAgent.onResume(Context),
     * <p>
     * onPause方法中调用 MobclickAgent.onPause(Context)
     *
     * @param context
     */
    public static void onResume(Activity context) {
//        MobclickAgent.onResume(context);
    }

    public static void onPause(Activity context) {
//        MobclickAgent.onPause(context);
    }

    /**
     * 需要统计应用自身的账号登录情况，请使用以下接口：
     *
     * @param id
     */
    public static void onProfileSignIn(String id) {
        MobclickAgent.onProfileSignIn("userID");
    }

    /**
     * 账号登出时需调用此接口，调用之后不再发送账号相关内容。
     */
    public static void onProfileSignOff() {
        MobclickAgent.onProfileSignOff();
    }

    /**
     * SDK通过Thread.UncaughtExceptionHandler 捕获程序崩溃日志，并在程序下次启动时发送到服务器。
     * 如不需要错误统计功能，可通过此方法关闭
     *
     * @param b
     */
    public static void setCatchUncaughtExceptions(boolean b) {
        MobclickAgent.setCatchUncaughtExceptions(b);
    }

    /**
     * 如果开发者自己捕获了错误，需要上传到友盟+服务器可以调用下面方法：
     *
     * @param mContext
     * @param ex
     */
    public static void reportError(Context mContext, Throwable ex) {
        if (!Constants.IS_DEBUG) {
            MobclickAgent.reportError(mContext, ex);//异常发送友盟服务器
        }
    }
}
