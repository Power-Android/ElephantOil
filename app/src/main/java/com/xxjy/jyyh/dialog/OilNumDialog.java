package com.xxjy.jyyh.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.databinding.DialogOilNumLayoutBinding;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/21/21 8:26 PM
 * @project ElephantOil
 * @description:
 */
public class OilNumDialog {
    private Context mContext;
    private OilEntity.StationsBean mStationsBean;
    private BottomSheetDialog mOilNumDialog;
    private BottomSheetBehavior mBehavior;
    private DialogOilNumLayoutBinding mBinding;
    private OilNumAdapter mOilNumAdapter;
    private List<OilEntity.StationsBean.OilPriceListBean> mList = new ArrayList<>();

    public OilNumDialog(Context context, OilEntity.StationsBean stationsBean) {
        this.mContext = context;
        this.mStationsBean = stationsBean;
        mBinding = DialogOilNumLayoutBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_oil_num_layout, null));
        init();
        initData();
    }

    private void init() {
        if (mOilNumDialog == null) {
            mOilNumDialog = new BottomSheetDialog(mContext, R.style.bottom_sheet_dialog);
            mOilNumDialog.getWindow().getAttributes().windowAnimations =
                    R.style.bottom_sheet_dialog;
            mOilNumDialog.setCancelable(true);
            mOilNumDialog.setCanceledOnTouchOutside(false);
            mOilNumDialog.setContentView(mBinding.getRoot());
            mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
            mBehavior.setSkipCollapsed(true);
        }
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initData() {
        if (mStationsBean.getOilPriceList() == null || mStationsBean.getOilPriceList().size() <= 0) {
            MyToast.showError(mContext, "暂无该油站数据");
            return;
        }
        mList = mStationsBean.getOilPriceList();
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mOilNumAdapter = new OilNumAdapter(R.layout.adapter_oil_num_layout, mList);
        mBinding.recyclerView.setAdapter(mOilNumAdapter);
        mOilNumAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onOilNumClick(adapter, view, position);
            }
        });
        mBinding.cancelIv.setOnClickListener(view -> mOilNumDialog.cancel());
    }

    public void show() {
        mOilNumDialog.show();
    }

    public void dismiss() {
        mOilNumDialog.dismiss();
    }

    public interface OnItemClickedListener {
        void onOilNumClick(BaseQuickAdapter adapter, View view, int position);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
