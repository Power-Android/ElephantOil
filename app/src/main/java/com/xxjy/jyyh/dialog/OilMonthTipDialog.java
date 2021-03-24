package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.databinding.DialogOilMonthRuleLayoutBinding;
import com.xxjy.jyyh.databinding.DialogOilMonthTipLayoutBinding;

/**
 * @author power
 * @date 3/18/21 5:23 PM
 * @project ElephantOil
 * @description:
 */
public class OilMonthTipDialog extends QMUIFullScreenPopup {
    private Context mContext;
    private BaseActivity mActivity;
    private String url;
    private final DialogOilMonthTipLayoutBinding mBinding;

    public OilMonthTipDialog(Context context, BaseActivity activity, String monthCouponImgUrl) {
        super(context);
        this.mContext = context;
        this.mActivity = activity;
        this.url = monthCouponImgUrl;
        mBinding = DialogOilMonthTipLayoutBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_oil_month_tip_layout, null));
        init();
        initData();
    }

    private void init() {
        addView(mBinding.getRoot());
        closeBtn(false);
        skinManager(QMUISkinManager.defaultInstance(mContext));

        Glide.with(mContext).load(url).apply(new RequestOptions().error(R.drawable.default_img_bg)).into(mBinding.imgIv);
        mBinding.cancleTv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null){
                mOnItemClickedListener.onRefuseClick();
            }
        });
        mBinding.useIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickedListener != null){
                    mOnItemClickedListener.onQueryClick();
                }
            }
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
        void onRefuseClick();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
