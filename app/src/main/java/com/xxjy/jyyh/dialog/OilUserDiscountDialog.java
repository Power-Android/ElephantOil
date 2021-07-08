package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.LayoutInflater;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.databinding.DialogOilUserDiscountBinding;

/**
 * @author power
 * @date 2021/7/8 2:02 下午
 * @project ElephantOil
 * @description:
 */
public class OilUserDiscountDialog extends QMUIFullScreenPopup {
    private Context mContext;
    private BaseActivity mActivity;
    private String title;
    private DialogOilUserDiscountBinding mBinding;

    public OilUserDiscountDialog(Context context, BaseActivity activity, String title) {
        super(context);
        this.mContext = context;
        this.mActivity = activity;
        this.title = title;
        mBinding = DialogOilUserDiscountBinding.bind(LayoutInflater.from(context).inflate(R.layout.dialog_oil_user_discount, null));
        init();
        initData();
    }

    private void init() {
        addView(mBinding.getRoot());
        closeBtn(false);
        skinManager(QMUISkinManager.defaultInstance(mContext));
        mBinding.itemFirmTv.setOnClickListener(view -> {
            if (mOnFirmClickListener != null){
                mOnFirmClickListener.onFirmClick();
            }
            dismiss();
        });
    }

    private void initData() {
        mBinding.itemContentTv.setText(title);
    }

    public interface onFirmClickListener{
        void onFirmClick();
    }

    private onFirmClickListener mOnFirmClickListener;

    public void setOnFirmClickListener(onFirmClickListener onFirmClickListener){
        this.mOnFirmClickListener = onFirmClickListener;
    }

}
