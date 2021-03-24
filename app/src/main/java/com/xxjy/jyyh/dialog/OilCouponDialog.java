package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilCouponAdapter;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.databinding.DialogOilCouponBinding;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/25/21 2:10 PM
 * @project ElephantOil
 * @description:
 */
public class OilCouponDialog extends BottomSheetDialog {
    private Context mContext;
    private BaseActivity mActivity;
    private OilEntity.StationsBean mStationsBean;
    private int oilNoPosition, gunNoPosition;
    private boolean isPlat;
    private BottomSheetBehavior mBehavior;
    private final DialogOilCouponBinding mBinding;
    private List<CouponBean> mList = new ArrayList<>();
    private OilCouponAdapter mOilCouponAdapter;
    private String amount, oilNo;

    public OilCouponDialog(Context context, BaseActivity activity, String amount, OilEntity.StationsBean stationsBean,
                           int oilNoPosition, int gunNoPosition, String oilNo, boolean isPlat) {
        super(context, R.style.bottom_sheet_dialog);
        this.mContext = context;
        this.mActivity = activity;
        this.amount = amount;
        this.mStationsBean = stationsBean;
        this.oilNoPosition = oilNoPosition;
        this.gunNoPosition = gunNoPosition;
        this.oilNo = oilNo;
        this.isPlat = isPlat;
        mBinding = DialogOilCouponBinding.bind(
                View.inflate(context, R.layout.dialog_oil_coupon, null));
        init();
        initData();
    }

    private void init() {
        getWindow().getAttributes().windowAnimations =
                R.style.bottom_sheet_dialog;
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(mBinding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
        mBehavior.setSkipCollapsed(true);
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initData() {
        mBinding.tv1.setText(isPlat ? "平台优惠券" : "商家优惠券");
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mOilCouponAdapter = new OilCouponAdapter(R.layout.adapter_oil_coupon, mList);
        mBinding.recyclerView.setAdapter(mOilCouponAdapter);
        mOilCouponAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null) {
                UiUtils.canClickViewStateDelayed(view, 1000);
                mOnItemClickedListener.onOilCouponClick(adapter, view, position, isPlat);
                dismiss();
            }
        });
        mBinding.noCouponTv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onNoCouponClick(isPlat);
            }
            dismiss();
        });

        if (isPlat) {
            getPlatformCoupon();
        } else {
            getBusinessCoupon();
        }
        mBinding.cancelIv.setOnClickListener(view -> dismiss());
    }

    private void getPlatformCoupon() {
        //0真正可用 1已用 2过期  3时间未到 4 金额未达到
        RxHttp.postForm(ApiService.PLATFORM_COUPON)
                .add("canUse", "1")
                .add("rangeType", "2")
                .add("amount", TextUtils.isEmpty(amount) ? "0" : amount)
                .add(Constants.OIL_NUMBER_ID, oilNo)
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .asResponseList(CouponBean.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(couponBeans -> {
                    dispatchData(couponBeans);
                });
    }

    private void getBusinessCoupon() {
        RxHttp.postForm(ApiService.BUSINESS_COUPON)
                .add("canUse", "1")
                .add("amount", amount)
                .add(Constants.OIL_NUMBER_ID, oilNo)
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .asResponseList(CouponBean.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(couponBeans -> {
                    dispatchData(couponBeans);
                });
    }

    private void dispatchData(List<CouponBean> couponBeans) {
        if (couponBeans != null && couponBeans.size() > 0) {
            mList = couponBeans;
            mOilCouponAdapter.setNewData(mList);
        }
    }

    public interface OnItemClickedListener {
        void onOilCouponClick(BaseQuickAdapter adapter, View view, int position, boolean isPlat);

        void onNoCouponClick(boolean isPlat);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
