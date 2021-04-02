package com.xxjy.jyyh.app;

import android.app.Application;
import android.content.Context;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.http.HttpManager;
import com.xxjy.jyyh.utils.AppManager;
import com.xxjy.jyyh.utils.ForegroundCallbacks;
import com.xxjy.jyyh.utils.JPushManager;
import com.xxjy.jyyh.utils.X5WebManager;
import com.xxjy.jyyh.utils.symanager.ShanYanManager;
import com.xxjy.jyyh.utils.umengmanager.UMengManager;
import com.xxjy.jyyh.utils.umengmanager.UMengOnEvent;

/**
 * @author power
 * @date 12/1/20 11:05 AM
 * @project RunElephant
 * @description:
 */
public class App extends Application {
    private static Context mContext;
    private static String appChannel = "";  //标记app的渠道

    public static Context getContext() {
        return mContext;
    }


    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            // 指定为经典Header，默认是 贝塞尔雷达Header
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setFinishDuration(0); //.setDrawableSize(20);
        });

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        init();
        initAppStatusListener();
    }
    private void initAppStatusListener() {

        ForegroundCallbacks.init(this).addListener(new ForegroundCallbacks.Listener() {
            @Override
            public void onBecameForeground() {
//                LogUtils.e("前台");
                if(!TextUtils.isEmpty(Constants.HUNTER_GAS_ID)){
                    long backgroundTime = UserConstants.getBackgroundTime();
                    if(System.currentTimeMillis()-backgroundTime>300000){
                        Constants.HUNTER_GAS_ID="";
                        BusUtils.postSticky(EventConstants.EVENT_JUMP_HUNTER_CODE,Constants.HUNTER_GAS_ID);
                    }
                }
            }

            @Override
            public void onBecameBackground() {
//                LogUtils.e("后台");
                if(!TextUtils.isEmpty(Constants.HUNTER_GAS_ID)) {
                    UserConstants.setBackgroundTime(System.currentTimeMillis());
                }
            }
        });
    }

    private void init() {
        //QMUI
        QMUISwipeBackActivityManager.init(this);

        //TODO 上线前记得注释
//        CrashUtils.init(crashInfo -> ToastUtils.showLong("崩溃日志已存储至目录！"));

        //网络请求Rxhttp
        HttpManager.init(this);

        // webview 在调用TBS初始化、创建WebView之前进行如下配置
        X5WebManager.initX5Web(this);

        //初始化闪验sdk
        ShanYanManager.initShanYnaSdk(this);

        appChannel =  UserConstants.getAppChannel();
        if (TextUtils.isEmpty(appChannel)) {
            appChannel = AppManager.getAppMetaChannel();
            UserConstants.setAppChannel(appChannel);
        }

        //标记首次进入app埋点
        UMengOnEvent.onFirstStartEvent();

        //极光推送配置
        JPushManager.initJPush(this);
        //友盟统计
        UMengManager.init(this);
    }

    public static String getAppChannel() {
        return appChannel;
    }

}
