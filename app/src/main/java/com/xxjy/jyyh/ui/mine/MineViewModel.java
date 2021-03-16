package com.xxjy.jyyh.ui.mine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.UserBean;

import java.util.List;

/**
 * @author power
 * @date 1/21/21 12:00 PM
 * @project ElephantOil
 * @description:
 */
public class MineViewModel extends BaseViewModel<MineRepository> {
    private MutableLiveData<Boolean> os1LiveData = new MutableLiveData<>();

    public MineViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<BannerBean>> bannersLiveData = new MutableLiveData<>();
    public MutableLiveData<UserBean> userLiveData = new MutableLiveData<>();
    public void queryUserInfo(){
        mRespository.queryUserInfo( userLiveData);
    }

    public void getBannerOfPostion() {
        mRespository.getBannerOfPostion(bannersLiveData);
    }

    public LiveData<Boolean> getOsBalance() {
        mRespository.getOsBalance(os1LiveData);
        return os1LiveData;
    }
}
