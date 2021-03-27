package com.xxjy.jyyh.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.HomeProductEntity;
import com.xxjy.jyyh.entity.LocationEntity;
import com.xxjy.jyyh.entity.OfentEntity;
import com.xxjy.jyyh.entity.OilDistanceEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;

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
    public MutableLiveData<String> refuelOilLiveData = new MutableLiveData<>();
    public MutableLiveData<List<HomeProductEntity.FirmProductsVoBean>> productLiveData = new MutableLiveData<>();
    public MutableLiveData<PayOrderEntity> payOrderLiveData = new MutableLiveData<>();
    public MutableLiveData<OilDistanceEntity> distanceLiveData = new MutableLiveData<>();
    public MutableLiveData<List<OilEntity.StationsBean>> storeLiveData = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void getLocation() {
        mRespository.getLocation(locationLiveData);
    }

    public void getHomeOil(double lat, double lng,String gasId) {
        mRespository.getHomeOil(lat, lng, gasId,homeOilLiveData);
    }

    public void getOftenOils() {
        mRespository.getOftenOils(oftenOilLiveData);
    }

    public void getRefuelJob(String gasId) {
        mRespository.getRefuelJob(gasId, refuelOilLiveData);
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
}
