package com.xxjy.jyyh.ui.order;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.IntegralOrderBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;

import java.util.List;

import rxhttp.RxHttp;

public class OrderListRepository extends BaseRepository {
    public void refuelOrderList(MutableLiveData<List<RefuelOrderBean>> refuelOrderListLiveData, int status, int pageNum, int pageSize){
        addDisposable(RxHttp.postForm(ApiService.REFUEL_ORDER_LIST)
                .add("status",status==-1?"":status)
                .add("pageNum",pageNum)
                .add("pageSize",pageSize)
                .asResponseList(RefuelOrderBean.class)
                .subscribe(data -> refuelOrderListLiveData.postValue(data))
        );
    }
    public void integralOrderList(MutableLiveData<List<IntegralOrderBean>> integralOrderListLiveData, int status, int pageNum, int pageSize){
        addDisposable(RxHttp.postForm(ApiService.INTEGRAL_ORDER_LIST)
                .add("status",status==-1?"":status)
                .add("pageNum",pageNum)
                .add("pageSize",pageSize)
                .asResponseList(IntegralOrderBean.class)
                .subscribe(data -> integralOrderListLiveData.postValue(data))
        );
    }
}
