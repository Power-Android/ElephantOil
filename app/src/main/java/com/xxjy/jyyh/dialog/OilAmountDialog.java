package com.xxjy.jyyh.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilAmountAdapter;
import com.xxjy.jyyh.adapter.OilDiscountAdapter;
import com.xxjy.jyyh.databinding.DialogOilAmountLayoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/22/21 1:25 PM
 * @project ElephantOil
 * @description:
 */
public class OilAmountDialog {
    private Context mContext;
    private BottomSheetDialog mOilAmountDialog;
    private BottomSheetBehavior mBehavior;
    private DialogOilAmountLayoutBinding mBinding;
    private List<String> mAmountList = new ArrayList<>();
    private List<String> mDiscountList = new ArrayList<>();

    public OilAmountDialog(Context context) {
        this.mContext = context;
        mBinding = DialogOilAmountLayoutBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_oil_amount_layout, null));
        init();
        initData();
    }

    private void init() {
        if (mOilAmountDialog == null) {
            mOilAmountDialog = new BottomSheetDialog(mContext, R.style.bottom_sheet_dialog);
            mOilAmountDialog.getWindow().getAttributes().windowAnimations =
                    R.style.bottom_sheet_dialog;
            mOilAmountDialog.setCancelable(true);
            mOilAmountDialog.setCanceledOnTouchOutside(false);
            mOilAmountDialog.setContentView(mBinding.getRoot());
            mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
            mBehavior.setSkipCollapsed(true);
        }
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initData() {
        //快捷价格列表
        for (int i = 0; i < 3; i++) {
            mAmountList.add("");
        }
        mBinding.amountRecyclerView.setLayoutManager(
                new GridLayoutManager(mContext, 3));
        OilAmountAdapter oilAmountAdapter = new OilAmountAdapter(R.layout.adapter_oil_amount, mAmountList);
        mBinding.amountRecyclerView.setAdapter(oilAmountAdapter);
        oilAmountAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null){
                mOnItemClickedListener.onOilAmountClick(adapter, view, position);
            }
        });

        //优惠列表
        for (int i = 0; i < 4; i++) {
            mDiscountList.add("");
        }
        mBinding.discountRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext));
        OilDiscountAdapter discountAdapter = new OilDiscountAdapter(R.layout.adapter_oil_discount, mDiscountList);
        mBinding.discountRecyclerView.setAdapter(discountAdapter);

        mBinding.cancelIv.setOnClickListener(view -> mOilAmountDialog.cancel());
        mBinding.backIv.setOnClickListener(view -> mOilAmountDialog.cancel());
        discountAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null){
                mOnItemClickedListener.onOilDiscountClick(adapter, view, position);
            }
        });

        mBinding.cancelIv.setOnClickListener(view -> mOilAmountDialog.cancel());
        mBinding.backIv.setOnClickListener(view -> mOilAmountDialog.cancel());
        mBinding.createOrderTv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null){
                mOnItemClickedListener.onCreateOrder(view);
            }
        });

    }

    public void show() {
        mOilAmountDialog.show();
    }

    public void dismiss(){
        mOilAmountDialog.dismiss();
    }

    public interface OnItemClickedListener{
        void onOilAmountClick(BaseQuickAdapter adapter, View view, int position);
        void onOilDiscountClick(BaseQuickAdapter adapter, View view, int position);
        void onCreateOrder(View view);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener){
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
