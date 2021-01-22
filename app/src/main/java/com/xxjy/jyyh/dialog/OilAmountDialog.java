package com.xxjy.jyyh.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilAmountAdapter;
import com.xxjy.jyyh.adapter.OilDiscountAdapter;
import com.xxjy.jyyh.databinding.DialogOilAmountLayoutBinding;
import com.xxjy.jyyh.databinding.DialogOilNumLayoutBinding;

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
    private Activity mActivity;
    private BottomSheetDialog mOilAmountDialog;
    private BottomSheetBehavior mBehavior;
    private DialogOilAmountLayoutBinding mBinding;
    private List<String> mAmountList = new ArrayList<>();
    private List<String> mDiscountList = new ArrayList<>();

    public OilAmountDialog(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
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

    }

    public void show() {
        mOilAmountDialog.show();
    }
}
