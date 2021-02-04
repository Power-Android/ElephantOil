package com.xxjy.jyyh.ui.integral;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.BannerBean;

import java.util.List;

/**
 * @author power
 * @date 1/21/21 11:58 AM
 * @project ElephantOil
 * @description:
 */
public class BannerViewModel extends BaseViewModel<BannerRepository> {

    public BannerViewModel(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<List<BannerBean>> getBannerOfPostion(int position) {
        MutableLiveData<List<BannerBean>> bannersLiveData = new MutableLiveData<>();
        mRespository.getBannerOfPostion(bannersLiveData, position);
        return bannersLiveData;
    }
}
