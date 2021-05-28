package com.xxjy.jyyh.dialog;

import android.view.Gravity;
import android.view.LayoutInflater;

import com.blankj.utilcode.util.SpanUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.databinding.DialogQueryLayoutBinding;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.dialog.DialogLayer;

/**
 * @author power
 * @date 12/11/20 4:07 PM
 * @project RunElephant
 * @description:
 */
public class QueryDialog {
    private BaseActivity mContext;
    private DialogQueryLayoutBinding mBinding;
    private DialogLayer mDialogLayer;
    private OnConfirmListener mOnConfirmListener;
    private String title, left, right;

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
                    if (mOnConfirmListener != null){
                        mOnConfirmListener.onRefuse();
                    }
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

    public void setContent(String title, String price){
        SpanUtils.with(mBinding.contentTv)
                .append("当前")
                .append(title)
                .setForegroundColor(mContext.getResources().getColor(R.color.color_76FF))
                .append("仅需" + price + "元带回家！确认放弃吗？")
                .create();
        mBinding.contentTv.setGravity(Gravity.START);
        mBinding.cancleTv.setText("残忍拒绝");
        mBinding.confirmTv.setText("继续购买");
    }

    public interface OnConfirmListener {
        void onConfirm();
        void onRefuse();
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener){
        this.mOnConfirmListener = onConfirmListener;
    }
}
