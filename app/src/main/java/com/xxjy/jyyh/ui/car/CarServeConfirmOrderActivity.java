package com.xxjy.jyyh.ui.car;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilRedeemAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.PayTypeConstants;
import com.xxjy.jyyh.databinding.ActivityCarServeConfirmOrderBinding;
import com.xxjy.jyyh.dialog.CarServeCouponDialog;
import com.xxjy.jyyh.dialog.QueryDialog;
import com.xxjy.jyyh.dialog.UnifiedPaymentCashierDialog;
import com.xxjy.jyyh.entity.CarServeCommitOrderBean;
import com.xxjy.jyyh.entity.CarServeCouponBean;
import com.xxjy.jyyh.entity.CarServeCouponListBean;
import com.xxjy.jyyh.entity.CarServeProductsBean;
import com.xxjy.jyyh.entity.CardStoreInfoVoBean;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.entity.ProductIdEntity;
import com.xxjy.jyyh.entity.RedeemEntity;
import com.xxjy.jyyh.ui.pay.CarServePayResultActivity;
import com.xxjy.jyyh.ui.web.WeChatWebPayActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.GlideUtils;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.Util;
import com.xxjy.jyyh.utils.WXSdkManager;
import com.xxjy.jyyh.utils.eventtrackingmanager.EventTrackingManager;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingConstant;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingEventConstant;
import com.xxjy.jyyh.utils.pay.IPayListener;
import com.xxjy.jyyh.utils.pay.PayHelper;
import com.xxjy.jyyh.utils.pay.PayListenerUtils;
import com.xxjy.jyyh.utils.toastlib.Toasty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CarServeConfirmOrderActivity extends BindingActivity<ActivityCarServeConfirmOrderBinding, CarServeViewModel> implements IPayListener {


    private CardStoreInfoVoBean mCardStoreInfoVo;
    private CarServeProductsBean selectCarServeProductsBean;
    private CarServeCouponBean selectCarServeCouponBean;


    private List<RedeemEntity.ProductOilGasListBean> mRedeemList = new ArrayList<>();
    private OilRedeemAdapter mOilRedeemAdapter;
    private List<ProductIdEntity> mProductIdList = new ArrayList<>();
    private String mJsonStr = "";//????????????ids
    private QueryDialog mQueryDialog;
    private double allProductPrice = 0;
    private double carServePrice = 0;
    private UnifiedPaymentCashierDialog unifiedPaymentCashierDialog;

    //?????????????????????????????????
    private boolean shouldJump = false;
    private PayOrderEntity mPayOrderEntity;
    private boolean isShouldAutoOpenWeb = false;    //???????????????????????????????????????????????????

    private CarServeCommitOrderBean mCarServeCommitOrderBean;

    private CarServeCouponDialog mCarServeCouponDialog;
    private CarServeCouponListBean mCarServeCouponListBean;
    private long selectCouponId = 0;
    private boolean isFromDetail=false;

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, true);
        mCardStoreInfoVo = (CardStoreInfoVoBean) getIntent().getSerializableExtra("store");
        selectCarServeProductsBean = (CarServeProductsBean) getIntent().getSerializableExtra("product");
        selectCarServeCouponBean = (CarServeCouponBean) getIntent().getSerializableExtra("coupon");
        isFromDetail = getIntent().getBooleanExtra("from_detail",false);
        carServePrice = Double.parseDouble(selectCarServeProductsBean.getSalePrice());
        refreshPrice();
        initData();
        tyingProduct();
        EventTrackingManager.getInstance().tracking(this, TrackingConstant.CF_PAGE_ORDER_CONFIRM,"store_no="+mCardStoreInfoVo.getStoreNo()+"&product_id="+selectCarServeProductsBean.getId());
    }

    @Override
    protected void initListener() {
        ClickUtils.applyGlobalDebouncing(mBinding.createOrderTv, this::onViewClicked);
        mBinding.backIv.setOnClickListener(this::onViewClicked);
        mBinding.couponLayout.setOnClickListener(this::onViewClicked);

    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.create_order_tv:
                if (mCarServeCommitOrderBean != null) {
                    showPay(mCarServeCommitOrderBean);
                } else {
                    commitOrder(mCardStoreInfoVo.getId() + "", selectCarServeProductsBean.getId() + "",
                            (Double.parseDouble(selectCarServeProductsBean.getSalePrice()) + allProductPrice) + "",
                            selectCarServeCouponBean == null ? "" : selectCarServeCouponBean.getCouponType(), selectCarServeCouponBean == null ? -1 : selectCarServeCouponBean.getId(),
                            selectCarServeCouponBean == null ? "" : selectCarServeCouponBean.getCouponValue() + "", mJsonStr);
                }
                EventTrackingManager.getInstance().trackingEvent(this,TrackingConstant.CF_PAGE_ORDER_CONFIRM, TrackingEventConstant.CF_EVENT_ORDER_CONFIRM_BUY);
                break;
            case R.id.coupon_layout:
                if (!(mCarServeCouponListBean !=null&&mCarServeCouponListBean.getRecords()!=null&&mCarServeCouponListBean.getRecords().size()>0)) {
                    return;
                }
                if (mCarServeCouponDialog == null) {
                    mCarServeCouponDialog = new CarServeCouponDialog(this, selectCouponId);
                }
                mCarServeCouponDialog.dispatchData(mCarServeCouponListBean.getRecords(), selectCouponId);
                mCarServeCouponDialog.show();
                mCarServeCouponDialog.setOnItemClickedListener(new CarServeCouponDialog.OnItemClickedListener() {
                    @Override
                    public void onOilCouponClick(BaseQuickAdapter adapter, View view, int position) {
                        selectCarServeCouponBean = mCarServeCouponListBean.getRecords().get(position);
                        selectCouponId = selectCarServeCouponBean.getId();
                        carServePrice = selectCarServeCouponBean.getCouponValue();
                        mBinding.couponNameView.setText(mCarServeCouponListBean.getRecords().get(position).getTitle());
                        refreshPrice();
                    }

                    @Override
                    public void onNoCouponClick() {
                        selectCouponId = 0;
                        mBinding.couponNameView.setText("???????????????");
                        selectCarServeCouponBean = null;
                        carServePrice = Double.parseDouble(selectCarServeProductsBean.getSalePrice());
                        refreshPrice();
                    }
                });
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.tyingProductLiveData.observe(this, redeemEntity -> {
            if (redeemEntity != null) {
                mBinding.redeemCl.setVisibility(View.VISIBLE);
                List<RedeemEntity.EntranceListBean> entranceList = redeemEntity.getEntranceCfList();
                Glide.with(CarServeConfirmOrderActivity.this).load(entranceList.get(0).getIcon()).into(mBinding.redeemView3);
                mBinding.redeemView4.setText(entranceList.get(0).getTitle());
                mBinding.redeemView5.setText(entranceList.get(0).getSubtitle());
                if (entranceList.get(0).getType() == 2) {
                    mBinding.redeemView6.setVisibility(View.VISIBLE);
                    mBinding.redeemView5.setTextColor(getResources().getColor(R.color.color_9A));
                } else {
                    mBinding.redeemView6.setVisibility(View.GONE);
                    mBinding.redeemView5.setTextColor(getResources().getColor(R.color.color_76FF));
                }
                mBinding.redeemView6.setOnClickListener(view -> WebViewActivity.openRealUrlWebActivity(CarServeConfirmOrderActivity.this, redeemEntity.getH5url()));


                mRedeemList = redeemEntity.getProductOilGasList();
                mOilRedeemAdapter.setNewData(mRedeemList);
                mOilRedeemAdapter.notifyDataSetChanged();

                if(mRedeemList!=null&&mRedeemList.size()>0){
                    if(Double.parseDouble(mRedeemList.get(0).getRedeemPrice())==0d){
                        mOilRedeemAdapter.setSelected(0);
                        mProductIdList.clear();
                        allProductPrice = 0;
                        for (int i = 0; i < mRedeemList.size(); i++) {
                            if (mRedeemList.get(i).isSelected()) {
                                mProductIdList.add(new ProductIdEntity(mRedeemList.get(i).getProductId() + "", "1"));
                                allProductPrice = allProductPrice + Double.parseDouble(mRedeemList.get(i).getRedeemPrice());
                            }
                        }
                        mJsonStr = GsonUtils.toJson(mProductIdList);
                        refreshPrice();
                    }
                }
            } else {
                mBinding.redeemCl.setVisibility(View.GONE);
            }
        });
        mViewModel.commitOrderLiveData.observe(this, data -> {
            mCarServeCommitOrderBean = data;
            showPay(data);
        });

        //??????????????????
        mViewModel.payOrderLiveData.observe(this, payOrderEntity -> {
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
                                    jumpToPayResultAct(payOrderEntity.getPayId(),
                                            payOrderEntity.getOrderId());
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
                jumpToPayResultAct(payOrderEntity.getPayId(), payOrderEntity.getOrderId());
            } else {
                showToastWarning(payOrderEntity.getMsg());
            }
        });

        mViewModel.usableCouponLiveData.observe(this, data -> {
            mCarServeCouponListBean = data;
            if (selectCarServeCouponBean != null) {
                mBinding.couponLayout.setVisibility(View.VISIBLE);
                mBinding.couponNameView.setText(selectCarServeCouponBean.getTitle());
                carServePrice = selectCarServeCouponBean.getCouponValue();
                selectCouponId = selectCarServeCouponBean.getId();
            } else {

                    if (selectCarServeProductsBean.getCategoryId()==1) {//??????
                        if (data.getRecords() != null && data.getRecords().size() > 0) {
                                if(isFromDetail){
                                    mBinding.couponLayout.setVisibility(View.VISIBLE);
                                    mBinding.couponNameView.setText("??????????????????");
                                }else{
                                    mBinding.couponLayout.setVisibility(View.VISIBLE);
                                    mBinding.couponNameView.setText(data.getRecords().get(0).getTitle());
                                    selectCouponId = data.getRecords().get(0).getId();
                                    selectCarServeCouponBean = data.getRecords().get(0);
                                    carServePrice = selectCarServeCouponBean.getCouponValue();
                                }

                        }else{
                            mBinding.couponLayout.setVisibility(View.VISIBLE);
                            mBinding.couponNameView.setText("???????????????");
                        }
                    } else {
                        mBinding.couponLayout.setVisibility(View.GONE);
                        carServePrice = Double.parseDouble(selectCarServeProductsBean.getSalePrice());
                }

            }
            refreshPrice();




        });
    }

    private void initData() {
        GlideUtils.loadImage(this, mCardStoreInfoVo.getStorePicture(), mBinding.shopImageView,R.drawable.ic_car_serve_store_image);
        mBinding.shopNameView.setText(mCardStoreInfoVo.getStoreName());
        mBinding.shopAddressView.setText(mCardStoreInfoVo.getAddress());

        GlideUtils.loadImage(this, selectCarServeProductsBean.getCover(), mBinding.productImageView,R.drawable.ic_car_default_product);
        mBinding.productNameView.setText(selectCarServeProductsBean.getName());
        mBinding.productLinePriceView.setText("??" + Util.formatDouble(Double.parseDouble(selectCarServeProductsBean.getLinePrice())));
        mBinding.productPriceView.setText("??" + Util.formatDouble(Double.parseDouble(selectCarServeProductsBean.getSalePrice())));
        mBinding.productLinePriceView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);


        getUsableCoupon(selectCarServeProductsBean.getChildCategoryId()==0?selectCarServeProductsBean.getCategoryId():selectCarServeProductsBean.getChildCategoryId());

        refreshPrice();

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
                        allProductPrice = 0;
                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).isSelected()) {
                                mProductIdList.add(new ProductIdEntity(data.get(i).getProductId() + "", "1"));
                                allProductPrice = allProductPrice + Double.parseDouble(data.get(i).getRedeemPrice());
                            }
                        }
                        mJsonStr = GsonUtils.toJson(mProductIdList);
                        refreshPrice();


                    }
                });
            } else {
                mOilRedeemAdapter.setSelected(position);
                mProductIdList.clear();
                allProductPrice = 0;
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isSelected()) {
                        mProductIdList.add(new ProductIdEntity(data.get(i).getProductId() + "", "1"));
                        allProductPrice = allProductPrice + Double.parseDouble(data.get(i).getRedeemPrice());
                    }
                }
                mJsonStr = GsonUtils.toJson(mProductIdList);
                refreshPrice();
            }
            EventTrackingManager.getInstance().trackingEvent(this,TrackingConstant.CF_PAGE_ORDER_CONFIRM, TrackingEventConstant.CF_EVENT_SELLSTH+data.get(position).getName());
        });
    }

    private void refreshPrice() {
//        BigDecimal.valueOf(allProductPrice).add(BigDecimal.valueOf(carServePrice));
        mBinding.currentPriceTv.setText("???????????" + Util.formatDouble(BigDecimal.valueOf(allProductPrice).add(BigDecimal.valueOf(carServePrice)).doubleValue()));
    }

    private void tyingProduct() {
        mViewModel.tyingProduct();
    }

    private void cancelOrder(String orderId) {
        mViewModel.cancelOrder(orderId);
    }

    private void commitOrder(String storeId, String productId,
                             String realMoney, String couponType, long couponId,
                             String couponAmount, String sku) {
        mViewModel.commitOrder(storeId, productId, realMoney, couponType, couponId, couponAmount, sku);

    }

    private void getUsableCoupon(int categoryId) {
        mViewModel.getUsableCoupon(categoryId);
    }

    private void showJump(PayOrderEntity orderEntity) {
        if (orderEntity == null) return;
        if (shouldJump) {
            shouldJump = false;
            CarServePayResultActivity.openPayResultPage(this,
                    orderEntity.getOrderId(), orderEntity.getPayId());
            closeDialog();
            finish();
        }
    }

    private void showPay(CarServeCommitOrderBean data) {
        if (unifiedPaymentCashierDialog == null) {
            unifiedPaymentCashierDialog = new UnifiedPaymentCashierDialog(this, data.getOrderId(), data.getRealMoney() + "", UnifiedPaymentCashierDialog.CARS_CODE);
        }
        unifiedPaymentCashierDialog.show();
        unifiedPaymentCashierDialog.setOnItemClickedListener(new UnifiedPaymentCashierDialog.OnItemClickedListener() {
            @Override
            public void onOilPayTypeClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onCloseAllClick() {
                unifiedPaymentCashierDialog.dismiss();
                cancelOrder(data.getOrderId());
                finish();
            }

            @Override
            public void onPayOrderClick(String payType, String orderId, String payAmount) {
                mViewModel.payOrder(payType, orderId, payAmount);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        showJump(mPayOrderEntity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unifiedPaymentCashierDialog = null;
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
        CarServePayResultActivity.openPayResultPage(this,
                mPayOrderEntity.getOrderId(), mPayOrderEntity.getPayId(), false, true);
        PayListenerUtils.getInstance().removeListener(this);
        closeDialog();
    }

    @Override
    public void onFail() {
        CarServePayResultActivity.openPayResultPage(this,
                mPayOrderEntity.getOrderId(), mPayOrderEntity.getPayId(), false, true);
        PayListenerUtils.getInstance().removeListener(this);
        closeDialog();
    }

    @Override
    public void onCancel() {
        Toasty.info(this, "????????????").show();
        PayListenerUtils.getInstance().removeListener(this);
        closeDialog();
    }

    private void closeDialog() {
        if (unifiedPaymentCashierDialog != null) {
            unifiedPaymentCashierDialog.dismiss();
            unifiedPaymentCashierDialog = null;
        }

    }

    public static void openPage(Context context, CardStoreInfoVoBean mCardStoreInfoVo, CarServeProductsBean selectCarServeProductsBean, CarServeCouponBean selectCarServeCouponBean) {
        Intent intent = new Intent(context, CarServeConfirmOrderActivity.class);
        intent.putExtra("store", mCardStoreInfoVo);
        intent.putExtra("product", selectCarServeProductsBean);
        intent.putExtra("coupon", selectCarServeCouponBean);
        context.startActivity(intent);
    }
    public static void openPage(Context context, CardStoreInfoVoBean mCardStoreInfoVo, CarServeProductsBean selectCarServeProductsBean, CarServeCouponBean selectCarServeCouponBean,boolean fromDetail) {
        Intent intent = new Intent(context, CarServeConfirmOrderActivity.class);
        intent.putExtra("store", mCardStoreInfoVo);
        intent.putExtra("product", selectCarServeProductsBean);
        intent.putExtra("coupon", selectCarServeCouponBean);
        intent.putExtra("from_detail", fromDetail);
        context.startActivity(intent);
    }

    private void jumpToPayResultAct(String orderPayNo, String orderNo) {
        if (TextUtils.isEmpty(orderPayNo) && TextUtils.isEmpty(orderNo)) {
            return;
        }
        Intent intent = new Intent(this, CarServePayResultActivity.class);
        intent.putExtra("orderPayNo", orderPayNo);
        intent.putExtra("orderNo", orderNo);
        startActivity(intent);
        closeDialog();
        finish();
    }

}