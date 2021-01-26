package com.xxjy.jyyh.ui.login;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.TextWatcherAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityMobileLoginBinding;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.symanager.ShanYanManager;
import com.xxjy.jyyh.utils.umengmanager.UMengManager;
import com.xxjy.jyyh.wight.MyCountDownTime;

import cn.jpush.android.api.JPushInterface;

public class MobileLoginActivity extends BindingActivity<ActivityMobileLoginBinding,LoginViewModel> {


    private MyCountDownTime mCountDownTime;
    private final int mMaxPhoneLength = 13;
    private int lineGetFocus, lineUnFocus;
    private String mPhoneNumber;
    private static boolean isDown = false;
    private String wxOpenId;
    private String wxUnionId;

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.topLayout);
        lineUnFocus = Color.parseColor("#B8B8B8");
        lineGetFocus = Color.parseColor("#000000");
        mCountDownTime = MyCountDownTime.getInstence(60 * 1000, 1000);

    }

    @Override
    protected void initListener() {
        mBinding.loginGetCode.setOnClickListener(this::onViewClicked);
        mBinding.loginV3Login.setOnClickListener(this::onViewClicked);

        mCountDownTime.setOnTimeCountDownListener(new MyCountDownTime.OnTimeCountDownListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.loginGetCode.setText("已发送 (" + (millisUntilFinished / 1000) + "S)");
            }

            @Override
            public void onFinish() {
                mBinding.loginGetCode.setEnabled(true);
                mBinding.loginGetCode.setText("重新获取");
            }
        });

        mBinding.userPhoneNumber.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                mBinding.loginGetCode.setEnabled(s.toString().trim().length() >= mMaxPhoneLength);
                if (s.toString().length() > 0) {
                    mBinding.registerUserClearPhone.setVisibility(View.VISIBLE);
                } else {
                    mBinding.registerUserClearPhone.setVisibility(View.GONE);
                }
                refreshLoginState();
            }
        });

        mBinding.userPhoneCode.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshLoginState();
            }
        });

        mBinding.userPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus || !TextUtils.isEmpty(mBinding.userPhoneNumber.getTextWithoutSpace())) {
                    mBinding.userPhoneNumberLine.setBackgroundColor(lineGetFocus);
                } else {
                    mBinding.userPhoneNumberLine.setBackgroundColor(lineUnFocus);
                }
            }
        });
        mBinding.userPhoneCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus || !TextUtils.isEmpty(mBinding.userPhoneCode.getText())) {
                    mBinding.userPhoneCodeLine.setBackgroundColor(lineGetFocus);
                } else {
                    mBinding.userPhoneCodeLine.setBackgroundColor(lineUnFocus);
                }
            }
        });
    }

    private void refreshLoginState() {
        String phoneNumber = mBinding.userPhoneNumber.getTextWithoutSpace();
        String codeNumber = mBinding.userPhoneCode.getText().toString();
        if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(codeNumber)
                && phoneNumber.length() >= 11 && codeNumber.length() > 5) {
            mBinding.loginV3Login.setEnabled(true);
        } else {
            mBinding.loginV3Login.setEnabled(false);
        }
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_get_code: //获取验证码
                if (!mCountDownTime.isFinished()) {
                    return;
                }
                mPhoneNumber = mBinding.userPhoneNumber.getTextWithoutSpace();
                if (TextUtils.isEmpty(mPhoneNumber)) {
                    showToastWarning(getResources().getString(R.string.login_null_phone));
                    break;
                }
                if (!RegexUtils.isMobileSimple(mPhoneNumber)) {
                    showToastWarning(getResources().getString(R.string.login_wrong_phone_number));
                    return;
                }
                UiUtils.canClickViewStateDelayed(mBinding.loginGetCode);
                getAutoCode();
                break;
            case R.id.close:        //返回
                onBackPressed();
                break;
            case R.id.login_v3_login:        //登录
                mPhoneNumber = mBinding.userPhoneNumber.getTextWithoutSpace();
                if (TextUtils.isEmpty(mPhoneNumber)) {
                    showToastWarning(getResources().getString(R.string.login_null_phone));
                    break;
                }
                if (!RegexUtils.isMobileSimple(mPhoneNumber)) {
                    showToastWarning(getResources().getString(R.string.login_wrong_phone_number));
                    return;
                }
                String codeNumber = mBinding.userPhoneCode.getText().toString();
                if (TextUtils.isEmpty(codeNumber)) {
                    showToastWarning("请输入验证码");
                    break;
                }

                String inviteNumber = mBinding.invitationEt.getText().toString();
                if (!TextUtils.isEmpty(inviteNumber)) {
                    if (!RegexUtils.isMobileSimple(inviteNumber)) {
                        showToastWarning("请输入正确邀请人手机号");
                        return;
                    }
                }
                UiUtils.canEnableViewStateDelayed(mBinding.loginV3Login);
                loginByCode(codeNumber);
                break;
            case R.id.register_user_clear_phone:        //清空
                mBinding.userPhoneNumber.setText("");
                break;
            case R.id.login_for_wx:        //微信登录
//                loginForWx();
                break;
            case R.id.user_invite_number_layout:        //邀请人手机号
                if (isDown){
                    mBinding.userInviteNumberImgState.animate().setDuration(200).rotation(90).start();
                    mBinding.invitationLl.setVisibility(View.VISIBLE);
                }else {
                    mBinding.userInviteNumberImgState.animate().setDuration(200).rotation(0).start();
                    mBinding.invitationLl.setVisibility(View.GONE);
                }
                break;
            case R.id.layout_1:        //用户政策
                break;
            case R.id.layout_2:        //隐私协议
                break;
            default:
                break;
        }
    }

    private void getAutoCode() {
        mCountDownTime.start();
        mBinding.loginGetCode.setEnabled(false);
        mViewModel.sendCode("2", mPhoneNumber)
                .observe(this, b -> {
                    if (b){
                        showToastSuccess("发送成功");
                    }else {
                        showToastError("发送失败，请重试");
                        mCountDownTime.onFinish();
                    }
                });
    }

    private void loginByCode(String codeNumber) {
        mViewModel.loginByCode(codeNumber, mPhoneNumber, wxOpenId, wxUnionId, UserConstants.getUuid(),
                JPushInterface.getRegistrationID(this),
                mBinding.invitationEt.getText().toString());
    }

    @Override
    protected void dataObservable() {
        mViewModel.loadingView().observe(this, aBoolean -> {
            if (aBoolean) {
                showLoadingDialog();
            } else {
                dismissLoadingDialog();
            }
        });

        mViewModel.mLoginLiveData.observe(this, s -> {
            mCountDownTime.stopCountDown();
            mCountDownTime.cancel();
            mCountDownTime = null;

            mViewModel.setLoginSuccess(s, mPhoneNumber);
        });
    }
}