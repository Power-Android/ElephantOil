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
import com.xxjy.jyyh.databinding.DialogOilGunLayoutBinding;
import com.xxjy.jyyh.entity.OilEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/22/21 1:07 PM
 * @project ElephantOil
 * @description:
 */
public class OilGunDialog extends BottomSheetDialog {
    private Context mContext;
    private OilEntity.StationsBean stationsBean;
    private int oilNoPosition;
    private BottomSheetBehavior mBehavior;
    private DialogOilGunLayoutBinding mBinding;
    private List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> mList = new ArrayList<>();
    private OilGunAdapter mOilGunAdapter;

    public OilGunDialog(Context context, OilEntity.StationsBean stationsBean, int oilNoPosition) {
        super(context, R.style.bottom_sheet_dialog);
        this.mContext = context;
        this.stationsBean = stationsBean;
        this.oilNoPosition = oilNoPosition;
        mBinding = DialogOilGunLayoutBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_oil_gun_layout, null));
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
        mList = stationsBean.getOilPriceList().get(oilNoPosition).getGunNos();
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelected(false);
        }
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mOilGunAdapter = new OilGunAdapter(R.layout.adapter_oil_num_layout, mList);
        mBinding.recyclerView.setAdapter(mOilGunAdapter);
        mOilGunAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onOilGunClick(adapter, view, position);
            }
        });
        mBinding.cancelIv.setOnClickListener(view -> dismiss());
        mBinding.backIv.setOnClickListener(view -> dismiss());
    }

    public interface OnItemClickedListener {
        void onOilGunClick(BaseQuickAdapter adapter, View view, int position);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
