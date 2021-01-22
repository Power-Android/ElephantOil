package com.xxjy.jyyh.ui.login;

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
public class MobileLoginViewModel extends BaseViewModel<MobileLoginRepository> {

    public MobileLoginViewModel(@NonNull Application application) {
        super(application);
    }
}
