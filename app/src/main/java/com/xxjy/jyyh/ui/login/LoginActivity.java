package com.xxjy.jyyh.ui.login;


import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.OneKeyLoginListener;
import com.chuanglan.shanyan_sdk.listener.OpenLoginAuthListener;
import com.chuanglan.shanyan_sdk.listener.ShanYanCustomInterface;
import com.chuanglan.shanyan_sdk.tool.ShanYanUIConfig;
import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityLoginBinding;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.utils.GsonTool;
import com.xxjy.jyyh.utils.JPushManager;
import com.xxjy.jyyh.utils.StatusBarUtil;
import com.xxjy.jyyh.utils.symanager.SYConfigUtils;
import com.xxjy.jyyh.utils.symanager.ShanYanManager;
import com.xxjy.jyyh.utils.umengmanager.UMengLoginWx;
import com.xxjy.jyyh.utils.umengmanager.UMengManager;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BindingActivity<ActivityLoginBinding, LoginViewModel> {

    private boolean isOpenAuth = false;     //是否已经调起了登录
    public static int loginState = -1;

    @Override
    protected void initView() {
        StatusBarUtil.setHeightAndPadding(this, mBinding.toolbar);
        OneKeyLoginManager.getInstance().setLoadingVisibility(false);
        tryOpenLoginActivity();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {
        mViewModel.mVerifyLoginLiveData.observe(this, s -> mViewModel.setLoginSuccess(s, ""));
        mViewModel.mWechatLoginLiveData.observe(this, data -> {
            if (data == null) {
                showToastWarning("登录失败,请使用其他登录方式");
                return;
            }
            String token = data.getToken();
            String openId = data.getOpenId();
            String unionId = data.getUnionId();
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(openId)) {
//                            loginToMain(token, "", openId);
                if (!TextUtils.isEmpty(openId)) {
                    UserConstants.setOpenId(openId);
                }
                UserConstants.setToken(token);
                UserConstants.setIsLogin(true);
                UMengManager.onProfileSignIn("userID");
                JPushManager.postJPushdata();
                SYConfigUtils.inviteCode = "";
                if (loginState == Constants.LOGIN_FINISH) {
                    finish();
                    return;
                }
                MainActivity.openMainActAndClearTask(LoginActivity.this);

            } else if (!TextUtils.isEmpty(openId) && !TextUtils.isEmpty(unionId)) {
                showToast("关联微信成功,请您绑定手机号");
                InputAutoActivity.TAG_LOGIN_WXOPENID = openId;
                InputAutoActivity.TAG_LOGIN_UNIONID = unionId;
                WeChatBindingPhoneActivity.openBindingWxAct(LoginActivity.this);
            } else {
                showToastWarning("登录失败,请使用其他登录方式");
            }

        });
    }

    private void tryOpenLoginActivity() {
        if (ShanYanManager.isShanYanSupport() && NetworkUtils.isConnected()) {
            OneKeyLoginManager.getInstance().setAuthThemeConfig(
                    SYConfigUtils.getCJSConfig(this, new ShanYanCustomInterface() {
                        @Override
                        public void onClick(Context context, View view) {
                            toLoginForOtherActivity();
                        }
                    }, new ShanYanCustomInterface() {
                        @Override
                        public void onClick(Context context, View view) {
                            if (view.getId() == R.id.login_for_wx) {
                                loginForWx();
                            }
                        }
                    }), null);
            openLoginActivity();
        } else {
            toLoginForOtherActivity();
        }
    }

    private void openLoginActivity() {
        //拉取授权页方法
        OneKeyLoginManager.getInstance().openLoginAuth(false, new OpenLoginAuthListener() {
            @Override
            public void getOpenLoginAuthStatus(int code, String result) {
                if (1000 == code) {
                    isOpenAuth = true;
//                    Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shanyan_dmeo_fade_out_anim);
//                    defultRl.startAnimation(animation);
//                    defultRl.setVisibility(View.GONE);
                    //拉起授权页成功
                    LogUtils.e("拉起授权页成功： _code==" + code + "   _result==" + result);
                } else {
                    //拉起授权页失败
                    LogUtils.e("拉起授权页失败： _code==" + code + "   _result==" + result);
                    toLoginForOtherActivity();
                }
            }
        }, new OneKeyLoginListener() {
            @Override
            public void getOneKeyLoginStatus(int code, String result) {
                if (1011 == code) {
//                    Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.shanyan_demo_fade_in_anim);
//                    defultRl.startAnimation(animation);
//                    defultRl.setVisibility(View.VISIBLE);
                    isOpenAuth = false;
                    finish();
//                    LogUtils.e("用户点击授权页返回： _code==" + code + "   _result==" + result);
//                    return;
                } else if (1000 == code) {
                    LogUtils.e("用户点击登录获取token成功： _code==" + code + "   _result==" + result);
                    //OneKeyLoginManager.getInstance().setLoadingVisibility(false);
                    //AbScreenUtils.showToast(getApplicationContext(), "用户点击登录获取token成功");
                    try {
                        SYConfigUtils.ShanYanResultBean shanYanResultBean =
                                GsonTool.parseJson(result, SYConfigUtils.ShanYanResultBean.class);


                        verifyNormal(shanYanResultBean.getToken());
                    } catch (Exception e) {
                        showToast("登录失败,请重试或者选择其他登录方式");
                    }
                } else {
//                    LogUtils.e("用户点击登录获取token失败： _code==" + code + "   _result==" + result);
                    showToast("登录失败,请重试或者选择其他登录方式");
                }
                isOpenAuth = false;
                long startTime = System.currentTimeMillis();
//                ShanYanManager.finishAuthActivity();
//                startResultActivity(code, result, startTime);
            }
        });
    }

    private void verifyNormal(String token) {


        if (!TextUtils.isEmpty(SYConfigUtils.inviteCode)) {
            if (SYConfigUtils.inviteCode.length() == 4 || SYConfigUtils.inviteCode.length() == 11) {

            } else {
                showToastWarning("请输入正确邀请人");
                return;
            }
        }
        mViewModel.verifyLogin(token, JPushInterface.getRegistrationID(this),
                SYConfigUtils.inviteCode);
    }

    private void toLoginForOtherActivity() {
        if (!ShanYanManager.isShanYanSupport() || !NetworkUtils.isConnected()) {
            OneKeyLoginManager.getInstance().finishAuthActivity();

            finish();
        }
        startActivity(new Intent(this, MobileLoginActivity.class));
    }

    private void loginForWx() {
        UMengLoginWx.loginFormWx(this, new UMengLoginWx.UMAuthAdapter() {
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                LogUtils.e(new Gson().toJson(map));
                if (map != null && map.containsKey("openid") && map.containsKey("accessToken")) {
                    String openId = map.get("openid");
                    String accessToken = map.get("accessToken");
                    openId2Login(openId, accessToken);
                }
            }
        });
    }

    private void openId2Login(String openId, String accessToken) {
        mViewModel.openId2Login(openId, accessToken);
    }

    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.bottom_dialog_enter, R.anim.bottom_dialog_exit);
    }
}