package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NumberUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilAmountAdapter;
import com.xxjy.jyyh.adapter.OilDiscountAdapter;
import com.xxjy.jyyh.adapter.OilMonthAdapter;
import com.xxjy.jyyh.adapter.TextWatcherAdapter;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.SPConstants;
import com.xxjy.jyyh.databinding.DialogOilAmountLayoutBinding;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.MonthCouponEntity;
import com.xxjy.jyyh.entity.MultiplePriceBean;
import com.xxjy.jyyh.entity.OilDefaultPriceEntity;
import com.xxjy.jyyh.entity.OilDiscountEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.util.ArrayList;
import java.util.List;

import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/22/21 1:25 PM
 * @project ElephantOil
 * @description:
 */
public class OilAmountDialog extends BottomSheetDialog {
    private Context mContext;
    private BaseActivity mActivity;
    private OilEntity.StationsBean mStationsBean;
    private List<OilEntity.StationsBean.OilPriceListBean> oilPriceListBeans;
    private int oilNoPosition, gunNoPosition;
    private BottomSheetBehavior mBehavior;
    private DialogOilAmountLayoutBinding mBinding;
    private List<OilDefaultPriceEntity.DefaultAmountBean> mAmountList = new ArrayList<>();
    private List<OilDiscountEntity> mDiscountList = new ArrayList<>();
    private MultiplePriceBean mMultiplePriceBean;
    private OilAmountAdapter mOilAmountAdapter;
    private OilDiscountAdapter mDiscountAdapter;
    private List<CouponBean> platformCoupons = new ArrayList<>();
    private List<CouponBean> businessCoupons = new ArrayList<>();
    private CouponBean mPlatCouponBean, mBusinessCouponBean;
    private String platId = "", businessId = "", businessAmount = "";//平台优惠券id， 商家优惠金额 每次刷新价格是要清空
    private OilHotDialog mOilHotDialog;
    private OilServiceDialog mOilServiceDialog;
    private List<MonthCouponEntity.MonthCouponTemplatesBean> mOilMonthList = new ArrayList<>();
    private OilMonthAdapter mOilMonthAdapter;
    private OilMonthRuleDialog mOilMonthRuleDialog;
    private Float totalMoney = 0f;//红包总额
    private Float mMonthAmount = 0f;
    private String mAmountReduce;
    private OilMonthTipDialog mOilMonthTipDialog;
    private String monthCouponId = ""; //月度红包id
    private String mExcludeType;//互斥规则
    private boolean isSelecedPlat = true; //是否选择了平台优惠券，用于判断月度红包是否弹框
    private String mMonthCouponImgUrl;
    private String mMonthCouponRule;
    private boolean isUseCoupon = true, isUseBusinessCoupon = true;

    public OilAmountDialog(Context context, BaseActivity activity, OilEntity.StationsBean stationsBean,
                           List<OilEntity.StationsBean.OilPriceListBean> oilPriceListBeans,
                           int oilNoPosition, int gunNoPosition) {
        super(context, R.style.bottom_sheet_dialog);
        this.mContext = context;
        this.mActivity = activity;
        this.mStationsBean = stationsBean;
        this.oilPriceListBeans = oilPriceListBeans;
        this.oilNoPosition = oilNoPosition;
        this.gunNoPosition = gunNoPosition;

        mBinding = DialogOilAmountLayoutBinding.bind(
                LayoutInflater.from(mContext).inflate(R.layout.dialog_oil_amount_layout, null));
        init();
        initData();
        initListener();
    }

