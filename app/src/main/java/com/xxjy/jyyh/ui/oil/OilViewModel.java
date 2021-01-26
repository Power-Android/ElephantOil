package com.xxjy.jyyh.ui.oil;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilNumBean;
import com.xxjy.jyyh.entity.OrderNewsEntity;

import java.util.List;

/**
 * @author power
 * @date 1/21/21 11:56 AM
 * @project ElephantOil
 * @description:
 */
public class OilViewModel extends BaseViewModel<OilRepository> {

    public OilViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<List<OrderNewsEntity>> orderNewsLiveData = new MutableLiveData<>();
    public MutableLiveData<List<OilNumBean>> oilNumLiveData = new MutableLiveData<>();
    public MutableLiveData<OilEntity> oilStationLiveData = new MutableLiveData<>();

    public void getOrderNews() {
        mRespository.getOrderNews(orderNewsLiveData);
    }
    public void getOilNums() {
        mRespository.getOilNums(oilNumLiveData);
    }
    public void getOilStations(String appLatitude,
                               String appLongitude, String oilNo, String orderBy, String distance,String pageNum,String pageSize) {
        mRespository.getOilStations(oilStationLiveData, appLatitude,
                 appLongitude,  oilNo,  orderBy,  distance, pageNum, pageSize);
    }
}
