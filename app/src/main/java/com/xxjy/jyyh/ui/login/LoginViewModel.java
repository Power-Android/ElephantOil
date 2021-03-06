package com.xxjy.jyyh.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ActivityUtils;
import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.entity.RespEntity;
import com.xxjy.jyyh.entity.WeChatLoginBean;
import com.xxjy.jyyh.http.Response;
import com.xxjy.jyyh.utils.symanager.ShanYanManager;
import com.xxjy.jyyh.utils.umengmanager.UMengManager;

/**
 * @author power
 * @date 1/21/21 11:39 AM
 * @project ElephantOil
 * @description:
 */
public class LoginViewModel extends BaseViewModel<LoginRepository> {
    public MutableLiveData<String> mVerifyLoginLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> mCodeLiveData = new MutableLiveData<>();
    public MutableLiveData<String> mLoginLiveData = new MutableLiveData<>();
    public MutableLiveData<WeChatLoginBean> mWechatLoginLiveData = new MutableLiveData<>();
    public MutableLiveData<String> mBindPhoneLiveData = new MutableLiveData<>();
    public MutableLiveData<Response<String>> specStationLiveData = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void verifyLogin(String twinklyToken, String jpushId, String inviteCode,boolean isInvite){
        mRespository.verifyLogin(twinklyToken, jpushId, inviteCode,   isInvite,mVerifyLoginLiveData);
    }

    public LiveData<Boolean> sendCode(String scene, String phoneNumber) {

        return mRespository.sendCode(scene, phoneNumber, mCodeLiveData);
    }

    public void loginByCode(String codeNumber, String phoneNumber, String wxOpenId,
                            String wxUnionId, String uuid, String registrationID, String invitationCode,boolean isInvite) {
        mRespository.loginByCode(codeNumber, phoneNumber, wxOpenId, wxUnionId, uuid,
                registrationID, invitationCode, isInvite, mLoginLiveData);
    }

    public void setLoginSuccess(String token, String mobile){
        UserConstants.setToken(token);
        UserConstants.setMobile(mobile);
        UserConstants.setIsLogin(true);

        UMengManager.onProfileSignIn("userID");
//        JPushManager.postJPushdata();

        ActivityUtils.finishActivity(LoginActivity.class);
        ShanYanManager.finishAuthActivity();
        ActivityUtils.finishActivity(MobileLoginActivity.class);
    }

    public void openId2Login(String openId,String accessToken,boolean isInvite){
        mRespository.openId2Login(mWechatLoginLiveData,openId,accessToken,isInvite);
    }
    public void appBindPhone(String phone,String validCode, String openId,String unionId,String invitationCode,String jpushId,boolean isInvite){
        mRespository.appBindPhone(mBindPhoneLiveData,phone,validCode,openId,unionId,invitationCode,jpushId, isInvite);
    }
    public void getSpecOil(String inviteCode) {
        mRespository.getSpecOil(inviteCode, specStationLiveData);
    }
}
