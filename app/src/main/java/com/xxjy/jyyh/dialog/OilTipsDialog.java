package com.xxjy.jyyh.dialog;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.databinding.DialogOilTipsLayoutBinding;
import com.xxjy.jyyh.entity.OilTipsEntity;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.popup.PopupLayer;
import per.goweii.anylayer.utils.AnimatorHelper;
import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/22/21 5:59 PM
 * @project ElephantOil
 * @description:
 */
public class OilTipsDialog {
    private Context mContext;
    private BaseActivity mActivity;
    private QMUIFullScreenPopup mPopup;
    private final DialogOilTipsLayoutBinding mBinding;


    public OilTipsDialog(Context context, BaseActivity activity) {
        this.mContext = context;
        this.mActivity = activity;
        mBinding = DialogOilTipsLayoutBinding.bind(
               LayoutInflater.from(context).inflate( R.layout.dialog_oil_tips_layout, null));
        init();
        initData();
    }

    private void init() {
        mPopup = QMUIPopups.fullScreenPopup(mContext)
                .addView(mBinding.getRoot())
                .closeBtn(false)
                .skinManager(QMUISkinManager.defaultInstance(mContext));

        mPopup.onDismiss(() -> {
            if (mOnItemClickedListener != null){
                mOnItemClickedListener.onQueryClick();
            }
        });
        mBinding.queryTv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null){
                mOnItemClickedListener.onQueryClick();
            }
        });

        mBinding.closeIv.setOnClickListener(view -> {
            if (mBinding.checkBox.isChecked()){
                if (mOnItemClickedListener != null){
                    mOnItemClickedListener.onQueryClick();
                }
            }else {
                MyToast.showInfo(mContext, "请勾选并阅读使用规则");
            }
        });

        mBinding.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                mBinding.queryTv.setEnabled(true);
            }else {
                mBinding.queryTv.setEnabled(false);
            }
        });
    }

    private void initData(){

    }

    private void getOrderTip() {
        RxHttp.postForm(ApiService.GET_ORDER_TIP)
                .asResponseList(OilTipsEntity.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(oilTipsEntities -> Glide.with(mContext)
                        .load(oilTipsEntities.get(0).getImgUrl())
                        .into(mBinding.tipIv));
    }

    public void show(View view) {
        if (mPopup != null) {
            mPopup.show(view);
        }
        getOrderTip();
    }

    public void dismiss(){
        if (mPopup != null){
            mPopup.dismiss();
        }
    }

    public interface OnItemClickedListener{
        void onQueryClick();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener){
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
