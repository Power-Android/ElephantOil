package com.xxjy.jyyh.ui.web;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.blankj.utilcode.util.LogUtils;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityWebviewBinding;
import com.xxjy.jyyh.entity.SharedInfoBean;
import com.xxjy.jyyh.jscalljava.JsOperation;
import com.xxjy.jyyh.jscalljava.jsbean.ToolBarStateBean;
import com.xxjy.jyyh.jscalljava.jscallback.OnJsCallListener;
import com.xxjy.jyyh.utils.GsonTool;
import com.xxjy.jyyh.utils.JPushManager;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.StatusBarUtil;
import com.xxjy.jyyh.utils.StringUtils;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.umengmanager.SharedInfoManager;


public class WebViewActivity extends BindingActivity<ActivityWebviewBinding,WebViewViewModel>
        implements View.OnClickListener, OnJsCallListener {

    private static final String BAR_TITLE = "WebView";

    private static final int SHARED_RESULT_SUCCESS = 101;
    private static final int SHARED_RESULT_ERROR = 102;
    private static final int SHARED_RESULT_CANCEL = 103;

    private Toolbar mToolbar;
    private TextView mWebTitle;
    private ImageView mWebShared;
    private ImageView mWebCallHelp;
    private ImageView mWebBack;
    private ImageView mWebClose;
    private WebView webView;

    private ProgressBar progressBar;
    private WebViewActivity INSTENCE;
    private boolean isShared = false;   //?????????????????????????????????
    private SharedInfoBean shared;      //???????????????
    private JsOperation mJsOperation;
    private String url;
    private boolean isShouldLoadUrl = false;

    private String mCallPhoneNumber = "";
    private String mLastShowTitle;

    private String mOrderNo;
    private boolean mIsOpenVip = false;
    private int defaultToolBarBgColor, defaultTitleColor;   //????????????????????????title??????
    private int defaultTitleStateColor; //?????????????????????title??????

    private boolean isShouldAutoOpenWeb = false;    //???????????????????????????????????????????????????
    //??????????????????????????????
    private boolean shouldShowSureDialog = false;

    protected void initData() {
        INSTENCE = this;
        StatusBarUtil.setHeightAndPadding(this, mToolbar);
        url = getIntent().getStringExtra("url");
        initListener();
        mJsOperation = new JsOperation(this);
        mJsOperation.setOnJsCallListener(this);
        initWebViewSettings();
        initPayWebViewSettings();

        initSharedInfo(url);
        initDefaultColor();

        webView.loadUrl(url);
        LogUtils.d("url=:" + url);
    }

    private void initDefaultColor() {
        defaultToolBarBgColor = Color.parseColor("#F9F9F9");
        defaultTitleColor = Color.parseColor("#232323");

        defaultTitleStateColor = Color.WHITE;
    }

//    @Override
//    protected String getPageTitle() {
//        return BAR_TITLE;
//    }

    @Override
    protected void initView() {
        webView = mBinding.webview;
        mToolbar = mBinding.toolbar;
        mWebTitle = mBinding.webTitle;
        mWebShared = mBinding.webShared;
        mWebCallHelp = mBinding.webHelp;
        mWebBack = mBinding.webBack;
        mWebClose = mBinding.webClose;
        initData();
    }
    @Override
    protected void initListener() {
        mWebShared.setOnClickListener(this);
        mWebCallHelp.setOnClickListener(this);
        mWebBack.setOnClickListener(this);
        mWebClose.setOnClickListener(this);
    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isShouldLoadUrl()) {
            setShouldLoadUrl(false);
            if (UserConstants.getIsLogin()) {
                webView.reload();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.webview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.webview.onPause();
    }

    public boolean isShouldLoadUrl() {
        return isShouldLoadUrl;
    }

    public void setShouldLoadUrl(boolean shouldLoadUrl) {
        isShouldLoadUrl = shouldLoadUrl;
    }

    private void initSharedInfo(String url) {
        if (TextUtils.isEmpty(url)) return;
        String aesInfo = StringUtils.getUrlDate(url, "nativeShare");
        String sharedInfo = null;
        if (!TextUtils.isEmpty(aesInfo)) {
            sharedInfo = StringUtils.decodePwd(aesInfo);
        }
        if (TextUtils.isEmpty(sharedInfo)) {
            hideSharedIcon();
        } else {
            showSharedIcon(sharedInfo);
        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param phoneNumber
     */
    public void showHelpIcon(String phoneNumber) {
        mCallPhoneNumber = phoneNumber;
        mWebCallHelp.setVisibility(View.VISIBLE);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param shardInfo
     */
    public void showSharedIcon(String shardInfo) {
        isShared = true;
        try {
            shared = GsonTool.parseJson(shardInfo, SharedInfoBean.class);
        } catch (Exception e) {
        }
        if (shared != null) {
            mWebShared.setVisibility(View.VISIBLE);
        }
    }

    /**
     * ???????????????????????????????????????
     */
    public void hideSharedIcon() {
        isShared = false;
        mWebShared.setVisibility(View.GONE);
    }

    /**
     * ?????????????????????
     *
     * @param intentInfo
     */
    public void startAliPay(String intentInfo) {
//        try {
//            PayOrderResponse.DataBean orderResponse = GsonTool.parseJson(intentInfo, PayOrderResponse.DataBean.class);
//            if (orderResponse != null) {
//                disPathPayOrderInfo(orderResponse);
//            }
//        } catch (Exception e) {
//            showToastWarning("????????????");
//        }
    }
    /**
     * ?????????????????????
     *
     * @param intentInfo
     */
    public void startUnionPay(String intentInfo) {
//        try {
//            PayOrderResponse.DataBean orderResponse = GsonTool.parseJson(intentInfo, PayOrderResponse.DataBean.class);
//            if (orderResponse != null) {
//
//                disPathPayOrderInfo(orderResponse);
//            }
//        } catch (Exception e) {
//            showToastWarning("????????????");
//        }
    }

    /**
     * ??????toolbar?????????
     *
     * @param isDefault ???????????????????????????
     * @param bgColor   ?????????, isDefault=false ???????????????
     */
    public void changeToolBarState(boolean isDefault, String bgColor) {
        try {
            if (isDefault) {
                mBinding.toolbar.setBackgroundColor(defaultToolBarBgColor);
//            mBinding.webBack.setImageTintList(ColorStateList.valueOf(defaultBackTintColor));
//            mBinding.webClose.setImageTintList(ColorStateList.valueOf(defaultBackTintColor));
                mBinding.webBack.setImageTintList(null);
                mBinding.webClose.setImageTintList(null);
                mBinding.webHelp.setImageTintList(null);
                mBinding.webShared.setImageTintList(null);
                mBinding.webTitle.setTextColor(defaultTitleColor);
            } else {
                if (TextUtils.isEmpty(bgColor)) {
                    return;
                }
                try {
                    ToolBarStateBean toolBarStateBean = null;
                    if (bgColor.startsWith("#")) {
                        toolBarStateBean = new ToolBarStateBean();
                        toolBarStateBean.setToolBarBgColor(bgColor);
                    } else {
                        toolBarStateBean = ToolBarStateBean.parseFromString(bgColor);
                    }
                    if (toolBarStateBean != null) {
                        String toolBarBgColor = toolBarStateBean.getToolBarBgColor();
                        String toolBarTitleColor = toolBarStateBean.getToolBarTitleColor();
                        int toolBarColor = 0;
                        if (!TextUtils.isEmpty(toolBarBgColor)) {
                            toolBarColor = Color.parseColor(toolBarBgColor);
                            mBinding.toolbar.setBackgroundColor(toolBarColor);
                        }
                        int toolBarTitle = defaultTitleStateColor;
                        if (!TextUtils.isEmpty(toolBarTitleColor)) {
                            toolBarTitle = Color.parseColor(toolBarTitleColor);
                        }
                        mBinding.webTitle.setTextColor(toolBarTitle);
                        mBinding.webBack.setImageTintList(ColorStateList.valueOf(toolBarTitle));
                        mBinding.webClose.setImageTintList(ColorStateList.valueOf(toolBarTitle));
                        mBinding.webHelp.setImageTintList(ColorStateList.valueOf(toolBarTitle));
                        mBinding.webShared.setImageTintList(ColorStateList.valueOf(toolBarTitle));
                    }
                } catch (Exception e) {

                }
            }
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
        }
    }

    protected void initPayWebViewSettings() {
        mBinding.payWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView webView, String url) {
                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true;
                }
                isShouldAutoOpenWeb = false;

                /**
                 * ????????????????????????????????????(payInterceptorWithUrl),??????????????????
                 */
                final PayTask task = new PayTask(INSTENCE);
                boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
                    @Override
                    public void onPayResult(final H5PayResultModel result) {
                        // ??????????????????
                        final String url = result.getReturnUrl();
                        if (!TextUtils.isEmpty(url)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    jumpToPayResultAct();
                                }
                            });
                        }
                    }
                });

                /**
                 * ????????????????????????
                 * ??????????????????????????????????????????URL?????????????????????
                 */
                if (!isIntercepted) {   //???????????????sdk?????????url??????????????????
                    UiUtils.openPhoneWebUrl(WebViewActivity.this, url);
                }
                return true;
            }
        });
    }

    public void setOpenId(String openId){
        // ???????????????
        if(webView==null){
            return;
        }
        webView.loadUrl("javascript:setOpenId('"+openId+"')");
    }

