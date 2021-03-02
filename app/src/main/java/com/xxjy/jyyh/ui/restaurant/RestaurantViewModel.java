package com.xxjy.jyyh.ui.restaurant;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xxjy.jyyh.base.BaseViewModel;

/**
 * @author power
 * @date 2/26/21 10:42 AM
 * @project ElephantOil
 * @description:
 */
public class RestaurantViewModel extends BaseViewModel<RestaurantRepository> {

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
    }
}
