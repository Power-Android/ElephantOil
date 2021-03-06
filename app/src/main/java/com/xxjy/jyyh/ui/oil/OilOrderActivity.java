package com.xxjy.jyyh.ui.oil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NumberUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilAmountAdapter;
import com.xxjy.jyyh.adapter.OilCouponAdapter;
import com.xxjy.jyyh.adapter.OilDiscountAdapter;
import com.xxjy.jyyh.adapter.OilGunAdapter;
import com.xxjy.jyyh.adapter.OilMonthAdapter;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.adapter.OilRedeemAdapter;
import com.xxjy.jyyh.adapter.TextWatcherAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.constants.PayTypeConstants;
import com.xxjy.jyyh.constants.SPConstants;
import com.xxjy.jyyh.databinding.ActivityOilOrderBinding;
import com.xxjy.jyyh.dialog.OilCouponDialog;
import com.xxjy.jyyh.dialog.OilHotDialog;
import com.xxjy.jyyh.dialog.OilMonthRuleDialog;
import com.xxjy.jyyh.dialog.OilMonthTipDialog;
import com.xxjy.jyyh.dialog.OilNumDialog;
import com.xxjy.jyyh.dialog.OilPayDialog;
import com.xxjy.jyyh.dialog.OilServiceDialog;
import com.xxjy.jyyh.dialog.OilUserDiscountDialog;
import com.xxjy.jyyh.dialog.QueryDialog;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.MonthCouponEntity;
import com.xxjy.jyyh.entity.MultiplePriceBean;
import com.xxjy.jyyh.entity.OilDefaultPriceEntity;
import com.xxjy.jyyh.entity.OilDiscountEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilPayTypeEntity;
import com.xxjy.jyyh.entity.OilTypeEntity;
import com.xxjy.jyyh.entity.OilUserDiscountEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.entity.ProductIdEntity;
import com.xxjy.jyyh.entity.RedeemEntity;
import com.xxjy.jyyh.ui.home.HomeViewModel;
import com.xxjy.jyyh.ui.order.OrderDetailsViewModel;
import com.xxjy.jyyh.ui.pay.RefuelingPayResultActivity;
import com.xxjy.jyyh.ui.web.WeChatWebPayActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.WXSdkManager;
import com.xxjy.jyyh.utils.eventtrackingmanager.EventTrackingManager;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingConstant;
import com.xxjy.jyyh.utils.pay.IPayListener;
import com.xxjy.jyyh.utils.pay.PayHelper;
import com.xxjy.jyyh.utils.pay.PayListenerUtils;
import com.xxjy.jyyh.utils.toastlib.MyToast;
import com.xxjy.jyyh.utils.toastlib.Toasty;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;
import per.goweii.anylayer.dialog.DialogLayer;
import rxhttp.RxHttp;

public class OilOrderActivity extends BindingActivity<ActivityOilOrderBinding, OilViewModel> implements IPayListener {

    private OilEntity.StationsBean mStationsBean;
    private int mOilNoPosition;
    private int mGunNoPosition = -1;
    private int mOilNo;
    private int mGunNo;
    private OilNumDialog mOilNumDialog;
    private OilAmountAdapter mOilAmountAdapter;
    private OilDiscountAdapter mDiscountAdapter;
    private List<OilDefaultPriceEntity.DefaultAmountBean> mAmountList = new ArrayList<>();
    private List<OilDiscountEntity> mDiscountList = new ArrayList<>();
    private List<MonthCouponEntity.MonthCouponTemplatesBean> mOilMonthList = new ArrayList<>();
    private OilMonthAdapter mOilMonthAdapter;
    private OilMonthRuleDialog mOilMonthRuleDialog;
    private Float totalMoney = 0f;//????????????
    private Float mMonthAmount = 0f;
    private String mAmountReduce;
    private OilMonthTipDialog mOilMonthTipDialog;
    private String monthCouponId = ""; //????????????id
    private String mExcludeType;//????????????
    private boolean isSelecedPlat = true; //?????????????????????????????????????????????????????????????????????
    private String mMonthCouponImgUrl;
    private String mMonthCouponRule;
    private CouponBean mPlatCouponBean, mBusinessCouponBean;
    private List<CouponBean> platformCoupons = new ArrayList<>();
    private List<CouponBean> businessCoupons = new ArrayList<>();
    private String platId = "", businessId = "", businessAmount = "";//???????????????id??? ?????????????????? ??????????????????????????????
    private OilHotDialog mOilHotDialog;
    private OilServiceDialog mOilServiceDialog;
    private MultiplePriceBean mMultiplePriceBean;
    private OilCouponDialog mOilCouponDialog;
    private OilPayDialog mOilPayDialog;
    private HomeViewModel mHomeViewModel;
    private OrderDetailsViewModel orderDetailsViewModel;
    //?????????????????????????????????
    private boolean shouldJump = false;
    private PayOrderEntity mPayOrderEntity;
    private boolean isShouldAutoOpenWeb = false;    //???????????????????????????????????????????????????
    private boolean mIsUseCoupon = true, mIsUseBusinessCoupon = true;//?????????????????????
    private boolean mIsShowMonthToast = true;//???????????????????????????????????????false

    private boolean isUseBill = true;
    private boolean isFirstOpen = true;
    private List<RedeemEntity.ProductOilGasListBean> mRedeemList = new ArrayList<>();
    private OilRedeemAdapter mOilRedeemAdapter;
    private List<ProductIdEntity> mProductIdList = new ArrayList<>();
    private String mJsonStr = "";//????????????ids
    private String mProductAmount;
    private QueryDialog mQueryDialog;
    private boolean isFirstIn = true;
    private OilUserDiscountDialog mOilUserDiscountDialog;

    private void showJump(PayOrderEntity orderEntity) {
        if (orderEntity == null) return;
        if (shouldJump) {
            shouldJump = false;
            RefuelingPayResultActivity.openPayResultPage(this,
                    orderEntity.getOrderNo(), orderEntity.getOrderPayNo());
            closeDialog();
            clearData();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showJump(mPayOrderEntity);
    }

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, true);
        BusUtils.register(this);
        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        orderDetailsViewModel = new ViewModelProvider(this).get(OrderDetailsViewModel.class);

        mStationsBean = (OilEntity.StationsBean) getIntent().getSerializableExtra("stationsBean");
        mOilNo = getIntent().getIntExtra("oilNo", 0);
        mGunNo = getIntent().getIntExtra("gunNo", 0);

