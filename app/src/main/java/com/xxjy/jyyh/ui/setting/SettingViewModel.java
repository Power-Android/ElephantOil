package com.xxjy.jyyh.ui.setting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;

public class SettingViewModel extends BaseViewModel<SettingRepository> {

    public SettingViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> logoutLiveData = new MutableLiveData<>();
    public void logout(){
        mRespository.logout( logoutLiveData);
    }

}
