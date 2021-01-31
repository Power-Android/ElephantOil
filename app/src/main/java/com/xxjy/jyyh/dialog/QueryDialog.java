package com.xxjy.jyyh.dialog;

import android.animation.Animator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.databinding.DialogQueryLayoutBinding;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.utils.AnimatorHelper;

/**
 * @author power
 * @date 12/11/20 4:07 PM
 * @project RunElephant
 * @description:
 */
public class QueryDialog {
    private BaseActivity mContext;
    private final DialogQueryLayoutBinding mBinding;
    private DialogLayer mDialogLayer;
    private OnConfirmListener mOnConfirmListener;

    public QueryDialog(BaseActivity context) {
        this.mContext = context;
        mBinding = DialogQueryLayoutBinding.bind(LayoutInflater.from(mContext)
                .inflate(R.layout.dialog_query_layout, null, false));
        initView();
    }

    private void initView() {
        mDialogLayer = AnyLayer.dialog(mContext)
                .contentView(mBinding.getRoot())
                .cancelableOnClickKeyBack(true)
                .cancelableOnTouchOutside(true)
                .gravity(Gravity.CENTER)
                .backgroundDimDefault()
                .animStyle(DialogLayer.AnimStyle.ALPHA);
        mDialogLayer.onClick((layer, view) -> {
            switch (view.getId()){
                case R.id.cancle_tv:
                    layer.dismiss();
                    break;
                case R.id.confirm_tv:
                    if (mOnConfirmListener != null){
                        mOnConfirmListener.onConfirm();
                    }
                    layer.dismiss();
                    break;
            }
        }, R.id.cancle_tv, R.id.confirm_tv);
    }

    public void show(){
        if (mDialogLayer != null && !mDialogLayer.isShown()){
            mDialogLayer.show();
        }
    }

    public interface OnConfirmListener {
        void onConfirm();
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener){
        this.mOnConfirmListener = onConfirmListener;
    }
}
