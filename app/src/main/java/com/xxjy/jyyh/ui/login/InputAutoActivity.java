package com.xxjy.jyyh.ui.login;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityInputAutoBinding;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.umengmanager.UMengManager;
import com.xxjy.jyyh.wight.MyCountDownTime;
import com.xxjy.jyyh.wight.autocodeedittextview.VerificationCodeInput;

import cn.jpush.android.api.JPushInterface;

public class InputAutoActivity extends BindingActivity<ActivityInputAutoBinding, LoginViewModel> {

    public static String TAG_LOGIN_WXOPENID;   //微信openid
    public static String TAG_LOGIN_UNIONID;   //微信unionId
    public static String TAG_LOGIN_PHONE_NUMBER;   //手机号

    private MyCountDownTime time;

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.toolbar);
        mBinding.inputAutoCodeText.setTextColor(getResources().getColor(R.color.colorAccent));
        mBinding.inputAutoCodeText.setTextSize(getResources().getDimensionPixelOffset(R.dimen.sp_20));
        mBinding.inputAutoCodeText.setCursorDrawable(R.drawable.cursor_drawable);

        time = MyCountDownTime.getInstence(60 * 1000, 1000);
        mBinding.sendSmsPhoneNumber.setText(TAG_LOGIN_PHONE_NUMBER);
        time.start();
    }

    @Override
    protected void initListener() {
        time.setOnTimeCountDownListener(new MyCountDownTime.OnTimeCountDownListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.autoCodeShowState.setVisibility(View.GONE);
                mBinding.inputAutoCodeGet.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                mBinding.inputAutoCodeGet.setText("重新发送");
                mBinding.autoCodeShowState.setVisibility(View.VISIBLE);
            }
        });
        mBinding.inputAutoCodeText.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                bindPhone(content);
            }
        });

        mBinding.close.setOnClickListener(this::onViewClicked);
        mBinding.inputAutoCodeGet.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.input_auto_code_get:  //获取验证码
                if (time.isFinished()) {
                    if (!NetworkUtils.isConnected()) {
                        showToastError("请检查网络配置");
                        return;
                    }
                    UiUtils.canClickViewStateDelayed(mBinding.inputAutoCodeGet);
                    getAutoCode();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.mBindPhoneLiveData.observe(this, data -> {
            if (!TextUtils.isEmpty(data)) {
                time.cancel();

                if (!TextUtils.isEmpty(TAG_LOGIN_WXOPENID)) {
                    UserConstants.setOpenId(TAG_LOGIN_WXOPENID);
                }
                UserConstants.setToken(data);UserConstants.setIsLogin(true);
                UMengManager.onProfileSignIn("userID");
//        Tool.postJPushdata();
                MainActivity.openMainActAndClearTask(InputAutoActivity.this);

            } else {
                mBinding.inputAutoCodeText.clearAllText();
                mBinding.inputAutoCodeText.setEnabled(true);
                showToastWarning("绑定失败，请再次尝试");
            }
        });
    }

    private SpannableStringBuilder getSpanString(long time) {
        return new SpanUtils()
                .append("重新发送(" + time + "s)").setForegroundColor(Color.parseColor("#F80235"))
                .create();
    }

    private void getAutoCode() {

        mViewModel.sendCode("10", TAG_LOGIN_PHONE_NUMBER).observe(this, data -> {
            if (data) {
                showToastSuccess("发送成功");
            } else {
                showToastWarning("发送失败");
            }
        });
    }

    private void bindPhone(String autoCode) {
        mViewModel.appBindPhone(TAG_LOGIN_PHONE_NUMBER, autoCode, TAG_LOGIN_WXOPENID, TAG_LOGIN_UNIONID, JPushInterface.getRegistrationID(this));
    }

    /**
     * 打开输入验证码界面的方法
     *
     * @param activity
     */
    public static void openInputAutoCodeAct(Activity activity) {
        Intent intent = new Intent(activity, InputAutoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (time != null) {
            time.cancel();
            time = null;
        }
        super.onDestroy();

    }
}