package com.xxjy.jyyh.ui.order;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.IntegralOrderBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;

import java.util.List;

import rxhttp.RxHttp;

public class OrderListRepository extends BaseRepository {
    public void refuelOrderList(MutableLiveData<List<RefuelOrderBean>> paymentOrderListLiveData, int status, int pageNum, int pageSize){
        addDisposable(RxHttp.postForm(ApiService.REFUEL_ORDER_LIST)
                .add("status",status==-1?"":status)
                .add("pageNum",pageNum)
                .add("pageSize",pageSize)
                .asResponseList(RefuelOrderBean.class)
                .subscribe(data -> paymentOrderListLiveData.postValue(data))
        );
    }
    public void orderRefundList(MutableLiveData<List<RefuelOrderBean>> refundOrderListLiveData, int status, int pageNum, int pageSize){
        addDisposable(RxHttp.postForm(ApiService.REFUND_ORDER_LIST)
                .add("refundStatus",status==-1?"":status)
                .add("pageNum",pageNum)
                .add("pageSize",pageSize)
                .asResponseList(RefuelOrderBean.class)
                .subscribe(data -> refundOrderListLiveData.postValue(data))
        );
    }
    public void integralOrderList(MutableLiveData<List<RefuelOrderBean>> integralOrderListLiveData, int status, int pageNum, int pageSize){
        addDisposable(RxHttp.postForm("http://192.168.1.84:8833/api/product/v1/queryOrders")
                .add("status",status==-1?"":status)
                .add("pageNum",pageNum)
                .add("pageSize",pageSize)
                .asResponseList(RefuelOrderBean.class)
                .subscribe(data -> integralOrderListLiveData.postValue(data))
        );
    }
    public void lifeOrderList(MutableLiveData<List<RefuelOrderBean>> lifeOrderListLiveData, int status, int pageNum, int pageSize){
        addDisposable(RxHttp.postForm(ApiService.GET_STORE_ORDER_LIST)
                .add("status",status==-1?"":status)
                .add("pageNum",pageNum)
                .add("pageSize",pageSize)
                .asResponseList(RefuelOrderBean.class)
                .subscribe(data -> lifeOrderListLiveData.postValue(data))
        );
    }
}
