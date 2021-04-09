package com.xxjy.jyyh.ui.oil;

import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.SpanUtils;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.MonthCouponEntity;
import com.xxjy.jyyh.entity.MultiplePriceBean;
import com.xxjy.jyyh.entity.OilDefaultPriceEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilNumBean;
import com.xxjy.jyyh.entity.OrderNewsEntity;

import java.util.List;

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
        addDisposable(RxHttp.postForm(ApiService.OIL_AND_SIGN_STATIONS)
                .add("appLatitude", appLatitude, Float.parseFloat(appLatitude) != 0)
                .add("appLongitude", appLongitude, Float.parseFloat(appLongitude) != 0)
                .add("oilNo", oilNo, !TextUtils.equals("全部", oilNo))
                .add("orderBy", orderBy)
                .add("distance", distance)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize)
                .add("gasName", gasName, !TextUtils.isEmpty(gasName))
                .add("isShowSign", method, !TextUtils.isEmpty(method))
                .asResponse(OilEntity.class)
                .subscribe(data -> oilStationLiveData.postValue(data))
        );
    }

    public void getOilStations1(MutableLiveData<OilEntity> oilStationLiveData, String appLatitude,
                                String appLongitude, String oilNo, String orderBy, String distance,
                                String pageNum, String pageSize, String gasName, String method) {
        addDisposable(RxHttp.postForm(ApiService.OIL_STATIONS)
                .add("appLatitude", appLatitude, Float.parseFloat(appLatitude) != 0)
                .add("appLongitude", appLongitude, Float.parseFloat(appLongitude) != 0)
                .add("oilNo", oilNo, !TextUtils.equals("全部", oilNo))
                .add("orderBy", orderBy)
                .add("distance", distance)
                .add("pageNum", pageNum)
                .add("pageSize", pageSize)
                .add("gasName", gasName, !TextUtils.isEmpty(gasName))
                .add("isShowSign", method, !TextUtils.isEmpty(method))
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
                .add(Constants.LATITUDE, lat + "", lat != 0)
                .add(Constants.LONGTIDUE, lng + "", lng != 0)
                .asResponse(OilEntity.StationsBean.class)
                .doOnSubscribe(disposable -> showLoading(true))
                .doFinally(() -> showLoading(false))
                .subscribe(stationsBean -> oilLiveData.postValue(stationsBean))
        );
    }

    public void getMultiplePrice(String amount, String gasId, String oilNo, String isUserBill, String platId, String businessAmount, String monthCouponId, MutableLiveData<MultiplePriceBean> multiplePriceLiveData) {
        addDisposable(RxHttp.postForm(ApiService.OIL_MULTIPLE_PRICE)
                .add("amount", amount)
                .add(Constants.GAS_STATION_ID, gasId)
                .add(Constants.OIL_NUMBER_ID, oilNo)
                .add("canUseBill", isUserBill)
                .add("czbCouponAmount", TextUtils.isEmpty(businessAmount) ? "0" : businessAmount)
                .add("couponId", platId)
                .add("monthCouponId", monthCouponId)
                .asResponse(MultiplePriceBean.class)
                .subscribe(multiplePriceBean -> {
                    multiplePriceLiveData.postValue(multiplePriceBean);
                }));
    }

    public void getMonthCoupon(String gasId, MutableLiveData<MonthCouponEntity> monthCouponLiveData) {
        addDisposable(RxHttp.postForm(ApiService.GET_MONTH_COUPON)
                .add(Constants.GAS_STATION_ID, gasId)
                .asResponse(MonthCouponEntity.class)
                .subscribe(monthCouponEntity -> {
                    monthCouponLiveData.postValue(monthCouponEntity);
                }));
    }

    public void getDefaultPrice(String gasId, String oilNo, MutableLiveData<OilDefaultPriceEntity> defaultPriceLiveData) {
        addDisposable(RxHttp.postForm(ApiService.OIL_PRICE_DEFAULT)
                .add(Constants.GAS_STATION_ID, gasId)
                .add(Constants.OIL_NUMBER_ID, oilNo)
                .asResponse(OilDefaultPriceEntity.class)
                .subscribe(priceEntity -> {
                    defaultPriceLiveData.postValue(priceEntity);
                }));
    }

    public void getPlatformCoupon(String amount, String gasId, String oilNo, MutableLiveData<List<CouponBean>> platformCouponLiveData) {
        //0真正可用 1已用 2过期  3时间未到 4 金额未达到
        if (TextUtils.isEmpty(amount)) return;
        addDisposable(RxHttp.postForm(ApiService.PLATFORM_COUPON)
                .add("canUse", "1")
                .add("rangeType", "2")
                .add("amount", amount)
                .add(Constants.OIL_NUMBER_ID, oilNo)
                .add(Constants.GAS_STATION_ID, gasId)
                .asResponseList(CouponBean.class)
                .subscribe(couponBeans -> {
                    platformCouponLiveData.postValue(couponBeans);
                }));
    }

    public void getBusinessCoupon(String amount, String gasId, String oilNo, MutableLiveData<List<CouponBean>> businessCouponLiveData) {
        if (TextUtils.isEmpty(amount)) return;
        addDisposable();
    }
}
