package com.xxjy.jyyh.ui;

import android.app.Application;

import androidx.annotation.NonNull;

import com.xxjy.jyyh.base.BaseViewModel;

/**
 * @author power
 * @date 1/21/21 11:39 AM
 * @project ElephantOil
 * @description:
 */
public class MainViewModel extends BaseViewModel<MainRepository> {

    public MainViewModel(@NonNull Application application) {
        super(application);
    }
}
