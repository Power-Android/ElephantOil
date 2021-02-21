package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.BannerPositionConstants;
import com.xxjy.jyyh.databinding.DialogOilHotLayoutBinding;
import com.xxjy.jyyh.databinding.DialogOilTipsLayoutBinding;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.OilTipsEntity;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import rxhttp.RxHttp;

/**
 * @author power
 * @date 2/8/21 3:22 PM
 * @project ElephantOil
 * @description:
 */
public class OilHotDialog extends QMUIFullScreenPopup {
    private Context mContext;
    private BaseActivity mActivity;
    private final DialogOilHotLayoutBinding mBinding;

    public OilHotDialog(Context context, BaseActivity activity) {
        super(context);
        this.mContext = context;
        this.mActivity = activity;
        mBinding = DialogOilHotLayoutBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_oil_hot_layout, null));
        init();
        initData();
    }

    private void init() {
        addView(mBinding.getRoot());
        closeBtn(false);
        skinManager(QMUISkinManager.defaultInstance(mContext));
        mBinding.closeIv.setOnClickListener(view -> {
            dismiss();
        });
    }

    private void initData() {
        getHotIV();
    }

    private void getHotIV() {
        RxHttp.postForm(ApiService.GET_BANNER_OF_POSITION)
                .add("position", BannerPositionConstants.OIL_HOT_TIP)
                .asResponseList(BannerBean.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(data -> Glide.with(mContext)
                        .load(data.get(0).getImgUrl())
                        .into(mBinding.hotIv));
    }

    public interface OnItemClickedListener {
        void onQueryClick();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
