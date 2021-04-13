package com.xxjy.jyyh.ui.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.RefuelOrderBean;
import com.xxjy.jyyh.http.Response;

public class OrderDetailsViewModel extends BaseViewModel<OrderDetailsRepository> {

    public OrderDetailsViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<RefuelOrderBean> refuelOrderDetailsLiveData = new MutableLiveData<>();
    public MutableLiveData<RefuelOrderBean> refundOrderDetailsLiveData = new MutableLiveData<>();
    public MutableLiveData<Response> cancelOrderDetailsLiveData = new MutableLiveData<>();
    public MutableLiveData<Response> productCancelOrderDetailsLiveData = new MutableLiveData<>();
    public MutableLiveData<Response> refuelApplyRefundLiveData = new MutableLiveData<>();
    public void refuelOrderDetails(String orderId) {
        mRespository.refuelOrderDetails(refuelOrderDetailsLiveData, orderId);
    }
    public void orderRefundDetail(String orderId) {
        mRespository.orderRefundDetail(refundOrderDetailsLiveData, orderId);
    }
    public void cancelOrder(String orderId) {
        mRespository.cancelOrder(cancelOrderDetailsLiveData, orderId);
    }
    public void productCancelOrder(String orderId) {
        mRespository.productCancelOrder(productCancelOrderDetailsLiveData, orderId);
    }
    public void refuelApplyRefund(String orderId,String refundReason) {
        mRespository.refuelApplyRefund(refuelApplyRefundLiveData, orderId,refundReason);
    }
}
