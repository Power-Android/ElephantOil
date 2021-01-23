package com.xxjy.jyyh.dialog;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.databinding.DialogOilTipsLayoutBinding;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.popup.PopupLayer;
import per.goweii.anylayer.utils.AnimatorHelper;

/**
 * @author power
 * @date 1/22/21 5:59 PM
 * @project ElephantOil
 * @description:
 */
public class OilTipsDialog {
    private Context mContext;
    private QMUIFullScreenPopup mPopup;
    private final DialogOilTipsLayoutBinding mBinding;


    public OilTipsDialog(Context context) {
        this.mContext = context;
        mBinding = DialogOilTipsLayoutBinding.bind(
               LayoutInflater.from(context).inflate( R.layout.dialog_oil_tips_layout, null));
        init();
    }

    private void init() {
        mPopup = QMUIPopups.fullScreenPopup(mContext)
                .addView(mBinding.getRoot())
                .closeBtn(false)
                .skinManager(QMUISkinManager.defaultInstance(mContext));

        mBinding.queryTv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null){
                mOnItemClickedListener.onQueryClick(view);
            }
        });
    }

    public void show(View view) {
        if (mPopup != null) {
            mPopup.show(view);
        }
    }

    public void dismiss(){
        if (mPopup != null){
            mPopup.dismiss();
        }
    }

    public interface OnItemClickedListener{
        void onQueryClick(View view);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener){
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
