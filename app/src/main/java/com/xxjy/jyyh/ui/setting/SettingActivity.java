package com.xxjy.jyyh.ui.setting;


import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.CleanUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivitySettingBinding;
import com.xxjy.jyyh.utils.DataCleanManager;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.toastlib.Toasty;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingActivity extends BindingActivity<ActivitySettingBinding,SettingViewModel> {


    private Disposable observableDisposable;
    @Override
    protected void initView() {
//        mBinding.topLayout.setTitle("设置");
//        mBinding.topLayout.addLeftImageButton(R.drawable.arrow_back_black,
//                R.id.qmui_topbar_item_left_back).setOnClickListener(v -> finish());
        mBinding.titleLayout.tvTitle.setText("设置");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);


        try {
            mBinding.cacheDataView.setText(DataCleanManager.getTotalCacheSize(this));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void initListener() {
        mBinding.aboutUsLayout.setOnClickListener(this::onViewClicked);
        mBinding.clearCacheLayout.setOnClickListener(this::onViewClicked);
        mBinding.logoutView.setOnClickListener(this::onViewClicked);

    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.about_us_layout:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;
            case R.id.clear_cache_layout:
                observableDisposable=  Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
                    CleanUtils.cleanInternalCache();
                    CleanUtils.cleanExternalCache();
                    emitter.onNext(true);
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isfinish ->{
                                    if(isfinish) {
                                        Toasty.success(SettingActivity.this,"清理成功").show();
                                        mBinding.cacheDataView.setText("0.0KB");
                                    }
                                }
                        );
                break;

            case R.id.logout_view:
                LoginHelper.loginOut(this);
                mViewModel.logout();
                finish();
                break;
        }

    }

    @Override
    protected void dataObservable() {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(observableDisposable!=null){
            observableDisposable.dispose();
        }

    }
}