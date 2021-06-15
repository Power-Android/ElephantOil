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

    public void getPayResult(String orderNo, String orderPayNo, String latitude,String longitude, MutableLiveData<PayResultEntity> payResultLiveData) {
        addDisposable(RxHttp.postForm(ApiService.PAY_ORDER_RESULT)
                .add("orderNo", orderNo)
                .add("orderPayNo", orderPayNo)
                .add("latitude", latitude)
                .add("longitude", longitude)
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
