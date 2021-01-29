package com.xxjy.jyyh.ui.setting;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.CallCenterBean;
import com.xxjy.jyyh.entity.UserBean;

import rxhttp.RxHttp;

public class AboutUsRepository extends BaseRepository {
    public void getCallCenter( MutableLiveData<CallCenterBean> callCenterLiveData){
        addDisposable(RxHttp.postForm(ApiService.CALL_CENTER)
                .asResponse(CallCenterBean.class)
                .subscribe(data -> callCenterLiveData.postValue(data))
        );
    }
}
