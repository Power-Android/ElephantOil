package com.xxjy.jyyh.ui.search;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.entity.ProductBean;

import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/23/21 4:33 PM
 * @project ElephantOil
 * @description:
 */
public class SearchRepository extends BaseRepository {
    public void getIntegrals(String content, String integralType, String pageNum, String pageSize,
                             MutableLiveData<List<ProductBean>> intergraLiveData) {
        addDisposable(RxHttp.postForm(ApiService.QUERY_PRODUCTS_BY_NAME)
                .add("name", content)
                .add("type", integralType)
                .add(Constants.PAGE, pageNum)
                .add(Constants.PAGE_SIZE, pageSize)
                .asResponseList(ProductBean.class)
                .subscribe(productBeans -> intergraLiveData.postValue(productBeans))
        );
    }
}