//    private void disPathPayOrderInfo(PayOrderResponse.DataBean data) {
//        mOrderNo = data.getOrderPayNo();
//        mIsOpenVip = data.isOpeningVip();
//        String payType = data.getPayType();
//        int result = data.getResult();
//        if (result == 0) {
//            String url = data.getUrl();
//            String params = GsonTool.toJson(data.getParams());
//            if (PayDialog.PAY_TYPE_ZHIFUBAO.equals(payType) || PayDialog.PAY_TYPE_ZHIFUBAO_2.equals(payType)) {
//                boolean urlCanUse = UiUtils.checkZhifubaoSdkCanPayUrl(this, url, new H5PayCallback() {
//                    @Override
//                    public void onPayResult(H5PayResultModel h5PayResultModel) {
//                        jumpToPayResultAct();
//                    }
//                });
//                if (!urlCanUse) {
//                    isShouldAutoOpenWeb = true;
//                    mBinding.payWebview.loadUrl(url);
//                    mBinding.payWebview.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (isShouldAutoOpenWeb) {
//                                UiUtils.openPhoneWebUrl(com.xxjy.jyyh.activity.WebViewActivity.this, url);
//                            }
//                        }
//                    }, 4000);
//                }
//            } else if (PayDialog.PAY_TYPE_WEIXIN.equals(payType)) {
//                WeChatWebPayActivity.openWebPayAct(this, null, params, url);
//            } else if (PayDialog.PAY_TYPE_WEIXIN_2.equals(payType)) {
//                WeChatWebNowPayActivity.openWebPayAct(this, null, params, url);
//            } else if (PayDialog.PAY_TYPE_LIANLIAN.equals(payType)) {
//                Intent intent = new Intent(this, RechargeWebviewActivity.class);
//                intent.putExtra("url", url);
//                intent.putExtra("param", params);
//                startActivity(intent);
//
//            } else {
//                mBinding.payWebview.loadUrl(url);
//            }
//        }
//    }
//
//    private void jumpToPayResultAct() {
//        if (mIsOpenVip) {
//            PayResultNewActivity.openPayResultAct(INSTENCE,
//                    PayResultNewActivity.ORDER_TYPE_RECHARGE_ONE_CARD, mOrderNo, null);
//        } else {
//            PayResultNewActivity.openPayResultAct(INSTENCE, mOrderNo);
//        }
//    }

    private void initWebViewSettings() {
        progressBar = (ProgressBar) findViewById(R.id.line_progress);

        UiUtils.initWebView(this, webView);

        webView.addJavascriptInterface(mJsOperation, JsOperation.JS_USE_NAME);

        webView.setWebChromeClient(new WebChromeClient() {

            //???????????????????????????????????????????????????TextView?????????
            @Override
            public void onProgressChanged(WebView webView, int progress) {
                super.onProgressChanged(webView, progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);//??????????????????????????????
                } else {
                    progressBar.setVisibility(View.VISIBLE);//????????????????????????????????????
                    progressBar.setProgress(progress);//???????????????
                }
            }

            //??????Web?????????title??????????????????????????????title
            //???????????????????????????????????????????????????onReceiveTitle????????????????????? ??????????????????,
            //?????????????????????onReceiveError??????????????????????????????title
            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                mWebTitle.setText(s);
                if (!TextUtils.isEmpty(s)) {
                    if (!TextUtils.isEmpty(mLastShowTitle)) {
                        JPushManager.onPageEnd(INSTENCE, mLastShowTitle);
                    }
                    JPushManager.onPageStart(INSTENCE, s);
                    mLastShowTitle = s;
                }
            }

            //??????alert????????????html ?????????????????????
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);

                builder.setTitle("?????????")
                        .setMessage(message)
                        .setPositiveButton("??????", null);

                // ???????????????????????????
                // ??????keycode??????84???????????????
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event);
                        return true;
                    }
                });
                // ???????????????back????????????
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                result.confirm();// ???????????????????????????????????????confirm,??????????????????????????????????????????
                return true;
            }

            //??????confirm?????????
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue,
                                      JsPromptResult result) {
                return true;
            }

            //??????prompt?????????
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return true;
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("weixin://wap/pay?")) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        showToastWarning("??????????????????????????????????????????, ????????????????????????");
                    }
                } else if (url.startsWith("alipays://platformapi/startApp?")) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        showToastWarning("????????????????????????????????????????????????, ???????????????????????????");
                    }
                } else if (!url.startsWith("http") && !url.startsWith("https")) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                    }
                    return true;
                }
                if (url.endsWith("account") || url.endsWith("account/")) {
//                    UiUtils.jumpToHome(WebViewActivity.this, Tool.LOGIN_TO_MY);
                    return true;
                } else if (url.endsWith("login/") || url.endsWith("login") || url.endsWith("loginPrev/") || url.endsWith("loginPrev")) {
                    setShouldLoadUrl(true);
//                    UiUtils.toLoginActivity(WebViewActivity.this, Tool.LOGIN_FINISH);
                    return true;
                } else {
                    if (!(url.startsWith("http") || url.startsWith("https"))) {
//                        try {
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                            startActivity(intent);
//                        } catch (ActivityNotFoundException e) {
//                        }
                    } else {
//                        view.loadUrl(url);
                    }
                }
                return false;
