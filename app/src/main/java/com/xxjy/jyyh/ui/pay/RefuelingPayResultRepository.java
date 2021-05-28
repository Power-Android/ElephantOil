package com.xxjy.jyyh.ui.pay;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.entity.PayResultEntity;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/21/21 11:40 AM
 * @project ElephantOil
 * @description:
 */
public class RefuelingPayResultRepository extends BaseRepository {

    public void getPayResult(String orderNo, String orderPayNo, MutableLiveData<PayResultEntity> payResultLiveData) {
        addDisposable(RxHttp.postForm("http://192.168.1.84:8833//api/tiein/v1/queryPayOrderResult")
                .add("orderNo", orderNo)
                .add("orderPayNo", orderPayNo)
                .asResponse(PayResultEntity.class)
                .subscribe(new Consumer<PayResultEntity>() {
                    @Override
                    public void accept(PayResultEntity resultEntity) throws Throwable {
                        payResultLiveData.postValue(resultEntity);
                    }
                })

        );
    }
}
