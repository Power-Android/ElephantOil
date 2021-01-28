package com.xxjy.jyyh.ui.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.IntegralOrderBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;

import java.util.List;

public class OrderDetailsViewModel extends BaseViewModel<OrderDetailsRepository> {

    public OrderDetailsViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<RefuelOrderBean> refuelOrderDetailsLiveData = new MutableLiveData<>();
    public void refuelOrderDetails(String orderId) {
        mRespository.refuelOrderDetails(refuelOrderDetailsLiveData, orderId);
    }
}
