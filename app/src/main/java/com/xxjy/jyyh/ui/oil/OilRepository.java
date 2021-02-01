package com.xxjy.jyyh.ui.oil;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilNumBean;
import com.xxjy.jyyh.entity.OrderNewsEntity;

import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/21/21 11:56 AM
 * @project ElephantOil
 * @description:
 */
public class OilRepository extends BaseRepository {

    public void getOrderNews(MutableLiveData<List<OrderNewsEntity>> newLiveData) {
        addDisposable(RxHttp.postForm(ApiService.ORDER_NEWS)
                .asResponseList(OrderNewsEntity.class)
                .subscribe(data -> newLiveData.postValue(data))
        );
    }

    public void getOilNums(MutableLiveData<List<OilNumBean>> oilNumLiveData) {
        addDisposable(RxHttp.postForm(ApiService.OIL_NUMS)
                .asResponseList(OilNumBean.class)
                .subscribe(data -> oilNumLiveData.postValue(data))
        );
    }

    public void getOilStations(MutableLiveData<OilEntity> oilStationLiveData, String appLatitude,
                               String appLongitude, String oilNo, String orderBy, String distance,
                               String pageNum, String pageSize, String gasName, String method) {
        addDisposable(RxHttp.postForm(ApiService.OIL_STATIONS)
                .add("appLatitude", TextUtils.equals(appLatitude, "0") ? null : appLatitude)
                .add("appLongitude", TextUtils.equals(appLongitude, "0") ? null : appLongitude)
                .add("oilNo", oilNo,!TextUtils.isEmpty(oilNo))
                .add("orderBy", orderBy)
                .add("distance",distance)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize)
                .add("gasName", gasName, !TextUtils.isEmpty(gasName))
                .add("isShowSign", method, !TextUtils.isEmpty(method))
                .asResponse(OilEntity.class)
                .subscribe(data -> oilStationLiveData.postValue(data))
        );
    }

    public void getSignOilStations(MutableLiveData<OilEntity> oilStationLiveData, String appLatitude,
                                   String appLongitude) {
        addDisposable(RxHttp.postForm(ApiService.SIGN_OIL_STATIONS)
                .add("appLatitude", TextUtils.equals(appLatitude, "0") ? null : appLatitude)
                .add("appLongitude", TextUtils.equals(appLongitude, "0") ? null : appLongitude)
                .asResponse(OilEntity.class)
                .subscribe(data -> oilStationLiveData.postValue(data))
        );
    }

    public void getBanners(MutableLiveData<List<BannerBean>> bannersLiveData) {
        addDisposable(RxHttp.postForm(ApiService.OIL_STATIONS_BANNERS)
                .asResponseList(BannerBean.class)
                .subscribe(data -> bannersLiveData.postValue(data))
        );
    }

    public void getOilDetail(String gasId, double lat, double lng,
                             MutableLiveData<OilEntity.StationsBean> oilLiveData) {
        addDisposable(RxHttp.postForm(ApiService.OIL_DETAIL)
                .add(Constants.GAS_STATION_ID, gasId)
                .add(Constants.LATITUDE, lat)
                .add(Constants.LONGTIDUE, lng)
                .asResponse(OilEntity.StationsBean.class)
                .subscribe(stationsBean -> oilLiveData.postValue(stationsBean))
        );
    }
}
