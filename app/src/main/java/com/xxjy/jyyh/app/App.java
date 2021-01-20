package com.xxjy.jyyh.app;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.xxjy.jyyh.http.HttpManager;

/**
 * @author power
 * @date 12/1/20 11:05 AM
 * @project RunElephant
 * @description:
 */
public class App extends Application {
    private static Context mContext;

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
    }

    private void init() {
        //TODO 上线前记得注释
        CrashUtils.init(crashInfo -> ToastUtils.showLong("崩溃日志已存储至目录！"));
        HttpManager.init(this);
        // webview 在调用TBS初始化、创建WebView之前进行如下配置
//        X5WebManager.initX5Web(this);
    }
}
