package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.content.DialogInterface;
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
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.databinding.DialogOilPayLayoutBinding;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilPayTypeEntity;
import com.xxjy.jyyh.entity.PayTypeBean;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.eventtrackingmanager.EventTrackingManager;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingConstant;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.util.ArrayList;
import java.util.List;

import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/23/21 2:29 PM
 * @project ElephantOil
 * @description:
 */
public class OilPayDialog extends BottomSheetDialog {
    private Context mContext;
    private BaseActivity mActivity;
    private OilEntity.StationsBean stationsBean;
    private List<OilEntity.StationsBean.OilPriceListBean> oilPriceListBean;
    private int oilNoPosition, gunNoPosition;
    private String orderId, payAmount;
    private BottomSheetBehavior mBehavior;
    private DialogOilPayLayoutBinding mBinding;
    private List<OilPayTypeEntity> mPayTypeList = new ArrayList<>();
    private OilPayTypeAdapter mOilPayTypeAdapter;

    public OilPayDialog(Context context, BaseActivity activity, OilEntity.StationsBean stationsBean,
                        List<OilEntity.StationsBean.OilPriceListBean> oilPriceListBean, int oilNoPosition, int gunNoPosition, String orderId, String payAmount) {
        super(context, R.style.bottom_sheet_dialog);
        this.mContext = context;
        this.mActivity = activity;
        this.stationsBean = stationsBean;
        this.oilPriceListBean = oilPriceListBean;
        this.oilNoPosition = oilNoPosition;
        this.gunNoPosition = gunNoPosition;
        this.orderId = orderId;
        this.payAmount = payAmount;
        mBinding = DialogOilPayLayoutBinding.bind(
                View.inflate(mContext, R.layout.dialog_oil_pay_layout, null));
        init();
        initData();
    }

    private void init() {
        getWindow().getAttributes().windowAnimations =
                R.style.bottom_sheet_dialog;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(mBinding.getRoot());
        mBehavior = BottomSheetBehavior.from((View) mBinding.getRoot().getParent());
        mBehavior.setSkipCollapsed(true);
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initData() {
        EventTrackingManager.getInstance().tracking(mContext, mActivity, String.valueOf(++Constants.PV_ID),
                TrackingConstant.GAS_PAYMENT, "", "gas_id=" + stationsBean.getGasId() + ";type=1");
        mBinding.payAmountTv.setText(payAmount);
        mBinding.payOilInfoTv.setText(stationsBean.getGasName() + "-" +
                oilPriceListBean.get(oilNoPosition).getOilName());

        mBinding.payTypeRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
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
                    MyToast.showInfo(mContext, "请选择支付方式");
                    return;
                }
                UiUtils.canClickViewStateDelayed(view, 1000);
                EventTrackingManager.getInstance().tracking(mContext, mActivity, String.valueOf(++Constants.PV_ID),
                        TrackingConstant.GAS_PAYMENT, "", "gas_id=" + stationsBean.getGasId() + ";type=2");
                mOnItemClickedListener.onPayOrderClick(payType, orderId, payAmount);
            }
        });

        getPayType();
    }

    private void getPayType() {
//        RxHttp.postForm(ApiService.GET_PAY_TYPE)
//                .asResponseList(String.class)
//                .to(RxLife.toMain(mActivity))
//                .subscribe(strings -> {
//                    for (int i = 0; i < strings.size(); i++) {
//                        OilPayTypeEntity oilPayTypeEntity = new OilPayTypeEntity();
//                        oilPayTypeEntity.setId(strings.get(i));
//                        mPayTypeList.add(i, oilPayTypeEntity);
//                    }
//                    mPayTypeList.get(0).setSelect(true);
//                    mOilPayTypeAdapter.setNewData(mPayTypeList);
//                });
        RxHttp.postForm(ApiService.GET_PAY_TAG)
                .asResponseList(PayTypeBean.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(strings -> {
                    for (int i = 0; i < strings.size(); i++) {
                        OilPayTypeEntity oilPayTypeEntity = new OilPayTypeEntity();
                        oilPayTypeEntity.setId(strings.get(i).getName());
                        oilPayTypeEntity.setActMes(strings.get(i).getActMes());
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