        //????????????????????????????????????
        for (int i = 0; i < mStationsBean.getOilPriceList().size(); i++) {
            mStationsBean.getOilPriceList().get(i).setSelected(false);
            if (String.valueOf(mStationsBean.getOilPriceList().get(i).getOilNo()).equals(String.valueOf(mOilNo))) {
                mOilNoPosition = i;
                mOilNo = mStationsBean.getOilPriceList().get(i).getOilNo();
                mStationsBean.getOilPriceList().get(i).setSelected(true);
                mBinding.oilNameTv.setText(mStationsBean.getGasName());
                mBinding.oilAddressTv.setText(mStationsBean.getGasAddress());
                SpanUtils.with(mBinding.oilPriceTv)
                        .append("??" + mStationsBean.getOilPriceList().get(mOilNoPosition).getPriceYfq())
                        .append("???")
                        .setFontSize(12, true)
                        .create();
//                mBinding.oilPriceTv.setText("??" + mStationsBean.getOilPriceList().get(mOilNoPosition).getPriceYfq() + "???");
//                mBinding.oilStationPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                mBinding.oilStationPriceTv.setText("????????? ??" + mStationsBean.getOilPriceList().get(mOilNoPosition).getPriceGun());
                mBinding.oilNumTv.setText(mStationsBean.getOilPriceList().get(mOilNoPosition).getOilName());
                for (int k = 0; k < mStationsBean.getOilPriceList().get(i).getGunNos().size(); k++) {
                    mStationsBean.getOilPriceList().get(i).getGunNos().get(k).setSelected(false);
                    if (String.valueOf(mStationsBean.getOilPriceList().get(i).getGunNos().get(k).getGunNo()).equals(String.valueOf(mGunNo))) {
                        mGunNoPosition = k;
                        mGunNo = mStationsBean.getOilPriceList().get(i).getGunNos().get(k).getGunNo();
                        mStationsBean.getOilPriceList().get(i).getGunNos().get(k).setSelected(true);
                        mBinding.oilGunTv.setText(mStationsBean.getOilPriceList().get(mOilNoPosition).getGunNos().get(mGunNoPosition).getGunNo() + "??????");
                    }
                }
            }
        }

        //??????????????????
        mBinding.amountRecyclerView.setLayoutManager(
                new GridLayoutManager(this, 3));
        mOilAmountAdapter = new OilAmountAdapter(R.layout.adapter_oil_amount, mAmountList);
        mBinding.amountRecyclerView.setAdapter(mOilAmountAdapter);
        mOilAmountAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<OilDefaultPriceEntity.DefaultAmountBean> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelected(false);
            }
            data.get(position).setSelected(true);
            mBinding.amountEt.setText(NumberUtils.format(
                    Float.parseFloat(data.get(position).getAmount()), 0));
            mOilAmountAdapter.notifyDataSetChanged();
            //??????????????????
            refreshData();
        });

        //????????????
        mBinding.monthRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mOilMonthAdapter = new OilMonthAdapter(R.layout.adapter_oil_month, mOilMonthList);
        mBinding.monthRecyclerView.setAdapter(mOilMonthAdapter);
        mBinding.monthRedCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if (TextUtils.isEmpty(mBinding.amountEt.getText().toString().trim())) {
                mBinding.monthRedCheck.setChecked(false);
                mOilMonthAdapter.isSelected(false);
                if (!mIsShowMonthToast) {
                    mIsShowMonthToast = true;
                    return;
                }
                MyToast.showInfo(this, "????????????????????????????????????");
            } else if (Float.parseFloat(mBinding.amountEt.getText().toString().trim()) < mMonthAmount) {
                MyToast.showInfo(this, "??????????????????" + mMonthAmount + "?????????????????????");
                mBinding.monthRedCheck.setChecked(false);
                mOilMonthAdapter.isSelected(false);
            } else {
                if (!b) {
                    if (!TextUtils.isEmpty(platId)) {
                        return;
                    }
                    if (mOilMonthTipDialog == null) {
                        mOilMonthTipDialog = new OilMonthTipDialog(this, this, mMonthCouponImgUrl);
                        mOilMonthTipDialog.setOnItemClickedListener(new OilMonthTipDialog.OnItemClickedListener() {
                            @Override
                            public void onQueryClick() {
                                mBinding.monthRedCheck.setChecked(true);
                                mOilMonthAdapter.isSelected(true);
                                mBinding.monthRedCl.setSelected(true);
                                mOilMonthTipDialog.dismiss();
                                refreshMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                                        mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(4).isUseBill() ? "1" : "0",
                                        "", businessAmount, monthCouponId, mIsUseCoupon, mIsUseBusinessCoupon, mJsonStr);
                            }

                            @Override
                            public void onRefuseClick() {
                                mBinding.monthRedCheck.setChecked(false);
                                mOilMonthAdapter.isSelected(false);
                                mBinding.monthRedCl.setSelected(false);
                                mOilMonthTipDialog.dismiss();
                                if (platformCoupons.size() > 0) {
                                    mPlatCouponBean = null;
                                    platId = "";
                                    mExcludeType = "";
                                    mDiscountList.get(1).setPlatformDesc("??????????????????");
                                } else {
                                    mPlatCouponBean = null;
                                    platId = "";
                                    mExcludeType = "";
                                    mDiscountList.get(1).setPlatformDesc("?????????????????????");
                                }
                                refreshMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                                        mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(4).isUseBill() ? "1" : "0",
                                        "", businessAmount, "", mIsUseCoupon, mIsUseBusinessCoupon, mJsonStr);
                            }
                        });
                        mOilMonthTipDialog.show(mBinding.monthRedCheck);
                    } else {
                        mOilMonthTipDialog.show(mBinding.monthRedCheck);
                    }
                } else {
                    mBinding.monthRedCl.setSelected(b);
                    mOilMonthAdapter.isSelected(b);
                    //?????????????????????????????????????????????
                    platId = "";
                    mIsUseCoupon = false;//??????????????????
                    mExcludeType = "";
                    mPlatCouponBean = null;
//                    mDiscountList.get(1).setPlatformDesc("??????????????????");
                    mDiscountList.get(1).setPlatformDesc("-??" + mAmountReduce);
                    mDiscountAdapter.notifyDataSetChanged();
                    refreshMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                            mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(4).isUseBill() ? "1" : "0",
                            "", businessAmount, b ? monthCouponId : "", mIsUseCoupon, mIsUseBusinessCoupon, mJsonStr);
                }
            }
        });
        mViewModel.getMonthCoupon(mStationsBean.getGasId());//??????????????????

        //??????
        mBinding.redeemRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mOilRedeemAdapter = new OilRedeemAdapter(mRedeemList);
        mBinding.redeemRecycler.setAdapter(mOilRedeemAdapter);
        mOilRedeemAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<RedeemEntity.ProductOilGasListBean> data = adapter.getData();
            if (data.get(position).isSelected()) {
                if (mQueryDialog == null) {
                    mQueryDialog = new QueryDialog(this);
                    mQueryDialog.setContent(data.get(position).getName(), data.get(position).getRedeemPrice());
                    mQueryDialog.show();
                } else {
                    mQueryDialog.setContent(data.get(position).getName(), data.get(position).getRedeemPrice());
                    mQueryDialog.show();
                }
                mQueryDialog.setOnConfirmListener(new QueryDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm() {

                    }

                    @Override
                    public void onRefuse() {
                        mOilRedeemAdapter.setSelected(position);
                        mProductIdList.clear();
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).isSelected()) {
                                mProductIdList.add(new ProductIdEntity(data.get(i).getProductId() + "", "1"));
                            }
                        }
                        mJsonStr = GsonUtils.toJson(mProductIdList);

                        //??????????????????
                        mPlatCouponBean = null;
                        platId = "";
                        mExcludeType = "";
                        businessId = "";
                        businessAmount = "";
                        mBusinessCouponBean = null;
                        refreshMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                                mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(4).isUseBill() ? "1" : "0",
                                platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "", mIsUseCoupon, mIsUseBusinessCoupon, mJsonStr);

                    }
                });
            } else {
                if (TextUtils.isEmpty(mBinding.amountEt.getText().toString())){
                    showToastInfo("???100??????????????????????????????????????????");
                    return;
                }
                if (Float.parseFloat(mBinding.amountEt.getText().toString()) < 100 && data.get(position).getTrialType() == 2) {
                    showToastInfo("???100??????????????????????????????????????????");
                    return;
                }
                if (data.get(position).getTrialType() == 2) {//??????????????????
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setSelected(false);
                    }
                }
                mOilRedeemAdapter.setSelected(position);
                mProductIdList.clear();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isSelected()) {
                        mProductIdList.add(new ProductIdEntity(data.get(i).getProductId() + "", "1"));
                    }
                }
                mJsonStr = GsonUtils.toJson(mProductIdList);
                mPlatCouponBean = null;
                platId = "";
                mExcludeType = "";
                businessId = "";
                businessAmount = "";
                mBusinessCouponBean = null;
                //??????????????????
                refreshMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                        mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(4).isUseBill() ? "1" : "0",
                        platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "", mIsUseCoupon, mIsUseBusinessCoupon, mJsonStr);


            }
        });
        mViewModel.getRedeem(mStationsBean.getGasId());

        //????????????
        for (int i = 0; i < 5; i++) {
            mDiscountList.add(new OilDiscountEntity(0, "?????????????????????", "?????????????????????",
                    "?????????????????????", 0, false, false, "0"));
        }
        mBinding.discountRecyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        mDiscountAdapter = new OilDiscountAdapter(R.layout.adapter_oil_discount, mDiscountList);
        mBinding.discountRecyclerView.setAdapter(mDiscountAdapter);

        mDiscountAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (platformCoupons.size() > 0 && position == 1 || businessCoupons.size() > 0 && position == 2) {
                UiUtils.canClickViewStateDelayed(view, 1000);
                showCouponDialog(mStationsBean, mBinding.amountEt.getText().toString(), mOilNoPosition,
                        mGunNoPosition, String.valueOf(mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()),
                        position == 1, position == 1 ? platId : businessId);
            }
        });

        mDiscountAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.item_switch_tv://??????????????????
                    List<OilDiscountEntity> data = adapter.getData();
                    if (data.get(position).isUseBill()) {
                        data.get(position).setUseBill(false);
                    } else {
                        if (data.get(position).getBalance() > 0) {
                            data.get(position).setUseBill(true);
                        }
                    }
                    isUseBill = mDiscountAdapter.getData().get(4).isUseBill();
                    //??????????????????
                    refreshMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                            mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(4).isUseBill() ? "1" : "0",
                            platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "", mIsUseCoupon, mIsUseBusinessCoupon, mJsonStr);

                    break;
                case R.id.item_title_desc://?????????????????????
                    if (mOilServiceDialog == null) {
                        mOilServiceDialog = new OilServiceDialog(this, this, mMultiplePriceBean);
                        mOilServiceDialog.show(view);
                    } else {
                        mOilServiceDialog.setData(mMultiplePriceBean);
                        mOilServiceDialog.show(view);
                    }
                    break;
            }
        });

        mViewModel.getDefaultPrice(mStationsBean.getGasId(), String.valueOf(mStationsBean.getOilPriceList()
                .get(mOilNoPosition).getOilNo()));//??????????????????
        mViewModel.getPlatformCoupon(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(),
                String.valueOf(mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()));//???????????????
        mViewModel.getBusinessCoupon(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(),
                String.valueOf(mStationsBean.getOilPriceList()
                        .get(mOilNoPosition).getOilNo()));//???????????????
        mViewModel.getBalance();//??????
        //?????????????????????????????????
