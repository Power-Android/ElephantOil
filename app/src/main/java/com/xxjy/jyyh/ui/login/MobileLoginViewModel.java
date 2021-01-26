package com.xxjy.jyyh.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.ui.MainRepository;

/**
 * @author power
 * @date 1/21/21 11:39 AM
 * @project ElephantOil
 * @description:
 */
public class MobileLoginViewModel extends BaseViewModel<MobileLoginRepository> {

    public MutableLiveData<Boolean> mCodeLiveData = new MutableLiveData<>();
    public MutableLiveData<String> mLoginLiveData = new MutableLiveData<>();

    public MobileLoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> sendCode(String scene, String phoneNumber) {

        return mRespository.sendCode(scene, phoneNumber, mCodeLiveData);
    }

    public void loginByCode(String codeNumber, String phoneNumber, String wxOpenId,
                            String wxUnionId, String uuid, String registrationID, String invitationCode) {
        mRespository.loginByCode(codeNumber, phoneNumber, wxOpenId, wxUnionId, uuid,
                registrationID, invitationCode, mLoginLiveData);
    }


}
