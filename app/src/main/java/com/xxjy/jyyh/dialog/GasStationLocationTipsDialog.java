package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.View;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.databinding.DialogGasStationLocationTipsBinding;
import com.xxjy.jyyh.databinding.DialogWithdrawalTipsBinding;

/**
 * @author power
 * @date 1/21/21 8:26 PM
 * @project ElephantOil
 * @description:
 */
public class GasStationLocationTipsDialog {
    private Context mContext;
    private QMUIFullScreenPopup mPopup;
    private DialogGasStationLocationTipsBinding mBinding;
    private View mView;
    private String mStationName;

    public GasStationLocationTipsDialog(Context context, View view, String stationName) {
        this.mContext = context;
        this.mView = view;
        this.mStationName = stationName;
        mBinding = DialogGasStationLocationTipsBinding.bind(View.inflate(mContext, R.layout.dialog_gas_station_location_tips, null));
        init();
    }
    public void showPayBt(boolean isShow){
        mBinding.continueView.setVisibility(isShow?View.VISIBLE:View.GONE);
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
        mBinding.stationNameView.setText(mStationName);

        mBinding.selectAgin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null){
                    mPopup.dismiss();
                    mOnClickListener.onClick(view);
                }
            }
        });
        mBinding.continueView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null){
                    mPopup.dismiss();
                    mOnClickListener.onClick(view);
                }
            }
        });
        mBinding.navigationTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null){
                    mPopup.dismiss();
                    mOnClickListener.onClick(view);
                }
            }
        });
    }


    public void show() {
        mPopup.show(mView);
    }

    public interface OnClickListener {
        void onClick(View view);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
}
