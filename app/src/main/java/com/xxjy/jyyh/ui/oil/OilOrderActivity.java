package com.xxjy.jyyh.ui.oil;

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
import android.view.View;

import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NumberUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilAmountAdapter;
import com.xxjy.jyyh.adapter.OilDiscountAdapter;
import com.xxjy.jyyh.adapter.OilGunAdapter;
import com.xxjy.jyyh.adapter.OilMonthAdapter;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.adapter.TextWatcherAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.ApiService;
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
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.MonthCouponEntity;
import com.xxjy.jyyh.entity.MultiplePriceBean;
import com.xxjy.jyyh.entity.OilDefaultPriceEntity;
import com.xxjy.jyyh.entity.OilDiscountEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilPayTypeEntity;
import com.xxjy.jyyh.entity.OilTypeEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.ui.home.HomeViewModel;
import com.xxjy.jyyh.ui.pay.RefuelingPayResultActivity;
import com.xxjy.jyyh.ui.web.WeChatWebPayActivity;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.WXSdkManager;
import com.xxjy.jyyh.utils.pay.IPayListener;
import com.xxjy.jyyh.utils.pay.PayHelper;
import com.xxjy.jyyh.utils.pay.PayListenerUtils;
import com.xxjy.jyyh.utils.toastlib.MyToast;
import com.xxjy.jyyh.utils.toastlib.Toasty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rxhttp.RxHttp;

public class OilOrderActivity extends BindingActivity<ActivityOilOrderBinding, OilViewModel> implements IPayListener {

    private OilEntity.StationsBean mStationsBean;
    private List<OilEntity.StationsBean.OilPriceListBean> mOilPriceListBean;
    private int mOilNoPosition;
    private int mGunNoPosition;
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
    private Float totalMoney = 0f;//红包总额
    private Float mMonthAmount = 0f;
    private String mAmountReduce;
    private OilMonthTipDialog mOilMonthTipDialog;
    private String monthCouponId = ""; //月度红包id
    private String mExcludeType;//互斥规则
    private boolean isSelecedPlat = true; //是否选择了平台优惠券，用于判断月度红包是否弹框
    private String mMonthCouponImgUrl;
    private String mMonthCouponRule;
    private CouponBean mPlatCouponBean, mBusinessCouponBean;
    private List<CouponBean> platformCoupons = new ArrayList<>();
    private List<CouponBean> businessCoupons = new ArrayList<>();
    private String platId = "", businessAmount = "";//平台优惠券id， 商家优惠金额 每次刷新价格是要清空
    private OilHotDialog mOilHotDialog;
    private OilServiceDialog mOilServiceDialog;
    private MultiplePriceBean mMultiplePriceBean;
    private OilCouponDialog mOilCouponDialog;
    private OilPayDialog mOilPayDialog;
    private HomeViewModel mHomeViewModel;
    //是否需要跳转支付确认页
    private boolean shouldJump = false;
    private PayOrderEntity mPayOrderEntity;
    private boolean isShouldAutoOpenWeb = false;    //标记是否应该自动打开浏览器进行支付


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
            RefuelingPayResultActivity.openPayResultPage(this,
                    orderEntity.getOrderNo(), orderEntity.getOrderPayNo());
            closeDialog();
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

        mStationsBean = (OilEntity.StationsBean) getIntent().getSerializableExtra("stationsBean");
        mOilNo = getIntent().getIntExtra("oilNo", 0);
        mGunNo = getIntent().getIntExtra("gunNo", 0);

