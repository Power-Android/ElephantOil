package com.xxjy.jyyh.ui.login;


import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.OneKeyLoginListener;
import com.chuanglan.shanyan_sdk.listener.OpenLoginAuthListener;
import com.chuanglan.shanyan_sdk.listener.ShanYanCustomInterface;
import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityLoginBinding;
import com.xxjy.jyyh.utils.GsonTool;
import com.xxjy.jyyh.utils.StatusBarUtil;
import com.xxjy.jyyh.utils.symanager.SYConfigUtils;
import com.xxjy.jyyh.utils.symanager.ShanYanManager;
import com.xxjy.jyyh.utils.umengmanager.UMengLoginWx;

import java.util.Map;

public class LoginActivity extends BindingActivity<ActivityLoginBinding,LoginViewModel> {

    private boolean isOpenAuth = false;     //是否已经调起了登录


    @Override
    protected void initView() {
        StatusBarUtil.setHeightAndPadding(this, mBinding.toolbar);

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
                    })
                    , null);
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
//                        loginNormal(shanYanResultBean.getToken());
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

    private void toLoginForOtherActivity() {
        if (!ShanYanManager.isShanYanSupport() || !NetworkUtils.isConnected()) {
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
//                    checkUserWxInfo(openId, accessToken);
                }
            }
        });
    }
}