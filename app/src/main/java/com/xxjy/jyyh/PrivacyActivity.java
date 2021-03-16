package com.xxjy.jyyh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.xxjy.jyyh.adapter.PrivacyAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityPrivacyBinding;
import com.xxjy.jyyh.entity.PrivacyEntity;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.MainViewModel;
import com.xxjy.jyyh.ui.integral.BannerViewModel;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.locationmanger.GPSUtil;
import com.xxjy.jyyh.utils.locationmanger.MapLocationHelper;

import java.util.ArrayList;
import java.util.List;

public class PrivacyActivity extends BindingActivity<ActivityPrivacyBinding, BannerViewModel>
        implements PermissionUtils.SimpleCallback {

    private List<PrivacyEntity> mList = new ArrayList<>();
    private boolean isFirstIn = true;

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.view1);

        PrivacyEntity privacyEntity1 = new PrivacyEntity();
        privacyEntity1.setImg(R.drawable.privacy_net);
        privacyEntity1.setTitle("连接网络");
        privacyEntity1.setContent("需要连接网络方能正常为您提供服务。");
        mList.add(privacyEntity1);

        PrivacyEntity privacyEntity2 = new PrivacyEntity();
        privacyEntity2.setImg(R.drawable.privacy_location);
        privacyEntity2.setTitle("定位信息");
        privacyEntity2.setContent("获取您的定位信息后，方便为您提供附近油站的定位服务。");
        mList.add(privacyEntity2);

        PrivacyEntity privacyEntity3 = new PrivacyEntity();
        privacyEntity3.setImg(R.drawable.privacy_notify);
        privacyEntity3.setTitle("通知");
        privacyEntity3.setContent("开启通知权限，方便您接收活动通知、业务通知等信息。");
        mList.add(privacyEntity3);

        PrivacyEntity privacyEntity4 = new PrivacyEntity();
        privacyEntity4.setImg(R.drawable.privacy_pic);
        privacyEntity4.setTitle("相册");
        privacyEntity4.setContent("开启相册权限，方可保存活动海报至手机相册中。");
        mList.add(privacyEntity4);

        PrivacyEntity privacyEntity5 = new PrivacyEntity();
        privacyEntity5.setImg(R.drawable.privacy_camre);
        privacyEntity5.setTitle("相机");
        privacyEntity5.setContent("开启相机权限，方可使用客服咨询拍照功能。");
        mList.add(privacyEntity5);

        PrivacyEntity privacyEntity6 = new PrivacyEntity();
        privacyEntity6.setImg(R.drawable.privacy_rili);
        privacyEntity6.setTitle("日历");
        privacyEntity6.setContent("开启日历权限，方便您在指定日期选择接收活动提醒。");
        mList.add(privacyEntity6);


        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PrivacyAdapter privacyAdapter = new PrivacyAdapter(R.layout.adapter_privacy_layout, mList);
        mBinding.recyclerView.setAdapter(privacyAdapter);
    }

    @Override
    protected void initListener() {
        mBinding.noAgreeTv.setOnClickListener(this::onViewClicked);
        mBinding.agreeTv.setOnClickListener(this::onViewClicked);
        mBinding.view2.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.no_agree_tv:
                AppUtils.exitApp();
                break;
            case R.id.agree_tv:
                requestPermission();
                break;
            case R.id.view2:
                NaviActivityInfo.disPathIntentFromUrl(this, Constants.YINSI_ZHENG_CE);
                break;
        }
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        PermissionUtils.permission(
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .callback(this)
                .request();
    }


    @Override
    protected void dataObservable() {

    }

    @Override
    public void onGranted() {
        next();
    }

    @Override
    public void onDenied() {
        if (GPSUtil.isOpenGPS(this)) {
            if (isFirstIn) {
                isFirstIn = false;
                next();
            } else {
                next();
            }
        } else {
            showToastWarning("软件需要对您的位置进行定位,建议您打开 GPS 定位获得更好的体验");
            next();
        }
    }

    private void next() {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        UserConstants.setAgreePrivacy(true);
        MapLocationHelper.refreshLocation();
        startActivity(new Intent(this, MainActivity.class));
    }
}