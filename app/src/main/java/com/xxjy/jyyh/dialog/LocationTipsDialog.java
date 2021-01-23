package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.SelectOilNoAdapter;
import com.xxjy.jyyh.databinding.DialogLocationTipsBinding;
import com.xxjy.jyyh.databinding.DialogSelectOilNumBinding;
import com.xxjy.jyyh.entity.OilNumCheckEntity;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.popup.PopupLayer;

/**
 * @author power
 * @date 1/21/21 8:26 PM
 * @project ElephantOil
 * @description:
 */
public class LocationTipsDialog {
    private Context mContext;
    private QMUIFullScreenPopup mPopup;
    private DialogLocationTipsBinding mBinding;
    private View mView;

    public LocationTipsDialog(Context context,View view) {
        this.mContext = context;
        this.mView = view;
        mBinding = DialogLocationTipsBinding.bind(
                View.inflate(mContext,R.layout.dialog_location_tips, null));
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