        //进来时，设置油站选中数据
        for (int i = 0; i < mStationsBean.getOilPriceList().size(); i++) {
            mStationsBean.getOilPriceList().get(i).setSelected(false);
            if (String.valueOf(mStationsBean.getOilPriceList().get(i).getOilNo()).equals(String.valueOf(mOilNo))) {
                mOilPriceListBean = mStationsBean.getOilPriceList();
                mOilNoPosition = i;
                mStationsBean.getOilPriceList().get(i).setSelected(true);
                mBinding.oilNameTv.setText(mStationsBean.getGasName());
                mBinding.oilAddressTv.setText(mStationsBean.getGasAddress());
                mBinding.oilPriceTv.setText("¥" + mOilPriceListBean.get(mOilNoPosition).getPriceYfq() + "起");
                mBinding.oilStationPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                mBinding.oilStationPriceTv.setText("油站价 ¥" + mOilPriceListBean.get(mOilNoPosition).getPriceGun());
                mBinding.oilNumTv.setText(mOilPriceListBean.get(mOilNoPosition).getOilName());
                for (int k = 0; k < mStationsBean.getOilPriceList().get(i).getGunNos().size(); k++) {
                    mStationsBean.getOilPriceList().get(i).getGunNos().get(k).setSelected(false);
                    if (String.valueOf(mStationsBean.getOilPriceList().get(i).getGunNos().get(k).getGunNo()).equals(String.valueOf(mGunNo))) {
                        mGunNoPosition = k;
                        mStationsBean.getOilPriceList().get(i).getGunNos().get(k).setSelected(true);
                        mBinding.oilGunTv.setText(mOilPriceListBean.get(mOilNoPosition).getGunNos().get(mGunNoPosition).getGunNo() + "号枪");
                    }
                }
            }
        }

        //快捷价格列表
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
            //刷新价格互斥
            refreshData();
        });

        //月度红包
        mBinding.monthRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mOilMonthAdapter = new OilMonthAdapter(R.layout.adapter_oil_month, mOilMonthList);
        mBinding.monthRecyclerView.setAdapter(mOilMonthAdapter);
        mBinding.monthRedCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if (TextUtils.isEmpty(mBinding.amountEt.getText().toString().trim())) {
                MyToast.showInfo(this, "请输入加油金额后再次点击");
                mBinding.monthRedCheck.setChecked(false);
                mOilMonthAdapter.isSelected(false);
            } else if (Float.parseFloat(mBinding.amountEt.getText().toString().trim()) < mMonthAmount) {
                MyToast.showInfo(this, "加油金额低于" + mMonthAmount + "元暂不支持购买");
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
                                mViewModel.getMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                                        mOilPriceListBean.get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(3).isUseBill() ? "1" : "0",
                                        "", businessAmount, monthCouponId);
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
                                mViewModel.getMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                                        mOilPriceListBean.get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(3).isUseBill() ? "1" : "0",
                                        "", businessAmount, "");
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
                    mExcludeType = "";
                    mPlatCouponBean = null;
//                    mDiscountList.get(1).setPlatformDesc("请选择优惠券");
                    mDiscountList.get(1).setPlatformDesc("-¥" + mAmountReduce);
                    mDiscountAdapter.notifyDataSetChanged();
                    mViewModel.getMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                            mOilPriceListBean.get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(3).isUseBill() ? "1" : "0",
                            "", businessAmount, b ? monthCouponId : "");
                }
            }
        });
        mViewModel.getMonthCoupon(mStationsBean.getGasId());//获取月度红包

        //优惠列表
        for (int i = 0; i < 4; i++) {
            mDiscountList.add(new OilDiscountEntity(0, "请选择加油金额", "请选择加油金额",
                    "请选择加油金额", 0, false, false));
        }
        mBinding.discountRecyclerView.setLayoutManager(
                new LinearLayoutManager(this));
        mDiscountAdapter = new OilDiscountAdapter(R.layout.adapter_oil_discount, mDiscountList);
        mBinding.discountRecyclerView.setAdapter(mDiscountAdapter);

        mDiscountAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (platformCoupons.size() > 0 && position == 1 || businessCoupons.size() > 0 && position == 2) {
                UiUtils.canClickViewStateDelayed(view, 1000);
                showCouponDialog(mStationsBean, mBinding.amountEt.getText().toString(), mOilNoPosition,
                        mGunNoPosition, String.valueOf(mOilPriceListBean.get(mOilNoPosition).getOilNo()), position == 1);
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
                    mViewModel.getMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                            mOilPriceListBean.get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(3).isUseBill() ? "1" : "0",
                            platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "");
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

        mViewModel.getDefaultPrice(mStationsBean.getGasId(), String.valueOf(mOilPriceListBean
                .get(mOilNoPosition).getOilNo()));//获取快捷价格
        mViewModel.getPlatformCoupon(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(),
                String.valueOf(mOilPriceListBean.get(mOilNoPosition).getOilNo()));//平台优惠券
        mViewModel.getBusinessCoupon(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(),
                String.valueOf(mOilPriceListBean
                        .get(mOilNoPosition).getOilNo()));//商家优惠券
        mViewModel.getBalance();//余额
        //再次进来时需要刷新数据
