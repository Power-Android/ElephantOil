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
import com.xxjy.jyyh.databinding.DialogOilServiceLayoutBinding;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.MultiplePriceBean;

import rxhttp.RxHttp;

/**
 * @author power
 * @date 2/17/21 7:33 PM
 * @project ElephantOil
 * @description:
 */
public class OilServiceDialog extends QMUIFullScreenPopup {
    private Context mContext;
    private BaseActivity mActivity;
    private MultiplePriceBean multiplePriceBean;
    private final DialogOilServiceLayoutBinding mBinding;

    public OilServiceDialog(Context context, BaseActivity activity, MultiplePriceBean multiplePriceBean) {
        super(context);
        this.mContext = context;
        this.mActivity = activity;
        this.multiplePriceBean = multiplePriceBean;

        mBinding = DialogOilServiceLayoutBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_oil_service_layout, null));
        init();
        initData();
    }

    public void setData(MultiplePriceBean multiplePriceBean) {
        this.multiplePriceBean = multiplePriceBean;
        dispatchData();
    }

    private void dispatchData() {
        mBinding.serviceMoneyTv.setText("¥" + multiplePriceBean.getTotalDiscountAmount());
        mBinding.fallMoney.setText("-¥" + multiplePriceBean.getDepreciateAmount() + "元");
        mBinding.serviceMoney.setText("+¥" + multiplePriceBean.getServiceChargeAmount() + "元");
        mBinding.tv4.setText("本次订单收取服务费 " + multiplePriceBean.getServiceChargeAmount() + " 元");
    }

    private void init() {
        addView(mBinding.getRoot());
        closeBtn(false);
        skinManager(QMUISkinManager.defaultInstance(mContext));

        dispatchData();

        mBinding.closeIv.setOnClickListener(view -> {
            dismiss();
        });
    }

    private void initData() {

    }


    public interface OnItemClickedListener {
        void onQueryClick();
    }

    private OilHotDialog.OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OilHotDialog.OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
