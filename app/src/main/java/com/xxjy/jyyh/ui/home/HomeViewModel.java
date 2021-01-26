package com.xxjy.jyyh.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.LocationEntity;
import com.xxjy.jyyh.entity.OilEntity;

/**
 * @author power
 * @date 1/21/21 11:46 AM
 * @project ElephantOil
 * @description:
 */
public class HomeViewModel extends BaseViewModel<HomeRepository> {
    public MutableLiveData<LocationEntity> locationLiveData = new MutableLiveData<>();
    public MutableLiveData<OilEntity> homeOilLiveData = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public void getLocation() {
        mRespository.getLocation(locationLiveData);
    }

    public void getHomeOil(double lat, double lng) {
        mRespository.getHomeOil(lat, lng, homeOilLiveData);
    }
}
