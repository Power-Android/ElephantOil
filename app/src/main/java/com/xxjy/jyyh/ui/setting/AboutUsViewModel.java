package com.xxjy.jyyh.ui.setting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.CallCenterBean;
import com.xxjy.jyyh.entity.VersionEntity;

public class AboutUsViewModel extends BaseViewModel<AboutUsRepository> {

    public AboutUsViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<CallCenterBean> callCenterLiveData = new MutableLiveData<>();
    public MutableLiveData<VersionEntity> checkVersionLiveData = new MutableLiveData<>();
    public void getCallCenter(){
        mRespository.getCallCenter( callCenterLiveData);
    }


    public void checkVersion(){
        mRespository.checkVersion(checkVersionLiveData);
    }

}
