package com.xxjy.jyyh.ui.order;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.ui.MainRepository;

/**
 * @author power
 * @date 1/21/21 11:39 AM
 * @project ElephantOil
 * @description:
 */
public class OrderListViewModel extends BaseViewModel<OrderListRepository> {

    public OrderListViewModel(@NonNull Application application) {
        super(application);
    }
}
