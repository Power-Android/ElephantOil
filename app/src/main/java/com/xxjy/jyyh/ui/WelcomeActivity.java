package com.xxjy.jyyh.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.xxjy.jyyh.PrivacyActivity;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.BannerPositionConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityWelcomeBinding;
import com.xxjy.jyyh.dialog.PrivacyAgreementDialog;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.ui.integral.BannerViewModel;
import com.xxjy.jyyh.utils.GlideUtils;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.X5WebManager;
import com.xxjy.jyyh.utils.locationmanger.GPSUtil;
import com.xxjy.jyyh.utils.locationmanger.MapLocationHelper;
import com.xxjy.jyyh.utils.umengmanager.UMengOnEvent;
import com.xxjy.jyyh.wight.MyCountDownTime;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.SubscriberMethod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

// BindingActivity<ActivityWelcomeBinding, BannerViewModel>
public class WelcomeActivity extends BindingActivity<ActivityWelcomeBinding, BannerViewModel>
        implements PermissionUtils.SingleCallback {
    private static final String BAR_TITLE = "启动页";

    public static final int PERMISSION_REQUEST_CODE = 1;

    private static final int MSG_WHAT_DISPATH_INFO = 0;         //分发信息
    private static final int MSG_WHAT_TRY_SHOW_GUIDE = 1;       //尝试展示引导
    private static final int MSG_WHAT_TRY_SHOW_AD = 2;          //尝试展示广告

    public static final String TYPE_ACT_LINK = "act";           //打开该界面的tag


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

    private Observable<Integer> mObservable;
    private ObservableEmitter<Integer> mEmitter;

    private MyCountDownTime mCountDownTime;


    private void toMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }


    @Override
    protected void initView() {
        BarUtils.setNavBarVisibility(this, false);
        mWelcomeAd = (ImageView) findViewById(R.id.welcome_ad);
        mWelcomeAdTv = (TextView) findViewById(R.id.welcome_ad_tv);
        int startFrom = getIntent().getIntExtra("startFrom", 0);
        UserConstants.setStartFrom(String.valueOf(startFrom));
        mAlphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(this, R.anim.alpha_welcome_ad_in);
        mCountDownTime = MyCountDownTime.getInstence(3 * 1000, 1000);
        mCountDownTime.setOnTimeCountDownListener(new MyCountDownTime.OnTimeCountDownListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                mWelcomeAdTv.setText("跳过 (" + (millisUntilFinished / 1000 + 1) + "秒)");
            }

            @Override
            public void onFinish() {
                toMainActivity();
            }
        });

        isShownYSXY = UserConstants.getAgreePrivacy();
        if (isShownYSXY) {
            initAD();
        } else {
            showYSXYDialog();
        }


        mObservable = Observable.create(new ObservableOnSubscribe<Integer>() {//create创建产生的实例类型ObservableCreate<T>
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                mEmitter = emitter;
//                emitter.onComplete();
            }
        });
        mObservable.subscribeOn(Schedulers.trampoline())
                .observeOn(AndroidSchedulers.mainThread()) //observeOn创建产生的实例类型ObservableObserveOn<T>
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Integer s) {
                        switch (s) {
                            case MSG_WHAT_DISPATH_INFO:
                                if (!isDisPathInfo) {
                                    isDisPathInfo = true;
                                    if (isAdClick) {
                                        if (!TextUtils.isEmpty(mAdLinkInfo)) {
                                            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                            intent.putExtra(MainActivity.TAG_FLAG_INTENT_VALUE_INFO, mAdLinkInfo);
                                            startActivity(intent);
                                            UMengOnEvent.onWelcomAd(mAdLinkInfo);
                                        } else {
                                            toMainActivity();
//                                            mEmitter.onComplete();
                                        }
                                    } else {
                                        toMainActivity();
//                                        mEmitter.onComplete();
                                    }

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
                                        LogUtils.e("isShowAd", TimeUtils.getNowString());
                                        mEmitter.onNext(MSG_WHAT_TRY_SHOW_AD);
                                    } else {
                                        mEmitter.onNext(MSG_WHAT_DISPATH_INFO);
                                    }
                                }
                                break;
                            case MSG_WHAT_TRY_SHOW_AD:
                                initAD();
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        toMainActivity();
                    }

                    @Override
                    public void onComplete() {
                        toMainActivity();
                    }
                });



        mWelcomeAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowAd) {
                    isAdClick = true;
                    mEmitter.onNext(MSG_WHAT_DISPATH_INFO);
                }
            }
        });
        mWelcomeAdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmitter.onNext(MSG_WHAT_DISPATH_INFO);
            }
        });


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
                AppUtils.exitApp();
            }
        });
        dialog.getConfirm().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                UserConstants.setAgreePrivacy(true);
                isShownYSXY = true;

                requestPermission();
            }
        });
        dialog.show();
    }



    private void initAD() {
        String json = UserConstants.getSplashScreenAd();
        if (TextUtils.isEmpty(json)) {
//            mEmitter.onNext(MSG_WHAT_DISPATH_INFO);
            toMainActivity();
        } else {
            Gson gson = new Gson();
            BannerBean bannerBean = gson.fromJson(json, BannerBean.class);

            if (bannerBean != null) {
                loadBanner(bannerBean);
            } else {
                toMainActivity();
//                mEmitter.onNext(MSG_WHAT_DISPATH_INFO);
            }
        }
    }

    private void loadBanner(BannerBean bannerBean) {
        if (bannerBean != null && TimeUtils.getNowMills() > TimeUtils.string2Date(bannerBean.getStartTime()).getTime() && TimeUtils.getNowMills() < TimeUtils.string2Date(bannerBean.getEndTime()).getTime()) {
            mAdImageUrl = bannerBean.getImgUrl();
            mAdLinkInfo = bannerBean.getLink();
            GlideUtils.loadImage(WelcomeActivity.this, mAdImageUrl, mWelcomeAd);
            mWelcomeAdTv.setVisibility(View.VISIBLE);
            mWelcomeAdTv.startAnimation(mAlphaAnimation);
            mCountDownTime.start();
        } else {
            mEmitter.onNext(MSG_WHAT_DISPATH_INFO);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (isShownYSXY) {
//            requestPermission();
            next();
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
        MapLocationHelper.refreshLocation();
        mEmitter.onNext(MSG_WHAT_TRY_SHOW_GUIDE);
    }


    @Override
    protected void onDestroy() {
        if (mCountDownTime != null) {
            mCountDownTime.cancel();
            mCountDownTime = null;
        }
        super.onDestroy();
    }

    @Override
    public void callback(boolean isAllGranted, @NonNull @NotNull List<String> granted, @NonNull @NotNull List<String> deniedForever, @NonNull @NotNull List<String> denied) {
        next();
    }
}