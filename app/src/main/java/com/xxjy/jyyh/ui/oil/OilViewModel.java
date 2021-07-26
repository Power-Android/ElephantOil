package com.xxjy.jyyh.ui.oil;

import android.app.Application;
import android.text.Editable;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
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
import com.xxjy.jyyh.entity.OilUserDiscountEntity;
import com.xxjy.jyyh.entity.OrderNewsEntity;
import com.xxjy.jyyh.entity.RedeemEntity;

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
    public MutableLiveData<OilEntity> oilStationLiveData1 = new MutableLiveData<>();
    public MutableLiveData<List<BannerBean>> bannersLiveData = new MutableLiveData<>();
    public MutableLiveData<OilEntity.StationsBean> oilLiveData = new MutableLiveData<>();
    public MutableLiveData<MultiplePriceBean> multiplePriceLiveData = new MutableLiveData<>();
    public MutableLiveData<MonthCouponEntity> monthCouponLiveData = new MutableLiveData<>();
    public MutableLiveData<OilDefaultPriceEntity> defaultPriceLiveData = new MutableLiveData<>();
    public MutableLiveData<List<CouponBean>> platformCouponLiveData = new MutableLiveData<>();
    public MutableLiveData<List<CouponBean>> businessCouponLiveData = new MutableLiveData<>();
    public MutableLiveData<Float> balanceLiveData = new MutableLiveData<>();
    public MutableLiveData<String> createOrderLiveData = new MutableLiveData<>();
    public MutableLiveData<RedeemEntity> redeemLiveData = new MutableLiveData<>();
    public MutableLiveData<AreaListBean> cityListLiveData = new MutableLiveData<>();
    public MutableLiveData<CarServeCategoryListBean> productCategoryLiveData = new MutableLiveData<>();
    public MutableLiveData<CarServeStoreListBean> storeListLiveData = new MutableLiveData<>();
    public MutableLiveData<RedeemEntity> dragViewLiveData = new MutableLiveData<>();
    public MutableLiveData<OilUserDiscountEntity> discountLiveData = new MutableLiveData<>();
    public MutableLiveData<OilUserDiscountEntity> discountMoneyLiveData = new MutableLiveData<>();
    public MutableLiveData<String> updateCouponLiveData = new MutableLiveData<>();
    public MutableLiveData<List<String>> updateOilsLiveData = new MutableLiveData<>();


    public void getOrderNews() {
        mRespository.getOrderNews(orderNewsLiveData);
    }

    public void getOilNums() {
        mRespository.getOilNums(oilNumLiveData);
    }

    public void getOilStations(String appLatitude, String appLongitude, String oilNo, String orderBy,
                               String distance, String pageNum, String pageSize, String gasName, String method) {
        mRespository.getOilStations(oilStationLiveData, appLatitude,
                appLongitude, oilNo, orderBy, distance, pageNum, pageSize, gasName, method);
    }

    public void getOilStations1(String appLatitude, String appLongitude, String oilNo, String orderBy,
                                String distance, String pageNum, String pageSize, String gasName, String method, String gasIds) {
        mRespository.getOilStations1(oilStationLiveData1, appLatitude,
                appLongitude, oilNo, orderBy, distance, pageNum, pageSize, gasName, method, gasIds);
    }

    public void getBanners() {
        mRespository.getBanners(bannersLiveData);
    }

    public void getOilDetail(String gasId, double lat, double lng) {
        mRespository.getOilDetail(gasId, lat, lng, oilLiveData);
    }

    public void getMultiplePrice(String amount, String gasId, String oilNo, String isUserBill,
                                 String platId, String businessAmount, String monthCouponId,
                                 boolean isUseCoupon, boolean isUseBusinessCoupon, String productIds) {
        mRespository.getMultiplePrice(amount, gasId, oilNo, isUserBill, platId,
                businessAmount, monthCouponId, isUseCoupon, isUseBusinessCoupon, productIds,
                multiplePriceLiveData);
    }

    public void getMonthCoupon(String gasId) {
        mRespository.getMonthCoupon(gasId, monthCouponLiveData);
    }

    public void getDefaultPrice(String gasId, String oilNo) {
        mRespository.getDefaultPrice(gasId, oilNo, defaultPriceLiveData);
    }

    public void getPlatformCoupon(String amount, String gasId, String oilNo) {
        mRespository.getPlatformCoupon(amount, gasId, oilNo, platformCouponLiveData);
    }

    public void getBusinessCoupon(String amount, String gasId, String oilNo) {
        mRespository.getBusinessCoupon(amount, gasId, oilNo, businessCouponLiveData);
    }


    public void getBalance() {
        mRespository.getBalance(balanceLiveData);
    }

    public void createOrder(String amount, String payAmount, String usedBalance, String gasId,
                            String gunNo, String oilNo, String oilName, String gasName, String priceGun,
                            String priceUnit, String oilType, String phone, String xxCouponId, String czbCouponId,
                            String czbCouponAmount, String xxMonthCouponId, String xxMonthCouponAmount,
                            boolean isAddMonthId, boolean isAddMonthAmouont, String productIds, String productAmount) {
        mRespository.createOrder(amount, payAmount, usedBalance, gasId, gunNo, oilNo, oilName, gasName,
                priceGun, priceUnit, oilType, phone, xxCouponId, czbCouponId, czbCouponAmount,
                xxMonthCouponId, xxMonthCouponAmount, isAddMonthId, isAddMonthAmouont, productIds, productAmount, createOrderLiveData);
    }

    public void getRedeem(String gasId) {
        mRespository.getRedeem(gasId, redeemLiveData);
    }

    public void getAreaList(String cityCode) {
        mRespository.getAreaList(cityCode, cityListLiveData);
    }
    public void getProductCategory() {
        mRespository.getProductCategory(productCategoryLiveData);
    }
    public void getCarServeStoreList(int pageIndex,String cityCode,String areaCode,long productCategoryId,int status) {
        mRespository.getCarServeStoreList(storeListLiveData, pageIndex, cityCode, areaCode, productCategoryId, status);
    }

    public void getDragViewInfo() {
        mRespository.getDragViewInfo(dragViewLiveData);
    }

    public void queryOilUserDiscount(String gasId, String oilAmount) {
        mRespository.queryOilUserDiscount(gasId, oilAmount, discountLiveData);
    }

    public void getDiscountMoney(String gasId, String oilAmount) {
        mRespository.getDiscountMoney(gasId, oilAmount, discountMoneyLiveData);
    }

    public void updateCoupon(String id, String gasId, String mapId) {
        mRespository.updateCoupon(id, gasId, mapId, updateCouponLiveData);
    }

    public void getUpdateOils() {
        mRespository.getUpdateOils(updateOilsLiveData);
    }
}