//        mViewModel.getMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
//                mOilPriceListBean.get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(4).isUseBill() ? "1" : "0",
//                platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "");
    }

    private void refreshData() {
        //???????????????????????????????????????
        platId = "";
        businessId = "";
        businessAmount = "";
        mBusinessCouponBean = null;
        if (!mBinding.monthRedCheck.isChecked()) {
            mIsUseCoupon = true;//???????????????????????????true
        }
        if (mOilMonthAdapter.getData().size() > 0) {
            refreshMonthStauts(mBinding.amountEt.getText().toString().trim());
        }
        if (mBinding.amountEt.getText().length() > 0){
            mViewModel.getDiscountMoney(mStationsBean.getGasId(), mBinding.amountEt.getText().toString());
        }
        mViewModel.getPlatformCoupon(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(),
                String.valueOf(mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()));
        mViewModel.getBusinessCoupon(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(),
                String.valueOf(mStationsBean.getOilPriceList()
                        .get(mOilNoPosition).getOilNo()));//???????????????
        if (isUseBill) {
            mViewModel.getBalance();
        }

        //??????????????????
        refreshMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(4).isUseBill() ? "1" : "0",
                platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "", mIsUseCoupon, mIsUseBusinessCoupon, mJsonStr);
    }

    private void refreshMonthStauts(String amount) {
        if (TextUtils.isEmpty(amount)) {
            if (mBinding.monthRedCheck.isChecked()) {
                MyToast.showInfo(this, "????????????????????????????????????");
                mBinding.monthRedCheck.setChecked(false);
                mBinding.monthRedCl.setSelected(false);
                mOilMonthAdapter.isSelected(false);
            }
        } else if (Float.parseFloat(amount) < mMonthAmount) {
            if (mBinding.monthRedCheck.isChecked()) {
                MyToast.showInfo(this, "??????????????????" + mMonthAmount + "???????????????????????????");
                mBinding.monthRedCheck.setChecked(false);
                mBinding.monthRedCl.setSelected(false);
                mOilMonthAdapter.isSelected(false);
            } else {
                mBinding.monthRedCheck.setChecked(false);
                mBinding.monthRedCl.setSelected(false);
                mOilMonthAdapter.isSelected(false);
            }
        } else {
            if (mBinding.monthRedCheck.isChecked()) {
                mBinding.monthRedCl.setSelected(true);
                mOilMonthAdapter.isSelected(true);
            }
        }
    }

    private void showNumDialog(OilEntity.StationsBean stationsBean) {
        //??????dialog
        mOilNumDialog = new OilNumDialog(this, stationsBean);
        mOilNumDialog.setOnItemClickedListener(new OilNumDialog.OnItemClickedListener() {
            @Override
            public void onOilTypeClick(BaseQuickAdapter adapter, View view, int position,
                                       OilNumAdapter oilNumAdapter, OilGunAdapter oilGunAdapter) {
                List<OilTypeEntity> data = adapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setSelect(false);
                }
                data.get(position).setSelect(true);
                adapter.notifyDataSetChanged();

                List<OilEntity.StationsBean.OilPriceListBean> oilPriceList = data.get(position).getOilPriceList();
                for (int i = 0; i < oilPriceList.size(); i++) {
                    oilPriceList.get(i).setSelected(false);
                }
                oilPriceList.get(0).setSelected(true);
                oilNumAdapter.setNewData(oilPriceList);

                for (int i = 0; i < oilPriceList.get(0).getGunNos().size(); i++) {
                    oilPriceList.get(0).getGunNos().get(i).setSelected(false);
                }
                oilGunAdapter.setNewData(oilPriceList.get(0).getGunNos());
                mGunNoPosition = -1;
            }

            @Override
            public void onOilNumClick(BaseQuickAdapter adapter, View view, int position, OilGunAdapter oilGunAdapter) {
                List<OilEntity.StationsBean.OilPriceListBean> data = adapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setSelected(false);
                }
                data.get(position).setSelected(true);
                adapter.notifyDataSetChanged();

                for (int i = 0; i < data.get(position).getGunNos().size(); i++) {
                    data.get(position).getGunNos().get(i).setSelected(false);
                }
                oilGunAdapter.setNewData(data.get(position).getGunNos());
                mGunNoPosition = -1;
            }

            @Override
            public void onOilGunClick(BaseQuickAdapter adapter, View view, int position) {
                List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> data = adapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setSelected(false);
                }
                data.get(position).setSelected(true);
                mGunNoPosition = position;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onQuickClick(View view, OilNumAdapter oilNumAdapter, OilGunAdapter oilGunAdapter) {
                if (mGunNoPosition == -1) {
                    showToastInfo("???????????????");
                } else {
                    List<OilEntity.StationsBean.OilPriceListBean> numData = oilNumAdapter.getData();
                    for (int i = 0; i < numData.size(); i++) {
                        if (numData.get(i).isSelected()) {
                            mOilNo = numData.get(i).getOilNo();
                            for (int j = 0; j < mStationsBean.getOilPriceList().size(); j++) {
                                mStationsBean.getOilPriceList().get(j).setSelected(false);
                                if (String.valueOf(mOilNo).equals(String.valueOf(mStationsBean.getOilPriceList().get(j).getOilNo()))) {
                                    mOilNoPosition = j;
                                    mStationsBean.getOilPriceList().get(j).setSelected(true);
                                }
                            }
                        }
                    }

                    List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> gunData = oilGunAdapter.getData();
                    for (int i = 0; i < gunData.size(); i++) {
                        if (gunData.get(i).isSelected()) {
                            mGunNo = gunData.get(i).getGunNo();
                            for (int j = 0; j < mStationsBean.getOilPriceList().size(); j++) {
                                if (String.valueOf(mOilNo).equals(String.valueOf(mStationsBean.getOilPriceList().get(j).getOilNo()))) {
                                    List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> gunNos = stationsBean.getOilPriceList().get(j).getGunNos();
                                    for (int k = 0; k < gunNos.size(); k++) {
                                        mStationsBean.getOilPriceList().get(j).getGunNos().get(k).setSelected(false);
                                        if (String.valueOf(mGunNo).equals(String.valueOf(gunNos.get(k).getGunNo()))) {
                                            mGunNoPosition = k;
                                            mStationsBean.getOilPriceList().get(j).getGunNos().get(k).setSelected(true);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    List<RedeemEntity.ProductOilGasListBean> redeemData = mOilRedeemAdapter.getData();
                    for (int i = 0; i < redeemData.size(); i++) {
                        redeemData.get(i).setSelected(false);
                    }
                    mProductIdList.clear();
                    mJsonStr = "";
                    mBinding.redeemRecycler.scrollToPosition(0);
                    mOilRedeemAdapter.notifyDataSetChanged();

                    mBinding.oilPriceTv.setText("??" + mStationsBean.getOilPriceList().get(mOilNoPosition).getPriceYfq() + "???");
                    mBinding.oilStationPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    mBinding.oilStationPriceTv.setText("????????? ??" + mStationsBean.getOilPriceList().get(mOilNoPosition).getPriceGun());
                    mBinding.oilNumTv.setText(mStationsBean.getOilPriceList().get(mOilNoPosition).getOilName());
                    mBinding.oilGunTv.setText(mStationsBean.getOilPriceList().get(mOilNoPosition).getGunNos()
                            .get(mGunNoPosition).getGunNo() + "??????");
                    platId = "";
                    businessAmount = "";
                    businessId = "";//??????????????????????????????id
                    mBinding.amountEt.getText().clear();//????????????,????????????????????????
                    mIsShowMonthToast = false;
                    mBinding.monthRedCheck.setChecked(false);//????????????????????????
                    List<OilDefaultPriceEntity.DefaultAmountBean> data = mOilAmountAdapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setSelected(false);
                    }
                    mOilAmountAdapter.notifyDataSetChanged();
                    refreshMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                            mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(4).isUseBill() ? "1" : "0",
                            platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "", mIsUseCoupon, mIsUseBusinessCoupon, mJsonStr);

                    mViewModel.getDefaultPrice(mStationsBean.getGasId(), String.valueOf(mStationsBean.getOilPriceList()
                            .get(mOilNoPosition).getOilNo()));//??????????????????
                    EventTrackingManager.getInstance().tracking(OilOrderActivity.this,
                            TrackingConstant.GAS_GUN_NO,  "type=4");
                    closeDialog();
                }
            }

            @Override
            public void closeAll() {
                closeDialog();
            }
        });

        mOilNumDialog.show();
    }

    private void showCouponDialog(OilEntity.StationsBean stationsBean, String amount,
                                  int oilNoPosition, int gunNoPosition, String oilNo,
                                  boolean isPlat, String couponId) {
        //?????????dialog
        mOilCouponDialog = new OilCouponDialog(this, this, amount, stationsBean,
                oilNoPosition, gunNoPosition, oilNo, isPlat, couponId);
        mOilCouponDialog.setOnItemClickedListener(new OilCouponDialog.OnItemClickedListener() {
            @Override
            public void onOilCouponClick(BaseQuickAdapter adapter, View view, int position, boolean isPlat) {
                if (isPlat) {
                    mIsUseCoupon = true;
                } else {
                    mIsUseBusinessCoupon = true;
                }
                List<CouponBean> data = adapter.getData();
                if (data.get(position).getCouponMapCzbVo() != null){
                    mOilCouponDialog.dismiss();
                    AnyLayer.dialog(OilOrderActivity.this)
                            .contentView(R.layout.dialog_exchange_coupon)
                            .gravity(Gravity.CENTER)
                            .backgroundDimDefault()
                            .animStyle(DialogLayer.AnimStyle.ALPHA)
                            .cancelableOnTouchOutside(false)
                            .outsideTouchedToDismiss(false)
                            .bindData(layer -> {
                                TextView titleTv = layer.getView(R.id.item_title_tv);
                                TextView descTv = layer.getView(R.id.item_desc_tv);
                                TextView oldMoneyTv = layer.getView(R.id.item_old_money_tv);
                                TextView newMoneyTv = layer.getView(R.id.item_new_money_tv);
                                titleTv.setText(String.format("??????%s??????", mStationsBean.getGasName()));
                                SpanUtils.with(descTv)
                                        .append("?????????????????????????????????")
                                        .append("??" + NumberUtils.format(Double.parseDouble(
                                                data.get(position).getCouponMapCzbVo().getAmountReduce()),0))
                                        .setForegroundColor(getResources().getColor(R.color.color_27))
                                        .append("???????????????")
                                        .create();
                                oldMoneyTv.setText(NumberUtils.format(Double.parseDouble(data.get(position).getAmountReduce()), 0));
                                newMoneyTv.setText(NumberUtils.format(Double.parseDouble(data.get(position).getCouponMapCzbVo().getAmountReduce()), 0));
                            })
                            .onClick((layer, view1) -> {
                                switch (view1.getId()){
                                    case R.id.item_exchange_tv:
                                        mViewModel.updateCoupon(
                                                data.get(position).getId(),
                                                mStationsBean.getGasId(),
                                                data.get(position).getCouponMapCzbVo().getId());
                                        layer.dismiss();
                                        break;
                                    case R.id.close_iv:
                                        layer.dismiss();
                                        break;
                                }
                            }, R.id.item_exchange_tv,R.id.close_iv)
                            .show();
                    return;
                }
                setCouponInfo(data.get(position), isPlat, data.get(position).getExcludeType());
                mOilCouponDialog.dismiss();
            }

            @Override
            public void onNoCouponClick(boolean isPlat) {
                if (isPlat) {
                    mIsUseCoupon = false;
                } else {
                    mIsUseBusinessCoupon = false;
                }
                setCouponInfo(null, isPlat, "");
                mOilCouponDialog.dismiss();
            }
        });

        mOilCouponDialog.show();
    }

    /**
     * @param couponBean
     * @param isPlat      ??????????????????
     * @param excludeType 0:?????????; 1:???????????????; 2:????????????????????????; 3:?????????????????????????????????
     */
    public void setCouponInfo(CouponBean couponBean, boolean isPlat, String excludeType) {//???????????????
        if (couponBean != null) {
            if (isPlat) {
                mPlatCouponBean = couponBean;
                platId = couponBean.getId();
                mExcludeType = excludeType;
                switch (mExcludeType) {
                    case "0"://?????????
                        break;
                    case "1"://???????????????
                        mDiscountAdapter.getData().get(0).setFallDesc("????????????");
                        mDiscountAdapter.getData().get(0).setService(false);
                        break;
                    case "2"://????????????????????????
                        if (businessCoupons.size() > 0) {
                            businessAmount = "";
                            businessId = "";
                            mBusinessCouponBean = null;
                            mDiscountList.get(2).setBusinessDesc("??????????????????");
                        } else {
                            businessAmount = "";
                            businessId = "";
                            mBusinessCouponBean = null;
                            mDiscountList.get(2).setBusinessDesc("?????????????????????");
                        }
                        break;
                    case "3"://?????????????????????????????????
                        mDiscountAdapter.getData().get(0).setFallDesc("????????????");
                        mDiscountAdapter.getData().get(0).setService(false);
                        if (businessCoupons.size() > 0) {
                            businessAmount = "";
                            businessId = "";
                            mBusinessCouponBean = null;
                            mDiscountList.get(2).setBusinessDesc("??????????????????");
                        } else {
                            businessAmount = "";
                            businessId = "";
                            mBusinessCouponBean = null;
                            mDiscountList.get(2).setBusinessDesc("?????????????????????");
                        }
                        break;
                }
                mDiscountList.get(1).setPlatformDesc("-??" + couponBean.getAmountReduce());
                //?????????????????????????????????????????????
                mBinding.monthRedCheck.setChecked(false);
                mOilMonthAdapter.isSelected(false);
                mBinding.monthRedCl.setSelected(false);
            } else {
                mBusinessCouponBean = couponBean;
                businessId = couponBean.getId();
                businessAmount = couponBean.getAmountReduce();
                mDiscountList.get(2).setBusinessDesc("-??" + couponBean.getAmountReduce());
                if ("2".equals(mExcludeType) || "3".equals(mExcludeType)) {//????????????????????????????????????????????????
                    mPlatCouponBean = null;
                    platId = "";
                    mExcludeType = "";
                    mDiscountList.get(1).setPlatformDesc("??????????????????");
                }
            }
        } else {//??????????????????
            if (isPlat) {
                if (!mBinding.monthRedCheck.isChecked()) {
                    platId = "";
                    mExcludeType = "";
                    mDiscountList.get(1).setPlatformDesc("??????????????????");
                }
            } else {
                businessAmount = "";
                businessId = "";
                mBusinessCouponBean = null;
                mDiscountList.get(2).setBusinessDesc("??????????????????");
            }
        }
        mDiscountAdapter.notifyDataSetChanged();
        refreshMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(),
                String.valueOf(mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(4).isUseBill() ? "1" : "0",
                platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "", mIsUseCoupon, mIsUseBusinessCoupon, mJsonStr);
    }

    @Override
    protected void initListener() {
        mBinding.amountEt.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mBinding.discountRecyclerView.setVisibility(View.VISIBLE);
                    mBinding.hotIv.setVisibility(View.VISIBLE);
//                    mBinding.redeemCl.setVisibility(View.VISIBLE);
                    //?????????????????????????????????
                    List<OilDefaultPriceEntity.DefaultAmountBean> data = mOilAmountAdapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        if (NumberUtils.format(Float.parseFloat(s.toString()), 0)
                                .equals(NumberUtils.format(Float.parseFloat(data.get(i).getAmount()), 0))) {
                            data.get(i).setSelected(true);
                        } else {
                            data.get(i).setSelected(false);
                        }
                    }
                    mOilAmountAdapter.notifyDataSetChanged();
                    mViewModel.getDiscountMoney(mStationsBean.getGasId(), mBinding.amountEt.getText().toString());
                } else {
                    mBinding.discountRecyclerView.setVisibility(View.GONE);
                    mBinding.hotIv.setVisibility(View.GONE);

//                    mBinding.redeemCl.setVisibility(View.GONE);
                }
            }
        });

        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            if (height == 0) {
                //??????????????????
                refreshData();
            }
        });

        mBinding.createOrderTv.setOnClickListener(view -> {
            List<RedeemEntity.ProductOilGasListBean> data = mOilRedeemAdapter.getData();
            if (Float.parseFloat(mBinding.amountEt.getText().toString()) < 100 && data.get(0).getTrialType() == 2) {
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isSelected()) {
                        showToastInfo("???100??????????????????????????????????????????");
                        return;
                    }
                }
            }

            UiUtils.canClickViewStateDelayed(view, 1000);
            mViewModel.createOrder(mBinding.amountEt.getText().toString(), mMultiplePriceBean.getDuePrice(),
                    mMultiplePriceBean.getBalancePrice(), mStationsBean.getGasId(), String.valueOf(mStationsBean.getOilPriceList().get(mOilNoPosition).
                            getGunNos().get(mGunNoPosition).getGunNo()), String.valueOf(mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()),
                    mStationsBean.getOilPriceList().get(mOilNoPosition).getOilName(), mStationsBean.getGasName(), mStationsBean.getOilPriceList().get(mOilNoPosition).getPriceGun(),
                    mStationsBean.getOilPriceList().get(mOilNoPosition).getPriceYfq(), mStationsBean.getOilPriceList().get(mOilNoPosition).getOilType() + "",
                    SPUtils.getInstance().getString(SPConstants.MOBILE), platId, TextUtils.isEmpty(businessId) ? "" : businessId,
                    businessAmount, monthCouponId, mAmountReduce, mBinding.monthRedCheck.isChecked() && !TextUtils.isEmpty(monthCouponId),
                    mBinding.monthRedCheck.isChecked() && !TextUtils.isEmpty(mAmountReduce), mJsonStr, mProductAmount);
        });

        ClickUtils.applySingleDebouncing(mBinding.editLayout, this::onViewClicked);
        mBinding.backIv.setOnClickListener(this::onViewClicked);

        mBinding.hotIv.setOnClickListener(view -> {
            if (mOilHotDialog == null) {
                mOilHotDialog = new OilHotDialog(this, this);
                mOilHotDialog.show(view);
            } else {
                mOilHotDialog.show(view);
            }

        });

        mBinding.monthRedRule.setOnClickListener(view -> {
            WebViewActivity.openRealUrlWebActivity(this, Constants.BUY_MONTH_CARD_URL);
//            if (mOilMonthRuleDialog == null) {
//                mOilMonthRuleDialog = new OilMonthRuleDialog(this, this, mMonthCouponRule);
//                mOilMonthRuleDialog.show(view);
//            } else {
//                mOilMonthRuleDialog.show(view);
//            }
        });
    }

    private void showPayDialog(OilEntity.StationsBean stationsBean,
                               List<OilEntity.StationsBean.OilPriceListBean> oilPriceListBean,
                               int oilNoPosition, int gunNoPosition, String orderId, String payAmount) {
        //??????dialog
        mOilPayDialog = new OilPayDialog(this, this, stationsBean,
                oilPriceListBean, oilNoPosition, gunNoPosition, orderId, payAmount);
        mOilPayDialog.setOnItemClickedListener(new OilPayDialog.OnItemClickedListener() {
            @Override
            public void onOilPayTypeClick(BaseQuickAdapter adapter, View view, int position) {
//                GasStationLocationTipsDialog gasStationLocationTipsDialog =
//                        new GasStationLocationTipsDialog(getContext(), mBinding.getRoot(), "???????????????");
//                gasStationLocationTipsDialog.show();
                List<OilPayTypeEntity> data = adapter.getData();
                for (OilPayTypeEntity item : data) {
                    item.setSelect(false);
                }
                data.get(position).setSelect(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCloseAllClick() {
                monthCouponId = "";//??????????????? ??????????????????
                mIsUseCoupon = true;
                mViewModel.getMonthCoupon(mStationsBean.getGasId());
                cancelOrder(orderId);
                stayDiscount(mStationsBean.getGasId(), mBinding.amountEt.getText().toString());
//                closeDialog();

            }

            @Override
            public void onPayOrderClick(String payType, String orderId, String payAmount) {
                mHomeViewModel.payOrder(payType, orderId, payAmount);
            }
        });

        mOilPayDialog.show();
    }

    private void stayDiscount(String gasId, String oilAmount) {
        mViewModel.queryOilUserDiscount(gasId, oilAmount);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.edit_layout:
                showNumDialog(mStationsBean);
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.loadingView().observe(this, aBoolean -> {
            if (aBoolean) {
                showLoadingDialog();
            } else {
                dismissLoadingDialog();
            }
        });

        mViewModel.multiplePriceLiveData.observe(this, new Observer<MultiplePriceBean>() {
            @Override
            public void onChanged(MultiplePriceBean multiplePriceBean) {
                mMultiplePriceBean = multiplePriceBean;
                //??????????????????????????????
                List<OilDefaultPriceEntity.DefaultAmountBean> data = mOilAmountAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    if (!TextUtils.isEmpty(data.get(i).getAmount()) && !TextUtils.isEmpty(mBinding.amountEt.getText().toString())
                            && Float.parseFloat(data.get(i).getAmount()) == Float.parseFloat(mBinding.amountEt.getText().toString())) {
                        data.get(i).setAlltotalDiscountAmount(mMultiplePriceBean.getSumDiscountPrice());
                    }
                }
                mOilAmountAdapter.notifyDataSetChanged();
                if (multiplePriceBean.getBestuserCoupon() != null || multiplePriceBean.getBestBusinessCoupon() != null) {
                    mBinding.discountDesc.setVisibility(View.VISIBLE);
                } else {
                    mBinding.discountDesc.setVisibility(View.GONE);
                }
                //???????????? //?????????
                if (Float.parseFloat(multiplePriceBean.getTotalDiscountAmount()) > 0) {
                    mDiscountAdapter.getData().get(0).setFallAmount(
                            Float.parseFloat(multiplePriceBean.getTotalDiscountAmount()));
                } else {
                    mDiscountAdapter.getData().get(0).setFallAmount(
                            Float.parseFloat(multiplePriceBean.getTotalDiscountAmount()));
                    mDiscountAdapter.getData().get(0).setFallDesc("????????????");
                }
                //??????
                mBinding.literTv.setText(String.format("???%sL",
                        multiplePriceBean.getLiter()));
                //????????????
                mBinding.currentPriceTv.setText(String.format("???????????%s",
                        multiplePriceBean.getDuePrice()));
                //????????????
                mBinding.discountPriceTv.setText(String.format("??????????????%s",
                        multiplePriceBean.getSumDiscountPrice()));

                mProductAmount = multiplePriceBean.getProductAmount();
                //?????????
                if (!TextUtils.isEmpty(multiplePriceBean.getServiceChargeAmount()) &&
                        Float.parseFloat(multiplePriceBean.getServiceChargeAmount()) > 0) {
                    mDiscountList.get(0).setService(true);
                } else {
                    mDiscountList.get(0).setService(false);
                }
                if (multiplePriceBean.getBestuserCoupon() != null) {
                    platId = multiplePriceBean.getBestuserCoupon().getId();
                    mDiscountAdapter.getData().get(1).setPlatformDesc("-??" + multiplePriceBean.getBestuserCoupon().getAmountReduce());
                }

                if (multiplePriceBean.getBestuserCoupon()!= null
                        && multiplePriceBean.getBestuserCoupon().getCouponMapCzbVo() != null){
                    mDiscountAdapter.getData().get(1).setSwell(multiplePriceBean.getBestuserCoupon()
                            .getCouponMapCzbVo().getAmountReduce());
                }
//                else {
//                    if (platformCoupons != null){
//                        mDiscountAdapter.getData().get(1).setPlatformDesc("??????????????????");
//                    }else {
//                        mDiscountAdapter.getData().get(1).setPlatformDesc("?????????????????????");
//                    }
//                }
                if (multiplePriceBean.getBestBusinessCoupon() != null) {
                    businessId = multiplePriceBean.getBestBusinessCoupon().getId();
                    businessAmount = multiplePriceBean.getBestBusinessCoupon().getAmountReduce();
                    mDiscountAdapter.getData().get(2).setBusinessDesc("-??" + multiplePriceBean.getBestBusinessCoupon().getAmountReduce());
                }
//                else {
//                    if (businessCoupons != null){
//                        mDiscountAdapter.getData().get(2).setBusinessDesc("??????????????????");
//                    }else {
//                        mDiscountAdapter.getData().get(2).setBusinessDesc("?????????????????????");
//                    }
//                }
                //????????????
                if (isUseBill) {
                    if (Float.parseFloat(multiplePriceBean.getBalancePrice()) > 0) {
                        mDiscountAdapter.getData().get(4).setUseBill(true);
                        mDiscountAdapter.getData().get(4).setBalanceDiscount(
                                Float.parseFloat(multiplePriceBean.getBalancePrice()));
                    } else {
                        mDiscountAdapter.getData().get(4).setUseBill(false);
                        isUseBill = false;
                    }
                } else {
                    mDiscountAdapter.getData().get(4).setUseBill(false);
                }

                mDiscountAdapter.notifyDataSetChanged();

                mBinding.createOrderTv.setEnabled(
                        Float.parseFloat(multiplePriceBean.getDuePrice()) > 0 ||
                                Float.parseFloat(multiplePriceBean.getSumDiscountPrice()) > 0);
            }
        });

        mViewModel.defaultPriceLiveData.observe(this, new Observer<OilDefaultPriceEntity>() {
            @Override
            public void onChanged(OilDefaultPriceEntity defaultPriceEntity) {
                List<OilDefaultPriceEntity.DefaultAmountBean> defaultAmount = defaultPriceEntity.getDefaultAmount();
                mOilAmountAdapter.setNewData(defaultAmount);
//                if (isFirstIn){
//                    //????????????200????????????
//                    mBinding.amountEt.setText("200");
//                    isFirstIn = false;
//                    refreshData();
//                }
            }
        });

        mViewModel.monthCouponLiveData.observe(this, new Observer<MonthCouponEntity>() {
            @Override
            public void onChanged(MonthCouponEntity monthCouponEntity) {
                if (monthCouponEntity != null && monthCouponEntity.getMonthCouponTemplates().size() > 0) {
                    mBinding.monthRedCl.setVisibility(View.VISIBLE);

                    SpanUtils.with(mBinding.monthRedMoney)
                            .append("??").setFontSize(13, true)
                            .append(monthCouponEntity.getMonthCouponAmount() + "")
                            .create();

                    mMonthCouponRule = monthCouponEntity.getMonthCouponRule();
                    mMonthCouponImgUrl = monthCouponEntity.getMonthCouponImgUrl();
                    for (int i = 0; i < monthCouponEntity.getMonthCouponTemplates().size(); i++) {
                        if (monthCouponEntity.getMonthCouponTemplates().get(i).isMonthCouponDefault()) {
                            monthCouponId = monthCouponEntity.getMonthCouponTemplates().get(i).getId() + "";
                            mMonthAmount = Float.parseFloat(monthCouponEntity.getMonthCouponTemplates().get(i).getAmount());
                            mAmountReduce = monthCouponEntity.getMonthCouponTemplates().get(i).getAmountReduce();
                        }
                        totalMoney = totalMoney + Float.parseFloat(monthCouponEntity.getMonthCouponTemplates().get(i).getAmountReduce());
                    }
//                    SpanUtils.with(mBinding.monthRedDesc)
//                            .append(monthCouponEntity.getMonthCouponAmount() + "???")
//                            .setForegroundColor(getResources().getColor(R.color.color_1300))
//                            .append("???")
//                            .setForegroundColor(getResources().getColor(R.color.color_34))
//                            .append(totalMoney + "???")
//                            .setForegroundColor(getResources().getColor(R.color.color_1300))
//                            .append("???????????????????????????")
//                            .setForegroundColor(getResources().getColor(R.color.color_34))
//                            .append(mAmountReduce + "???")
//                            .setForegroundColor(getResources().getColor(R.color.color_1300))
//                            .create();
                    mBinding.monthRedDesc.setText(monthCouponEntity.getMonthCouponAmount() + "??????" +
                            totalMoney + "??????????????????????????????" + mAmountReduce + "???");

                    mOilMonthAdapter.setNewData(monthCouponEntity.getMonthCouponTemplates());
                } else {
                    mBinding.monthRedCl.setVisibility(View.GONE);
                }
            }
        });

        mViewModel.platformCouponLiveData.observe(this, new Observer<List<CouponBean>>() {
            @Override
            public void onChanged(List<CouponBean> couponBeans) {
                if (couponBeans != null && couponBeans.size() > 0) {
                    platformCoupons = couponBeans;
                    if (mBinding.monthRedCheck.isChecked()) {//????????????????????????????????????????????????
                        refreshMonthStauts(mBinding.amountEt.getText().toString().trim());
                    } else {
                        mDiscountAdapter.getData().get(1).setPlatformDesc("??????????????????");
                    }
                } else {
                    if (mBinding.monthRedCheck.isChecked()) {//????????????????????????????????????????????????
                        refreshMonthStauts(mBinding.amountEt.getText().toString().trim());
                    } else {
                        mDiscountAdapter.getData().get(1).setPlatformDesc("?????????????????????");
                    }
                }
                mDiscountAdapter.notifyDataSetChanged();
            }
        });

        mViewModel.businessCouponLiveData.observe(this, new Observer<List<CouponBean>>() {
            @Override
            public void onChanged(List<CouponBean> couponBeans) {
                if (couponBeans != null && couponBeans.size() > 0) {
                    businessCoupons = couponBeans;
                    mDiscountAdapter.getData().get(2).setBusinessDesc("??????????????????");
                } else {
                    mDiscountAdapter.getData().get(2).setBusinessDesc("?????????????????????");
                }
                mDiscountAdapter.notifyDataSetChanged();
            }
        });

        mViewModel.redeemLiveData.observe(this, new Observer<RedeemEntity>() {
            @Override
            public void onChanged(RedeemEntity redeemEntity) {
                if (redeemEntity != null) {
                    List<RedeemEntity.EntranceListBean> entranceList = redeemEntity.getEntranceList();
                    Glide.with(OilOrderActivity.this).load(entranceList.get(0).getIcon()).into(mBinding.redeemView3);
                    mBinding.redeemView4.setText(entranceList.get(0).getTitle());
                    mBinding.redeemView5.setText(entranceList.get(0).getSubtitle());
                    if (entranceList.get(0).getType() == 2) {//????????????
                        mBinding.redeemView6.setVisibility(View.VISIBLE);
                        mBinding.redeemView5.setTextColor(getResources().getColor(R.color.color_9A));
                    } else {
                        mBinding.redeemView6.setVisibility(View.GONE);
                        mBinding.redeemView5.setTextColor(getResources().getColor(R.color.color_76FF));
                    }
                    mBinding.redeemView6.setOnClickListener(view -> WebViewActivity.openRealUrlWebActivity(OilOrderActivity.this, redeemEntity.getH5url()));

                    mRedeemList = redeemEntity.getProductOilGasList();
                    mOilRedeemAdapter.setNewData(mRedeemList);
                    mOilRedeemAdapter.notifyDataSetChanged();
                } else {
                    mBinding.redeemCl.setVisibility(View.GONE);
                }
            }
        });

        mViewModel.balanceLiveData.observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float balance) {

                if (balance > 0) {
                    mDiscountAdapter.getData().get(4).setBalance(balance);
                    if (!isFirstOpen) {
                        mDiscountAdapter.getData().get(4).setUseBill(true);
                        isUseBill = true;
                    } else {
                        if (isUseBill) {
                            mDiscountAdapter.getData().get(4).setUseBill(true);
                        } else {
                            mDiscountAdapter.getData().get(4).setUseBill(false);
                        }

                    }

                } else {
                    mDiscountAdapter.getData().get(4).setBalance(0);
                    mDiscountAdapter.getData().get(4).setUseBill(false);
                    isUseBill = false;
                }
                if (isFirstOpen) {
                    isFirstOpen = false;
                }
                mDiscountAdapter.notifyDataSetChanged();
            }
        });

        mViewModel.createOrderLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String orderId) {
                showPayDialog(mStationsBean, mStationsBean.getOilPriceList(), mOilNoPosition, mGunNoPosition,
                        orderId, mMultiplePriceBean.getDuePrice());
            }
        });

        //??????????????????
        mHomeViewModel.payOrderLiveData.observe(this, payOrderEntity -> {
            if (payOrderEntity.getResult() == 0) {//???????????????
                switch (payOrderEntity.getPayType()) {
                    case PayTypeConstants.PAY_TYPE_WEIXIN://??????H5
                        WeChatWebPayActivity.openWebPayAct(this, payOrderEntity.getUrl());
                        shouldJump = true;
                        break;
                    case PayTypeConstants.PAY_TYPE_WEIXIN_APP://????????????
//                        WeChatWebPayActivity.openWebPayAct(this, payOrderEntity.getUrl());
                        PayListenerUtils.getInstance().addListener(this);
                        PayHelper.getInstance().WexPay(payOrderEntity.getPayParams());

                        break;
                    case PayTypeConstants.PAY_TYPE_WEIXIN_XCX://???????????????
                        WXSdkManager.newInstance().useWXLaunchMiniProgramToPay(this, payOrderEntity.getOrderNo());
                        shouldJump = true;
                        break;
                    case PayTypeConstants.PAY_TYPE_ZHIFUBAO://?????????H5
                        boolean urlCanUse = UiUtils.checkZhifubaoSdkCanPayUrl(this,
                                payOrderEntity.getUrl(),
                                h5PayResultModel -> {//?????????????????????
                                    jumpToPayResultAct(payOrderEntity.getOrderPayNo(),
                                            payOrderEntity.getOrderNo());
                                });
                        if (!urlCanUse) {//?????????????????????
                            isShouldAutoOpenWeb = true;
                            mBinding.payWebView.loadUrl(payOrderEntity.getUrl());
                            mBinding.payWebView.post(() -> {
                                if (isShouldAutoOpenWeb) {
                                    UiUtils.openPhoneWebUrl(this,
                                            payOrderEntity.getUrl());
                                }
                            });
                        }
                        shouldJump = true;
                        break;
                    case PayTypeConstants.PAY_TYPE_ZHIFUBAO_APP:
                        PayListenerUtils.getInstance().addListener(this);
                        PayHelper.getInstance().AliPay(this, payOrderEntity.getStringPayParams());

                        break;
                    case PayTypeConstants.PAY_TYPE_UNIONPAY:
                        PayListenerUtils.getInstance().addListener(this);
                        PayHelper.getInstance().unionPay(this, payOrderEntity.getPayNo());
                        break;
                }
                //                BusUtils.postSticky(EventConstants.EVENT_JUMP_PAY_QUERY, payOrderEntity);
                mPayOrderEntity = payOrderEntity;
            } else if (payOrderEntity.getResult() == 1) {//????????????
                jumpToPayResultAct(payOrderEntity.getOrderPayNo(), payOrderEntity.getOrderNo());
            } else {
                showToastWarning(payOrderEntity.getMsg());
            }
        });

        mViewModel.discountLiveData.observe(this, oilUserDiscountEntity -> {
            if (oilUserDiscountEntity.getFlag() == 0){
                closeDialog();
            }else {
                if (mOilUserDiscountDialog == null){
                    mOilUserDiscountDialog = new OilUserDiscountDialog(OilOrderActivity.this,
                            OilOrderActivity.this, oilUserDiscountEntity.getTitle());
                }
                mOilUserDiscountDialog.show(mOilPayDialog.findViewById(R.id.cancel_iv));
                mOilUserDiscountDialog.setOnFirmClickListener(() -> {
                    closeDialog();
                });
            }
        });

        mViewModel.discountMoneyLiveData.observe(this, new Observer<OilUserDiscountEntity>() {
            @Override
            public void onChanged(OilUserDiscountEntity oilUserDiscountEntity) {
                if (TextUtils.equals("0", oilUserDiscountEntity.getAmountReduce())){
                    mDiscountAdapter.getData().get(3).setDiscount("0");
                }else {
                    mDiscountAdapter.getData().get(3).setDiscount(oilUserDiscountEntity.getAmountReduce());
                }
                mDiscountAdapter.notifyDataSetChanged();
            }
        });

        mViewModel.updateCouponLiveData.observe(this, s -> {
            ToastUtils.showShort("????????????");
            showCouponDialog(
                    mStationsBean,
                    mBinding.amountEt.getText().toString().trim(),
                    mOilNoPosition,
                    mGunNoPosition,
                    String.valueOf(mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()),
                    false,
                    "");
            //??????????????????
            refreshMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                    mStationsBean.getOilPriceList().get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(4).isUseBill() ? "1" : "0",
                    "", businessAmount, monthCouponId, mIsUseCoupon, mIsUseBusinessCoupon, mJsonStr);
        });

    }
    private void cancelOrder(String orderId) {
        orderDetailsViewModel.cancelOrder(orderId);
    }


    public void refreshMultiplePrice(String amount, String gasId, String oilNo, String isUserBill,
                                     String platId, String businessAmount, String monthCouponId,
                                     boolean isUseCoupon, boolean isUseBusinessCoupon, String productIds) {
        mViewModel.getMultiplePrice(amount, gasId, oilNo, isUserBill, platId, businessAmount, monthCouponId, isUseCoupon, isUseBusinessCoupon, productIds);
    }

    private void jumpToPayResultAct(String orderPayNo, String orderNo) {
        if (TextUtils.isEmpty(orderPayNo) && TextUtils.isEmpty(orderNo)) {
            return;
        }
        Intent intent = new Intent(this, RefuelingPayResultActivity.class);
        intent.putExtra("orderPayNo", orderPayNo);
        intent.putExtra("orderNo", orderNo);
        startActivity(intent);
        closeDialog();
        clearData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
         * ???????????????????????????:success???fail???cancel ??????????????????????????????????????????????????????
         */

        if (data == null) {
            return;
        }
        String str = data.getExtras().getString("pay_result");
        if (!TextUtils.isEmpty(str)) {
            if (str.equalsIgnoreCase("success")) {
                //?????????????????????????????????????????????????????????????????????????????????
                //??????????????????????????????????????????sign???data???mode(???????????????)???sign???data?????????result_data?????????
                /**
                 * result_data???????????????
                 * sign ?????? ????????????Base64?????????
                 * data ?????? ???????????????????????????
                 *      data????????????????????????
                 *      pay_result ?????? ????????????success???fail???cancel
                 *      tn ?????? ?????????
                 */
//            msg = "?????????????????????";
                PayListenerUtils.getInstance().addSuccess();
            } else if (str.equalsIgnoreCase("fail")) {
//            msg = "????????????????????????";
                PayListenerUtils.getInstance().addFail();
            } else if (str.equalsIgnoreCase("cancel")) {
//            msg = "??????????????????????????????";
                PayListenerUtils.getInstance().addCancel();

            }
        }

    }

    @Override
    public void onSuccess() {
        RefuelingPayResultActivity.openPayResultPage(this,
                mPayOrderEntity.getOrderNo(), mPayOrderEntity.getOrderPayNo(), false, true);
        PayListenerUtils.getInstance().removeListener(this);
        closeDialog();
        clearData();
    }

    @Override
    public void onFail() {
        RefuelingPayResultActivity.openPayResultPage(this,
                mPayOrderEntity.getOrderNo(), mPayOrderEntity.getOrderPayNo(), false, true);
        PayListenerUtils.getInstance().removeListener(this);
        closeDialog();
        clearData();
    }

    @Override
    public void onCancel() {
        Toasty.info(this, "????????????").show();
        PayListenerUtils.getInstance().removeListener(this);
        closeDialog();
        clearData();
    }

    private void closeDialog() {
        if (mOilCouponDialog != null) {
            mOilCouponDialog.dismiss();
            mOilCouponDialog = null;
        }
        if (mOilNumDialog != null) {
            mOilNumDialog.dismiss();
            mOilNumDialog = null;
        }
        if (mOilPayDialog != null) {
            mOilPayDialog.dismiss();
            mOilPayDialog = null;
        }
        if (mOilUserDiscountDialog != null){
            mOilUserDiscountDialog.dismiss();
            mOilUserDiscountDialog = null;
        }

        refreshData();
    }

    private void clearData() {
//        mBinding.monthRedCheck.setChecked(false);
        //?????????????????????????????????
//        List<OilDefaultPriceEntity.DefaultAmountBean> data = mOilAmountAdapter.getData();
//        for (int i = 0; i < data.size(); i++) {
//            data.get(i).setSelected(false);
//        }
//        mOilAmountAdapter.notifyDataSetChanged();
//        mViewModel.getMonthCoupon(mStationsBean.getGasId());//??????????????????
//        mViewModel.getRedeem(mStationsBean.getGasId());
//        mBinding.amountEt.getText().clear();
//        refreshData();
        finish();
    }
}