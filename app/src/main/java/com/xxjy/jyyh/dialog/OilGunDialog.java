package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilGunAdapter;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.databinding.DialogOilGunLayoutBinding;
import com.xxjy.jyyh.databinding.DialogOilNumLayoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/22/21 1:07 PM
 * @project ElephantOil
 * @description:
 */
public class OilGunDialog {
    private Context mContext;
    private BottomSheetDialog mOilGunDialog;
    private BottomSheetBehavior mBehavior;
    private DialogOilGunLayoutBinding mBinding;
    private List<String> mList = new ArrayList<>();
    private OilGunAdapter mOilGunAdapter;

    public OilGunDialog(Context context) {
        this.mContext = context;
        mBinding = DialogOilGunLayoutBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_oil_gun_layout, null));
        init();
        initData();
    }

    private void init() {
        if (mOilGunDialog == null) {
            mOilGunDialog = new BottomSheetDialog(mContext, R.style.bottom_sheet_dialog);
            mOilGunDialog.getWindow().getAttributes().windowAnimations =
                    R.style.bottom_sheet_dialog;
            mOilGunDialog.setCancelable(true);
            mOilGunDialog.setCanceledOnTouchOutside(false);
            mOilGunDialog.setContentView(mBinding.getRoot());
            mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
            mBehavior.setSkipCollapsed(true);
        }
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initData() {
        for (int i = 0; i < 7; i++) {
            mList.add("1");
        }
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mOilGunAdapter = new OilGunAdapter(R.layout.adapter_oil_num_layout, mList);
        mBinding.recyclerView.setAdapter(mOilGunAdapter);
        mOilGunAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null){
                mOnItemClickedListener.onOilGunClick(adapter, view, position);
            }
        });
        mBinding.cancelIv.setOnClickListener(view -> mOilGunDialog.cancel());
        mBinding.backIv.setOnClickListener(view -> mOilGunDialog.cancel());
    }

    public void show(){
        mOilGunDialog.show();
    }

    public void dismiss(){
        mOilGunDialog.dismiss();
    }

    public interface OnItemClickedListener{
        void onOilGunClick(BaseQuickAdapter adapter, View view, int position);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener){
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
