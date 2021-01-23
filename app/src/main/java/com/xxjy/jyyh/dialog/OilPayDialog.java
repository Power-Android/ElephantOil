package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilPayTypeAdapter;
import com.xxjy.jyyh.databinding.DialogOilPayLayoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/23/21 2:29 PM
 * @project ElephantOil
 * @description:
 */
public class OilPayDialog {
    private Context mContext;
    private BottomSheetDialog mOilPayDialog;
    private BottomSheetBehavior mBehavior;
    private final DialogOilPayLayoutBinding mBinding;
    private List<String> mPayTypeList = new ArrayList<>();

    public OilPayDialog(Context context) {
        this.mContext = context;
        mBinding = DialogOilPayLayoutBinding.bind(
                View.inflate(mContext, R.layout.dialog_oil_pay_layout, null));
        init();
        initData();
    }

    private void init() {
        if (mOilPayDialog == null) {
            mOilPayDialog = new BottomSheetDialog(mContext, R.style.bottom_sheet_dialog);
            mOilPayDialog.getWindow().getAttributes().windowAnimations =
                    R.style.bottom_sheet_dialog;
            mOilPayDialog.setCancelable(true);
            mOilPayDialog.setCanceledOnTouchOutside(false);
            mOilPayDialog.setContentView(mBinding.getRoot());
            mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
            mBehavior.setSkipCollapsed(true);
        }
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initData() {
        for (int i = 0; i < 3; i++) {
            mPayTypeList.add("");
        }
        mBinding.payTypeRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        OilPayTypeAdapter oilPayTypeAdapter = new OilPayTypeAdapter(R.layout.adapter_oil_pay, mPayTypeList);
        mBinding.payTypeRecyclerView.setAdapter(oilPayTypeAdapter);
        oilPayTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onOilPayTypeClick(adapter, view, position);
            }
        });

        mBinding.cancelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOilPayDialog.dismiss();
                if (mOnItemClickedListener != null){
                    mOnItemClickedListener.onCloseAllClick();
                }
            }
        });
    }

    public void show(){
        mOilPayDialog.show();
    }

    public void dismiss(){
        mOilPayDialog.dismiss();
    }

    public interface OnItemClickedListener {
        void onOilPayTypeClick(BaseQuickAdapter adapter, View view, int position);
        void onCloseAllClick();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
