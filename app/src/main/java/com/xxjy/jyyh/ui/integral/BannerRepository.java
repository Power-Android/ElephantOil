package com.xxjy.jyyh.ui.integral;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.BannerBean;

import java.util.List;

import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/21/21 11:59 AM
 * @project ElephantOil
 * @description:
 */
public class BannerRepository extends BaseRepository {

    public void getBannerOfPostion(MutableLiveData<List<BannerBean>> bannersLiveData,int position){
        addDisposable(RxHttp.postForm(ApiService.GET_BANNER_OF_POSITION)
                .add("position",position)
                .asResponseList(BannerBean.class)
                .subscribe(data -> bannersLiveData.postValue(data))
        );
    }
}
