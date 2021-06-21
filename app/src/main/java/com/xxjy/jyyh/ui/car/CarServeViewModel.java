package com.xxjy.jyyh.ui.car;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.AreaListBean;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.CarServeCategoryListBean;
import com.xxjy.jyyh.entity.CarServeCommitOrderBean;
import com.xxjy.jyyh.entity.CarServeCouponListBean;
import com.xxjy.jyyh.entity.CarServeStoreDetailsBean;
import com.xxjy.jyyh.entity.CarServeStoreListBean;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.MonthCouponEntity;
import com.xxjy.jyyh.entity.MultiplePriceBean;
import com.xxjy.jyyh.entity.OilDefaultPriceEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilNumBean;
import com.xxjy.jyyh.entity.OrderNewsEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.entity.RedeemEntity;
import com.xxjy.jyyh.http.Response;
import com.xxjy.jyyh.jscalljava.jsbean.OrderBean;

import java.util.List;

public class CarServeViewModel extends BaseViewModel<CarServeRepository> {

    public CarServeViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<CarServeStoreDetailsBean> storeLiveData = new MutableLiveData<>();
    public MutableLiveData<CarServeCouponListBean> usableCouponLiveData = new MutableLiveData<>();
    public MutableLiveData<CarServeCommitOrderBean> commitOrderLiveData = new MutableLiveData<>();
    public MutableLiveData<RedeemEntity> tyingProductLiveData = new MutableLiveData<>();
    public MutableLiveData<PayOrderEntity> payOrderLiveData = new MutableLiveData<>();

    public MutableLiveData<AreaListBean> cityListLiveData = new MutableLiveData<>();
    public MutableLiveData<CarServeCategoryListBean> productCategoryLiveData = new MutableLiveData<>();
    public MutableLiveData<CarServeStoreListBean> storeListLiveData = new MutableLiveData<>();
    public MutableLiveData<Response> cancelOrderLiveData = new MutableLiveData<>();

    public void getStoreDetails(String storeNo) {
        mRespository.getStoreDetails(storeLiveData,storeNo);
    }
    public void getUsableCoupon(int categoryId) {
        mRespository.getUsableCoupon(usableCouponLiveData,categoryId);
    }
    public void commitOrder(String storeId,String productId,
                            String realMoney, String couponType,long couponId,
                            String couponAmount,String sku) {
        mRespository.commitOrder(commitOrderLiveData, storeId, productId,
                 realMoney,  couponType, couponId, couponAmount, sku);
    }
    public void cancelOrder(String orderId) {
        mRespository.cancelOrder(cancelOrderLiveData, orderId);
    }
    public void tyingProduct() {
        mRespository.tyingProduct(tyingProductLiveData);
    }
    public void payOrder(String payType, String orderId, String payAmount) {
        mRespository.payOrder(payType, orderId, payAmount, payOrderLiveData);
    }

    public void getAreaList(String cityCode) {
        mRespository.getAreaList(cityCode, cityListLiveData);
    }
    public void getProductCategory() {
        mRespository.getProductCategory(productCategoryLiveData);
    }
    public void getCarServeStoreList(int pageIndex,String cityCode,String areaCode,long productCategoryId,int status,String storeName) {
        mRespository.getCarServeStoreList(storeListLiveData, pageIndex, cityCode, areaCode, productCategoryId, status,storeName);
    }
}
