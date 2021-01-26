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
public class LoginViewModel extends BaseViewModel<LoginRepository> {
    public MutableLiveData<String> mVerifyLoginLiveData = new MutableLiveData<>();


    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void verifyLogin(String twinklyToken, String jpushId, String inviteCode){
        mRespository.verifyLogin(twinklyToken, jpushId, inviteCode,  mVerifyLoginLiveData);
    }
}
