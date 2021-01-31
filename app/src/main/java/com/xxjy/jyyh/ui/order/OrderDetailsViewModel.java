package com.xxjy.jyyh.ui.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.IntegralOrderBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;
import com.xxjy.jyyh.http.Response;

import java.util.List;

public class OrderDetailsViewModel extends BaseViewModel<OrderDetailsRepository> {

    public OrderDetailsViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<RefuelOrderBean> refuelOrderDetailsLiveData = new MutableLiveData<>();
    public MutableLiveData<Response> cancelOrderDetailsLiveData = new MutableLiveData<>();
    public MutableLiveData<Response> productCancelOrderDetailsLiveData = new MutableLiveData<>();
    public void refuelOrderDetails(String orderId) {
        mRespository.refuelOrderDetails(refuelOrderDetailsLiveData, orderId);
    }
    public void cancelOrder(String orderId) {
        mRespository.cancelOrder(cancelOrderDetailsLiveData, orderId);
    }
    public void productCancelOrder(String orderId) {
        mRespository.productCancelOrder(productCancelOrderDetailsLiveData, orderId);
    }
}
