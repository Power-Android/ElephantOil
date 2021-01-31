package com.xxjy.jyyh.ui.pay;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.PayResultEntity;

/**
 * @author power
 * @date 1/21/21 11:39 AM
 * @project ElephantOil
 * @description:
 */
public class RefuelingPayResultViewModel extends BaseViewModel<RefuelingPayResultRepository> {
    public MutableLiveData<PayResultEntity> payResultLiveData = new MutableLiveData<>();


    public RefuelingPayResultViewModel(@NonNull Application application) {
        super(application);
    }

    public void getPayResult(String orderNo, String orderPayNo) {
        mRespository.getPayResult(orderNo, orderPayNo, payResultLiveData);
    }

    public void getHomeProduct() {

    }
}
