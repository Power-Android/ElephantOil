package com.xxjy.jyyh.ui.order;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xxjy.jyyh.base.BaseViewModel;
import com.xxjy.jyyh.ui.MainRepository;
public class OrderListViewModel extends BaseViewModel<OrderListRepository> {

    public OrderListViewModel(@NonNull Application application) {
        super(application);
    }
}
