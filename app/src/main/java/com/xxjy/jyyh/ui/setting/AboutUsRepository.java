package com.xxjy.jyyh.ui.setting;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.CallCenterBean;
import com.xxjy.jyyh.entity.VersionEntity;

import rxhttp.RxHttp;

public class AboutUsRepository extends BaseRepository {
    public void getCallCenter( MutableLiveData<CallCenterBean> callCenterLiveData){
        addDisposable(RxHttp.postForm(ApiService.CALL_CENTER)
                .asResponse(CallCenterBean.class)
                .subscribe(data -> callCenterLiveData.postValue(data))
        );
    }
    public void checkVersion( MutableLiveData<VersionEntity> checkVersionLiveData){
        addDisposable(RxHttp.postForm(ApiService.CHECK_VERSION)
                .asResponse(VersionEntity.class)
                .subscribe(data -> checkVersionLiveData.postValue(data))
        );
    }
}
