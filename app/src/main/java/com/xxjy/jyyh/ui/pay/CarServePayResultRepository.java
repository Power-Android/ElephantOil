package com.xxjy.jyyh.ui.pay;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.CarServeApiService;
import com.xxjy.jyyh.entity.CarServeOrderBean;
import com.xxjy.jyyh.entity.PayResultEntity;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/21/21 11:40 AM
 * @project ElephantOil
 * @description:
 */
public class CarServePayResultRepository extends BaseRepository {
    public void getPayResult(String orderId, String payId, String latitude,String longitude, MutableLiveData<PayResultEntity> payResultLiveData) {
        addDisposable(RxHttp.get(CarServeApiService.PAYMENT_RESULT)
                .add("orderId", orderId)
                .add("payId", payId)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("code", "04")
                .asResponse(PayResultEntity.class)
                .subscribe(resultEntity -> payResultLiveData.postValue(resultEntity))

        );
    }
    public void getOrderInfo(String orderId, MutableLiveData<CarServeOrderBean> payResultLiveData) {
        addDisposable(RxHttp.get(CarServeApiService.ORDER_INFO+orderId)
                .asResponse(CarServeOrderBean.class)
                .subscribe(data -> payResultLiveData.postValue(data))

        );
    }
}
