package com.xxjy.jyyh.ui.restaurant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.MultiplePriceBean;
import com.xxjy.jyyh.entity.OilEntity;

import java.util.List;

/**
 * @author power
 * @date 2/26/21 10:42 AM
 * @project ElephantOil
 * @description:
 */
public class RestaurantViewModel extends BaseViewModel<RestaurantRepository> {

    public MutableLiveData<OilEntity.StationsBean> storeLiveData = new MutableLiveData<>();
    public MutableLiveData<Float> balanceLiveData = new MutableLiveData<>();
    public MutableLiveData<MultiplePriceBean> multiplePriceLiveData = new MutableLiveData<>();
    public MutableLiveData<List<CouponBean>> platformLiveData = new MutableLiveData<>();
    public MutableLiveData<List<CouponBean>> businessCouponLiveData = new MutableLiveData<>();
    public MutableLiveData<String> createOrderLiveData = new MutableLiveData<>();

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
    }

    public void getStoreInfo(String gasId) {
        mRespository.getStoreInfo(gasId, storeLiveData);
    }

    public void getBalance() {
        mRespository.getBalance(balanceLiveData);
    }

    public void getPlatformCoupon(String amount, String gasId) {
        mRespository.getPlatformCoupon(amount, gasId, platformLiveData);
    }

    public void getBusinessCoupon(String amount, String gasId, String oilNo) {
        mRespository.getBusinessCoupon(amount, gasId, oilNo, businessCouponLiveData);
    }

    public void getMultiplePrice(String platId, String amount, String gasId, String oilNo, boolean canUseBill, String businessAmount) {
        mRespository.getMultiplePrice(platId, amount, gasId, oilNo, canUseBill, businessAmount, multiplePriceLiveData);
    }

    public void createOrder(String amount, String payAmount, String usedBalance,
                            String gasId, String gunNo, String oilNo, String oilName, String gasName, String priceGun,
                            String priceUnit, String oilType,String phone, String xxCouponId, String czbCouponId, String czbCouponAmount) {
        mRespository.createOrder(amount, payAmount, usedBalance,
                gasId, gunNo, oilNo, oilName, gasName, priceGun,
                priceUnit, oilType,phone, xxCouponId, czbCouponId, czbCouponAmount, createOrderLiveData);
    }
}
