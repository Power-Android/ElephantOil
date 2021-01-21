package com.xxjy.jyyh.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xxjy.jyyh.base.BaseViewModel;

/**
 * @author power
 * @date 1/21/21 11:46 AM
 * @project ElephantOil
 * @description:
 */
public class HomeViewModel extends BaseViewModel<HomeRepository> {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }
}
