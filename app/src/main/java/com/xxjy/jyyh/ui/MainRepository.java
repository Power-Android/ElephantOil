package com.xxjy.jyyh.ui;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.HomeNewUserBean;

import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/21/21 11:40 AM
 * @project ElephantOil
 * @description:
 */
public class MainRepository extends BaseRepository {

    public void getOsOverAll(MutableLiveData<Boolean> osLiveData) {
        addDisposable(RxHttp.postForm(ApiService.GET_OS_OVERALL)
                .asResponse(Boolean.class)
                .subscribe(b -> {
                    osLiveData.postValue(b);
                })
        );
    }
    public void newUserStatus(MutableLiveData<HomeNewUserBean> newUserLiveData) {
        addDisposable(RxHttp.postForm(ApiService.NEW_USER_STATUS)
                .add("code","newuser")
                .asResponse(HomeNewUserBean.class)
                .subscribe(b -> {
                    newUserLiveData.postValue(b);
                })
        );
    }

    public void getIsNewUser(MutableLiveData<Boolean> isNewLiveData) {
        addDisposable(RxHttp.postForm(ApiService.IS_NEW_USER)
                .asResponse(Boolean.class)
                .subscribe(b -> isNewLiveData.postValue(b))

        );
    }
}
