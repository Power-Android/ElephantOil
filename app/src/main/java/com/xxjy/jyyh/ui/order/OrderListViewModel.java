package com.xxjy.jyyh.ui.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.IntegralOrderBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;

import java.util.List;

public class OrderListViewModel extends BaseViewModel<OrderListRepository> {

    public OrderListViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<RefuelOrderBean>> refuelOrderListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<IntegralOrderBean>> integralOrderListLiveData = new MutableLiveData<>();
    public void refuelOrderList(int status,int pageNum,int pageSize) {
        mRespository.refuelOrderList(refuelOrderListLiveData, status, pageNum, pageSize);
    }
    public void integralOrderList(int status,int pageNum,int pageSize) {
        mRespository.integralOrderList(integralOrderListLiveData, status, pageNum, pageSize);
    }
}
