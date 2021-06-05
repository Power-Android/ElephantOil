package com.xxjy.jyyh.ui.car;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.CarServeCouponListBean;
import com.xxjy.jyyh.entity.CarServeStoreDetailsBean;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.MonthCouponEntity;
import com.xxjy.jyyh.entity.MultiplePriceBean;
import com.xxjy.jyyh.entity.OilDefaultPriceEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilNumBean;
import com.xxjy.jyyh.entity.OrderNewsEntity;
import com.xxjy.jyyh.entity.RedeemEntity;

import java.util.List;

public class CarServeViewModel extends BaseViewModel<CarServeRepository> {

    public CarServeViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<CarServeStoreDetailsBean> storeLiveData = new MutableLiveData<>();
    public MutableLiveData<CarServeCouponListBean> usableCouponLiveData = new MutableLiveData<>();
    public MutableLiveData<String> commitOrderLiveData = new MutableLiveData<>();
    public MutableLiveData<String> tyingProductLiveData = new MutableLiveData<>();


    public void getStoreDetails(String storeNo) {
        mRespository.getStoreDetails(storeLiveData,storeNo);
    }
    public void getUsableCoupon() {
        mRespository.getUsableCoupon(usableCouponLiveData);
    }
    public void commitOrder() {
        mRespository.commitOrder(commitOrderLiveData);
    }
    public void tyingProduct() {
        mRespository.tyingProduct(commitOrderLiveData);
    }

}
