package com.xxjy.jyyh.ui.home;

import androidx.lifecycle.MutableLiveData;

import com.amap.api.location.AMapLocation;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.ProductMapKeyConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.entity.HomeProductEntity;
import com.xxjy.jyyh.entity.LocationEntity;
import com.xxjy.jyyh.entity.OfentEntity;
import com.xxjy.jyyh.entity.OilDistanceEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.entity.TaskBean;
import com.xxjy.jyyh.utils.locationmanger.MapLocationHelper;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.util.ArrayList;
import java.util.List;

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
            locationEntity.setSuccess(true);
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
                    locationEntity.setSuccess(true);
                    locationLiveData.postValue(locationEntity);
                }

                @Override
                public void locationFiler() {
                    locationEntity.setSuccess(false);
                    locationLiveData.postValue(locationEntity);
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
    public void getHomeOil(double lat, double lng,
                           MutableLiveData<OilEntity> homeOilLiveData) {
        addDisposable(RxHttp.postForm(ApiService.HOME_OIL)
                .add(Constants.LATITUDE, lat)
                .add(Constants.LONGTIDUE, lng)
                .asResponse(OilEntity.class)
                .subscribe(oilEntity -> homeOilLiveData.postValue(oilEntity))
        );
    }

    /**
     * @param oftenOilLiveData
     * 常去油站
     */
    public void getOftenOils(MutableLiveData<List<OfentEntity>> oftenOilLiveData) {
        addDisposable(RxHttp.postForm(ApiService.OFTEN_OIL)
                .asResponseList(OfentEntity.class)
                .subscribe(ofentEntities -> oftenOilLiveData.postValue(ofentEntities),
                        throwable -> oftenOilLiveData.postValue(new ArrayList<OfentEntity>()))
        );
    }

    /**
     * @param gasId
     * @param refuelOilLiveData
     * 加油任务
     */
    public void getRefuelJob(String gasId,
                             MutableLiveData<List<TaskBean>> refuelOilLiveData) {
        addDisposable(RxHttp.postForm(ApiService.REFUEL_JOB)
                .add(Constants.GAS_STATION_ID, gasId)
                .asResponseList(TaskBean.class)
                .subscribe( data-> refuelOilLiveData.postValue(data))
        );
    }

    /**
     * @param productLiveData
     * 首页积分豪礼
     */
    public void getHomeProduct(MutableLiveData<List<HomeProductEntity.FirmProductsVoBean>> productLiveData) {
        addDisposable(RxHttp.postForm(ApiService.HOME_PRODUCT)
                .add(Constants.PAGE, "1")
                .add(Constants.PAGE_SIZE, "10")
                .add("mapKey", ProductMapKeyConstants.INDEX)
                .asResponseList(HomeProductEntity.class)
                .subscribe(homeProductEntities ->
                        productLiveData.postValue(homeProductEntities.get(0).getFirmProductsVo()))

        );
    }

    public void payOrder(String payType, String orderId, String payAmount, MutableLiveData<PayOrderEntity> payOrderLiveData) {
        addDisposable(RxHttp.postForm(ApiService.PAY_ORDER)
                .add("payType", payType)
                .add("orderId", orderId)
                .add("payAmount", payAmount)
                .asResponse(PayOrderEntity.class)
                .subscribe(payOrderEntity -> payOrderLiveData.postValue(payOrderEntity))
        );
    }

    public void checkDistance(String gasId, MutableLiveData<OilDistanceEntity> distanceLiveData) {
        addDisposable(RxHttp.postForm(ApiService.GET_OIL_DISTANCE)
                .add("gasId",gasId)
                .add(Constants.LATITUDE, UserConstants.getLatitude())
                .add(Constants.LONGTIDUE, UserConstants.getLongitude())
                .asResponse(OilDistanceEntity.class)
                .subscribe(oilDistanceEntity -> distanceLiveData.postValue(oilDistanceEntity))
        );
    }
}
