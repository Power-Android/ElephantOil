package com.xxjy.jyyh.base;

import android.animation.Animator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;
import com.qmuiteam.qmui.arch.QMUIActivity;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.utils.AnimatorHelper;

/**
 * @author power
 * @date 12/1/20 1:10 PM
 * @project RunElephant
 * @description:
 */
public abstract class BaseActivity extends QMUIFragmentActivity {

    private DialogLayer mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLoadingDialog();
        BarUtils.setStatusBarLightMode(this, true);
    }

    /**
     * 设置透明状态栏-默认为深色
     */
    public void setTransparentStatusBar(){
        BarUtils.transparentStatusBar(this);
        BarUtils.setStatusBarLightMode(this, true);
    }

    /**
     * @param view toolbar设置margin
     *
     * 设置透明状态栏的margin
     */
    public void setTransparentStatusBar(View view){
        setTransparentStatusBar(view, true);
    }

    /**
     * @param isDark 默认为深色
     *
     * 设置透明状态栏的颜色
     */
    public void setTransparentStatusBar(View view, boolean isDark){
        BarUtils.transparentStatusBar(this);
        if (view != null){
            BarUtils.addMarginTopEqualStatusBarHeight(view);
        }
        BarUtils.setStatusBarLightMode(this, isDark);
    }

    public void showToast(String msg) {
        MyToast.showContentToast(this, msg);
    }

    public void showToastInfo(String msg) {
        MyToast.showInfo(this, msg);
    }

    public void showToastWarning(String msg) {
        MyToast.showWarning(this, msg);
    }

    public void showToastError(String msg) {
        MyToast.showError(this, msg);
    }

    public void showToastSuccess(String msg) {
        MyToast.showSuccess(this, msg);
    }

    public void initLoadingDialog(){
        mLoadingDialog = AnyLayer.dialog()
                .contentView(R.layout.loading_layout)
                .cancelableOnClickKeyBack(true)
                .gravity(Gravity.CENTER)
                .contentAnimator(new Layer.AnimatorCreator() {
                    @Override
                    public Animator createInAnimator(@NonNull View target) {
                        return AnimatorHelper.createAlphaInAnim(target);
                    }

                    @Override
                    public Animator createOutAnimator(@NonNull View target) {
                        return AnimatorHelper.createAlphaOutAnim(target);
                    }
                })
                .cancelableOnTouchOutside(false);
    }

    /**
     * 展示读取的 dialog
     */
    public void showLoadingDialog() {
        if (mLoadingDialog != null){
            mLoadingDialog.show();
        }
    }

    /**
     * 隐藏读取的 dialog
     */
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShown()) {
            mLoadingDialog.dismiss();
        }
    }


}
