package com.xxjy.jyyh.ui.car;


import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.CarServeApiService;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.entity.CarServeCommitOrderBean;
import com.xxjy.jyyh.entity.CarServeCouponListBean;
import com.xxjy.jyyh.entity.CarServeStoreDetailsBean;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.entity.RedeemEntity;

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
    public void commitOrder(MutableLiveData<CarServeCommitOrderBean> liveData, String storeId, String productId,
                            String realMoney, String couponType, long couponId,
                            String couponAmount, String sku) {
        addDisposable(RxHttp.postForm(CarServeApiService.COMMIT_ORDER)
                .add("orderType",2)
                .add("storeId",storeId)
                .add("productId",productId)
                .add("channel",101)
                .add("realMoney",realMoney)
                .add("couponType",couponType, !TextUtils.isEmpty(couponType))
                .add("couponId",couponId,couponId!=-1)
                .add("couponAmount",couponAmount,!TextUtils.isEmpty(couponAmount))
                .add("sku",sku,!TextUtils.isEmpty(sku)&&!TextUtils.equals("[]",sku))
                .asResponse(CarServeCommitOrderBean.class)
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
    public void tyingProduct(MutableLiveData<RedeemEntity> liveData) {
        addDisposable(RxHttp.postForm(CarServeApiService.TYING_PRODUCT)
                .asResponse(RedeemEntity.class)
                .subscribe(data -> liveData.postValue(data))
        );
    }
    public void payOrder(String payType, String orderId, String payAmount,MutableLiveData<PayOrderEntity> liveData) {
        addDisposable(RxHttp.postForm(CarServeApiService.PAYMENT_ORDER)
                .add("payType", payType)
                .add("orderId", orderId)
                .add("payAmount", payAmount)
                .add("code","04")
                .asResponse(PayOrderEntity.class)
                .subscribe(payOrderEntity -> liveData.postValue(payOrderEntity))
        );
    }

}
