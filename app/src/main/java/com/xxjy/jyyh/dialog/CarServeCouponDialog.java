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
import com.xxjy.jyyh.adapter.CarServeCouponAdapter;
import com.xxjy.jyyh.adapter.OilCouponAdapter;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.databinding.DialogCarServeCouponBinding;
import com.xxjy.jyyh.databinding.DialogOilCouponBinding;
import com.xxjy.jyyh.entity.CarServeCouponBean;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;


public class CarServeCouponDialog extends BottomSheetDialog {
    private Context mContext;
    private BaseActivity mActivity;
    private boolean isPlat;
    private BottomSheetBehavior mBehavior;
    private final DialogCarServeCouponBinding mBinding;
    private List<CarServeCouponBean> mList = new ArrayList<>();
    private CarServeCouponAdapter mCarServeCouponAdapter;
    private long selectId;

    public CarServeCouponDialog(Context context,long id) {
        super(context, R.style.bottom_sheet_dialog);
        this.mContext = context;
        this.selectId=id;
        mBinding = DialogCarServeCouponBinding.bind(
                View.inflate(context, R.layout.dialog_car_serve_coupon, null));
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
//        mBinding.tv1.setText(isPlat ? "平台优惠券" : "商家优惠券");
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mCarServeCouponAdapter = new CarServeCouponAdapter(R.layout.adapter_car_serve_coupon, mList);
        mBinding.recyclerView.setAdapter(mCarServeCouponAdapter);
        mCarServeCouponAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (mOnItemClickedListener != null) {
                if(((CarServeCouponBean)adapter.getData().get(position)).getCouponStatus()==0){
                    UiUtils.canClickViewStateDelayed(view, 1000);
                    mOnItemClickedListener.onOilCouponClick(adapter, view, position);
                    mCarServeCouponAdapter.setChecked(((CarServeCouponBean)adapter.getData().get(position)).getId());
                    dismiss();
                }
            }
        });
        mBinding.noCouponTv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onNoCouponClick();
                mCarServeCouponAdapter.setChecked(0);
            }
            dismiss();
        });
        mBinding.cancelIv.setOnClickListener(view -> dismiss());
    }



    public void dispatchData(List<CarServeCouponBean> couponBeans,long selectId) {
        if (couponBeans != null && couponBeans.size() > 0) {
            mList = couponBeans;
            mCarServeCouponAdapter.setNewData(mList);
            if (selectId!=0){
                mCarServeCouponAdapter.setChecked(selectId);
            }
        }
    }

    public interface OnItemClickedListener {
        void onOilCouponClick(BaseQuickAdapter adapter, View view, int position);

        void onNoCouponClick();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
