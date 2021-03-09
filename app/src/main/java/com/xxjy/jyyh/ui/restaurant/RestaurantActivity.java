package com.xxjy.jyyh.ui.restaurant;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.LifeDiscountAdapter;
import com.xxjy.jyyh.adapter.OilStationFlexAdapter;
import com.xxjy.jyyh.adapter.TextWatcherAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.constants.PayTypeConstants;
import com.xxjy.jyyh.constants.SPConstants;
import com.xxjy.jyyh.databinding.ActivityRestaurantBinding;
import com.xxjy.jyyh.dialog.NavigationDialog;
import com.xxjy.jyyh.dialog.OilCouponDialog;
import com.xxjy.jyyh.dialog.OilPayDialog;
import com.xxjy.jyyh.dialog.OilServiceDialog;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.MultiplePriceBean;
import com.xxjy.jyyh.entity.OilDiscountEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilPayTypeEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.ui.home.HomeViewModel;
import com.xxjy.jyyh.ui.pay.RefuelingPayResultActivity;
import com.xxjy.jyyh.ui.web.WeChatWebPayActivity;
import com.xxjy.jyyh.utils.InputFilterMinMax;
import com.xxjy.jyyh.utils.MoneyValueFilter;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.Util;
import com.xxjy.jyyh.utils.WXSdkManager;
import com.xxjy.jyyh.utils.locationmanger.MapIntentUtils;
import com.xxjy.jyyh.utils.pay.IPayListener;
import com.xxjy.jyyh.utils.pay.PayHelper;
import com.xxjy.jyyh.utils.pay.PayListenerUtils;
import com.xxjy.jyyh.utils.toastlib.Toasty;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RectangleIndicator;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends BindingActivity<ActivityRestaurantBinding, RestaurantViewModel> implements IPayListener {
    private HomeViewModel mHomeViewModel;
    private List<OilDiscountEntity> mDiscountList = new ArrayList<>(4);
    private LifeDiscountAdapter mDiscountAdapter;
    private String mGasId;
    private String mOilNo;
    private String mGunNo;
    private OilEntity.StationsBean mStationsBean;
    private OilEntity.StationsBean.OilPriceListBean mOilPriceListBean;
    List<OilEntity.StationsBean.OilPriceListBean> oilPriceListBeanList;
    private OilStationFlexAdapter mFlexAdapter;
    private List<OilEntity.StationsBean.CzbLabelsBean> mTagList = new ArrayList<>();
    private List<CouponBean> platformCoupons = new ArrayList<>();
    private List<CouponBean> businessCoupons = new ArrayList<>();
    private MultiplePriceBean mMultiplePriceBean;
    private String platId = "", businessId = "", businessAmount = "";//平台优惠券id， 商家优惠券id   商机优惠金额 每次刷新价格是要清空
    private OilServiceDialog mOilServiceDialog;
    private OilCouponDialog mOilCouponDialog;
    private OilPayDialog mOilPayDialog;
    private boolean isShouldAutoOpenWeb = false;    //标记是否应该自动打开浏览器进行支付
    //是否需要跳转支付确认页
    private boolean shouldJump = false;
    private PayOrderEntity mPayOrderEntity;
    private String phone;
    private float maxAmount = 50000f;

    /**
     * @param orderEntity 消息事件：支付后跳转支付确认页
     */
    @BusUtils.Bus(tag = EventConstants.EVENT_JUMP_PAY_QUERY, sticky = true)
    public void onEvent(PayOrderEntity orderEntity) {
        showJump(orderEntity);
    }

    private void showJump(PayOrderEntity orderEntity) {
        if (orderEntity == null) return;
        if (shouldJump) {
            shouldJump = false;
//            PayQueryActivity.openPayQueryActivity(this,
//                    orderEntity.getOrderPayNo(), orderEntity.getOrderNo());
            RefuelingPayResultActivity.openPayResultPage(this,
                    orderEntity.getOrderNo(), orderEntity.getOrderPayNo(), true);
            closeDialog();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshData();
        showJump(mPayOrderEntity);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusUtils.removeSticky(EventConstants.EVENT_JUMP_PAY_QUERY);
        PayListenerUtils.getInstance().removeListener(this);
    }

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, false);
        BusUtils.register(this);
        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        //优惠列表
        for (int i = 0; i < 4; i++) {
            mDiscountList.add(new OilDiscountEntity(0, "请输入消费金额", "请输入消费金额",
                    "请输入消费金额", 0, false, false));
        }
        mBinding.discountRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDiscountAdapter = new LifeDiscountAdapter(R.layout.adapter_oil_discount, mDiscountList);
        mBinding.discountRecyclerView.setAdapter(mDiscountAdapter);
        mGasId = getIntent().getStringExtra(Constants.GAS_STATION_ID);
        mViewModel.getStoreInfo(mGasId);
        mBinding.amountEt.setFilters(new InputFilter[]{new MoneyValueFilter(), new InputFilterMinMax(0d, 50000d)});
        //标签列表
//        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
//        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
//        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
//        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
//        mBinding.tagRecyclerView.setLayoutManager(flexboxLayoutManager);
//        mFlexAdapter = new OilStationFlexAdapter(R.layout.adapter_oil_detail_tag, mTagList);
//        mBinding.tagRecyclerView.setAdapter(mFlexAdapter);
        getBalance();//余额

    }

    private void getMultiplePrice(String platId, String businessAmount) {
        mViewModel.getMultiplePrice(platId, mBinding.amountEt.getText().toString(), mGasId, mOilNo, mDiscountAdapter.getData().get(3).isUseBill(), businessAmount);
    }

    private void getBalance() {
        mViewModel.getBalance();
    }

    private void getBusinessCoupon() {
        if (TextUtils.isEmpty(mBinding.amountEt.getText())) return;
        mViewModel.getBusinessCoupon(mBinding.amountEt.getText().toString(), mGasId, mOilNo);
    }

    private void getPlatformCoupon() {
        if (TextUtils.isEmpty(mBinding.amountEt.getText())) return;
        mViewModel.getPlatformCoupon(mBinding.amountEt.getText().toString(), mGasId);
    }


    private void refreshData() {
        //每次价格改变时，要清空这俩
        platId = "";
        businessAmount = "";
        businessId = "";

        getPlatformCoupon();
        getBusinessCoupon();
        //刷新价格互斥
        getMultiplePrice(platId, businessAmount);
        getBalance();//余额
    }

    @Override
    protected void initListener() {
        ClickUtils.applySingleDebouncing(mBinding.createOrderTv, this::onViewClicked);
        mBinding.createOrderTv.setOnClickListener(this::onViewClicked);
        mBinding.backIv.setOnClickListener(this::onViewClicked);
        mBinding.navigationRl.setOnClickListener(this::onViewClicked);
        mBinding.phoneView.setOnClickListener(this::onViewClicked);
        mBinding.amountEt.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBinding.amountEt.addTextChangedListener(new TextWatcher() {
            boolean hint;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    // no text, hint is visible
                    hint = true;
                    mBinding.amountEt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    // no hint, text is visible
                    hint = false;
                    mBinding.amountEt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mBinding.moneyTag.setVisibility(s.toString().trim().length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            if (height == 0) {
                //刷新价格互斥
                refreshData();
            }
        });
        mDiscountAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (platformCoupons.size() > 0 && position == 1 ||
                    businessCoupons.size() > 0 && position == 2) {
                UiUtils.canClickViewStateDelayed(view, 1000);
                showCouponDialog(mStationsBean, mBinding.amountEt.getText().toString(), "0", "0", position == 1);
            }
        });

        mDiscountAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.item_switch_tv://是否使用余额
                    List<OilDiscountEntity> data = adapter.getData();
                    if (data.get(position).isUseBill()) {
                        data.get(position).setUseBill(false);
                    } else {
                        if (data.get(position).getBalance() > 0) {
                            data.get(position).setUseBill(true);
                        }
                    }
                    //刷新互斥价格
                    getMultiplePrice(platId, businessAmount);
                    break;
                case R.id.item_title_desc://直降优惠服务费
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
    }

    private void showPayDialog(OilEntity.StationsBean stationsBean, List<OilEntity.StationsBean.OilPriceListBean> oilPriceListBean, int oilNoPosition,
                               int gunNoPosition, String orderId, String payAmount) {
        //支付dialog
        mOilPayDialog = new OilPayDialog(this, this, stationsBean, oilPriceListBean,
                oilNoPosition, gunNoPosition, orderId, payAmount);

        mOilPayDialog.setOnItemClickedListener(new OilPayDialog.OnItemClickedListener() {
            @Override
            public void onOilPayTypeClick(BaseQuickAdapter adapter, View view, int position) {
                List<OilPayTypeEntity> data = adapter.getData();
                for (OilPayTypeEntity item : data) {
                    item.setSelect(false);
                }
                data.get(position).setSelect(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCloseAllClick() {
                closeDialog();
            }

            @Override
            public void onPayOrderClick(String payType, String orderId, String payAmount) {
                mHomeViewModel.payOrder(payType, orderId, payAmount);
            }
        });

        mOilPayDialog.show();
    }

    private void showCouponDialog(OilEntity.StationsBean stationsBean, String amount,
                                  String oilNoPosition, String gunNoPosition, boolean isPlat) {
        //优惠券dialog
        mOilCouponDialog = new OilCouponDialog(this, this, amount, stationsBean,
                Integer.valueOf(oilNoPosition), Integer.valueOf(gunNoPosition), isPlat);
        mOilCouponDialog.setOnItemClickedListener(new OilCouponDialog.OnItemClickedListener() {
            @Override
            public void onOilCouponClick(BaseQuickAdapter adapter, View view, int position, boolean isPlat) {
                List<CouponBean> data = adapter.getData();
                setCouponInfo(data.get(position), isPlat);
                mOilCouponDialog.dismiss();
            }

            @Override
            public void onNoCouponClick(boolean isPlat) {
                setCouponInfo(null, isPlat);
                mOilCouponDialog.dismiss();
            }
        });

        mOilCouponDialog.show();
    }

    public void setCouponInfo(CouponBean couponBean, boolean isPlat) {
        if (couponBean != null) {
            if (isPlat) {
                platId = couponBean.getId();
                mDiscountList.get(1).setPlatformDesc("-¥" + couponBean.getAmountReduce());
            } else {
                businessId = couponBean.getId();
                businessAmount = couponBean.getAmountReduce();
                mDiscountList.get(2).setBusinessDesc("-¥" + couponBean.getAmountReduce());
            }
        } else {
            if (isPlat) {
                platId = "";
                mDiscountList.get(1).setPlatformDesc("请选择优惠券");
            } else {
                businessAmount = "";
                businessId = "";
                mDiscountList.get(2).setBusinessDesc("请选择优惠券");
            }
        }
        mDiscountAdapter.notifyDataSetChanged();
        getMultiplePrice(platId, businessAmount);
    }

    private void closeDialog() {
        if (mOilCouponDialog != null) {
            mOilCouponDialog.dismiss();
            mOilCouponDialog = null;
        }
        if (mOilPayDialog != null) {
            mOilPayDialog.dismiss();
            mOilPayDialog = null;
        }
        //关掉以后重新刷新数据,否则再次打开时上下选中不一致
//        mViewModel.getStoreInfo(mGasId);
        refreshData();
    }

    private void jumpToPayResultAct(String orderPayNo, String orderNo) {
        if (TextUtils.isEmpty(orderPayNo) && TextUtils.isEmpty(orderNo)) {
            return;
        }
//        Intent intent = new Intent(this, RefuelingPayResultActivity.class);
//        intent.putExtra("orderPayNo", orderPayNo);
//        intent.putExtra("orderNo", orderNo);
//        startActivity(intent);
        RefuelingPayResultActivity.openPayResultPage(this,
                orderNo, orderPayNo, true);
        closeDialog();
    }

    private void addTagView(String content, QMUIFloatLayout floatLayout) {
        TextView textView = new TextView(this);
        int textViewPadding = QMUIDisplayHelper.dp2px(this, 4);
        int textViewPadding2 = QMUIDisplayHelper.dp2px(this, 2);
        textView.setPadding(textViewPadding, textViewPadding2, textViewPadding, textViewPadding2);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
        textView.setTextColor(ContextCompat.getColor(this, R.color.color_6431));
        textView.setBackgroundResource(R.drawable.shape_soild_station_tag_red);

        textView.setText(content);
        floatLayout.addView(textView);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.create_order_tv:
                if (Float.parseFloat(mBinding.amountEt.getText().toString().trim()) > maxAmount) {
                    showToastWarning("单笔消费金额不可超过" + maxAmount + "元");
                    return;
                }
                mViewModel.createOrder(mBinding.amountEt.getText().toString(),
                        mMultiplePriceBean.getDuePrice(),
                        mMultiplePriceBean.getBalancePrice(),
                        mGasId,
                        mGunNo,
                        mOilNo,
                        mOilPriceListBean.getOilName(),
                        mStationsBean.getGasName(),
                        mOilPriceListBean.getPriceGun(),
                        mOilPriceListBean.getPriceYfq(),
                        mOilPriceListBean.getOilType() + "",
                        SPUtils.getInstance().getString(SPConstants.MOBILE),
                        platId,
                        businessId,
                        businessAmount
                );
                break;
            case R.id.navigation_rl:
                if (MapIntentUtils.isPhoneHasMapNavigation()) {
                    NavigationDialog navigationDialog = new NavigationDialog(this,
                            mStationsBean.getStationLatitude(), mStationsBean.getStationLongitude(),
                            mStationsBean.getGasName());
                    navigationDialog.show();
                } else {
                    showToastWarning("您当前未安装地图软件，请先安装");
                }
                break;
            case R.id.phone_view:
                Util.toDialPhoneAct(this, phone);
                break;
            case R.id.back_iv:
                finish();
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
        mViewModel.storeLiveData.observe(this, data -> {
            if (data != null) {
                mStationsBean = data;
                oilPriceListBeanList = mStationsBean.getOilPriceList();
                if (mStationsBean.getOilPriceList() != null && mStationsBean.getOilPriceList().size() > 0) {
                    mOilPriceListBean = mStationsBean.getOilPriceList().get(0);
                    mOilNo = String.valueOf(mStationsBean.getOilPriceList().get(0).getOilNo());
                    getBusinessCoupon();//商家优惠券
                    //再次进来时需要刷新数据
                    getMultiplePrice(platId, businessAmount);
                    if (mStationsBean.getOilPriceList().get(0).getGunNos().size() > 0) {
                        mGunNo = String.valueOf(mStationsBean.getOilPriceList().get(0).getGunNos().get(0).getGunNo());
                    }
                }
                mBinding.hotelName.setText(data.getGasName());
                mBinding.addressTv.setText(data.getGasAddress());
                mBinding.distanceTv.setText("距离您" + data.getDistance() + "km");
                mBinding.floatLayout.removeAllViews();
                if (mStationsBean.getCzbLabels() != null && mStationsBean.getCzbLabels().size() > 0) {
//                    mTagList = mStationsBean.getCzbLabels();
//                    mFlexAdapter.setNewData(mTagList);
//                    mBinding.tagRecyclerView.setVisibility(View.VISIBLE);
                    for (OilEntity.StationsBean.CzbLabelsBean lab :
                            mStationsBean.getCzbLabels()) {
                        addTagView(lab.getTagName(),
                                mBinding.floatLayout);
                    }
                    mBinding.floatLayout.setVisibility(View.VISIBLE);
                } else {
                    mBinding.floatLayout.setVisibility(View.INVISIBLE);

                }

                if (data.getPhoneTimeMap() != null) {
                    phone = data.getPhoneTimeMap().getPhone();
                    mBinding.phoneView.setText("联系电话：" + data.getPhoneTimeMap().getPhone());
                    mBinding.timeView.setText(data.getPhoneTimeMap().getHours());
                }

                mBinding.topBanner.setVisibility(View.VISIBLE);
                //banner
                mBinding.topBanner.setAdapter(new BannerImageAdapter<String>(data.getTopImgList()) {
                    @Override
                    public void onBindView(BannerImageHolder holder, String url, int position, int size) {
                        Glide.with(holder.imageView)
                                .load(url)
                                .apply(new RequestOptions()
                                        .error(R.drawable.default_img_bg))
                                .into(holder.imageView);
                    }
                }).addBannerLifecycleObserver(this)
                        .setIndicator(new RectangleIndicator(this))
                        .setIndicatorMargins(new IndicatorConfig.Margins(0, 0, 0, SizeUtils.dp2px(25)));

                getPlatformCoupon();//平台优惠券

            }
        });


        mViewModel.balanceLiveData.observe(this, balance -> {
            if (balance > 0) {
                mDiscountAdapter.getData().get(3).setBalance(balance);
                mDiscountAdapter.getData().get(3).setUseBill(true);
            } else {
                mDiscountAdapter.getData().get(3).setBalance(0);
                mDiscountAdapter.getData().get(3).setUseBill(false);
            }
            mDiscountAdapter.notifyDataSetChanged();
        });

        mViewModel.multiplePriceLiveData.observe(this, multiplePriceBean -> {
            this.mMultiplePriceBean = multiplePriceBean;
            //直降金额
            if (Float.parseFloat(multiplePriceBean.getTotalDiscountAmount()) > 0) {
                mDiscountAdapter.getData().get(0).setFallAmount(
                        Float.parseFloat(multiplePriceBean.getTotalDiscountAmount()));
            } else {
                mDiscountAdapter.getData().get(0).setFallAmount(
                        Float.parseFloat(multiplePriceBean.getTotalDiscountAmount()));
                mDiscountAdapter.getData().get(0).setFallDesc("暂无优惠");
            }
            //升数
//            mBinding.literTv.setText(String.format("约%sL",
//                    multiplePriceBean.getLiter()));
            //实付金额
            mBinding.currentPriceTv.setText(String.format("实付：¥%s",
                    multiplePriceBean.getDuePrice()));
            //优惠金额
            mBinding.discountPriceTv.setText(String.format("已优惠：¥%s",
                    multiplePriceBean.getSumDiscountPrice()));
            //服务费
            if (!TextUtils.isEmpty(multiplePriceBean.getServiceChargeAmount()) &&
                    Float.parseFloat(multiplePriceBean.getServiceChargeAmount()) > 0) {
                mDiscountList.get(0).setService(true);
            } else {
                mDiscountList.get(0).setService(false);
            }
            //抵扣余额
            mDiscountAdapter.getData().get(3).setBalanceDiscount(
                    Float.parseFloat(multiplePriceBean.getBalancePrice()));

            mDiscountAdapter.notifyDataSetChanged();

            mBinding.createOrderTv.setEnabled(
                    Float.parseFloat(multiplePriceBean.getDuePrice()) > 0 ||
                            Float.parseFloat(multiplePriceBean.getSumDiscountPrice()) > 0);

        });

        mViewModel.platformLiveData.observe(this, couponBeans -> {
            if (couponBeans != null && couponBeans.size() > 0) {
                platformCoupons = couponBeans;
                mDiscountAdapter.getData().get(1).setPlatformDesc("请选择优惠券");
            } else {
                mDiscountAdapter.getData().get(1).setPlatformDesc("暂无可用优惠券");
            }
            mDiscountAdapter.notifyDataSetChanged();
        });

        mViewModel.businessCouponLiveData.observe(this, couponBeans -> {
            if (couponBeans != null && couponBeans.size() > 0) {
                businessCoupons = couponBeans;
                mDiscountAdapter.getData().get(2).setBusinessDesc("请选择优惠券");
            } else {
                mDiscountAdapter.getData().get(2).setBusinessDesc("暂无可用优惠券");
            }
            mDiscountAdapter.notifyDataSetChanged();
        });
        mViewModel.createOrderLiveData.observe(this, orderId -> {
            showPayDialog(mStationsBean, oilPriceListBeanList, 0, 0, orderId, mMultiplePriceBean.getDuePrice());
        });
        //支付结果回调
        mHomeViewModel.payOrderLiveData.observe(this, payOrderEntity -> {

            if (payOrderEntity.getResult() == 0) {//支付未完成
                switch (payOrderEntity.getPayType()) {
                    case PayTypeConstants.PAY_TYPE_WEIXIN://微信H5
                        WeChatWebPayActivity.openWebPayAct(this, payOrderEntity.getUrl());
                        shouldJump = true;
                        break;
                    case PayTypeConstants.PAY_TYPE_WEIXIN_APP://微信原生
//                        WeChatWebPayActivity.openWebPayAct(this, payOrderEntity.getUrl());
                        PayListenerUtils.getInstance().addListener(this);
                        PayHelper.getInstance().WexPay(payOrderEntity.getPayParams());

                        break;
                    case PayTypeConstants.PAY_TYPE_WEIXIN_XCX://微信小程序
                        WXSdkManager.newInstance().useWXLaunchMiniProgramToPay(this, payOrderEntity.getOrderNo());
                        shouldJump = true;
                        break;
                    case PayTypeConstants.PAY_TYPE_ZHIFUBAO://支付宝H5
                        boolean urlCanUse = UiUtils.checkZhifubaoSdkCanPayUrl(this,
                                payOrderEntity.getUrl(),
                                h5PayResultModel -> {//直接跳转支付宝
                                    jumpToPayResultAct(payOrderEntity.getOrderPayNo(),
                                            payOrderEntity.getOrderNo());
                                });
                        if (!urlCanUse) {//外部浏览器打开
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

            } else if (payOrderEntity.getResult() == 1) {//支付成功
                jumpToPayResultAct(payOrderEntity.getOrderPayNo(), payOrderEntity.getOrderNo());
            } else {
                showToastWarning(payOrderEntity.getMsg());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */

        if (data == null) {
            return;
        }
        String str = data.getExtras().getString("pay_result");
        if (!TextUtils.isEmpty(str)) {
            if (str.equalsIgnoreCase("success")) {
                //如果想对结果数据校验确认，直接去商户后台查询交易结果，
                //校验支付结果需要用到的参数有sign、data、mode(测试或生产)，sign和data可以在result_data获取到
                /**
                 * result_data参数说明：
                 * sign —— 签名后做Base64的数据
                 * data —— 用于签名的原始数据
                 *      data中原始数据结构：
                 *      pay_result —— 支付结果success，fail，cancel
                 *      tn —— 订单号
                 */
//            msg = "云闪付支付成功";
                PayListenerUtils.getInstance().addSuccess();
            } else if (str.equalsIgnoreCase("fail")) {
//            msg = "云闪付支付失败！";
                PayListenerUtils.getInstance().addFail();

            } else if (str.equalsIgnoreCase("cancel")) {
//            msg = "用户取消了云闪付支付";
                PayListenerUtils.getInstance().addCancel();

            }
        }

    }

    @Override
    public void onSuccess() {
        RefuelingPayResultActivity.openPayResultPage(this,
                mPayOrderEntity.getOrderNo(), mPayOrderEntity.getOrderPayNo(), true, true);
        PayListenerUtils.getInstance().removeListener(this);
        closeDialog();
    }

    @Override
    public void onFail() {
        RefuelingPayResultActivity.openPayResultPage(this,
                mPayOrderEntity.getOrderNo(), mPayOrderEntity.getOrderPayNo(), true, true);
        PayListenerUtils.getInstance().removeListener(this);
        closeDialog();
    }

    @Override
    public void onCancel() {
        Toasty.info(this, "支付取消").show();
        PayListenerUtils.getInstance().removeListener(this);
        closeDialog();
    }
}