package com.xxjy.jyyh.ui.restaurant;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.UiThread;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.SPUtils;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.SPConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.MultiplePriceBean;
import com.xxjy.jyyh.entity.OilDefaultPriceEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.util.List;

import rxhttp.RxHttp;

/**
 * @author power
 * @date 2/26/21 10:42 AM
 * @project ElephantOil
 * @description:
 */
public class RestaurantRepository extends BaseRepository {

    public void getStoreInfo(String gasId, MutableLiveData<OilEntity.StationsBean> storeLiveData) {
        addDisposable(RxHttp.postForm(ApiService.GET_STORE_INFO)
                .add("gasId", gasId)
                .add(Constants.LATITUDE, UserConstants.getLatitude())
                .add(Constants.LONGTIDUE, UserConstants.getLongitude())
                .asResponse(OilEntity.StationsBean.class)
                .subscribe(data -> storeLiveData.postValue(data))
        );
    }


    public void getBalance(MutableLiveData<Float> balanceLiveData) {
        addDisposable(RxHttp.postForm(ApiService.QUERY_BALANCE)
                .asResponse(Float.class)
                .subscribe(balance -> {
                    balanceLiveData.postValue(balance);
                }));
    }

    public void getPlatformCoupon(String amount, String gasId, MutableLiveData<List<CouponBean>> couponLiveData) {
        addDisposable(RxHttp.postForm(ApiService.PLATFORM_COUPON)
                .add("canUse", "1")
                .add("rangeType", "2")
                .add("amount", amount)
                .add(Constants.GAS_STATION_ID, gasId)
                .asResponseList(CouponBean.class)
                .subscribe(couponBeans -> {

                    couponLiveData.postValue(couponBeans);
                }));
    }

    public void getBusinessCoupon(String amount, String gasId, String oilNo, MutableLiveData<List<CouponBean>> couponLiveData) {
        addDisposable(RxHttp.postForm(ApiService.BUSINESS_COUPON)
                .add("canUse", "1")
                .add("amount", amount)
                .add(Constants.GAS_STATION_ID, gasId)
                .add(Constants.OIL_NUMBER_ID, oilNo)
                .asResponseList(CouponBean.class)
                .subscribe(couponBeans -> {
                    couponLiveData.postValue(couponBeans);
                }));
    }

    /**
     * 获取互斥价格
     * 说明：商家优惠券和直降金额互斥
     */
    public void getMultiplePrice(String platId, String amount, String gasId, String oilNo, boolean canUseBill, String businessAmount, MutableLiveData<MultiplePriceBean> mMultiplePriceLiveData) {
        addDisposable(RxHttp.postForm(ApiService.OIL_MULTIPLE_PRICE)
                .add("amount", amount)
                .add(Constants.GAS_STATION_ID, gasId)
                .add(Constants.OIL_NUMBER_ID, oilNo)
                .add("canUseBill", canUseBill ? "1" : "0")
                .add("czbCouponAmount", TextUtils.isEmpty(businessAmount) ? "0" : businessAmount)
                .add("couponId", platId)
                .asResponse(MultiplePriceBean.class)
                .subscribe(multiplePriceBean -> {
                    mMultiplePriceLiveData.postValue(multiplePriceBean);
                }, throwable -> {
                    MyToast.showError(App.getContext(), throwable.getMessage());
                }, () -> {

                })

        );
    }

    public void createOrder(String amount, String payAmount, String usedBalance,
                             String gasId, String gunNo, String oilNo, String oilName, String gasName, String priceGun,
                             String priceUnit, String oilType,String phone, String xxCouponId, String czbCouponId, String czbCouponAmount, MutableLiveData<String> orderLiveData) {
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
                .asResponse(String.class)
                .subscribe(orderId -> {
                            orderLiveData.postValue(orderId);
                        },
                        onError -> {
                            MyToast.showError(App.getContext(), onError.getMessage());
                        },
                        () -> {
                        }));
    }

}
