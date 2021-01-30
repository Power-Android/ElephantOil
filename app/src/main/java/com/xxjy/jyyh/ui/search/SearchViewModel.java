package com.xxjy.jyyh.ui.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.ProductBean;

import java.util.List;

/**
 * @author power
 * @date 1/23/21 4:32 PM
 * @project ElephantOil
 * @description:
 */
public class SearchViewModel extends BaseViewModel<SearchRepository> {
    public MutableLiveData<List<ProductBean>> intergraLiveData = new MutableLiveData<>();

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void getIntegrals(String content, String integralType, String pageNum, String pageSize) {
        getRepository().getIntegrals(content, integralType, pageNum, pageSize, intergraLiveData);
    }
}
