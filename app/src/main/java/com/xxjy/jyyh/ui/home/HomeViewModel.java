package com.xxjy.jyyh.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.HomeMenuEntity;
import com.xxjy.jyyh.entity.HomeProductEntity;
import com.xxjy.jyyh.entity.LocationEntity;
import com.xxjy.jyyh.entity.OfentEntity;
import com.xxjy.jyyh.entity.OilDistanceEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.entity.QueryRefuelJobEntity;

import java.util.List;

/**
 * @author power
 * @date 1/21/21 11:46 AM
 * @project ElephantOil
 * @description:
 */
public class HomeViewModel extends BaseViewModel<HomeRepository> {
    public MutableLiveData<LocationEntity> locationLiveData = new MutableLiveData<>();
    public MutableLiveData<OilEntity> homeOilLiveData = new MutableLiveData<>();
    public MutableLiveData<List<OfentEntity>> oftenOilLiveData = new MutableLiveData<>();
    public MutableLiveData<QueryRefuelJobEntity> refuelOilLiveData = new MutableLiveData<>();
    public MutableLiveData<String> receiverCouponLiveData = new MutableLiveData<>();
    public MutableLiveData<List<HomeProductEntity.FirmProductsVoBean>> productLiveData = new MutableLiveData<>();
    public MutableLiveData<PayOrderEntity> payOrderLiveData = new MutableLiveData<>();
    public MutableLiveData<OilDistanceEntity> distanceLiveData = new MutableLiveData<>();
    public MutableLiveData<List<OilEntity.StationsBean>> storeLiveData = new MutableLiveData<>();
    public MutableLiveData<List<HomeMenuEntity>> menuLiveData = new MutableLiveData<>();


    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void getLocation() {
        mRespository.getLocation(locationLiveData);
    }

    public void getOftenOils() {
        mRespository.getOftenOils(oftenOilLiveData);
    }

    public void getRefuelJob() {
        mRespository.getRefuelJob(refuelOilLiveData);
    }

    public void getHomeProduct() {
        mRespository.getHomeProduct(productLiveData);
    }

    public void payOrder(String payType, String orderId, String payAmount) {
        mRespository.payOrder(payType, orderId, payAmount, payOrderLiveData);
    }

    public void checkDistance(String gasId) {
        mRespository.checkDistance(gasId, distanceLiveData);
    }

    public void getStoreList(int pageNum,int pageSize){
        mRespository.getStoreList( pageNum, pageSize,storeLiveData);
    }

    public void receiverJobCoupon(String id, String couponId) {
        mRespository.receiverJobCoupon(id, couponId, receiverCouponLiveData);
    }

    public void getHomeCard(double lat, double lng,String gasId) {
        mRespository.getHomeCard(lat, lng, gasId, homeOilLiveData);
    }

    public void getMenu() {
        mRespository.getMenu(menuLiveData);
    }
}
