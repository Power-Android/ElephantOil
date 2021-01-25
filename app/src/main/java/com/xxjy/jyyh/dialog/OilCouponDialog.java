package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilCouponAdapter;
import com.xxjy.jyyh.databinding.DialogOilCouponBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/25/21 2:10 PM
 * @project ElephantOil
 * @description:
 */
public class OilCouponDialog {
    private Context mContext;
    private BottomSheetDialog mOilCouponDialog;
    private BottomSheetBehavior mBehavior;
    private final DialogOilCouponBinding mBinding;
    private List<String> mList = new ArrayList<>();

    public OilCouponDialog(Context context) {
        this.mContext = context;
        mBinding = DialogOilCouponBinding.bind(
                View.inflate(context, R.layout.dialog_oil_coupon, null));
        init();
        initData();
    }

    private void init(){
        if (mOilCouponDialog == null) {
            mOilCouponDialog = new BottomSheetDialog(mContext, R.style.bottom_sheet_dialog);
            mOilCouponDialog.getWindow().getAttributes().windowAnimations =
                    R.style.bottom_sheet_dialog;
            mOilCouponDialog.setCancelable(true);
            mOilCouponDialog.setCanceledOnTouchOutside(false);
            mOilCouponDialog.setContentView(mBinding.getRoot());
            mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
            mBehavior.setSkipCollapsed(true);
        }
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initData(){
        for (int i = 0; i < 10; i++) {
            mList.add("");
        }
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        OilCouponAdapter oilCouponAdapter = new OilCouponAdapter(R.layout.adapter_oil_coupon, mList);
        mBinding.recyclerView.setAdapter(oilCouponAdapter);
        oilCouponAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mOnItemClickedListener != null){
                    mOnItemClickedListener.onOilCouponClick(adapter, view, position);
                    dismiss();
                }
            }
        });
        mBinding.noCouponTv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null){
                mOnItemClickedListener.onNoCouponClick();
            }
            dismiss();
        });
    }

    public void show(){
        mOilCouponDialog.show();
    }

    public void dismiss(){
        mOilCouponDialog.dismiss();
    }

    public interface OnItemClickedListener{
        void onOilCouponClick(BaseQuickAdapter adapter, View view, int position);
        void onNoCouponClick();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener){
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
