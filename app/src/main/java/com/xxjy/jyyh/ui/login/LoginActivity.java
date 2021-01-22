package com.xxjy.jyyh.ui.login;


import android.content.Context;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.ShanYanCustomInterface;
import com.google.gson.Gson;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityLoginBinding;
import com.xxjy.jyyh.utils.StatusBarUtil;
import com.xxjy.jyyh.utils.symanager.SYConfigUtils;
import com.xxjy.jyyh.utils.symanager.ShanYanManager;
import com.xxjy.jyyh.utils.umengmanager.UMengLoginWx;

import java.util.Map;

public class LoginActivity extends BindingActivity<ActivityLoginBinding,LoginViewModel> {


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
//            openLoginActivity();
        } else {
//            toLoginForOtherActivity();
        }
    }
    private void toLoginForOtherActivity() {
        if (!ShanYanManager.isShanYanSupport() || !NetworkUtils.isConnected()) {
            finish();
        }
//        LoginForOtherActivity.openLoginOtherAct(this);
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