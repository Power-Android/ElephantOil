package com.xxjy.jyyh.ui.login;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/21/21 11:40 AM
 * @project ElephantOil
 * @description:
 */
public class MobileLoginRepository extends BaseRepository {

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
                .add("invitePhone", invitationCode, !TextUtils.isEmpty(invitationCode))
                .asResponse(String.class)
                .doOnSubscribe(disposable -> showLoading(true))
                .doFinally(() -> showLoading(false))
                .subscribe(s -> loginLiveData.postValue(s))
        );
    }
}
