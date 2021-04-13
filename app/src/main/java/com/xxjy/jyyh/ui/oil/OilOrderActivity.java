package com.xxjy.jyyh.ui.oil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.NumberUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilAmountAdapter;
import com.xxjy.jyyh.adapter.OilDiscountAdapter;
import com.xxjy.jyyh.adapter.OilMonthAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityOilOrderBinding;
import com.xxjy.jyyh.dialog.OilHotDialog;
import com.xxjy.jyyh.dialog.OilMonthRuleDialog;
import com.xxjy.jyyh.dialog.OilMonthTipDialog;
import com.xxjy.jyyh.dialog.OilServiceDialog;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.MonthCouponEntity;
import com.xxjy.jyyh.entity.MultiplePriceBean;
import com.xxjy.jyyh.entity.OilDefaultPriceEntity;
import com.xxjy.jyyh.entity.OilDiscountEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OilOrderActivity extends BindingActivity<ActivityOilOrderBinding, OilViewModel> {

    private OilEntity.StationsBean mStationsBean;
    private List<OilEntity.StationsBean.OilPriceListBean> mOilPriceListBean;
    private int mOilNoPosition;
    private int mGunNoPosition;

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

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, true);
        BusUtils.register(this);

        mStationsBean = (OilEntity.StationsBean) getIntent().getSerializableExtra("stationsBean");
        mOilPriceListBean = (List<OilEntity.StationsBean.OilPriceListBean>) getIntent().getSerializableExtra("oilPriceListBean");
        mOilNoPosition = getIntent().getIntExtra("oilNoPosition", -1);
        mGunNoPosition = getIntent().getIntExtra("gunNoPosition", -1);

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

//        mDiscountAdapter.setOnItemClickListener((adapter, view, position) -> {
//            if (platformCoupons.size() > 0 && position == 1 ||
//                    businessCoupons.size() > 0 && position == 2 &&
//                            mOnItemClickedListener != null) {
//                UiUtils.canClickViewStateDelayed(view, 1000);
//                mOnItemClickedListener.onOilDiscountClick(adapter, view, position,
//                        mBinding.amountEt.getText().toString(),
//                        String.valueOf(oilPriceListBeans
//                                .get(oilNoPosition).getOilNo()));
//            }
//        });

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
//        getBusinessCoupon();//商家优惠券
//        getBalance();//余额
        //再次进来时需要刷新数据
        mViewModel.getMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                mOilPriceListBean.get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(3).isUseBill() ? "1" : "0",
                platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "");
    }

    private void refreshData() {
        //每次价格改变时，要清空这俩
        platId = "";
        businessAmount = "";
        mBusinessCouponBean = null;
        if (mOilMonthAdapter.getData().size() > 0) {
//            refreshMonthStauts(mBinding.amountEt.getText().toString().trim());
        }
        mViewModel.getPlatformCoupon(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(),
                String.valueOf(mOilPriceListBean.get(mOilNoPosition).getOilNo()));
//        getBusinessCoupon();
        //刷新价格互斥
        mViewModel.getMultiplePrice(mBinding.amountEt.getText().toString(), mStationsBean.getGasId(), String.valueOf(
                mOilPriceListBean.get(mOilNoPosition).getOilNo()), mDiscountAdapter.getData().get(3).isUseBill() ? "1" : "0",
                platId, businessAmount, mBinding.monthRedCheck.isChecked() ? monthCouponId : "");
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {
        mViewModel.multiplePriceLiveData.observe(this, new Observer<MultiplePriceBean>() {
            @Override
            public void onChanged(MultiplePriceBean multiplePriceBean) {

            }
        });

        mViewModel.monthCouponLiveData.observe(this, new Observer<MonthCouponEntity>() {
            @Override
            public void onChanged(MonthCouponEntity monthCouponEntity) {

            }
        });

        mViewModel.platformCouponLiveData.observe(this, new Observer<List<CouponBean>>() {
            @Override
            public void onChanged(List<CouponBean> couponBeans) {

            }
        });
    }
}