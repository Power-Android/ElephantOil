package com.xxjy.jyyh.dialog;

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
import com.xxjy.jyyh.databinding.DialogSelectPayLayoutBinding;
import com.xxjy.jyyh.entity.OilPayTypeEntity;
import com.xxjy.jyyh.utils.toastlib.MyToast;
import java.util.ArrayList;
import java.util.List;
import rxhttp.RxHttp;

public class SelectPayDialog extends BottomSheetDialog {
    private BaseActivity mActivity;

    private String orderId, payAmount,mTitle;
    private BottomSheetBehavior mBehavior;
    private DialogSelectPayLayoutBinding mBinding;
    private List<OilPayTypeEntity> mPayTypeList = new ArrayList<>();
    private OilPayTypeAdapter mOilPayTypeAdapter;

    public SelectPayDialog(BaseActivity activity, String title, String orderId, String payAmount) {
        super(activity, R.style.bottom_sheet_dialog);
        this.mActivity = activity;
        this.orderId = orderId;
        this.payAmount = payAmount;
        this.mTitle = title;
        mBinding = DialogSelectPayLayoutBinding.bind(
                View.inflate(mActivity, R.layout.dialog_select_pay_layout, null));
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
        mBinding.payAmountTv.setText(payAmount);
        mBinding.payOilInfoTv.setText(mTitle);

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
                mOnItemClickedListener.onPayOrderClick(payType, orderId, payAmount);
            }
        });

        getPayType();
    }

    private void getPayType() {
        RxHttp.postForm(ApiService.GET_PAY_TYPE)
                .asResponseList(String.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(strings -> {
                    for (int i = 0; i < strings.size(); i++) {
                        OilPayTypeEntity oilPayTypeEntity = new OilPayTypeEntity();
                        oilPayTypeEntity.setId(strings.get(i));
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
