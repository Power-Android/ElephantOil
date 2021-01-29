package com.xxjy.jyyh.ui.setting;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;

import rxhttp.RxHttp;

public class SettingRepository extends BaseRepository {
    public void logout( MutableLiveData<String> logoutLiveData){
        addDisposable(RxHttp.postForm(ApiService.USER_LOGOUT)
                .asResponse(String.class)
                .subscribe(data -> logoutLiveData.postValue(data))
        );
    }
}
