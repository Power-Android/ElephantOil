package com.xxjy.jyyh.ui.integral;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.ProductBean;
import com.xxjy.jyyh.entity.ProductClassBean;
import com.xxjy.jyyh.entity.SignInBean;
import com.xxjy.jyyh.entity.SignInResultBean;

import java.util.List;

/**
 * @author power
 * @date 1/21/21 11:58 AM
 * @project ElephantOil
 * @description:
 */
public class IntegralViewModel extends BaseViewModel<IntegralRepository> {

    public IntegralViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<BannerBean>> bannersLiveData = new MutableLiveData<>();
    public MutableLiveData<List<ProductClassBean>> productCategorysLiveData = new MutableLiveData<>();
    public MutableLiveData<List<ProductBean>> productLiveData = new MutableLiveData<>();
    public MutableLiveData<String> integralBalanceLiveData = new MutableLiveData<>();
    public MutableLiveData<SignInBean> integralInfoLiveData = new MutableLiveData<>();
    public MutableLiveData<SignInResultBean> integralSignLiveData = new MutableLiveData<>();

    public void getBannerOfPostion() {
        mRespository.getBannerOfPostion(bannersLiveData);
    }
    public void queryProductCategorys() {
        mRespository.queryProductCategorys(productCategorysLiveData);
    }
    public void queryProducts(int categoryId,int pageNum,int pageSize) {
        mRespository.queryProducts(productLiveData, categoryId, pageNum, pageSize);
    }
    public void queryIntegralBalance() {
        mRespository.queryIntegralBalance(integralBalanceLiveData);
    }
    public void getIntegralInfo() {
        mRespository.getIntegralInfo(integralInfoLiveData);
    }
    public void integralSign(int dayOfWeek,int integral,String couponId) {
        mRespository.integralSign( dayOfWeek, integral, couponId,integralSignLiveData);
    }
}
