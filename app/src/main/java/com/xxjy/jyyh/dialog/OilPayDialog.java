package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilPayTypeAdapter;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.databinding.DialogOilPayLayoutBinding;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.PayOrderParams;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/23/21 2:29 PM
 * @project ElephantOil
 * @description:
 */
public class OilPayDialog extends BottomSheetDialog {
    private Context mContext;
    private BaseActivity mActivity;
    private PayOrderParams params;
    private BottomSheetBehavior mBehavior;
    private DialogOilPayLayoutBinding mBinding;
    private List<String> mPayTypeList = new ArrayList<>();

    public OilPayDialog(Context context, BaseActivity activity, OilEntity.StationsBean stationsBean,
                        int oilNoPosition, int gunNoPosition, String orderId) {
        super(context, R.style.bottom_sheet_dialog);
        this.mContext = context;
        this.mActivity = activity;
        mBinding = DialogOilPayLayoutBinding.bind(
                View.inflate(mContext, R.layout.dialog_oil_pay_layout, null));
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

        mBinding.cancelIv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onCloseAllClick();
            }
        });

        mBinding.oilPayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void dispatchData() {
        mBinding.payAmountTv.setText(params.getPayAmount());
        String oilType = "";
        switch (params.getOilType()) {
            case "1":
                oilType = "汽油";
                break;
            case "2":
                oilType = "柴油";
                break;
            case "3":
                oilType = "天然气";
                break;
        }
        mBinding.payOilInfoTv.setText(params.getGasName() +
                "-" + params.getOilName() + oilType + "-" +
                params.getGunNo() + "号枪");

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
