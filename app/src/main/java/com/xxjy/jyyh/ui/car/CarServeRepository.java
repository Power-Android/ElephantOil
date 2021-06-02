package com.xxjy.jyyh.ui.car;


import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.CarServeApiService;
import com.xxjy.jyyh.entity.CarServeStoreDetailsBean;

import rxhttp.RxHttp;


public class CarServeRepository extends BaseRepository {

    public void getStoreDetails(MutableLiveData<CarServeStoreDetailsBean> liveData, String storeNo) {
        addDisposable(RxHttp.get(CarServeApiService.GET_STORE_DETAILS+storeNo+"/"+CarServeApiService.APP_ID)
                .asCarServeResponse(CarServeStoreDetailsBean.class)
                .subscribe(data -> liveData.postValue(data))
        );
    }

}
