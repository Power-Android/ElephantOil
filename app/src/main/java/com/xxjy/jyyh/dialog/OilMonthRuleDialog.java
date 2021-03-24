package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.BannerPositionConstants;
import com.xxjy.jyyh.databinding.DialogOilHotLayoutBinding;
import com.xxjy.jyyh.databinding.DialogOilMonthRuleLayoutBinding;
import com.xxjy.jyyh.entity.BannerBean;

import rxhttp.RxHttp;

/**
 * @author power
 * @date 3/18/21 2:34 PM
 * @project ElephantOil
 * @description:
 */
public class OilMonthRuleDialog extends QMUIFullScreenPopup {
    private Context mContext;
    private BaseActivity mActivity;
    private String url;
    private final DialogOilMonthRuleLayoutBinding mBinding;

    public OilMonthRuleDialog(Context context, BaseActivity activity, String monthCouponRule) {
        super(context);
        this.mContext = context;
        this.mActivity = activity;
        this.url = monthCouponRule;
        mBinding = DialogOilMonthRuleLayoutBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_oil_month_rule_layout, null));
        init();
        initData();
    }

    private void init() {
        addView(mBinding.getRoot());
        closeBtn(false);
        skinManager(QMUISkinManager.defaultInstance(mContext));

        Glide.with(mContext).load(url).apply(new RequestOptions().error(R.drawable.default_img_bg)).into(mBinding.imageView);
        mBinding.closeIv.setOnClickListener(view -> {
            dismiss();
        });
    }

    private void initData() {
        getHotIV();
    }

    private void getHotIV() {
//        RxHttp.postForm(ApiService.GET_BANNER_OF_POSITION)
//                .add("position", BannerPositionConstants.OIL_HOT_TIP)
//                .asResponseList(BannerBean.class)
//                .to(RxLife.toMain(mActivity))
//                .subscribe(data -> Glide.with(mContext)
//                        .load(data.get(0).getImgUrl())
//                        .into(mBinding.hotIv));
    }

    public interface OnItemClickedListener {
        void onQueryClick();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
