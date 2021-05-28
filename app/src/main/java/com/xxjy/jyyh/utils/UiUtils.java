
package com.xxjy.jyyh.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.http.Response;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.login.LoginActivity;

import java.io.File;

public class UiUtils {
    @SuppressLint("SetJavaScriptEnabled")
    public static void initWebView(WebView webView) {
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);

        //有图：正常加载显示所有图片 https://x5.tencent.com/docs/webview.html
        webSetting.setLoadsImagesAutomatically(true);
        webSetting.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//            webSetting.setMixedContentMode(WebSettings.LOAD_NORMAL);
        }


        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSetting.setDefaultTextEncodingName("utf-8");
//        webView.clearCache(true);
    }

    public static void initWebView(Context context, WebView webView) {
        initWebView(webView);
        WebSettings webSetting = webView.getSettings();
        webSetting.setAppCacheEnabled(true);
        File webCacheFile = new File(context.getCacheDir(), "web_cache");
        File webDataFile = new File(context.getCacheDir(), "web_database");
        File webGeoLocFile = new File(context.getCacheDir(), "web_geo_loc");
        webSetting.setAppCachePath(webCacheFile.getPath());
        webSetting.setDatabasePath(webDataFile.getPath());
        webSetting.setGeolocationDatabasePath(webGeoLocFile.getPath());
    }

    /**
     * 检测url连接是否可以使用支付宝sdk进行支付
     *
     * @return
     */
    public static boolean checkZhifubaoSdkCanPayUrl(Activity activity, String url, H5PayCallback callback) {
        boolean result = false;
        if (TextUtils.isEmpty(url)) return false;
        if (url.startsWith("http") || url.startsWith("https")) {
            /**
             * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
             */
            final PayTask task = new PayTask(activity);
            result = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
                @Override
                public void onPayResult(final H5PayResultModel result) {
                    // 支付结果返回
                    final String url = result.getReturnUrl();
                    if (!TextUtils.isEmpty(url)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onPayResult(result);
                                }
                            }
                        });
                    }
                }
            });
        }
        return result;
    }

    public static boolean checkWebUrl(BaseActivity activity, String url) {
        if (url.startsWith("weixin://wap/pay?")) {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                activity.startActivity(intent);
            } catch (Exception e) {
                activity.showToastWarning("未安装微信或者微信版本不支持, 不可使用微信支付");
            }
            return true;
        } else if (url.startsWith("alipays://platformapi/")) {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                activity.startActivity(intent);
            } catch (Exception e) {
                activity.showToastWarning("未安装支付宝或者支付宝版本不支持, 不可使用支付宝支付");
            }
            return true;
        } else if (!url.startsWith("http") && !url.startsWith("https")) {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                activity.startActivity(intent);
            } catch (Exception e) {
            }
            return true;
        }
        return false;
    }

    //打开手机的url链接
    public static void openPhoneWebUrl(BaseActivity activity, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            activity.startActivity(intent);
        } catch (Exception e) {
            activity.showToastWarning("数据处理失败，请联系客服");
        }
    }


    /**
     * View 防止抖动,即防止多次点击
     *
     * @param view
     * @param mills
     */
    public static void canClickViewStateDelayed(final View view, long mills) {
        view.setClickable(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        }, mills);
    }

    /**
     * View防止抖动,默认2S以后可以点击
     *
     * @param view
     */
    public static void canClickViewStateDelayed(View view) {
        canClickViewStateDelayed(view, 2000);
    }

    /**
     * View 防止抖动,即防止多次点击
     *
     * @param view
     * @param mills
     */
    public static void canEnableViewStateDelayed(final View view, long mills) {
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, mills);
    }

    /**
     * View防止抖动,默认2S以后可以使用
     *
     * @param view
     */
    public static void canEnableViewStateDelayed(View view) {
        canEnableViewStateDelayed(view, 2000);
    }


    /**
     * 跳转至login登录界面
     *
     * @param context
     */
    public static void toLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转至login登录界面
     *
     * @param context
     */
    public static void toLoginActivity(Context context, int state) {
        Intent intent = new Intent(context, LoginActivity.class);
        LoginActivity.loginState = state;
        context.startActivity(intent);
    }
    /**
     * 跳转至login登录界面  邀请好友
     *
     * @param context
     */
    public static void toLoginActivity(Context context, int state,boolean isInvite) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("invite",isInvite);
        LoginActivity.loginState = state;
        context.startActivity(intent);
    }
    /**
     * 跳转首页的方法
     *
     * @param jumpState {@link 0} 首页
     *                  {@link 1} 加油页
     *                  {@link 2} 积分权益
     *                  {@link 3} 我的
     */
    public static void jumpToHome(BaseActivity activity, int jumpState) {
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            if (jumpState == Constants.TYPE_HOME) {
                mainActivity.showFragment(jumpState);
            } else if (jumpState == Constants.TYPE_OIL) {
                mainActivity.showFragment(jumpState);
            } else if (jumpState == Constants.TYPE_INTEGRAL) {
                mainActivity.showFragment(jumpState);
            } else if(jumpState == Constants.TYPE_MINE){
                mainActivity.showFragment(jumpState);
            }
        } else {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("jumpState",jumpState);
            activity.startActivity(intent);
        }
    }



}
