package com.xxjy.jyyh.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.BannerPositionConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityWelcomeBinding;
import com.xxjy.jyyh.dialog.LocationTipsDialog;
import com.xxjy.jyyh.dialog.PrivacyAgreementDialog;
import com.xxjy.jyyh.ui.integral.BannerViewModel;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.GlideUtils;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.locationmanger.GPSUtil;
import com.xxjy.jyyh.utils.locationmanger.MapLocationHelper;
import com.xxjy.jyyh.utils.umengmanager.UMengOnEvent;

public class WelcomeActivity extends BindingActivity<ActivityWelcomeBinding,BannerViewModel> implements PermissionUtils.SimpleCallback {
    private static final String BAR_TITLE = "启动页";

    public static final int PERMISSION_REQUEST_CODE = 1;

    private static final int MSG_WHAT_DISPATH_INFO = 0;         //分发信息
    private static final int MSG_WHAT_TRY_SHOW_GUIDE = 1;       //尝试展示引导
    private static final int MSG_WHAT_TRY_SHOW_AD = 2;          //尝试展示广告
    private static final int MSG_WHAT_COUNT_DOWN_TIME = 3;      //倒计时显示

    public static final String TYPE_ACT_LINK = "act";           //打开该界面的tag

    private int mSeconds = 4;   //倒计时的秒数

    private ImageView mWelcomeAd;
    private TextView mWelcomeAdTv;

    private boolean isFirstIn = true;
    private boolean isAdClick = false;  //是否是ad点击
    private boolean isShowAd = false;  //是否展示ad广告
    private boolean isDisPathInfo = false;  //是否已经处理信息了

    private String mAdImageUrl;
    private String mAdLinkInfo;     //跳转信息

    private boolean isShownYSXY = false;    //是否展示过隐私协议

    private AlphaAnimation mAlphaAnimation;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WHAT_DISPATH_INFO:
                    if (!isDisPathInfo) {
                        isDisPathInfo = true;
                        handler.removeMessages(MSG_WHAT_DISPATH_INFO);
                        handler.removeMessages(MSG_WHAT_TRY_SHOW_GUIDE);
                        handler.removeMessages(MSG_WHAT_TRY_SHOW_AD);
                        handler.removeMessages(MSG_WHAT_COUNT_DOWN_TIME);
                        if (isAdClick) {
                            if (!TextUtils.isEmpty(mAdLinkInfo)) {
                                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                intent.putExtra(MainActivity.TAG_FLAG_INTENT_VALUE_INFO, mAdLinkInfo);
                                startActivity(intent);
                                UMengOnEvent.onWelcomAd(mAdLinkInfo);
                            } else {
                                toMainActivity();
                            }
                        } else {
                            toMainActivity();
                        }
                        finish();
                    }
                    break;
                case MSG_WHAT_TRY_SHOW_GUIDE:
                    String link = getIntent().getStringExtra(TYPE_ACT_LINK);
                    if (!TextUtils.isEmpty(link)) {     //如果是极光推送打开的app优先展示消息,不展示广告
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        if ("msg".equals(link)) {
                            intent.putExtra(MainActivity.TAG_FLAG_INTENT_VALUE_INFO, NaviActivityInfo.NATIVE_TO_MSG_CENTER);
                        } else {
                            intent.putExtra(MainActivity.TAG_FLAG_INTENT_VALUE_INFO, link);
                        }
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    } else {
                        isShowAd = !TextUtils.isEmpty(mAdImageUrl);
                        if (isShowAd) {
                            handler.sendEmptyMessage(MSG_WHAT_TRY_SHOW_AD);
                        } else {
                            handler.sendEmptyMessage(MSG_WHAT_DISPATH_INFO);
                        }
                    }
                    break;
                case MSG_WHAT_TRY_SHOW_AD:
                    showAd();
                    break;
                case MSG_WHAT_COUNT_DOWN_TIME:
                    mWelcomeAdTv.setText("跳过 (" + mSeconds + "秒)");
                    if (mSeconds <= 0) {
                        handler.sendEmptyMessage(MSG_WHAT_DISPATH_INFO);
                    } else {
                        mSeconds--;
                        handler.sendEmptyMessageDelayed(MSG_WHAT_COUNT_DOWN_TIME, 1000);
                    }
                    break;
            }
        }
    };

    private void toMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(0, 0);
    }

    @Override
    protected void initView() {
        mWelcomeAd = (ImageView) findViewById(R.id.welcome_ad);
        mWelcomeAdTv = (TextView) findViewById(R.id.welcome_ad_tv);
        ImageView headerImg = (ImageView) findViewById(R.id.splash_img_header);
        BarUtils.addMarginTopEqualStatusBarHeight(headerImg);
        mAlphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.alpha_welcome_ad_in);
        mWelcomeAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowAd) {
                    isAdClick = true;
                    mSeconds = 0;
                    handler.sendEmptyMessage(MSG_WHAT_DISPATH_INFO);
                }
            }
        });
        mWelcomeAdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSeconds = 0;
                handler.sendEmptyMessage(MSG_WHAT_DISPATH_INFO);
            }
        });


        isShownYSXY = UserConstants.getAgreePrivacy();
        if (isShownYSXY) {
            getAdInfo();
        } else {
            showYSXYDialog();
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {

    }

    private void showYSXYDialog() {
        PrivacyAgreementDialog dialog = new PrivacyAgreementDialog(this);
        dialog.getCancel().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.getConfirm().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UserConstants.setAgreePrivacy(true);
                isShownYSXY = true;
                getAdInfo();
                requestPermission();
            }
        });
        dialog.show();
    }

    private void getAdInfo() {
        mViewModel.getBannerOfPostion(BannerPositionConstants.APP_START_AD).observe(this,data ->{
            if(data!=null&&data.size()>0){
                mAdImageUrl = data.get(0).getImgUrl();
                mAdLinkInfo =data.get(0).getLink();
            }
            handler.sendEmptyMessageDelayed(MSG_WHAT_TRY_SHOW_GUIDE, 2000);
        });
    }

    private void showAd() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GlideUtils.loadImage(WelcomeActivity.this, mAdImageUrl, mWelcomeAd);
                            mWelcomeAdTv.setVisibility(View.VISIBLE);
                            mWelcomeAdTv.startAnimation(mAlphaAnimation);
                            handler.sendEmptyMessage(MSG_WHAT_COUNT_DOWN_TIME);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(MSG_WHAT_COUNT_DOWN_TIME);
                }
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isShownYSXY) {
            requestPermission();
        }
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        PermissionUtils.permission(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .callback(this)
                .request();
    }



    private void next() {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        MapLocationHelper.refreshLocation();
        handler.sendEmptyMessageDelayed(MSG_WHAT_TRY_SHOW_GUIDE, 2000);
    }

    @Override
    public void onGranted() {
        next();
    }

    @Override
    public void onDenied() {
        if (GPSUtil.isOpenGPS(this)) {
            if (isFirstIn) {
                isFirstIn = false;
//                AndPermissionManager.showSettingDialog(this, 2, null,
//                        "您取消了一些必要权限,有些产品功能将无法使用,是否配置权限?", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                next();
//                            }
//                        });
//
            } else {
                next();
            }
        } else {
            showToastWarning("软件需要对您的位置进行定位,建议您打开 GPS 定位获得更好的体验");
            next();
        }
    }
}