//                } else {
//                    return super.shouldOverrideUrlLoading(view, url);
//                }
//                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                refreshCloseIcon();
            }
        });
    }

    private void refreshCloseIcon() {
        mWebClose.setVisibility(webView.canGoBack() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case -1:
            case R.id.web_back:
                onBackClick();
                break;
            case R.id.web_shared:
//                if (Tool.checkUserLoginDialog(this)) {
//                    sharedLink();
//                }
                break;
            case R.id.web_help:
//                Tool.showCallHelpDialog(this, mCallPhoneNumber);
                break;
            case R.id.web_close:
                finish();
                break;
        }
    }

    //??????????????????
    public void onBackClick() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void onJsCallListener(int callType, String content) {
        if (callType == OnJsCallListener.CALL_TYPE_SHARE) {
            shared = GsonTool.parseJson(content, SharedInfoBean.class);
            isShared = true;
            sharedLink();
        }
    }

    private void sharedLink() {
        if (shared != null) {
            String icon = shared.getIcon();
            if (TextUtils.isEmpty(icon)) {
                icon = "";
            }
            SharedInfoManager.shareInfo(this, icon, shared.getLink(),
                    shared.getTitle(), shared.getDesc(), umShareListener);
        }
    }

    //???????????????
    public void addToCalendar(String result) {
//        try {
//            CalendarBean calendarBean = GsonTool.parseJson(result, CalendarBean.class);
//            CalendarEvent event = new CalendarEvent();
//            event.setStartTime(calendarBean.getStartTime());
//            event.setTitle(calendarBean.getName());
//            event.setCouponId(calendarBean.getCouponId());
//            CalendarManager.requestCalendarPermission(INSTENCE, new OnPermissionSuccess() {
//                @Override
//                public void onPermissionSuccess() {
//                    if (CalendarManager.addCalendarEvent(INSTENCE, event)) {
//                        addToCalendarH5();
//                    }
//                }
//
//                @Override
//                public void onPermissionFiler() {
//                }
//            });
//        } catch (Exception e) {
//            LogUtils.e(e.getMessage());
//        }
    }

    //???????????????
    public void deleteFromCalendar(String result) {
//        try {
//            CalendarBean calendarBean = GsonTool.parseJson(result, CalendarBean.class);
//            CalendarManager.requestCalendarPermission(INSTENCE, new OnPermissionSuccess() {
//                @Override
//                public void onPermissionSuccess() {
//                    if (CalendarManager.deleteCalendarEvent(INSTENCE, calendarBean.getCouponId())) {
//                        deleteFromCalendarH5();
//                    }
//                }
//
//                @Override
//                public void onPermissionFiler() {
//                }
//            });
//        } catch (Exception e) {
//            LogUtils.e(e.getMessage());
//        }
    }

    private void deleteFromCalendarH5() {
        String resultH5Method = "javascript:deleteCalendarSuccess()";
        webView.loadUrl(resultH5Method);
    }

    private void addToCalendarH5() {
        String resultH5Method = "javascript:addCalendarSuccess()";
        webView.loadUrl(resultH5Method);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isShared) {
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        }

    }
    private void showSureDialog() {
//        MakeSurePayResultDialog dialog1 = new MakeSurePayResultDialog(this);
//        dialog1.setOnConfirmListener(new MakeSurePayResultDialog.OnConfirm() {
//            @Override
//            public void onConfirm() {
//                jumpToPayResultAct();
//            }
//        });
//        dialog1.show();
    }
    private SharedInfoManager.OnSharedAllResultListener umShareListener = new SharedInfoManager.OnSharedAllResultListener() {
        @Override
        public void onSharedSuccess(SHARE_MEDIA platform) {
            setSharedResult(SHARED_RESULT_SUCCESS, null);
        }

        @Override
        public void onSharedError(SHARE_MEDIA platform, Throwable t) {
            setSharedResult(SHARED_RESULT_ERROR, t.getMessage());
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            setSharedResult(SHARED_RESULT_CANCEL, null);
        }
    };

    private void setSharedResult(int resultType, String reason) {
        String resultToast = "";
        String resultH5Method = "";
        String resultId = shared.getId();
        if (TextUtils.isEmpty(resultId)) {
            resultId = "";
        }
        switch (resultType) {
            case SHARED_RESULT_SUCCESS:
                resultToast = " ???????????? ";
                resultH5Method = "javascript:shareSuccess(" + resultId + ")";
                break;
            case SHARED_RESULT_ERROR:
                resultToast = " ???????????? " + reason;
                resultH5Method = "javascript:sharefail(" + resultId + ")";
                break;
            case SHARED_RESULT_CANCEL:
                resultToast = " ????????????";
                break;
        }
        final String finalResultToast = resultToast;
        final String finalResultH5Method = resultH5Method;
        final String finalResultId = resultId;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(finalResultId)) {   //???h5?????????
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(finalResultH5Method)) {
                                webView.loadUrl(finalResultH5Method);
                            }
                        }
                    });
                } else {
                    showToastSuccess(finalResultToast);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mJsOperation != null) {
            mJsOperation.onDestory();
        }
        clearWebView(mBinding.webview);
        clearWebView(mBinding.payWebview);
        if (!TextUtils.isEmpty(mLastShowTitle)) {
            JPushManager.onPageEnd(INSTENCE, mLastShowTitle);
        }
        super.onDestroy();
    }

    private void clearWebView(WebView webview) {
        try {
            if (webview != null) {
                if (null != webview.getParent()) {
                    ((ViewGroup) webview.getParent()).removeView(webview);
                }
                webview.removeAllViews();
                webview.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * ????????????web?????????,??????url??????????????????
     *
     * @param activity
     * @param url
     */
    public static void openWebActivity(BaseActivity activity, String url) {
        NaviActivityInfo.disPathIntentFromUrl(activity, url);
    }

    /**
     * ????????????web?????????,?????????????????????url??????
     *
     * @param activity
     * @param url
     */
    public static void openRealUrlWebActivity(BaseActivity activity, String url) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }
}
