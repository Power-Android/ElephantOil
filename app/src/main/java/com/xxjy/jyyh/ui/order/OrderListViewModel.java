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

    public MutableLiveData<List<RefuelOrderBean>> paymentOrderListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<RefuelOrderBean>> refundOrderListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<RefuelOrderBean>> integralOrderListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<RefuelOrderBean>> lifeOrderListLiveData = new MutableLiveData<>();
    public void refuelOrderList(int status,int pageNum,int pageSize) {
        mRespository.refuelOrderList(paymentOrderListLiveData, status, pageNum, pageSize);
    }
    public void orderRefundList(int status,int pageNum,int pageSize) {
        mRespository.orderRefundList(refundOrderListLiveData, status, pageNum, pageSize);
    }
    public void integralOrderList(int status,int pageNum,int pageSize) {
        mRespository.integralOrderList(integralOrderListLiveData, status, pageNum, pageSize);
    }
    public void lifeOrderList(int status,int pageNum,int pageSize) {
        mRespository.lifeOrderList(lifeOrderListLiveData, status, pageNum, pageSize);
    }
}
