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
import com.xxjy.jyyh.adapter.OilPayTypeAdapter;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.CarServeApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.databinding.DialogOilPayLayoutBinding;
import com.xxjy.jyyh.databinding.DialogUnifiedPaymentCashierLayoutBinding;
import com.xxjy.jyyh.entity.CarServePayTypeBean;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilPayTypeEntity;
import com.xxjy.jyyh.entity.PayTypeBean;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.eventtrackingmanager.EventTrackingManager;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingConstant;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.util.ArrayList;
import java.util.List;

import rxhttp.RxHttp;

/**
 * 收银台
 */
public class UnifiedPaymentCashierDialog extends BottomSheetDialog {

    //支付入口 refuel:加油，mall:商城，cars:车服店,card:权益卡
    public static final String REFUEL_CODE="refuel";
    public static final String MALL_CODE="mall";
    public static final String CARS_CODE="cars";
    public static final String CARD_CODE="card";

    private BaseActivity mActivity;
    private List<OilEntity.StationsBean.OilPriceListBean> oilPriceListBean;
    private String orderId, payAmount;
    private BottomSheetBehavior mBehavior;
    private DialogUnifiedPaymentCashierLayoutBinding mBinding;
    private List<OilPayTypeEntity> mPayTypeList = new ArrayList<>();
    private OilPayTypeAdapter mOilPayTypeAdapter;
    private String mCode;



    public UnifiedPaymentCashierDialog(BaseActivity activity,  String orderId, String payAmount,String code) {
        super(activity, R.style.bottom_sheet_dialog);
        this.mActivity = activity;
        this.orderId = orderId;
        this.payAmount = payAmount;
        this.mCode = code;
        mBinding = DialogUnifiedPaymentCashierLayoutBinding.bind(
                View.inflate(activity, R.layout.dialog_unified_payment_cashier_layout, null));
        init();
        initData();
    }

    private void init() {
        getWindow().getAttributes().windowAnimations =
                R.style.bottom_sheet_dialog;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(mBinding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
        mBehavior.setSkipCollapsed(true);
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initData() {
        mBinding.payAmountTv.setText(payAmount);

        mBinding.payTypeRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mOilPayTypeAdapter = new OilPayTypeAdapter(R.layout.adapter_oil_pay, mPayTypeList);
        mBinding.payTypeRecyclerView.setAdapter(mOilPayTypeAdapter);
        mOilPayTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            mOilPayTypeAdapter.setCheckItem(position);
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onOilPayTypeClick(adapter, view, position);
            }
        });

        mBinding.cancelIv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onCloseAllClick();
            }
        });

        mBinding.oilPayTv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null){
                String payType = "";
                for (int i = 0; i < mPayTypeList.size(); i++) {
                    if (mPayTypeList.get(i).isSelect()){
                        payType = mPayTypeList.get(i).getId();
                    }
                }
                if (TextUtils.isEmpty(payType)){
                    MyToast.showInfo(mActivity, "请选择支付方式");
                    return;
                }
                UiUtils.canClickViewStateDelayed(view, 1000);

                mOnItemClickedListener.onPayOrderClick(payType, orderId, payAmount);
            }
        });

        getPayType(mCode);
    }

    private void getPayType(String code) {
        RxHttp.get(CarServeApiService.PAYMENT_CASHIER)
                .add("code",code)
                .asResponseList(CarServePayTypeBean.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(strings -> {
                    for (int i = 0; i < strings.size(); i++) {
                        OilPayTypeEntity oilPayTypeEntity = new OilPayTypeEntity();
                        oilPayTypeEntity.setId(strings.get(i).getPayType());
                        oilPayTypeEntity.setActMes("");
                        mPayTypeList.add(i, oilPayTypeEntity);
                    }
                    mPayTypeList.get(0).setSelect(true);
                    mOilPayTypeAdapter.setNewData(mPayTypeList);
                });
    }

    public interface OnItemClickedListener {
        void onOilPayTypeClick(BaseQuickAdapter adapter, View view, int position);

        void onCloseAllClick();

        void onPayOrderClick(String payType, String orderId, String payAmount);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
