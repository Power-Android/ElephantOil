package com.xxjy.jyyh.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.LocationEntity;
import com.xxjy.jyyh.entity.OfentEntity;
import com.xxjy.jyyh.entity.OilEntity;

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

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void getLocation() {
        mRespository.getLocation(locationLiveData);
    }

    public void getHomeOil(double lat, double lng) {
        mRespository.getHomeOil(lat, lng, homeOilLiveData);
    }

    public void getOftenOils() {
        mRespository.getOftenOils(oftenOilLiveData);
    }

    public void getRefuelJob(String gasId) {
        mRespository.getRefuelJob(gasId, refuelOilLiveData);
    }
}