//        mViewModel.getMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
//                mOilPriceListBean.get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(3).isUseBill() ? "1" : "0",
//                platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "");
    }

    private void refreshData() {
        //每次价格改变时，要清空这俩
        platId = "";
        businessAmount = "";
        mBusinessCouponBean = null;
        if (mOilMonthAdapter.getData().size() > 0) {
            refreshMonthStauts(mBinding.amountEt.getText().toString().trim());
        }
        mViewModel.getPlatformCoupon(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(),
                String.valueOf(mOilPriceListBean.get(mOilNoPosition).getOilNo()));
        mViewModel.getBusinessCoupon(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(),
                String.valueOf(mOilPriceListBean
                        .get(mOilNoPosition).getOilNo()));//商家优惠券
        //刷新价格互斥
        mViewModel.getMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                mOilPriceListBean.get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(3).isUseBill() ? "1" : "0",
                platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "");
    }

    private void refreshMonthStauts(String amount) {
        if (TextUtils.isEmpty(amount)) {
            if (mBinding.monthRedCheck.isChecked()) {
                MyToast.showInfo(this, "请输入加油金额后再次点击");
                mBinding.monthRedCheck.setChecked(false);
                mBinding.monthRedCl.setSelected(false);
                mOilMonthAdapter.isSelected(false);
            }
        } else if (Float.parseFloat(amount) < mMonthAmount) {
            if (mBinding.monthRedCheck.isChecked()) {
                MyToast.showInfo(this, "加油金额低于" + mMonthAmount + "元暂不支持购买红包");
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
        //油号dialog
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
                for (int i = 0; i < mStationsBean.getOilPriceList().size(); i++) {
                    for (int j = 0; j < mStationsBean.getOilPriceList().get(i).getGunNos().size(); j++) {
                        mStationsBean.getOilPriceList().get(i).getGunNos().get(j).setSelected(false);
                    }
                }
                List<OilEntity.StationsBean.OilPriceListBean> oilPriceList = data.get(position).getOilPriceList();
                for (int i = 0; i < oilPriceList.size(); i++) {
                    oilPriceList.get(i).setSelected(false);
                }
                oilPriceList.get(0).setSelected(true);
                oilNumAdapter.setNewData(oilPriceList);
                oilGunAdapter.setNewData(oilPriceList.get(mGunNoPosition).getGunNos());
            }

            @Override
            public void onOilNumClick(BaseQuickAdapter adapter, View view, int position, OilGunAdapter oilGunAdapter) {
                List<OilEntity.StationsBean.OilPriceListBean> data = adapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setSelected(false);
                }
                data.get(position).setSelected(true);
                adapter.notifyDataSetChanged();
                for (int i = 0; i < mStationsBean.getOilPriceList().size(); i++) {
                    for (int j = 0; j < mStationsBean.getOilPriceList().get(i).getGunNos().size(); j++) {
                        mStationsBean.getOilPriceList().get(i).getGunNos().get(j).setSelected(false);
                    }
                }
                oilGunAdapter.setNewData(data.get(position).getGunNos());
            }

            @Override
            public void onOilGunClick(BaseQuickAdapter adapter, View view, int position) {
                List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> data = adapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setSelected(false);
                }
                data.get(position).setSelected(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onQuickClick(View view, OilNumAdapter oilNumAdapter, OilGunAdapter oilGunAdapter) {
                for (int i = 0; i < mStationsBean.getOilPriceList().size(); i++) {
                    if (mStationsBean.getOilPriceList().get(i).isSelected()) {
                        mOilPriceListBean = mStationsBean.getOilPriceList();
                        mOilNoPosition = i;
                        for (int j = 0; j < mStationsBean.getOilPriceList().get(i).getGunNos().size(); j++) {
                            if (mStationsBean.getOilPriceList().get(i).getGunNos().get(j).isSelected()) {
                                mGunNoPosition = j;
                            }
                        }
                    }
                }

                mBinding.oilPriceTv.setText("¥" + mOilPriceListBean.get(mOilNoPosition).getPriceYfq() + "起");
                mBinding.oilStationPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                mBinding.oilStationPriceTv.setText("油站价 ¥" + mOilPriceListBean.get(mOilNoPosition).getPriceGun());
                mBinding.oilNumTv.setText(mStationsBean.getOilPriceList().get(mOilNoPosition).getOilName());
                mBinding.oilGunTv.setText(mStationsBean.getOilPriceList().get(mOilNoPosition).getGunNos()
                        .get(mGunNoPosition).getGunNo() + "号枪");

                closeDialog();
            }

            @Override
            public void closeAll() {
                closeDialog();
            }
        });

        mOilNumDialog.show();
    }

    private void showCouponDialog(OilEntity.StationsBean stationsBean, String amount,
                                  int oilNoPosition, int gunNoPosition, String oilNo, boolean isPlat) {
        //优惠券dialog
        mOilCouponDialog = new OilCouponDialog(this, this, amount, stationsBean,
                oilNoPosition, gunNoPosition, oilNo, isPlat);
        mOilCouponDialog.setOnItemClickedListener(new OilCouponDialog.OnItemClickedListener() {
            @Override
            public void onOilCouponClick(BaseQuickAdapter adapter, View view, int position, boolean isPlat) {
                List<CouponBean> data = adapter.getData();
                setCouponInfo(data.get(position), isPlat, data.get(position).getExcludeType());
                mOilCouponDialog.dismiss();
            }

            @Override
            public void onNoCouponClick(boolean isPlat) {
                setCouponInfo(null, isPlat, "");
                mOilCouponDialog.dismiss();
            }
        });

        mOilCouponDialog.show();
    }

    /**
     * @param couponBean
     * @param isPlat      是否是平台券
     * @param excludeType 0:不互斥; 1:与直降互斥; 2:与商家优惠券互斥; 3:与直降和商家优惠券互斥
     */
    public void setCouponInfo(CouponBean couponBean, boolean isPlat, String excludeType) {//优惠券互斥
        if (couponBean != null) {
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
                            mBusinessCouponBean = null;
                            mDiscountList.get(2).setBusinessDesc("请选择优惠券");
                        } else {
                            businessAmount = "";
                            mBusinessCouponBean = null;
                            mDiscountList.get(2).setBusinessDesc("暂无可用优惠券");
                        }
                        break;
                    case "3"://与直降和商家优惠券互斥
                        mDiscountAdapter.getData().get(0).setFallDesc("暂无优惠");
                        mDiscountAdapter.getData().get(0).setService(false);
                        if (businessCoupons.size() > 0) {
                            businessAmount = "";
                            mBusinessCouponBean = null;
                            mDiscountList.get(2).setBusinessDesc("请选择优惠券");
                        } else {
                            businessAmount = "";
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
                if (!mBinding.monthRedCheck.isChecked()) {
                    platId = "";
                    mExcludeType = "";
                    mDiscountList.get(1).setPlatformDesc("请选择优惠券");
                }
            } else {
                businessAmount = "";
                mBusinessCouponBean = null;
                mDiscountList.get(2).setBusinessDesc("请选择优惠券");
            }
        }
        mDiscountAdapter.notifyDataSetChanged();
        mViewModel.getMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(),
                String.valueOf(mOilPriceListBean.get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(3).isUseBill() ? "1" : "0",
                platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "");
    }

    @Override
    protected void initListener() {
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

        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            if (height == 0) {
                //刷新价格互斥
                refreshData();
            }
        });

        mBinding.createOrderTv.setOnClickListener(view -> {
            UiUtils.canClickViewStateDelayed(view, 1000);
            mViewModel.createOrder(mBinding.amountEt.getText().toString(), mMultiplePriceBean.getDuePrice(),
                    mMultiplePriceBean.getBalancePrice(), mStationsBean.getGasId(), String.valueOf(mOilPriceListBean.get(mOilNoPosition).
                            getGunNos().get(mGunNoPosition).getGunNo()), String.valueOf(mOilPriceListBean.get(mOilNoPosition).getOilNo()),
                    mOilPriceListBean.get(mOilNoPosition).getOilName(), mStationsBean.getGasName(), mOilPriceListBean.get(mOilNoPosition).getPriceGun(),
                    mOilPriceListBean.get(mOilNoPosition).getPriceYfq(), mOilPriceListBean.get(mOilNoPosition).getOilType() + "",
                    SPUtils.getInstance().getString(SPConstants.MOBILE), platId, mBusinessCouponBean == null ? "" : mBusinessCouponBean.getId(),
                    businessAmount, monthCouponId, mAmountReduce, mBinding.monthRedCheck.isChecked() && !TextUtils.isEmpty(monthCouponId),
                    mBinding.monthRedCheck.isChecked() && !TextUtils.isEmpty(mAmountReduce));
        });

        mBinding.oilEditTv.setOnClickListener(this::onViewClicked);
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
            if (mOilMonthRuleDialog == null) {
                mOilMonthRuleDialog = new OilMonthRuleDialog(this, this, mMonthCouponRule);
                mOilMonthRuleDialog.show(view);
            } else {
                mOilMonthRuleDialog.show(view);
            }
        });
    }

    private void showPayDialog(OilEntity.StationsBean stationsBean,
                               List<OilEntity.StationsBean.OilPriceListBean> oilPriceListBean,
                               int oilNoPosition, int gunNoPosition, String orderId, String payAmount) {
        //支付dialog
        mOilPayDialog = new OilPayDialog(this, this, stationsBean,
                oilPriceListBean, oilNoPosition, gunNoPosition, orderId, payAmount);
        mOilPayDialog.setOnItemClickedListener(new OilPayDialog.OnItemClickedListener() {
            @Override
            public void onOilPayTypeClick(BaseQuickAdapter adapter, View view, int position) {
//                GasStationLocationTipsDialog gasStationLocationTipsDialog =
//                        new GasStationLocationTipsDialog(getContext(), mBinding.getRoot(), "成都加油站");
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
                closeDialog();
            }

            @Override
            public void onPayOrderClick(String payType, String orderId, String payAmount) {
                mHomeViewModel.payOrder(payType, orderId, payAmount);
            }
        });

        mOilPayDialog.show();
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.oil_edit_tv:
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
                //快捷价格优惠价格更新
                List<OilDefaultPriceEntity.DefaultAmountBean> data = mOilAmountAdapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    if (!TextUtils.isEmpty(data.get(i).getAmount()) && !TextUtils.isEmpty(mBinding.amountEt.getText().toString())
                            && Float.parseFloat(data.get(i).getAmount()) == Float.parseFloat(mBinding.amountEt.getText().toString())) {
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
                if (multiplePriceBean.getBestuserCoupon() != null){
                    platId = multiplePriceBean.getBestuserCoupon().getId();
                    mDiscountAdapter.getData().get(1).setPlatformDesc("-¥" + multiplePriceBean.getBestuserCoupon().getAmountReduce());
                }else {
                    mPlatCouponBean = null;
                    platId = "";
                    mExcludeType = "";
                }
                if (multiplePriceBean.getBestBusinessCoupon() != null){
                    businessAmount = multiplePriceBean.getBestBusinessCoupon().getAmountReduce();
                    mDiscountAdapter.getData().get(2).setBusinessDesc("-¥" + multiplePriceBean.getBestBusinessCoupon().getAmountReduce());
                }else {
                    businessAmount = "";
                    mBusinessCouponBean = null;
                }
                //抵扣余额
                mDiscountAdapter.getData().get(3).setBalanceDiscount(
                        Float.parseFloat(multiplePriceBean.getBalancePrice()));

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
            }
        });

        mViewModel.monthCouponLiveData.observe(this, new Observer<MonthCouponEntity>() {
            @Override
            public void onChanged(MonthCouponEntity monthCouponEntity) {
                if (monthCouponEntity != null && monthCouponEntity.getMonthCouponTemplates().size() > 0) {
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
                    SpanUtils.with(mBinding.monthRedDesc)
                            .append(monthCouponEntity.getMonthCouponAmount() + "元")
                            .setForegroundColor(getResources().getColor(R.color.color_1300))
                            .append("享")
                            .setForegroundColor(getResources().getColor(R.color.color_34))
                            .append(totalMoney + "元")
                            .setForegroundColor(getResources().getColor(R.color.color_1300))
                            .append("立减红包，本单立减")
                            .setForegroundColor(getResources().getColor(R.color.color_34))
                            .append(mAmountReduce + "元")
                            .setForegroundColor(getResources().getColor(R.color.color_1300))
                            .create();

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
                    if (mBinding.monthRedCheck.isChecked()) {//切换快捷价格时，如果勾选月度红包
                        refreshMonthStauts(mBinding.amountEt.getText().toString().trim());
                    } else {
                        mDiscountAdapter.getData().get(1).setPlatformDesc("请选择优惠券");
                    }
                } else {
                    mDiscountAdapter.getData().get(1).setPlatformDesc("暂无可用优惠券");
                }
                mDiscountAdapter.notifyDataSetChanged();
            }
        });

        mViewModel.businessCouponLiveData.observe(this, new Observer<List<CouponBean>>() {
            @Override
            public void onChanged(List<CouponBean> couponBeans) {
                if (couponBeans != null && couponBeans.size() > 0) {
                    businessCoupons = couponBeans;
                    mDiscountAdapter.getData().get(2).setBusinessDesc("请选择优惠券");
                } else {
                    mDiscountAdapter.getData().get(2).setBusinessDesc("暂无可用优惠券");
                }
                mDiscountAdapter.notifyDataSetChanged();
            }
        });

        mViewModel.balanceLiveData.observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float balance) {
                if (balance > 0) {
                    mDiscountAdapter.getData().get(3).setBalance(balance);
                    mDiscountAdapter.getData().get(3).setUseBill(true);
                } else {
                    mDiscountAdapter.getData().get(3).setBalance(0);
                    mDiscountAdapter.getData().get(3).setUseBill(false);
                }
                mDiscountAdapter.notifyDataSetChanged();
            }
        });

        mViewModel.createOrderLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String orderId) {
                showPayDialog(mStationsBean, mOilPriceListBean, mOilNoPosition, mGunNoPosition,
                        orderId, mMultiplePriceBean.getDuePrice());
            }
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

    private void jumpToPayResultAct(String orderPayNo, String orderNo) {
        if (TextUtils.isEmpty(orderPayNo) && TextUtils.isEmpty(orderNo)) {
            return;
        }
        Intent intent = new Intent(this, RefuelingPayResultActivity.class);
        intent.putExtra("orderPayNo", orderPayNo);
        intent.putExtra("orderNo", orderNo);
        startActivity(intent);
        closeDialog();
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
                mPayOrderEntity.getOrderNo(), mPayOrderEntity.getOrderPayNo(), false, true);
        PayListenerUtils.getInstance().removeListener(this);
        closeDialog();
    }

    @Override
    public void onFail() {
        RefuelingPayResultActivity.openPayResultPage(this,
                mPayOrderEntity.getOrderNo(), mPayOrderEntity.getOrderPayNo(), false, true);
        PayListenerUtils.getInstance().removeListener(this);
        closeDialog();
    }

    @Override
    public void onCancel() {
        Toasty.info(this, "支付取消").show();
        PayListenerUtils.getInstance().removeListener(this);
        closeDialog();
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
    }
}