package com.xxjy.jyyh.ui.web;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.databinding.ActivityWeChatWebPayBinding;
import com.xxjy.jyyh.jscalljava.JsOperation;
import com.xxjy.jyyh.utils.StatusBarUtil;
import com.xxjy.jyyh.utils.UiUtils;

import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WeChatWebPayActivity extends BindingActivity<ActivityWeChatWebPayBinding, WebViewViewModel> {

    private static final String BAR_TITLE = "现代支付界面";

    private static final String TAG_PAY_LOAD_JS = "load_js";

    private String mPayLoadJs;
    private WeChatWebPayActivity instence;
    private boolean isFirstLoad = false;
    private boolean isFirstUse = true;
    private boolean isFirstWriteJs = true;

    private String HTTP_URL = Constants.HTTP_CALL_BACK_URL;

    @Override
    protected void initView() {
        instence = this;
        StatusBarUtil.setHeightAndPadding(this, mBinding.toolbar);
        UiUtils.initWebView(mBinding.webview);
        Intent intent = getIntent();
        String resultJs = intent.getStringExtra(TAG_PAY_LOAD_JS);
//        mPayLoadJs = intent.getStringExtra(TAG_PAY_LOAD_JS);
        String first = "<script type=\"text/javascript\">location.href=\"";
        String second = "\"</script>";
        mPayLoadJs = first + resultJs + second;

        mBinding.webview.addJavascriptInterface(new JsOperation(this), JsOperation.JS_USE_NAME);

        initWebChromeClient();
        initWebViewClient();

        LogUtils.d(mPayLoadJs);

        //该方法可以使用,但是shouldOverrideUrlLoading需要返回true,否则会出现不显示支付信息,商家参数格式有误的情况
        //使用返回true的方式推荐本方法
        mBinding.webview.loadUrl("file:///android_asset/weixin.html");
    }

    @Override
    protected void initListener() {
        mBinding.webBack.setOnClickListener(this::onViewClicked);

    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.web_back:
                finish();
                break;
        }
    }

    @Override
    protected void dataObservable() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        mBinding.webview.onResume();
        if (!isFirstUse) {
            finish();
        } else {
            isFirstUse = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.webview.onPause();
    }

    private void initWebViewClient() {
        mBinding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isFirstWriteJs) {
                    isFirstWriteJs = false;
                    mBinding.webview.loadUrl("javascript:document.write('" + mPayLoadJs + "');");
//                    mBinding.webview.loadUrl("javascript:" + mPayLoadJs);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url.endsWith("login/") || url.endsWith("login") || url.endsWith("loginPrev/") || url.endsWith("loginPrev")) {
                    UiUtils.toLoginActivity(WeChatWebPayActivity.this, 1);
                    return true;
                }
                Map<String, String> headers = new HashMap<>();
                if (url.contains("wx.tenpay.com")) {
                    //wx.tenpay.com 收银台点击微信时，shouldOverrideUrlLoading会调用两次，这里是第二次
                    headers.put("Referer", HTTP_URL);//第三方支付平台请求头 一般是对方固定
                } else {
                    //payh5.bbnpay
                    if (!isFirstLoad) {
                        //跳转到收银台
                        headers.put("Referer", HTTP_URL);//商户申请H5时提交的授权域名
                        isFirstLoad = true;
                    } else {
                        //收银台点击微信时，shouldOverrideUrlLoading会调用两次，这里是第一次
                        headers.put("Referer", HTTP_URL);//第三方支付平台请求头 一般是对方固定
                    }
                }
                LogUtils.d("shouldOverrideUrlLoading>>" + url);
                doSchemeJump(url, headers);
                return true;
