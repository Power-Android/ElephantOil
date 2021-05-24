package com.xxjy.jyyh.ui.mine;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.BannerPositionConstants;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.MonthCardBean;
import com.xxjy.jyyh.entity.UserBean;

import java.util.List;

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

    public void getBannerOfPostion(MutableLiveData<List<BannerBean>> bannersLiveData){
        addDisposable(RxHttp.postForm(ApiService.GET_BANNER_OF_POSITION)
                .add("position", BannerPositionConstants.MINE_BANNER)
                .asResponseList(BannerBean.class)
                .subscribe(data -> bannersLiveData.postValue(data))
        );
    }
    public void getMonthEquityInfo(MutableLiveData<MonthCardBean> monthEquityInfoLiveData){
        addDisposable(RxHttp.postForm(ApiService.GET_MONTH_EQUITY_INFO)
                .asResponse(MonthCardBean.class)
                .subscribe(data -> monthEquityInfoLiveData.postValue(data))
        );
    }


    public void getOsBalance(MutableLiveData<Boolean> os1LiveData) {
        addDisposable(RxHttp.postForm(ApiService.GET_OS_BALANCE)
                .asResponse(Boolean.class)
                .subscribe(b -> {
                    os1LiveData.postValue(b);
                })
        );
    }
}