    @Override
    public void dismiss() {
        super.dismiss();
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
        //快捷价格列表
        mBinding.amountRecyclerView.setLayoutManager(
                new GridLayoutManager(mContext, 3));
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
            //刷新价格互斥
            refreshData();
        });

        //月度红包
        mBinding.monthRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mOilMonthAdapter = new OilMonthAdapter(R.layout.adapter_oil_month, mOilMonthList);
        mBinding.monthRecyclerView.setAdapter(mOilMonthAdapter);
        mBinding.monthRedCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if (TextUtils.isEmpty(mBinding.amountEt.getText().toString().trim())) {
                MyToast.showInfo(mContext, "请输入加油金额后再次点击");
                mBinding.monthRedCheck.setChecked(false);
                mOilMonthAdapter.isSelected(false);
            } else if (Float.parseFloat(mBinding.amountEt.getText().toString().trim()) < mMonthAmount) {
                MyToast.showInfo(mContext, "加油金额低于" + mMonthAmount + "元暂不支持购买");
                mBinding.monthRedCheck.setChecked(false);
                mOilMonthAdapter.isSelected(false);
            } else {
                if (!b) {
                    if (!TextUtils.isEmpty(platId)) {
                        return;
                    }
                    if (mOilMonthTipDialog == null) {
                        mOilMonthTipDialog = new OilMonthTipDialog(mContext, mActivity, mMonthCouponImgUrl);
                        mOilMonthTipDialog.setOnItemClickedListener(new OilMonthTipDialog.OnItemClickedListener() {
                            @Override
                            public void onQueryClick() {
                                mBinding.monthRedCheck.setChecked(true);
                                mOilMonthAdapter.isSelected(true);
                                mBinding.monthRedCl.setSelected(true);
                                mOilMonthTipDialog.dismiss();
                                getMultiplePrice("", businessAmount, monthCouponId, isUseCoupon, isUseBusinessCoupon);
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
                                    mDiscountList.get(1).setPlatformDesc("请选择优惠券");
                                } else {
                                    mPlatCouponBean = null;
                                    platId = "";
                                    mExcludeType = "";
                                    mDiscountList.get(1).setPlatformDesc("暂无可用优惠券");
                                }
                                getMultiplePrice("", businessAmount, "", isUseCoupon, isUseBusinessCoupon);
                            }
                        });
                        mOilMonthTipDialog.show(mBinding.monthRedCheck);
                    } else {
                        mOilMonthTipDialog.show(mBinding.monthRedCheck);
                    }
                } else {
                    mBinding.monthRedCl.setSelected(b);
                    mOilMonthAdapter.isSelected(b);
                    //勾选了月度红包，取消平台优惠券
                    platId = "";
                    isUseCoupon = false;//取消最优组合
                    mExcludeType = "";
                    mPlatCouponBean = null;
//                    mDiscountList.get(1).setPlatformDesc("请选择优惠券");
                    mDiscountList.get(1).setPlatformDesc("-¥" + mAmountReduce);
                    mDiscountAdapter.notifyDataSetChanged();
                    getMultiplePrice("", businessAmount, b ? monthCouponId : "", isUseCoupon, isUseBusinessCoupon);
                }
            }
        });
        getMonthCoupon();//获取月度红包

        //优惠列表
        for (int i = 0; i < 4; i++) {
            mDiscountList.add(new OilDiscountEntity(0, "请选择加油金额", "请选择加油金额",
                    "请选择加油金额", 0, false, false, ""));
        }
        mBinding.discountRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext));
        mDiscountAdapter = new OilDiscountAdapter(R.layout.adapter_oil_discount, mDiscountList);
        mBinding.discountRecyclerView.setAdapter(mDiscountAdapter);

        mBinding.cancelIv.setOnClickListener(view -> dismiss());
        mBinding.backIv.setOnClickListener(view -> dismiss());
        mDiscountAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (platformCoupons.size() > 0 && position == 1 ||
                    businessCoupons.size() > 0 && position == 2 &&
                            mOnItemClickedListener != null) {
                UiUtils.canClickViewStateDelayed(view, 1000);
                mOnItemClickedListener.onOilDiscountClick(adapter, view, position,
                        mBinding.amountEt.getText().toString(),
                        String.valueOf(oilPriceListBeans
                                .get(oilNoPosition).getOilNo()), position == 1 ? platId : businessId);
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
                    getMultiplePrice(platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "", isUseCoupon, isUseBusinessCoupon);
                    break;
                case R.id.item_title_desc://直降优惠服务费
                    if (mOilServiceDialog == null) {
                        mOilServiceDialog = new OilServiceDialog(mContext, mActivity, mMultiplePriceBean);
                        mOilServiceDialog.show(view);
                    } else {
                        mOilServiceDialog.setData(mMultiplePriceBean);
                        mOilServiceDialog.show(view);
                    }
                    break;
            }
        });

        getDefaultPrice();//获取快捷价格
        getPlatformCoupon();//平台优惠券
        getBusinessCoupon();//商家优惠券
        getBalance();//余额
        //再次进来时需要刷新数据
        getMultiplePrice(platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "", isUseCoupon, isUseBusinessCoupon);
    }

    private void refreshMonthStauts(String amount) {
        if (TextUtils.isEmpty(amount)) {
            if (mBinding.monthRedCheck.isChecked()) {
                MyToast.showInfo(mContext, "请输入加油金额后再次点击");
                mBinding.monthRedCheck.setChecked(false);
                mBinding.monthRedCl.setSelected(false);
                mOilMonthAdapter.isSelected(false);
            }
        } else if (Float.parseFloat(amount) < mMonthAmount) {
            if (mBinding.monthRedCheck.isChecked()) {
                MyToast.showInfo(mContext, "加油金额低于" + mMonthAmount + "元暂不支持购买红包");
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

    /**
     * 获取月度红包  重要解释：月度红包只和平台优惠券互斥！！！！！！
     */
    private void getMonthCoupon() {
        RxHttp.postForm(ApiService.GET_MONTH_COUPON)
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .asResponse(MonthCouponEntity.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(monthCouponEntity -> {
                    if (monthCouponEntity != null && monthCouponEntity.getMonthCouponTemplates().size() > 0){
                        mBinding.monthRedCl.setVisibility(View.VISIBLE);

                        SpanUtils.with(mBinding.monthRedMoney)
                                .append("¥").setFontSize(13, true)
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
//                        SpanUtils.with(mBinding.monthRedDesc)
//                                .append(monthCouponEntity.getMonthCouponAmount() + "元")
//                                .setForegroundColor(mContext.getResources().getColor(R.color.color_1300))
//                                .append("享")
//                                .setForegroundColor(mContext.getResources().getColor(R.color.color_34))
//                                .append(totalMoney + "元")
//                                .setForegroundColor(mContext.getResources().getColor(R.color.color_1300))
//                                .append("立减红包，本单立减")
//                                .setForegroundColor(mContext.getResources().getColor(R.color.color_34))
//                                .append(mAmountReduce + "元")
//                                .setForegroundColor(mContext.getResources().getColor(R.color.color_1300))
//                                .create();
                        mBinding.monthRedDesc.setText(monthCouponEntity.getMonthCouponAmount() + "元享" +
                                totalMoney + "元立减红包，本单立减" + mAmountReduce + "元");

                        mOilMonthAdapter.setNewData(monthCouponEntity.getMonthCouponTemplates());
                    }
                }, throwable -> mBinding.monthRedCl.setVisibility(View.GONE));
    }

    private void initListener() {
        mBinding.amountEt.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mBinding.discountRecyclerView.setVisibility(View.VISIBLE);
                    //刷新快捷价格的选中状态
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
                } else {
                    mBinding.discountRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        KeyboardUtils.registerSoftInputChangedListener(mActivity, height -> {
            if (height == 0) {
                //刷新价格互斥
                refreshData();
            }
        });

        mBinding.cancelIv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.closeAll();
            }
        });
        mBinding.backIv.setOnClickListener(view -> dismiss());
        mBinding.createOrderTv.setOnClickListener(view -> {
            createOrder(view);
        });
        mBinding.hotIv.setOnClickListener(view -> {
            if (mOilHotDialog == null) {
                mOilHotDialog = new OilHotDialog(mContext, mActivity);
                mOilHotDialog.show(view);
            } else {
                mOilHotDialog.show(view);
            }

        });

        mBinding.monthRedRule.setOnClickListener(view -> {
            if (mOilMonthRuleDialog == null) {
                mOilMonthRuleDialog = new OilMonthRuleDialog(mContext, mActivity, mMonthCouponRule);
                mOilMonthRuleDialog.show(view);
            } else {
                mOilMonthRuleDialog.show(view);
            }
        });
    }

    private void createOrder(View view) {
        mBinding.loadingView.setVisibility(View.VISIBLE);
        RxHttp.postForm(ApiService.CREATE_ORDER)
                .add("amount", mBinding.amountEt.getText().toString())
                .add("payAmount", mMultiplePriceBean.getDuePrice())
                .add("usedBalance", mMultiplePriceBean.getBalancePrice())
                .add("gasId", mStationsBean.getGasId())
                .add("gunNo", String.valueOf(oilPriceListBeans.get(oilNoPosition).
                        getGunNos().get(gunNoPosition).getGunNo()))
                .add("oilNo", String.valueOf(oilPriceListBeans.get(oilNoPosition).getOilNo()))
                .add("oilName", oilPriceListBeans
                        .get(oilNoPosition).getOilName())
                .add("gasName", mStationsBean.getGasName())
                .add("priceGun", oilPriceListBeans.get(oilNoPosition).getPriceGun())
                .add("priceUnit", oilPriceListBeans.get(oilNoPosition).getPriceYfq())
                .add("oilType", oilPriceListBeans.get(oilNoPosition).getOilType() + "")
                .add("phone", SPUtils.getInstance().getString(SPConstants.MOBILE))
                .add("xxCouponId", platId)
                .add("czbCouponId", TextUtils.isEmpty(businessId) ? "" : businessId)
                .add("czbCouponAmount", businessAmount)
                .add("xxMonthCouponId", monthCouponId, mBinding.monthRedCheck.isChecked() && !TextUtils.isEmpty(monthCouponId))
                .add("xxMonthCouponAmount", mAmountReduce, mBinding.monthRedCheck.isChecked() && !TextUtils.isEmpty(mAmountReduce))
                .asResponse(String.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(orderId -> {
                            if (mOnItemClickedListener != null) {
                                UiUtils.canClickViewStateDelayed(view, 1000);
                                mOnItemClickedListener.onCreateOrder(view, orderId, mMultiplePriceBean.getDuePrice());
                            }
                        },
                        onError -> {
                            MyToast.showError(mContext, onError.getMessage());
                            mBinding.loadingView.setVisibility(View.GONE);
                        },
                        () -> mBinding.loadingView.setVisibility(View.GONE));
    }

    private void getPlatformCoupon() {
        //0真正可用 1已用 2过期  3时间未到 4 金额未达到
        if (TextUtils.isEmpty(mBinding.amountEt.getText())) return;
        RxHttp.postForm(ApiService.PLATFORM_COUPON)
                .add("canUse", "1")
                .add("rangeType", "2")
                .add("amount", mBinding.amountEt.getText())
                .add(Constants.OIL_NUMBER_ID, String.valueOf(oilPriceListBeans.get(oilNoPosition).getOilNo()))
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .asResponseList(CouponBean.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(couponBeans -> {
                    if (couponBeans != null && couponBeans.size() > 0) {
                        platformCoupons = couponBeans;
                        if (mBinding.monthRedCheck.isChecked()){//切换快捷价格时，如果勾选月度红包
                            refreshMonthStauts(mBinding.amountEt.getText().toString().trim());
                        }else {
                            mDiscountAdapter.getData().get(1).setPlatformDesc("请选择优惠券");
                        }
                    } else {
                        mDiscountAdapter.getData().get(1).setPlatformDesc("暂无可用优惠券");
                    }
//                    mDiscountAdapter.notifyDataSetChanged();
                });
    }

    private void getBusinessCoupon() {
        if (TextUtils.isEmpty(mBinding.amountEt.getText())) return;
        RxHttp.postForm(ApiService.BUSINESS_COUPON)
                .add("canUse", "1")
                .add("amount", mBinding.amountEt.getText())
                .add(Constants.OIL_NUMBER_ID, String.valueOf(oilPriceListBeans
                        .get(oilNoPosition).getOilNo()))
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .asResponseList(CouponBean.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(couponBeans -> {
                    if (couponBeans != null && couponBeans.size() > 0) {
                        businessCoupons = couponBeans;
                        mDiscountAdapter.getData().get(2).setBusinessDesc("请选择优惠券");
                    } else {
                        mDiscountAdapter.getData().get(2).setBusinessDesc("暂无可用优惠券");
                    }
//                    mDiscountAdapter.notifyDataSetChanged();
                });
    }

    private void getDefaultPrice() {
        RxHttp.postForm(ApiService.OIL_PRICE_DEFAULT)
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .add(Constants.OIL_NUMBER_ID, String.valueOf(oilPriceListBeans
                        .get(oilNoPosition).getOilNo()))
                .asResponse(OilDefaultPriceEntity.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(priceEntity -> {
                    List<OilDefaultPriceEntity.DefaultAmountBean> defaultAmount = priceEntity.getDefaultAmount();
                    mOilAmountAdapter.setNewData(defaultAmount);
                });
    }

    private void getBalance() {
        RxHttp.postForm(ApiService.QUERY_BALANCE)
                .asResponse(Float.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(balance -> {
                    if (balance > 0) {
                        mDiscountAdapter.getData().get(3).setBalance(balance);
                        mDiscountAdapter.getData().get(3).setUseBill(true);
                    } else {
                        mDiscountAdapter.getData().get(3).setBalance(0);
                        mDiscountAdapter.getData().get(3).setUseBill(false);
                    }
                    mDiscountAdapter.notifyDataSetChanged();
                });
    }

    /**
     * 获取互斥价格
     * 说明：商家优惠券和直降金额互斥
     */
    private void getMultiplePrice(String platId, String businessAmount, String monthCouponId, boolean isUseCoupon, boolean isUseBusinessCoupon) {
        mBinding.loadingView.setVisibility(View.VISIBLE);
        RxHttp.postForm(ApiService.OIL_MULTIPLE_PRICE)
                .add("amount", mBinding.amountEt.getText())
                .add(Constants.GAS_STATION_ID, mStationsBean.getGasId())
                .add(Constants.OIL_NUMBER_ID, String.valueOf(
                        oilPriceListBeans.get(oilNoPosition).getOilNo()))
                .add("canUseBill", mDiscountAdapter.getData().get(3).isUseBill() ? "1" : "0")
                .add("czbCouponAmount", TextUtils.isEmpty(businessAmount) ? "0" : businessAmount)
                .add("couponId", platId)
                .add("monthCouponId", monthCouponId)
                .add("canUseUserCoupon", isUseCoupon)
                .add("canUseCzbCoupon", isUseBusinessCoupon)
                .asResponse(MultiplePriceBean.class)
                .to(RxLife.toMain(mActivity))
                .subscribe(multiplePriceBean -> {
                            this.mMultiplePriceBean = multiplePriceBean;
                            //快捷价格优惠价格更新
                            List<OilDefaultPriceEntity.DefaultAmountBean> data = mOilAmountAdapter.getData();
                            for (int i = 0; i < data.size(); i++) {
                                if (Float.parseFloat(data.get(i).getAmount()) == Float.parseFloat(mBinding.amountEt.getText().toString())) {
                                    data.get(i).setAlltotalDiscountAmount(mMultiplePriceBean.getSumDiscountPrice());
                                }
                            }
                            mOilAmountAdapter.notifyDataSetChanged();
                            if (multiplePriceBean.getBestuserCoupon() != null || multiplePriceBean.getBestBusinessCoupon() != null){
                                mBinding.discountDesc.setVisibility(View.VISIBLE);
                            }else {
                                mBinding.discountDesc.setVisibility(View.GONE);
                            }
                            //直降金额 //服务费
                            if (Float.parseFloat(multiplePriceBean.getTotalDiscountAmount()) > 0) {
                                mDiscountAdapter.getData().get(0).setFallAmount(
                                        Float.parseFloat(multiplePriceBean.getTotalDiscountAmount()));
                            } else {
                                mDiscountAdapter.getData().get(0).setFallAmount(
                                        Float.parseFloat(multiplePriceBean.getTotalDiscountAmount()));
                                mDiscountAdapter.getData().get(0).setFallDesc("暂无优惠");
                            }
                            if (multiplePriceBean.getBestuserCoupon() != null){
                                this.platId = multiplePriceBean.getBestuserCoupon().getId();
                                mDiscountAdapter.getData().get(1).setPlatformDesc("-¥" + multiplePriceBean.getBestuserCoupon().getAmountReduce());
                            }
                            if (multiplePriceBean.getBestBusinessCoupon() != null){
                                businessId = multiplePriceBean.getBestBusinessCoupon().getId();
                                this.businessAmount = multiplePriceBean.getBestBusinessCoupon().getAmountReduce();
                                mDiscountAdapter.getData().get(2).setBusinessDesc("-¥" + multiplePriceBean.getBestBusinessCoupon().getAmountReduce());
                            }
                            //升数
                            mBinding.literTv.setText(String.format("约%sL",
                                    multiplePriceBean.getLiter()));
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

                        }, throwable -> mBinding.loadingView.setVisibility(View.GONE),
                        () -> mBinding.loadingView.setVisibility(View.GONE));
    }

    /**
     * @param couponBean
     * @param isPlat      是否是平台券
     * @param excludeType 0:不互斥; 1:与直降互斥; 2:与商家优惠券互斥; 3:与直降和商家优惠券互斥
     * @param isUseCoupon
     * @param isUseBusinessCoupon
     */
    public void setCouponInfo(CouponBean couponBean, boolean isPlat, String excludeType, boolean isUseCoupon, boolean isUseBusinessCoupon) {//优惠券互斥
        if (couponBean != null) {
            this.isUseCoupon = isUseCoupon;
            this.isUseBusinessCoupon = isUseBusinessCoupon;
            if (isPlat) {
                mPlatCouponBean = couponBean;
                platId = couponBean.getId();
                mExcludeType = excludeType;
                switch (mExcludeType) {
                    case "0"://不互斥
                        break;
                    case "1"://与直降互斥
                        mDiscountAdapter.getData().get(0).setFallDesc("暂无优惠");
                        mDiscountAdapter.getData().get(0).setService(false);
                        break;
                    case "2"://与商家优惠券互斥
                        if (businessCoupons.size() > 0) {
                            businessAmount = "";
                            businessId = "";
                            mBusinessCouponBean = null;
                            mDiscountList.get(2).setBusinessDesc("请选择优惠券");
                        } else {
                            businessAmount = "";
                            businessId = "";
                            mBusinessCouponBean = null;
                            mDiscountList.get(2).setBusinessDesc("暂无可用优惠券");
                        }
                        break;
                    case "3"://与直降和商家优惠券互斥
                        mDiscountAdapter.getData().get(0).setFallDesc("暂无优惠");
                        mDiscountAdapter.getData().get(0).setService(false);
                        if (businessCoupons.size() > 0) {
                            businessAmount = "";
                            businessId = "";
                            mBusinessCouponBean = null;
                            mDiscountList.get(2).setBusinessDesc("请选择优惠券");
                        } else {
                            businessAmount = "";
                            businessId = "";
                            mBusinessCouponBean = null;
                            mDiscountList.get(2).setBusinessDesc("暂无可用优惠券");
                        }
                        break;
                }
                mDiscountList.get(1).setPlatformDesc("-¥" + couponBean.getAmountReduce());
                //勾选了平台优惠券，取消月度红包
                mBinding.monthRedCheck.setChecked(false);
                mOilMonthAdapter.isSelected(false);
                mBinding.monthRedCl.setSelected(false);
            } else {
                mBusinessCouponBean = couponBean;
                businessId = couponBean.getId();
                businessAmount = couponBean.getAmountReduce();
                mDiscountList.get(2).setBusinessDesc("-¥" + couponBean.getAmountReduce());
                if ("2".equals(mExcludeType) || "3".equals(mExcludeType)) {//选择商家优惠券后与平台优惠券互斥
                    mPlatCouponBean = null;
                    platId = "";
                    mExcludeType = "";
                    mDiscountList.get(1).setPlatformDesc("请选择优惠券");
                }
            }
        } else {//不使用优惠券
            if (isPlat) {
                if (!mBinding.monthRedCheck.isChecked()){
                    platId = "";
                    mExcludeType = "";
                    mDiscountList.get(1).setPlatformDesc("请选择优惠券");
                }
            } else {
                businessAmount = "";
                businessId = "";
                mBusinessCouponBean = null;
                mDiscountList.get(2).setBusinessDesc("请选择优惠券");
            }
        }
        mDiscountAdapter.notifyDataSetChanged();
        getMultiplePrice(platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "", isUseCoupon, isUseBusinessCoupon);
    }

    private void refreshData() {
        //每次价格改变时，要清空这俩
        platId = "";
        businessId = "";
        businessAmount = "";
        mBusinessCouponBean = null;
        if (!mBinding.monthRedCheck.isChecked()){
            isUseCoupon = true;//价格改变时，设置成true
        }
        if (mOilMonthAdapter.getData().size() > 0) {
            refreshMonthStauts(mBinding.amountEt.getText().toString().trim());
        }
        getPlatformCoupon();
        getBusinessCoupon();
        //刷新价格互斥
        getMultiplePrice(platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "", isUseCoupon, isUseBusinessCoupon);
    }

    public interface OnItemClickedListener {

        void onOilDiscountClick(BaseQuickAdapter adapter, View view, int position,
                                String amount, String oilNo, String couponId);

        void onCreateOrder(View view, String orderId, String payAmount);

        void closeAll();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