//                return super.shouldOverrideUrlLoading(webView, url);
            }
        });
    }

    /**
     * 根据scheme 协议作出响应跳转是跳系统浏览器还是应用内页面还是用webView 打开
     */
    public void doSchemeJump(String linkUrl, Map<String, String> headers) {
        try {
            LogUtils.d("doSchemeJump>>" + linkUrl);
            if (!TextUtils.isEmpty(linkUrl)) {
                Uri uri = Uri.parse(linkUrl);
                String scheme = uri.getScheme();

                if (scheme.equals("http") || scheme.equals("https")) {
                    LogUtils.d("doSchemeJump>>>loadUrl>>" + linkUrl);
                    loadUrl(linkUrl, uri, headers);
                } else {
                    // 调用系统浏览器
                    LogUtils.d("doSchemeJump>>>loadUrl>>" + linkUrl);
                    openBrowser(linkUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUrl(String linkUrl, Uri uri, Map<String, String> headers) {
        Bundle bundle = parseExtras(uri);
        if (bundle != null) {
            if (bundle.containsKey("scheme")) {
                String scheme = bundle.getString("scheme");
                if (scheme != null && scheme.startsWith("alipays")) {
                    String schemeUrl = URLDecoder.decode(scheme);
                    try {
                        LogUtils.d("loadUrl>>open>>" + linkUrl);
                        open(schemeUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                        openBrowser(linkUrl);
                        finish();
                    }
                    return;
                }
            }
        }
        LogUtils.d("loadUrl>>webview加载url>>" + linkUrl);

//        mBinding.webview.loadUrl(linkUrl, headers);

        if (("4.4.3".equals(android.os.Build.VERSION.RELEASE))
                || ("4.4.4".equals(android.os.Build.VERSION.RELEASE))) {
            //兼容这两个版本设置referer无效的问题
            mBinding.webview.loadDataWithBaseURL(HTTP_URL,
                    "<script>window.location.href=\"" + linkUrl + "\";</script>",
                    "text/html", "utf-8", null);
        } else {
            Map<String, String> extraHeaders = new HashMap<>();
            extraHeaders.put("Referer", HTTP_URL);
            mBinding.webview.loadUrl(linkUrl, extraHeaders);
        }
    }

    private void openBrowser(String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void open(String url) throws URISyntaxException {
        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
        intent.addCategory("android.intent.category.BROWSABLE");
        intent.setComponent(null);
        startActivity(intent);
    }

    public static Bundle parseExtras(Uri uri) {
        Bundle extras = null;
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        for (String key : queryParameterNames) {
            String value = uri.getQueryParameter(key);
            if (extras == null) {
                extras = new Bundle();
            }
            extras.putString(key, value);
        }

        return extras;
    }


    private void initWebChromeClient() {
        mBinding.webview.setWebChromeClient(new com.tencent.smtt.sdk.WebChromeClient() {
            //
            //获得网页的加载进度，显示在右上角的TextView控件中
            @Override
            public void onProgressChanged(WebView webView, int progress) {
                super.onProgressChanged(webView, progress);
                if (progress == 100) {
                    mBinding.lineProgress.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    mBinding.lineProgress.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    mBinding.lineProgress.setProgress(progress);//设置进度值
                }
            }
            //获取Web页中的title用来设置自己界面中的title
            //当加载出错的时候，比如无网络，这时onReceiveTitle中获取的标题为 找不到该网页,
            //因此建议当触发onReceiveError时，不要使用获取到的title

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                mBinding.webPayTitle.setText(s);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(instence);
                builder.setTitle("对话框")
                        .setMessage(message)
                        .setPositiveButton("确定", null);
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event);
                        return true;
                    }
                });
                // 禁止响应按back键的事件
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
                return true;
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        try {
            if (mBinding.webview != null) {
                if (null != mBinding.webview.getParent()) {
                    ((ViewGroup) mBinding.webview.getParent()).removeView(mBinding.webview);
                }
                mBinding.webview.removeAllViews();
                mBinding.webview.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public static void openWebPayAct(Activity activity, String loadJs) {
        Intent intent = new Intent(activity, WeChatWebPayActivity.class);
        if (!TextUtils.isEmpty(loadJs)) {
            intent.putExtra(TAG_PAY_LOAD_JS, loadJs);
        }
        activity.startActivity(intent);
    }
}