package com.xxjy.jyyh.ui.order;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.RefuelOrderBean;
import com.xxjy.jyyh.http.Response;

import rxhttp.RxHttp;
import rxhttp.wrapper.exception.ParseException;

public class OrderDetailsRepository extends BaseRepository {
    public void refuelOrderDetails(MutableLiveData<RefuelOrderBean> refuelOrderDetailsLiveData, String orderId){
        addDisposable(RxHttp.postForm(ApiService.REFUEL_ORDER_DETAILS)
                .add("orderId",orderId)
                .asResponse(RefuelOrderBean.class)
                .subscribe(data -> refuelOrderDetailsLiveData.postValue(data))
        );
    }
    public void orderRefundDetail(MutableLiveData<RefuelOrderBean> refundOrderDetailsLiveData, String orderId){
        addDisposable(RxHttp.postForm(ApiService.ORDER_REFUND_DETAILS)
                .add("id",orderId)
                .asResponse(RefuelOrderBean.class)
                .subscribe(data -> refundOrderDetailsLiveData.postValue(data))
        );
    }
    public void cancelOrder(MutableLiveData<Response> cancelOrderDetailsLiveData, String orderId){
        addDisposable(RxHttp.postForm(ApiService.REFUEL_ORDER_CANCEL)
                .add("orderId",orderId)
                .asCodeResponse(Response.class)
                .subscribe(data -> cancelOrderDetailsLiveData.postValue(data))
        );
    }
    public void productCancelOrder(MutableLiveData<Response> productCancelOrderDetailsLiveData, String orderId){
        addDisposable(RxHttp.postForm(ApiService.PRODUCT_ORDER_CANCEL)
                .add("orderId",orderId)
                .asCodeResponse(Response.class)
                .subscribe(data -> productCancelOrderDetailsLiveData.postValue(data))
        );
    }
    public void refuelApplyRefund(MutableLiveData<Response> refuelApplyRefundLiveData, String orderId,String refundReason){
        addDisposable(RxHttp.postForm(ApiService.REFUEL_APPLY_REFUND)
                .add("orderId",orderId)
                .add("refundReason",refundReason)
                .asCodeResponse(Response.class)
                .subscribe(data -> refuelApplyRefundLiveData.postValue(data))
        );
    }
}
