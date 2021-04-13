package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.databinding.DialogOilDistanceCheckedBinding;
import com.xxjy.jyyh.databinding.DialogRefundLayoutBinding;
import com.xxjy.jyyh.utils.toastlib.Toasty;

/**
 * 退款弹窗
 */

public class RefundDialog extends QMUIFullScreenPopup {

    private Context mContext;
    private View mView;
    private DialogRefundLayoutBinding mBinding;
    private OnConfirmListener mOnConfirmListener;

    public RefundDialog(Context context, View view) {
        super(context);
        this.mContext = context;
        this.mView = view;
        mBinding = DialogRefundLayoutBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_refund_layout, null));
        init();
    }

    private void init() {
        addView(mBinding.getRoot());
        closeBtn(false);
        skinManager(QMUISkinManager.defaultInstance(mContext));
        mBinding.cancelBt.setOnClickListener(v -> dismiss());
        mBinding.confirmBt.setOnClickListener(v -> {
            if (mBinding.checkBox.isChecked()) {
                if (mOnConfirmListener != null) {
                    mOnConfirmListener.onConfirm(mBinding.checkBox.getText().toString().trim() + mBinding.editView.getText().toString().trim());
                }
            } else {
                if (TextUtils.isEmpty(mBinding.editView.getText().toString().trim())) {
                    Toasty.info(mContext, "请输入退款原因").show();
                    return;
                }
                if (mOnConfirmListener != null) {
                    mOnConfirmListener.onConfirm(mBinding.editView.getText().toString().trim());
                }
            }
            dismiss();
        });
    }

    public interface OnConfirmListener {
        void onConfirm(String content);
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        mOnConfirmListener = onConfirmListener;

    }
}
