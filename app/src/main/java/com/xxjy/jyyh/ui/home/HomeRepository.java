package com.xxjy.jyyh.ui.home;

import androidx.lifecycle.MutableLiveData;

import com.amap.api.location.AMapLocation;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.LocationEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.utils.locationmanger.MapLocationHelper;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/21/21 11:46 AM
 * @project ElephantOil
 * @description:
 */
public class HomeRepository extends BaseRepository {

    /**
     * @param locationLiveData
     * 定位获取经纬度
     */
    public void getLocation(MutableLiveData<LocationEntity> locationLiveData) {
        LocationEntity locationEntity = new LocationEntity();
        AMapLocation mapLocation = MapLocationHelper.getMapLocation();
        if (MapLocationHelper.isLocationValid() && mapLocation != null) {
            locationEntity.setLat(mapLocation.getLatitude());
            locationEntity.setLng(mapLocation.getLongitude());
            locationEntity.setCity(mapLocation.getCity());
            locationEntity.setDistrict(mapLocation.getDistrict());
            locationEntity.setAddress(mapLocation.getAddress());
            locationLiveData.postValue(locationEntity);
        } else {
            MapLocationHelper.getInstance().getLocation(new MapLocationHelper.LocationResult() {
                @Override
                public void locationSuccess(AMapLocation location) {
                    locationEntity.setLat(location.getLatitude());
                    locationEntity.setLng(location.getLongitude());
                    locationEntity.setCity(location.getCity());
                    locationEntity.setDistrict(location.getDistrict());
                    locationEntity.setAddress(location.getAddress());
                    locationLiveData.postValue(locationEntity);
                }

                @Override
                public void locationFiler() {
                    MyToast.showWarning(App.getContext(),"请去设置里开启定位权限!");
                }
            });
        }
    }

    /**
     * @param lat
     * @param lng
     * @param homeOilLiveData
     * 首页油站
     */
    public void getHomeOil(double lat, double lng, MutableLiveData<OilEntity> homeOilLiveData) {
        addDisposable(RxHttp.postForm(ApiService.HOME_OIL)
                .add("appLatitude", lat)
                .add("appLongitude", lng)
                .asResponse(OilEntity.class)
                .subscribe(oilEntity -> homeOilLiveData.postValue(oilEntity))
        );
    }
}
