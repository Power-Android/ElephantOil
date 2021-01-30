package com.xxjy.jyyh.dialog;

import android.animation.Animator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.databinding.DialogNavigationBinding;
import com.xxjy.jyyh.utils.locationmanger.MapIntentUtils;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.utils.AnimatorHelper;

/**
 * @author power
 * @date 12/7/20 7:35 PM
 * @project RunElephant
 * @description:
 */
public class NavigationDialog {
    private BaseActivity mContext;
    private DialogLayer mDialogLayer;
    private DialogNavigationBinding mBinding;
    private double latitude;
    private double longtitude;
    private String endName;

    public NavigationDialog(BaseActivity context, double latitude, double longtitude, String endName) {
        mContext = context;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.endName = endName;
        mBinding = DialogNavigationBinding.bind(LayoutInflater.from(mContext)
                .inflate(R.layout.dialog_navigation, null, false));

        init();
    }

    private void init() {
        mDialogLayer = AnyLayer.dialog(mContext)
                .contentView(mBinding.getRoot())
                .cancelableOnClickKeyBack(true)
                .cancelableOnTouchOutside(true)
                .gravity(Gravity.BOTTOM)
                .contentAnimator(new Layer.AnimatorCreator() {
                    @Override
                    public Animator createInAnimator(@NonNull View target) {
                        return AnimatorHelper.createBottomInAnim(target);
                    }

                    @Override
                    public Animator createOutAnimator(@NonNull View target) {
                        return AnimatorHelper.createBottomOutAnim(target);
                    }
                });

        if (MapIntentUtils.isPhoneHasMapGaoDe()) {
            mBinding.mapGaode.setVisibility(View.VISIBLE);
            mBinding.lineGaode.setVisibility(View.VISIBLE);
        }
        if (MapIntentUtils.isPhoneHasMapBaiDu()) {
            mBinding.mapBaidu.setVisibility(View.VISIBLE);
            mBinding.lineBaidu.setVisibility(View.VISIBLE);
        }
        if (MapIntentUtils.isPhoneHasMapTencent()) {
            mBinding.mapTencent.setVisibility(View.VISIBLE);
            mBinding.lineTencent.setVisibility(View.VISIBLE);
        }

        mDialogLayer.onClick((layer, view) -> {
            switch (view.getId()){
                case R.id.cancle:
                    layer.dismiss();
                    break;
                case R.id.map_gaode:
                    MapIntentUtils.openGaoDe(mContext, latitude, longtitude, endName);
                    break;
                case R.id.map_baidu:
                    MapIntentUtils.openBaidu(mContext, latitude, longtitude, endName);
                    break;
                case R.id.map_tencent:
                    MapIntentUtils.openTencent(mContext, latitude, longtitude, endName);
                    break;
            }
            layer.dismiss();
        }, R.id.cancle, R.id.map_gaode, R.id.map_baidu, R.id.map_tencent);
    }

    public void show(){
        if (mDialogLayer != null && !mDialogLayer.isShown()){
            mDialogLayer.show();
        }
    }
}
