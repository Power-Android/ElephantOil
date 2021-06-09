package com.xxjy.jyyh.ui.mine;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.CarServeApiService;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.http.Response;

import java.util.List;

import rxhttp.RxHttp;

public class MyCouponRepository extends BaseRepository {


    public void getPlatformCouponVOs(MutableLiveData<List<CouponBean>> platformCouponLiveData, int canUse){
        addDisposable(RxHttp.postForm(ApiService.PLATFORM_COUPON_LIST)
                .add("canUse",canUse)// 0 falae 1 true
                .asResponseList(CouponBean.class)
                .subscribe(data -> platformCouponLiveData.postValue(data))
        );
    }
    public void getBusinessCoupons(MutableLiveData<List<CouponBean>> businessCouponLiveData, int canUse){
        addDisposable(RxHttp.postForm(ApiService.BUSINESS_COUPON)
                .add("canUse",canUse)// 0 falae 1 true
                .asResponseList(CouponBean.class)
                .subscribe(data -> businessCouponLiveData.postValue(data))
        );
    }
    public void exchangeCoupon(MutableLiveData<Response> exchangeCouponLiveData, String couponCode){
        addDisposable(RxHttp.postForm(ApiService.EXCHANGE_COUPON)
                .add("couponCode",couponCode)
                .asCodeResponse(Response.class)
                .subscribe(data -> exchangeCouponLiveData.postValue(data))
        );
    }
    public void carServeCoupon(MutableLiveData<String> exchangeCouponLiveData, int currentUsable){
        addDisposable(RxHttp.postJson(CarServeApiService.COUPON_LIST)
                .add("pageIndex",1)
                .add("pageSize",10000)
                .add("currentUsable",currentUsable)
                .addHeader("token", UserConstants.getToken())
                .asCarServeResponse(String.class)
                .subscribe(data -> exchangeCouponLiveData.postValue(data))
        );
    }
}
