package com.xxjy.jyyh.ui.search;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.ProductBean;
import com.xxjy.jyyh.entity.RecomdEntity;

import java.util.List;

/**
 * @author power
 * @date 1/23/21 4:32 PM
 * @project ElephantOil
 * @description:
 */
public class SearchViewModel extends BaseViewModel<SearchRepository> {
    public MutableLiveData<List<ProductBean>> intergraLiveData = new MutableLiveData<>();
    public MutableLiveData<List<RecomdEntity>> recomdLiveData = new MutableLiveData<>();
    public MutableLiveData<List<RecomdEntity>> recomdLiveData1 = new MutableLiveData<>();

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void getIntegrals(String content, String integralType, String pageNum, String pageSize) {
        getRepository().getIntegrals(content, integralType, pageNum, pageSize, intergraLiveData);
    }

    public void getRecomnd(String type) {
        if (TextUtils.equals("2", type)){
            mRespository.getRecomnd(type, recomdLiveData);
        }else {
            mRespository.getRecomnd1(type, recomdLiveData1);
        }
    }
}
