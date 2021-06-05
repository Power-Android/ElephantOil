package com.xxjy.jyyh.ui.oil;

import android.text.TextUtils;


import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.app.App;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.CarServeApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.SPConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.entity.AreaListBean;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.CarServeCategoryListBean;
import com.xxjy.jyyh.entity.CarServeStoreListBean;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.MonthCouponEntity;
import com.xxjy.jyyh.entity.MultiplePriceBean;
import com.xxjy.jyyh.entity.OilDefaultPriceEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilNumBean;
import com.xxjy.jyyh.entity.OrderNewsEntity;
import com.xxjy.jyyh.entity.RedeemEntity;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.toastlib.MyToast;

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
                                String pageNum, String pageSize, String gasName, String method, String gasIds) {
        addDisposable(RxHttp.postForm(ApiService.OIL_STATIONS)
                .add("appLatitude", appLatitude, Float.parseFloat(appLatitude) != 0)
                .add("appLongitude", appLongitude, Float.parseFloat(appLongitude) != 0)
                .add("oilNo", oilNo, !TextUtils.equals("全部", oilNo))
                .add("gasIds", gasIds, !TextUtils.isEmpty(gasIds))
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

    public void getMultiplePrice(String amount, String gasId, String oilNo, String isUserBill, String platId,
                                 String businessAmount, String monthCouponId, boolean isUseCoupon, boolean isUseBusinessCoupon,
                                 String productIds, MutableLiveData<MultiplePriceBean> multiplePriceLiveData) {
        addDisposable(RxHttp.postForm(ApiService.OIL_MULTIPLE_PRICE)
                .add("amount", amount)
                .add(Constants.GAS_STATION_ID, gasId)
                .add(Constants.OIL_NUMBER_ID, oilNo)
                .add("canUseBill", isUserBill)
                .add("czbCouponAmount", TextUtils.isEmpty(businessAmount) ? "" : businessAmount)
                .add("couponId", platId)
                .add("monthCouponId", monthCouponId)
                .add("canUseUserCoupon", isUseCoupon)
                .add("canUseCzbCoupon", isUseBusinessCoupon)
                .add("productIds", productIds, !TextUtils.isEmpty(productIds) && !TextUtils.equals("[]", productIds))
                .asResponse(MultiplePriceBean.class)
                .doOnSubscribe(disposable -> {
                    showLoading(true);
                })
                .doFinally(() -> {
                    showLoading(false);
                })
                .subscribe(multiplePriceBean -> {
                    multiplePriceLiveData.postValue(multiplePriceBean);
                    showLoading(false);
                }));
    }

    public void getMonthCoupon(String gasId, MutableLiveData<MonthCouponEntity> monthCouponLiveData) {
        addDisposable(RxHttp.postForm(ApiService.GET_MONTH_COUPON)
                .add(Constants.GAS_STATION_ID, gasId)
                .asResponse(MonthCouponEntity.class)
                .subscribe(monthCouponEntity -> {
                    monthCouponLiveData.postValue(monthCouponEntity);
                }, throwable -> monthCouponLiveData.postValue(null)));
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
        addDisposable(RxHttp.postForm(ApiService.BUSINESS_COUPON)
                .add("canUse", "1")
                .add("amount", amount)
                .add(Constants.OIL_NUMBER_ID, oilNo)
                .add(Constants.GAS_STATION_ID, gasId)
                .asResponseList(CouponBean.class)
                .subscribe(couponBeans -> {
                    businessCouponLiveData.postValue(couponBeans);
                }));
    }

    public void getBalance(MutableLiveData<Float> balanceLiveData) {
        addDisposable(RxHttp.postForm(ApiService.QUERY_BALANCE)
                .asResponse(Float.class)
                .subscribe(balance -> {
                    balanceLiveData.postValue(balance);
                }));
    }

    public void createOrder(String amount, String payAmount, String usedBalance, String gasId, String gunNo,
                            String oilNo, String oilName, String gasName, String priceGun, String priceUnit,
                            String oilType, String phone, String xxCouponId, String czbCouponId, String czbCouponAmount,
                            String xxMonthCouponId, String xxMonthCouponAmount, boolean isAddMonthId, boolean isAddMonthAmouont,
                            String productIds, String productAmount,
                            MutableLiveData<String> createOrderLiveData) {
        addDisposable(RxHttp.postForm(ApiService.CREATE_ORDER)
                .add("amount", amount)
                .add("payAmount", payAmount)
                .add("usedBalance", usedBalance)
                .add("gasId", gasId)
                .add("gunNo", gunNo)
                .add("oilNo", oilNo)
                .add("oilName", oilName)
                .add("gasName", gasName)
                .add("priceGun", priceGun)
                .add("priceUnit", priceUnit)
                .add("oilType", oilType)
                .add("phone", phone)
                .add("xxCouponId", xxCouponId)
                .add("czbCouponId", czbCouponId)
                .add("czbCouponAmount", czbCouponAmount)
                .add("xxMonthCouponId", xxMonthCouponId, isAddMonthId)
                .add("xxMonthCouponAmount", xxMonthCouponAmount, isAddMonthAmouont)
                .add("productIds", productIds, !TextUtils.isEmpty(productIds) && !TextUtils.equals("[]", productIds))
                .add("productAmount", productAmount, !TextUtils.isEmpty(productAmount))
                .asResponse(String.class)
                .doOnSubscribe(disposable -> showLoading(true))
                .doFinally(() -> showLoading(false))
                .subscribe(orderId -> {
                            createOrderLiveData.postValue(orderId);
                        },
                        onError -> {
                            MyToast.showError(App.getContext(), onError.getMessage());
                        }));
    }

    public void getRedeem(String gasId, MutableLiveData<RedeemEntity> redeemLiveData) {
        addDisposable(RxHttp.postForm(ApiService.QUERY_SALE_INFO)
                .add(Constants.GAS_STATION_ID, gasId)
                .asResponse(RedeemEntity.class)
                .subscribe(new Consumer<RedeemEntity>() {
                    @Override
                    public void accept(RedeemEntity s) throws Throwable {
                        redeemLiveData.postValue(s);
                    }
                })
        );
    }
    public void getAreaList(String cityCode, MutableLiveData<AreaListBean> areaListLiveData) {
        addDisposable(RxHttp.get(CarServeApiService.GET_AREA+cityCode)
                .asCarServeResponse(AreaListBean.class)
                .subscribe( s -> areaListLiveData.postValue(s))
        );
    }
    public void getProductCategory(MutableLiveData<CarServeCategoryListBean> productCategoryLiveData) {
        addDisposable(RxHttp.postJson(CarServeApiService.GET_PRODUCT_CATEGORY)
//                .add("pageIndex",1)
                .asCarServeResponse(CarServeCategoryListBean.class)
                .subscribe( s -> productCategoryLiveData.postValue(s))
        );
    }
    public void getCarServeStoreList(MutableLiveData<CarServeStoreListBean> liveData, int pageIndex, String cityCode, String areaCode, long productCategoryId, int status) {
        addDisposable(RxHttp.postJson(CarServeApiService.GET_STORE_LIST)
                .add("pageIndex",pageIndex)
                .add("pageSize",10)
                .add("cityCode",cityCode)
                .add("areaCode",areaCode,!TextUtils.equals("-1",areaCode))
                .add("longitude", UserConstants.getLongitude())
                .add("latitude", UserConstants.getLatitude())
                .add("productCategoryId",productCategoryId,productCategoryId!=-1)
                .add("status",status,status!=-1)
                .asCarServeResponse(CarServeStoreListBean.class)
                .subscribe( s -> liveData.postValue(s))
        );
    }
}
