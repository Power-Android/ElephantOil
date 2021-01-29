package com.xxjy.jyyh.ui.setting;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.CallCenterBean;

import rxhttp.RxHttp;

public class SettingRepository extends BaseRepository {
    public void logout( MutableLiveData<String> logoutLiveData){
        addDisposable(RxHttp.postForm(ApiService.CALL_CENTER)
                .asResponse(String.class)
                .subscribe(data -> logoutLiveData.postValue(data))
        );
    }
}
