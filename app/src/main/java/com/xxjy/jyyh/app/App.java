package com.xxjy.jyyh.app;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.xxjy.jyyh.constants.SPConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.http.HttpManager;
import com.xxjy.jyyh.utils.AppManager;
import com.xxjy.jyyh.utils.X5WebManager;

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


    /**
     * 服务器连接url配置
     */
    public static final boolean URL_IS_DEBUG = true;   //测试用这个
//    public static final boolean URL_IS_DEBUG = false;   //正式上线用这个

    /**
     * 配置debug模式
     */
    public static final boolean IS_DEBUG = true;  //测试用这个
    //    public static final boolean IS_DEBUG = false;   //正式上线用这个
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
    }

    private void init() {
        QMUISwipeBackActivityManager.init(this);
        //TODO 上线前记得注释
        CrashUtils.init(crashInfo -> ToastUtils.showLong("崩溃日志已存储至目录！"));
        HttpManager.init(this);
        // webview 在调用TBS初始化、创建WebView之前进行如下配置
        X5WebManager.initX5Web(this);

        appChannel =  UserConstants.getAppChannel();
        if (TextUtils.isEmpty(appChannel)) {
            appChannel = AppManager.getAppMetaChannel();
            UserConstants.setAppChannel(appChannel);
        }
    }

    public static String getAppChannel() {
        return appChannel;
    }

}
