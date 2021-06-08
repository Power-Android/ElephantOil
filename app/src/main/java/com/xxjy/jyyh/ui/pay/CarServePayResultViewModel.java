package com.xxjy.jyyh.ui.pay;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.CarServeOrderBean;
import com.xxjy.jyyh.entity.OrderNewsEntity;
import com.xxjy.jyyh.entity.PayResultEntity;

/**
 * @author power
 * @date 1/21/21 11:39 AM
 * @project ElephantOil
 * @description:
 */
public class CarServePayResultViewModel extends BaseViewModel<CarServePayResultRepository> {

    public CarServePayResultViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<PayResultEntity> payResultLiveData = new MutableLiveData<>();
    public MutableLiveData<CarServeOrderBean> orderLiveData = new MutableLiveData<>();

    public void getPayResult(String orderNo, String orderPayNo) {
        mRespository.getPayResult(orderNo, orderPayNo, payResultLiveData);
    }
    public void getOrderInfo(String orderNo) {
        mRespository.getOrderInfo(orderNo, orderLiveData);
    }



}
