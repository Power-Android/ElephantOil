package com.xxjy.jyyh.ui;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;

import io.reactivex.rxjava3.functions.Consumer;
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
}
