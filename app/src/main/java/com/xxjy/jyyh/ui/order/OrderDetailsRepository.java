package com.xxjy.jyyh.ui.order;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.RefuelOrderBean;


import rxhttp.RxHttp;

public class OrderDetailsRepository extends BaseRepository {
    public void refuelOrderDetails(MutableLiveData<RefuelOrderBean> refuelOrderDetailsLiveData, String orderId){
        addDisposable(RxHttp.postForm(ApiService.REFUEL_ORDER_DETAILS)
                .add("orderId",orderId)
                .asResponse(RefuelOrderBean.class)
                .subscribe(data -> refuelOrderDetailsLiveData.postValue(data))
        );
    }
}
