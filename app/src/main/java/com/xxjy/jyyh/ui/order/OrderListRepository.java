package com.xxjy.jyyh.ui.order;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.CarServeApiService;
import com.xxjy.jyyh.entity.CarServeOrderListBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;
import com.xxjy.jyyh.http.Response;

import java.util.List;

import rxhttp.RxHttp;

public class OrderListRepository extends BaseRepository {
    public void refuelOrderList(MutableLiveData<List<RefuelOrderBean>> paymentOrderListLiveData, int status, int pageNum, int pageSize) {
        addDisposable(RxHttp.postForm(ApiService.REFUEL_ORDER_LIST)
                .add("status", status == -1 ? "" : status)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize)
                .asResponseList(RefuelOrderBean.class)
                .subscribe(data -> paymentOrderListLiveData.postValue(data))
        );
    }

    public void orderRefundList(MutableLiveData<List<RefuelOrderBean>> refundOrderListLiveData, int status, int pageNum, int pageSize) {
        addDisposable(RxHttp.postForm(ApiService.REFUND_ORDER_LIST)
                .add("refundStatus", status == -1 ? "" : status)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize)
                .asResponseList(RefuelOrderBean.class)
                .subscribe(data -> refundOrderListLiveData.postValue(data))
        );
    }

    public void integralOrderList(MutableLiveData<List<RefuelOrderBean>> integralOrderListLiveData, int status, int pageNum, int pageSize) {
        addDisposable(RxHttp.postForm(ApiService.INTEGRAL_ORDER_LIST)
                .add("status", status == -1 ? "" : status)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize)
                .asResponseList(RefuelOrderBean.class)
                .subscribe(data -> integralOrderListLiveData.postValue(data))
        );
    }

    public void lifeOrderList(MutableLiveData<List<RefuelOrderBean>> lifeOrderListLiveData, int status, int pageNum, int pageSize) {
        addDisposable(RxHttp.postForm(ApiService.GET_STORE_ORDER_LIST)
                .add("status", status == -1 ? "" : status)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize)
                .asResponseList(RefuelOrderBean.class)
                .subscribe(data -> lifeOrderListLiveData.postValue(data))
        );
    }

    public void carServeOrderList(MutableLiveData<CarServeOrderListBean> liveData, int orderStatus, int verificationStatus, int pageNum) {
        addDisposable(RxHttp.get(CarServeApiService.GET_ORDER_LIST)
                .add("orderType",2)
                .add("orderStatus",orderStatus,orderStatus!=-1)
                .add("verificationStatus",verificationStatus,verificationStatus!=-1)
                .add("pageNum",pageNum)
                .add("pageSize",10)
               .add("appId",CarServeApiService.APP_ID)
                .asResponse(CarServeOrderListBean.class)
                .subscribe(data -> liveData.postValue(data))
        );
    }
    public void cancelCarServeOrder(MutableLiveData<Response> liveData, String orderId) {
        addDisposable(RxHttp.postForm(CarServeApiService.CANCEL_ORDER)
                .add("orderId",orderId)
                .asCodeResponse(Response.class)
                .subscribe(data -> liveData.postValue(data))
        );
    }
}
