package com.xxjy.jyyh.ui.web;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityWebChatViewBinding;
import com.xxjy.jyyh.utils.StatusBarUtil;
import com.xxjy.jyyh.utils.UiUtils;


public class WebChatActivity extends BindingActivity<ActivityWebChatViewBinding,WebViewViewModel>
        implements View.OnClickListener {

    private static final String BAR_TITLE = "在线客服聊天";

    //图片
    private final static int FILE_CHOOSER_RESULT_CODE = 128;

    boolean loadingFinished = true;
    boolean redirect = false;

    protected Toolbar mToolbar;
    protected TextView mWebTitle;
    protected ImageView mWebShared;
    protected ImageView mWebCallHelp;
    protected WebView webView;

    protected ProgressBar progressBar;
    protected WebChatActivity INSTENCE;
    protected String url;

    //选择照片相关
    ////5.0及以上使用
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    // 5.0以下使用
    public ValueCallback<Uri> mUploadMessage;


    private void initData() {
        INSTENCE = this;

        StatusBarUtil.setHeightAndPadding(this, mToolbar);

        url = getIntent().getStringExtra("url");
        initListener();

        showLoadingDialog();
        initWebViewSettings();

        webView.setVisibility(View.INVISIBLE);
        webView.loadUrl(url);
        mWebTitle.setText("在线客服");
    }
@Override
    protected void initView() {
        webView = mBinding.webview;
        mToolbar = mBinding.toolbar;
        mWebTitle = mBinding.webTitle;
        mWebShared = mBinding.webShared;
        mWebCallHelp = mBinding.webHelp;
    initData();
    }

    protected void initListener() {
        mToolbar.setNavigationOnClickListener(this);
        mWebShared.setOnClickListener(this);
        mWebCallHelp.setOnClickListener(this);
    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {

    }

    protected void initWebViewSettings() {
        progressBar = (ProgressBar) findViewById(R.id.line_progress);

        initWebViewClient();
        initWebChromeClient();
        UiUtils.initWebView(this, webView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case -1:
                onBackClick();
                break;
        }
    }

    //点击返回按钮
    protected void onBackClick() {
        finish();
    }

    protected void initWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback,
                                             FileChooserParams fileChooserParams) {
                mUploadMessageForAndroid5 = valueCallback;
                openImageChooserActivity();
                return true;
            }

            //
            //获得网页的加载进度，显示在右上角的TextView控件中
            @Override
            public void onProgressChanged(WebView webView, int progress) {
                super.onProgressChanged(webView, progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(progress);//设置进度值
                }
            }

            //处理alert弹出框，html 弹框的一种方式
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(INSTENCE);

                builder.setTitle("对话框")
                        .setMessage(message)
                        .setPositiveButton("确定", null);

                // 不需要绑定按键事件
                // 屏蔽keycode等于84之类的按键
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

    private void openImageChooserActivity() {
        try {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
        } catch (Exception e) {
            setImageResult(null);
            LogUtils.e("error: " + e.getMessage());
        }
    }

    protected void initWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.endsWith("login/") || url.endsWith("login") || url.endsWith("loginPrev/") || url.endsWith("loginPrev")) {
//                    UiUtils.toLoginActivity(WebChatActivity.this, Tool.LOGIN_FINISH);
                    return true;
                }
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                loadingFinished = false;
            }

            @Override
            public void onPageFinished(WebView web, String s) {
                if (!redirect) {
                    loadingFinished = true;
                }

                if (loadingFinished && !redirect) {
                    //HIDE LOADING IT HAS FINISHED
                    String js = "javascript:function displaymodifynone() {" +
                            "document.getElementsByClassName('top')[0].style.display = 'none';" +
                            "}";
                    webView.loadUrl(js);
                    webView.loadUrl("javascript:displaymodifynone()");
                    webView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            webView.setVisibility(View.VISIBLE);
                            dismissLoadingDialog();
                        }
                    }, 1000);
                } else {
                    redirect = false;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == mUploadMessage && null == mUploadMessageForAndroid5) return;
        if (resultCode != RESULT_OK) {//同上所说需要回调onReceiveValue方法防止下次无法响应js方法
            setImageResult(null);
            return;
        }
        Uri result = null;
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (data != null) {
                result = data.getData();
            }
            setImageResult(result);
        }
    }

    private void setImageResult(Uri result) {
        if (mUploadMessageForAndroid5 != null) {
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    //    https://www.jianshu.com/p/13f4b8f70947 , 这段代码备用, 主要用来处理多张图片的情况
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(Intent intent) {
        Uri[] results = null;
        if (intent != null) {
            String dataString = intent.getDataString();
            ClipData clipData = intent.getClipData();
            if (clipData != null) {
                results = new Uri[clipData.getItemCount()];
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    results[i] = item.getUri();
                }
            }
            if (dataString != null)
                results = new Uri[]{Uri.parse(dataString)};
        }
        mUploadMessageForAndroid5.onReceiveValue(results);
        mUploadMessageForAndroid5 = null;
    }

    /**
     * 一般打开web的方法
     *
     * @param context
     * @param url
     */
    public static void openWebChatActivity(Context context, String url) {
        Intent intent = new Intent(context, WebChatActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

}
