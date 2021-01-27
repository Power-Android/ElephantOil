package com.xxjy.jyyh.ui.mine;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.UserBean;

import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/21/21 12:00 PM
 * @project ElephantOil
 * @description:
 */
public class MineRepository extends BaseRepository {


    public void queryUserInfo( MutableLiveData<UserBean> userLiveData){
        addDisposable(RxHttp.postForm(ApiService.USER_INFO)
                .asResponse(UserBean.class)
                .subscribe(data -> userLiveData.postValue(data))
        );
    }
}
