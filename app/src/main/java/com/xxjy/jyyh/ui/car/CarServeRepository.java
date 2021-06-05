package com.xxjy.jyyh.ui.car;


import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.CarServeApiService;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.entity.CarServeCouponListBean;
import com.xxjy.jyyh.entity.CarServeStoreDetailsBean;

import rxhttp.RxHttp;


public class CarServeRepository extends BaseRepository {

    public void getStoreDetails(MutableLiveData<CarServeStoreDetailsBean> liveData, String storeNo) {
        addDisposable(RxHttp.get(CarServeApiService.GET_STORE_DETAILS+storeNo+"/"+CarServeApiService.APP_ID)
                .asCarServeResponse(CarServeStoreDetailsBean.class)
                .subscribe(data -> liveData.postValue(data))
        );
    }
    public void getUsableCoupon(MutableLiveData<CarServeCouponListBean> liveData) {
        addDisposable(RxHttp.postJson(CarServeApiService.COUPON_USABLE)
                .add("pageIndex",1)
                .add("pageSize",100)
                .addHeader("token", UserConstants.getToken())
                .asCarServeResponse(CarServeCouponListBean.class)
                .subscribe(data -> liveData.postValue(data))
        );
    }
    public void commitOrder(MutableLiveData<String> liveData) {
        addDisposable(RxHttp.postForm(CarServeApiService.COMMIT_ORDER)
                .add("pageIndex",1)
                .add("pageSize",100)
                .addHeader("token", UserConstants.getToken())
                .asResponse(String.class)
                .subscribe(data -> liveData.postValue(data))
        );
    }
    public void getOrderList(MutableLiveData<String> liveData,int orderType,int orderStatus,int pageNum) {
        addDisposable(RxHttp.get(CarServeApiService.GET_ORDER_LIST)
                .add("orderType",orderType)
                .add("orderStatus",orderStatus)
                .add("pageNum",pageNum)
                .add("pageSize",10)
                .addHeader("token", UserConstants.getToken())
                .asResponse(String.class)
                .subscribe(data -> liveData.postValue(data))
        );
    }
    public void tyingProduct(MutableLiveData<String> liveData) {
        addDisposable(RxHttp.get(CarServeApiService.GET_ORDER_LIST)
                .add("pageSize",10)
                .addHeader("token", UserConstants.getToken())
                .asResponse(String.class)
                .subscribe(data -> liveData.postValue(data))
        );
    }

}
