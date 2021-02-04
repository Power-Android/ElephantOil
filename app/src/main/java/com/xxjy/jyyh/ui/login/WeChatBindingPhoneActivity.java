package com.xxjy.jyyh.ui.login;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.RegexUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.TextWatcherAdapter;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityWeChatBindingPhoneBinding;
import com.xxjy.jyyh.utils.UiUtils;

public class WeChatBindingPhoneActivity extends BindingActivity<ActivityWeChatBindingPhoneBinding, LoginViewModel> {


    private String mPhoneNumber;
    private String inviteNumber;
    private static boolean isDown = false;

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.toolbar);
    }

    @Override
    protected void initListener() {
        mBinding.userPhoneNumber.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
//                if (!TextUtils.isEmpty(s)) {
//                    mBinding.userPhoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeBig);
//                } else {
//                    mBinding.userPhoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeSmall);
//                }
                if (s.toString().length() > 0) {
                    mBinding.registerUserClearPhone.setVisibility(View.VISIBLE);
                } else {
                    mBinding.registerUserClearPhone.setVisibility(View.GONE);
                }
                refreshLoginState();
            }
        });
        mBinding.close.setOnClickListener(this::onViewClicked);
        mBinding.registerUserClearPhone.setOnClickListener(this::onViewClicked);
        mBinding.loginGetCode.setOnClickListener(this::onViewClicked);
        mBinding.userInviteNumberLayout.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:        //返回
                finish();
                break;
            case R.id.register_user_clear_phone:        //清空
                mBinding.userPhoneNumber.setText("");
                break;
            case R.id.login_get_code:

                mPhoneNumber = mBinding.userPhoneNumber.getText().toString();
                if (TextUtils.isEmpty(mPhoneNumber)) {
                    showToastWarning(getResources().getString(R.string.login_null_phone));
                    break;
                }
                if (!RegexUtils.isMobileSimple(mPhoneNumber)) {
                    showToastWarning(getResources().getString(R.string.login_wrong_phone_number));
                    return;
                }
                inviteNumber = mBinding.invitationEt.getText().toString();
                if (!TextUtils.isEmpty(inviteNumber)) {
                    if (inviteNumber.length() == 4 || inviteNumber.length() == 11) {

                    } else {
                        showToastWarning("请输入正确邀请人");
                        return;
                    }
                }


                UiUtils.canClickViewStateDelayed(mBinding.loginGetCode);
                getAutoCode();
//                InputAutoActivity.openInputAutoCodeAct(WeChatBindingPhoneActivity.this);
                break;
            case R.id.user_invite_number_layout:        //邀请人手机号
                if (isDown) {
                    mBinding.userInviteNumberImgState.animate().setDuration(200).rotation(90).start();
                    mBinding.invitationLl.setVisibility(View.GONE);
                    isDown = false;
                } else {
                    mBinding.userInviteNumberImgState.animate().setDuration(200).rotation(0).start();
                    mBinding.invitationLl.setVisibility(View.VISIBLE);
                    isDown = true;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.mCodeLiveData.observe(this,data->{
            if (data) {
                showToastSuccess("发送成功");
                InputAutoActivity.TAG_LOGIN_PHONE_NUMBER = mPhoneNumber;
                InputAutoActivity.INVITE_CODE = inviteNumber;
                InputAutoActivity.openInputAutoCodeAct(WeChatBindingPhoneActivity.this);
            } else {
                showToastWarning("发送失败");
            }
        });
    }
    private void getAutoCode() {

        mViewModel.sendCode("10",mPhoneNumber);
    }
    private void refreshLoginState() {
        String phoneNumber = mBinding.userPhoneNumber.getText().toString();
        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() > 10) {
            mBinding.loginGetCode.setEnabled(true);
        } else {
            mBinding.loginGetCode.setEnabled(false);
        }
    }

    public static void openBindingWxAct(BaseActivity activity) {
        Intent intent = new Intent(activity, WeChatBindingPhoneActivity.class);
        activity.startActivity(intent);
    }
}