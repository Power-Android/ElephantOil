package com.xxjy.jyyh.ui.home;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.amap.api.location.AMapLocation;
import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.CarServeApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.ProductMapKeyConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.entity.HomeMenuEntity;
import com.xxjy.jyyh.entity.HomeProductEntity;
import com.xxjy.jyyh.entity.LocationEntity;
import com.xxjy.jyyh.entity.OfentEntity;
import com.xxjy.jyyh.entity.OftenCarsEntity;
import com.xxjy.jyyh.entity.OilDistanceEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.entity.QueryRefuelJobEntity;
import com.xxjy.jyyh.http.HeaderUtils;
import com.xxjy.jyyh.utils.locationmanger.MapLocationHelper;

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
     * @param locationLiveData 定位获取经纬度
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
                    locationEntity.setAddress("暂无定位");
                    locationLiveData.postValue(locationEntity);
                }
            });
        }
    }

    /**
     * @param lat
     * @param lng
     * @param homeOilLiveData 首页油站
     */
    public void getHomeOil(double lat, double lng, String gasId,
                           MutableLiveData<OilEntity> homeOilLiveData) {
        addDisposable(RxHttp.postForm(ApiService.HOME_OIL)
                .add(Constants.LATITUDE, lat, lat != 0)
                .add(Constants.LONGTIDUE, lng, lng != 0)
                .add(Constants.GAS_STATION_ID, gasId, !TextUtils.isEmpty(gasId))
                .asResponse(OilEntity.class)
                .subscribe(oilEntity -> homeOilLiveData.postValue(oilEntity))
        );
    }

    /**
     * @param oftenOilLiveData 常去油站
     */
    public void getOftenOils(MutableLiveData<List<OfentEntity>> oftenOilLiveData) {
        addDisposable(RxHttp.postForm(ApiService.OFTEN_OIL)
                .asResponseList(OfentEntity.class)
                .subscribe(ofentEntities -> oftenOilLiveData.postValue(ofentEntities),
                        throwable -> oftenOilLiveData.postValue(new ArrayList<OfentEntity>()))
        );
    }

    /**
     * @param refuelOilLiveData 加油任务
     */
    public void getRefuelJob(MutableLiveData<QueryRefuelJobEntity> refuelOilLiveData) {
        addDisposable(RxHttp.postForm(ApiService.QUERY_REFUEL_JOB)
                .asResponse(QueryRefuelJobEntity.class)
                .subscribe(data -> refuelOilLiveData.postValue(data))
        );
    }

    /**
     * @param productLiveData 首页积分豪礼
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
        addDisposable(RxHttp.postForm(ApiService.GET_PAY_DISTANCE)
                .add("gasId", gasId)
                .add(Constants.LATITUDE, UserConstants.getLatitude())
                .add(Constants.LONGTIDUE, UserConstants.getLongitude())
                .asResponse(OilDistanceEntity.class)
                .subscribe(oilDistanceEntity -> distanceLiveData.postValue(oilDistanceEntity))
        );
    }

    public void getStoreList(int pageNum, int pageSize, MutableLiveData<List<OilEntity.StationsBean>> storeLiveData) {
        addDisposable(RxHttp.postForm(ApiService.GET_STORE_LIST)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize)
                .add(Constants.LATITUDE, UserConstants.getLatitude())
                .add(Constants.LONGTIDUE, UserConstants.getLongitude())
                .asResponseList(OilEntity.StationsBean.class)
                .subscribe(data -> storeLiveData.postValue(data))
        );
    }

    public void receiverJobCoupon(String id, String couponId, MutableLiveData<String> receiverCouponLiveData) {
        addDisposable(RxHttp.postForm(ApiService.RECEIVE_OIL_JOB_COUPON)
                .add("id", id)
                .add("couponId", couponId)
                .asResponse(String.class)
                .subscribe(s -> receiverCouponLiveData.postValue(s)));
    }

    public void getHomeCard(double lat, double lng, String gasId,
                            MutableLiveData<OilEntity> homeOilLiveData) {
        addDisposable(RxHttp.postForm(ApiService.HOME_CARD_INFO)
                .add(Constants.LATITUDE, lat, lat != 0)
                .add(Constants.LONGTIDUE, lng, lng != 0)
                .add(Constants.GAS_STATION_ID, gasId, !TextUtils.isEmpty(gasId))
                .asResponse(OilEntity.class)
                .subscribe(homeCardEntity -> homeOilLiveData.postValue(homeCardEntity)));
    }

    public void getMenu(MutableLiveData<List<HomeMenuEntity>> menuLiveData) {
        addDisposable(RxHttp.postForm(ApiService.HOME_MENU_INFO)
                .asResponseList(HomeMenuEntity.class)
                .subscribe(menuEntityList -> menuLiveData.postValue(menuEntityList)));
    }

    public void getOftenCars(MutableLiveData<List<OftenCarsEntity>> oftenCarsLiveData) {
        addDisposable(RxHttp.postJson(CarServeApiService.OFENT_CAR_SERVE)
                .addHeader("token", UserConstants.getToken())
                .add("appId", CarServeApiService.APP_ID)
                .add("longitude", Double.parseDouble(UserConstants.getLongitude())==0d?"116.470866":UserConstants.getLongitude())
                .add("latitude", Double.parseDouble(UserConstants.getLatitude())==0d?"39.911205":UserConstants.getLatitude())
                .asCarServeResponseList(OftenCarsEntity.class)
                .subscribe(new Consumer<List<OftenCarsEntity>>() {
                    @Override
                    public void accept(List<OftenCarsEntity> s) throws Throwable {
                        oftenCarsLiveData.postValue(s);
                    }
                })
        );

    }
}
