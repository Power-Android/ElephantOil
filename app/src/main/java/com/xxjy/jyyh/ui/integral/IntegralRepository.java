package com.xxjy.jyyh.ui.integral;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.ProductMapKeyConstants;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.ProductBean;
import com.xxjy.jyyh.entity.ProductClassBean;

import java.util.List;

import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/21/21 11:59 AM
 * @project ElephantOil
 * @description:
 */
public class IntegralRepository extends BaseRepository {

    public void queryIntegralBalance(MutableLiveData<String> integralBalanceLiveData){
        addDisposable(RxHttp.postForm(ApiService.QUERY_INTEGRAL_BALANCE)
                .asResponse(String.class)
                .subscribe(data -> integralBalanceLiveData.postValue(data))
        );
    }
    public void getBannerOfPostion(MutableLiveData<List<BannerBean>> bannersLiveData){
        addDisposable(RxHttp.postForm(ApiService.GET_BANNER_OF_POSITION)
                .add("position",7)
                .asResponseList(BannerBean.class)
                .subscribe(data -> bannersLiveData.postValue(data))
        );
    }
    public void queryProductCategorys(MutableLiveData<List<ProductClassBean>> productCategorysLiveData){
        addDisposable(RxHttp.postForm(ApiService.PRODUCT_CATEGORYS)
                .add("mapKey", ProductMapKeyConstants.SHOPPING_INDEX)
                .asResponseList(ProductClassBean.class)
                .subscribe(data -> productCategorysLiveData.postValue(data))
        );
    }
    public void queryProducts(MutableLiveData<List<ProductBean>> productLiveData, int categoryId, int pageNum, int pageSize){
        addDisposable(RxHttp.postForm(ApiService.QUERY_PRODUCTS)
                .add("categoryId",categoryId)
                .add("pageNum",pageNum)
                .add("pageSize",pageSize)
                .asResponseList(ProductBean.class)
                .subscribe(data -> productLiveData.postValue(data))
        );
    }
}
