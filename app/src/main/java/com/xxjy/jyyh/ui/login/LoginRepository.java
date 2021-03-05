package com.xxjy.jyyh.ui.login;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.DeviceUtils;
import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.entity.WeChatLoginBean;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/21/21 11:40 AM
 * @project ElephantOil
 * @description:
 */
public class LoginRepository extends BaseRepository {

    public void verifyLogin(String twinklyToken, String jpushId, String inviteCode,
                            MutableLiveData<String> verifyLoginLiveData) {
        addDisposable(RxHttp.postForm(ApiService.VERIFY_LOGIN)
                .add("twinklyToken", twinklyToken)
                .add("did", DeviceUtils.getUniqueDeviceId())
                .add("jpushId", jpushId)
                .add("invitePhone", inviteCode,!TextUtils.isEmpty(inviteCode)&&inviteCode.length()==11)
                .add("inviteCode", inviteCode, !TextUtils.isEmpty(inviteCode)&&inviteCode.length()==4)
                .asResponse(String.class)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        verifyLoginLiveData.postValue(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        OneKeyLoginManager.getInstance().setLoadingVisibility(false);
                    }
                })
        );
    }

    public LiveData<Boolean> sendCode(String scene, String phoneNumber, MutableLiveData<Boolean> codeLiveData) {
        addDisposable(RxHttp.postForm(ApiService.GET_CODE)
                .add("scene", scene)
                .add("mobile", phoneNumber)
                .add("openId", TextUtils.isEmpty(UserConstants.getOpenId()) ?
                        "" : UserConstants.getOpenId())
                .asString()
                .subscribe(s -> codeLiveData.postValue(true),
                        throwable -> codeLiveData.postValue(false))
        );
        return codeLiveData;
    }

    public void loginByCode(String codeNumber, String phoneNumber, String wxOpenId,
                            String wxUnionId, String uuid, String registrationID,
                            String invitationCode, MutableLiveData<String> loginLiveData) {
        addDisposable(RxHttp.postForm(ApiService.VERIFY_LOGIN)
                .add("phone", phoneNumber)
                .add("validCode", codeNumber)
                .add("openId", wxOpenId, !TextUtils.isEmpty(wxOpenId))
                .add("unionId", wxUnionId, !TextUtils.isEmpty(wxUnionId))
                .add("did", uuid)
                .add("jpushId", registrationID)
                .add("invitePhone", invitationCode,!TextUtils.isEmpty(invitationCode)&&invitationCode.length()==11)
                .add("inviteCode", invitationCode, !TextUtils.isEmpty(invitationCode)&&invitationCode.length()==4)
                .asResponse(String.class)
                .doOnSubscribe(disposable -> showLoading(true))
                .doFinally(() -> showLoading(false))
                .subscribe(s -> loginLiveData.postValue(s))
        );
    }

    public void openId2Login(MutableLiveData<WeChatLoginBean> mWechatLoginLiveData, String openId, String accessToken){
        addDisposable(RxHttp.postForm(ApiService.WECHAT_LOGIN)
                .add("openId", openId)
                .add("did", DeviceUtils.getUniqueDeviceId())
                .add("accessToken", accessToken)
                .asResponse(WeChatLoginBean.class)
                .subscribe(s -> mWechatLoginLiveData.postValue(s))
        );
    }
    public void appBindPhone(MutableLiveData<String> mBindPhoneLiveData, String phone, String validCode,String openId,String unionId,String invitationCode,String jpushId){
        addDisposable(RxHttp.postForm(ApiService.APP_BIND_PHONE)
                .add("phone", phone)
                .add("validCode", validCode)
                .add("invitePhone", invitationCode,!TextUtils.isEmpty(invitationCode)&&invitationCode.length()==11)
                .add("inviteCode", invitationCode, !TextUtils.isEmpty(invitationCode)&&invitationCode.length()==4)
                .add("openId", TextUtils.isEmpty(openId)?null:openId)
                .add("unionId", TextUtils.isEmpty(unionId)?null:unionId)
                .add("did", DeviceUtils.getUniqueDeviceId())
                .add("jpushId", jpushId)
                .asResponse(String.class)
                .subscribe(s -> mBindPhoneLiveData.postValue(s))
        );
    }
    public void getSpecOil(String inviteCode,
                           MutableLiveData<String> specStationLiveData) {
        addDisposable(RxHttp.postForm(ApiService.GET_SPEC_GAS_ID)
                .add("invitePhone", inviteCode,!TextUtils.isEmpty(inviteCode)&&inviteCode.length()==11)
                .add("inviteCode", inviteCode, !TextUtils.isEmpty(inviteCode)&&inviteCode.length()==4)
                .asResponse(String.class)
                .doOnSubscribe(disposable -> showLoading(true))
                .doFinally(() -> showLoading(false))
                .subscribe(stationsBean -> specStationLiveData.postValue(stationsBean))
        );
    }
}
