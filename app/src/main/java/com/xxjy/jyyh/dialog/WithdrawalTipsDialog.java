package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.View;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.databinding.DialogLocationTipsBinding;
import com.xxjy.jyyh.databinding.DialogWithdrawalTipsBinding;

/**
 * @author power
 * @date 1/21/21 8:26 PM
 * @project ElephantOil
 * @description:
 */
public class WithdrawalTipsDialog {
    private Context mContext;
    private QMUIFullScreenPopup mPopup;
    private DialogWithdrawalTipsBinding mBinding;
    private View mView;

    public WithdrawalTipsDialog(Context context, View view) {
        this.mContext = context;
        this.mView = view;
        mBinding = DialogWithdrawalTipsBinding.bind(
                View.inflate(mContext,R.layout.dialog_withdrawal_tips, null));
        init();
    }

    private void init() {
        if (mPopup == null) {
            mPopup = QMUIPopups.fullScreenPopup(mContext)
                    .addView(mBinding.getRoot())
                    .closeBtn(false)
                    .skinManager(QMUISkinManager.defaultInstance(mContext));

        }
        mPopup.onBlankClick(new QMUIFullScreenPopup.OnBlankClickListener() {
            @Override
            public void onBlankClick(QMUIFullScreenPopup popup) {
                popup.dismiss();
            }
        });
        mBinding.closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.dismiss();
            }
        });
        mBinding.noTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.dismiss();
            }
        });
    }



    public void show(){
        mPopup.show(mView);
    }

    public interface OnClickListener{
        void onClick(boolean isConfirm);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }
}
