package com.xxjy.jyyh.ui.mine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.CarCouponEntity;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.CouponNumEntity;
import com.xxjy.jyyh.http.Response;

import java.util.List;

public class MyCouponViewModel extends BaseViewModel<MyCouponRepository> {

    public MyCouponViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<CouponBean>> platformCouponLiveData = new MutableLiveData<>();
    public MutableLiveData<List<CouponBean>> businessCouponLiveData = new MutableLiveData<>();
    public MutableLiveData<CarCouponEntity> carServeCouponLiveData = new MutableLiveData<>();
    public MutableLiveData<Response> exchangeCouponLiveData = new MutableLiveData<>();
    public MutableLiveData<CouponNumEntity> couponNumLiveData1 = new MutableLiveData<>();
    public MutableLiveData<CouponNumEntity> couponNumLiveData2 = new MutableLiveData<>();
    public void getPlatformCouponVOs(int canUse){
        mRespository.getPlatformCouponVOs( platformCouponLiveData,canUse);
    }
    public void getBusinessCoupons(int canUse){
        mRespository.getBusinessCoupons( businessCouponLiveData,canUse);
    }
    public void exchangeCoupon(String couponCode){
        mRespository.exchangeCoupon( exchangeCouponLiveData,couponCode);
    }
    public void carServeCoupon(){
        mRespository.carServeCoupon(carServeCouponLiveData);
    }

    public void getCouponNum1(int type) {
        mRespository.getCouponNum(type, couponNumLiveData1);
    }

    public void getCouponNum2(int type) {
        mRespository.getCouponNum(type, couponNumLiveData2);
    }
}
