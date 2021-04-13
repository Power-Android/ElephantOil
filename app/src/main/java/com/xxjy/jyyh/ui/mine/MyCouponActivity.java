package com.xxjy.jyyh.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnMultiListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.MyCouponAdapter;
import com.xxjy.jyyh.adapter.MyViewPagerAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.databinding.ActivityMyCouponBinding;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.EventEntity;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.oil.CouponOilStationsActivity;
import com.xxjy.jyyh.ui.oil.OilDetailActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.wight.SettingLayout;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class MyCouponActivity extends BindingActivity<ActivityMyCouponBinding, MyCouponViewModel> {


    private final String[] titles = new String[]{"平台优惠券", "商家优惠券", "兑换"};
    private final List<View> mList = new ArrayList<>(3);
    private List<CouponBean> data = new ArrayList<>();
    private List<CouponBean> data2 = new ArrayList<>();
    private List<CouponBean> unCanData = new ArrayList<>();
    private List<CouponBean> unCanData2 = new ArrayList<>();
    private MyCouponAdapter platformCouponAdapter;
    private MyCouponAdapter businessCouponAdapter;

    private View mPlatformCoupon;
    private View mBusinessCoupon;
    private View mExchangeCoupon;

    private RecyclerView mPlatformRecyclerView;
    private RecyclerView mBusinessRecyclerView;
    private SmartRefreshLayout mPlatformRefreshView;
    private SmartRefreshLayout mBusinessRefreshView;

    private TextView platformShowView;
    private TextView businessShowView;

    private QMUIRoundButton convertBtn;
    private EditText convertEdit;

    private int platformCanUse = 1;
    private int businessCanUse = 1;

    private boolean isPlatformShowAdd = false;
    private boolean isBusinessShowAdd = false;

    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("我的优惠券");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);

        mPlatformCoupon = View.inflate(this, R.layout.coupon_platform, null);
        mBusinessCoupon = View.inflate(this, R.layout.coupon_business, null);
        mExchangeCoupon = View.inflate(this, R.layout.coupon_exchange, null);
        mList.add(mPlatformCoupon);
        mList.add(mBusinessCoupon);
        mList.add(mExchangeCoupon);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(titles, mList);
        mBinding.viewPager.setOffscreenPageLimit(1);
        mBinding.viewPager.setAdapter(adapter);

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        int padding = 70;
        commonNavigator.setLeftPadding(padding);
        commonNavigator.setRightPadding(padding);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SettingLayout simplePagerTitleView = new SettingLayout(context);
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setTextViewSize(15, 15);
                simplePagerTitleView.setmNormalColor(getResources().getColor(R.color.color_34));
                simplePagerTitleView.setmSelectedColor(getResources().getColor(R.color.colorAccent));
                simplePagerTitleView.setOnClickListener(v -> mBinding.viewPager.setCurrentItem(index));

                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setLineWidth(UIUtil.dip2px(context, 25));
                indicator.setRoundRadius(UIUtil.dip2px(context, 10));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
//                indicator.setBackgroundResource(R.drawable.shape_indicator);
                indicator.setColors(getResources().getColor(R.color.colorAccent));
                return indicator;
            }
        });
        mBinding.indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mBinding.indicator, mBinding.viewPager);
        commonNavigator.setOnClickListener(v -> QMUIKeyboardHelper.hideKeyboard(v));

        platformShowView = mPlatformCoupon.findViewById(R.id.show_view);
        mPlatformRecyclerView = mPlatformCoupon.findViewById(R.id.recycler_view);
//        mPlatformCanUseBt = mPlatformCoupon.findViewById(R.id.can_use_bt);
//        mPlatformNoCanUseBt = mPlatformCoupon.findViewById(R.id.no_can_use_bt);
        mPlatformRefreshView = mPlatformCoupon.findViewById(R.id.refresh_view);
        mPlatformRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        platformCouponAdapter = new MyCouponAdapter(R.layout.adapter_my_coupon, data);
        platformCouponAdapter.setEmptyView(R.layout.empty_coupon_layout, mPlatformRecyclerView);
        mPlatformRecyclerView.setAdapter(platformCouponAdapter);

        businessShowView = mBusinessCoupon.findViewById(R.id.show_view);
        mBusinessRecyclerView = mBusinessCoupon.findViewById(R.id.recycler_view);
//        mBusinessCanUseBt = mBusinessCoupon.findViewById(R.id.can_use_bt);
//        mBusinessNoCanUseBt = mBusinessCoupon.findViewById(R.id.no_can_use_bt);
        mBusinessRefreshView = mBusinessCoupon.findViewById(R.id.refresh_view);
        mBusinessRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        businessCouponAdapter = new MyCouponAdapter(R.layout.adapter_my_coupon, data2);

        businessCouponAdapter.setEmptyView(R.layout.empty_coupon_layout, mBusinessRecyclerView);
        mBusinessRecyclerView.setAdapter(businessCouponAdapter);

        convertBtn = mExchangeCoupon.findViewById(R.id.convert_btn);
        convertEdit = mExchangeCoupon.findViewById(R.id.convert_edit);
        getPlatformCouponVOs(platformCanUse);
        getBusinessCoupons(businessCanUse);
        getPlatformCouponVOs(0);
        getBusinessCoupons(0);

        SpanUtils.with(platformShowView).append("不可使用优惠券已被隐藏，")
                .append("点击查看")
                .setForegroundColor(Color.parseColor("#FD6431"))
                .create();

        SpanUtils.with(businessShowView).append("不可使用优惠券已被隐藏，")
                .append("点击查看")
                .setForegroundColor(Color.parseColor("#FD6431"))
                .create();

    }

    @Override
    protected void initListener() {
        mPlatformRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                isPlatformShowAdd = false;
                getPlatformCouponVOs(platformCanUse);
                platformShowView.setVisibility(View.VISIBLE);
            }
        });
        mBusinessRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                isBusinessShowAdd = false;
                getBusinessCoupons(businessCanUse);
                businessShowView.setVisibility(View.VISIBLE);
            }
        });
        convertBtn.setOnClickListener(this::onViewClicked);

        platformCouponAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.use_view) {
                    CouponBean couponBean = (CouponBean) adapter.getData().get(position);
                    if (couponBean.getRangeType() == 1) {

                        if (TextUtils.isEmpty(couponBean.getLinkUrl())) {
                            MainActivity.openMainActAndClearTaskJump(MyCouponActivity.this, 2);
                            finish();
                        } else {
                            WebViewActivity.openRealUrlWebActivity(MyCouponActivity.this, couponBean.getLinkUrl());
                        }

                    } else {
                        String oilStations = couponBean.getOilStations();
                        if (TextUtils.isEmpty(oilStations)) {
                            MainActivity.openMainActAndClearTaskJump(MyCouponActivity.this, 1);
                            finish();
//                            BusUtils.postSticky(EventConstants.EVENT_CHANGE_FRAGMENT, new EventEntity(EventConstants.EVENT_CHANGE_FRAGMENT));

                        } else {
                            String[] strs = oilStations.split(",");
                            if (strs.length == 1) {
                                startActivity(new Intent(MyCouponActivity.this, OilDetailActivity.class)
                                        .putExtra(Constants.GAS_STATION_ID, strs[0]));
                            } else {
                                startActivity(new Intent(MyCouponActivity.this, CouponOilStationsActivity.class)
                                        .putExtra("gasIds", oilStations));
                            }

                        }
                    }
                }

            }
        });
        businessCouponAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.use_view) {
                    CouponBean couponBean = (CouponBean) adapter.getData().get(position);
                    String oilStations = couponBean.getOilStations();
                    if (TextUtils.isEmpty(oilStations)) {
                        MainActivity.openMainActAndClearTaskJump(MyCouponActivity.this, 1);
                        finish();

                    }

                }

            }
        });
        platformShowView.setOnClickListener(v -> {
//            isPlatformShowAdd = true;
//            getPlatformCouponVOs(0);
            platformCouponAdapter.addData(unCanData);
            platformShowView.setVisibility(View.INVISIBLE);
        });
        businessShowView.setOnClickListener(v -> {
//            isBusinessShowAdd = true;
//            getBusinessCoupons(0);
            businessCouponAdapter.addData(unCanData2);
            businessShowView.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.convert_btn:
                if (TextUtils.isEmpty(convertEdit.getText().toString().trim())) {
                    showToastInfo("请输入兑换码");
                    return;
                }
                exchangeCoupon(convertEdit.getText().toString().trim());
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.platformCouponLiveData.observe(this, data -> {
            mPlatformRefreshView.finishRefresh();
//            if (isPlatformShowAdd) {
//                platformCouponAdapter.addData(data);
//            } else {
                platformCouponAdapter.setNewData(data);
//            }

        });
        mViewModel.businessCouponLiveData.observe(this, data -> {
            mBusinessRefreshView.finishRefresh();
//            if (isBusinessShowAdd) {
//                businessCouponAdapter.addData(data);
//            } else {
                businessCouponAdapter.setNewData(data);
//            }
        });

        mViewModel.disablePlatformCouponLiveData.observe(this, data -> {
//            mPlatformRefreshView.finishRefresh();
//            if (isPlatformShowAdd) {
//                platformCouponAdapter.addData(data);
//            } else {
//                platformCouponAdapter.setNewData(data);
//            }
            if (data != null && data.size() > 0) {
                platformShowView.setVisibility(View.VISIBLE);
                unCanData.addAll(data);
            } else {
                platformShowView.setVisibility(View.INVISIBLE);
            }


        });
        mViewModel.disableBusinessCouponLiveData.observe(this, data -> {
//            mBusinessRefreshView.finishRefresh();
//            if (isBusinessShowAdd) {
//                businessCouponAdapter.addData(data);
//            } else {
//                businessCouponAdapter.setNewData(data);
//            }
            if (data != null && data.size() > 0) {
                businessShowView.setVisibility(View.VISIBLE);
                unCanData2.addAll(data);
            } else {
                businessShowView.setVisibility(View.INVISIBLE);
            }
        });
        mViewModel.exchangeCouponLiveData.observe(this, data -> {
            if (data.getCode() == 1) {
                showToastSuccess("兑换成功");
            } else {
                showToastError("兑换失败");
            }
        });
    }

    //    private void initTab() {
//        QMUITabBuilder tabBuilder = mBinding.tabView.tabBuilder().setGravity(Gravity.CENTER);
//        tabBuilder.setTextSize(QMUIDisplayHelper.sp2px(this, 16), QMUIDisplayHelper.sp2px(this, 17));
//        tabBuilder.setColor(Color.parseColor("#8ABAFF"), Color.parseColor("#FFFFFF"));
//        mBinding.tabView.addTab(tabBuilder.setText("平台优惠券").build(this));
//        mBinding.tabView.addTab(tabBuilder.setText("商家优惠券").build(this));
//        mBinding.tabView.addTab(tabBuilder.setText("兑换").build(this));
//
//        int space = QMUIDisplayHelper.dp2px(this, 10);
//        mBinding.tabView.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(this, 2), false, true));
//        mBinding.tabView.setItemSpaceInScrollMode(space);
//        mBinding.tabView.setPadding(space, 0, space, 0);
//        mBinding.tabView.setMode(QMUITabSegment.MODE_FIXED);
//        mBinding.tabView.selectTab(0);
//        mBinding.tabView.notifyDataChanged();
//    }
    private void changeBt(int type, QMUIRoundButton canUseBt, QMUIRoundButton noCanUseBt) {
        if (type == 1) {
            canUseBt.setBackgroundResource(R.drawable.shape_stroke_blue_15radius);
            canUseBt.setTextColor(Color.parseColor("#1676FF"));
            noCanUseBt.setBackgroundResource(0);
            noCanUseBt.setTextColor(Color.parseColor("#585858"));
        } else {
            canUseBt.setBackgroundResource(0);
            canUseBt.setTextColor(Color.parseColor("#585858"));
            noCanUseBt.setBackgroundResource(R.drawable.shape_stroke_blue_15radius);
            noCanUseBt.setTextColor(Color.parseColor("#1676FF"));
        }
    }

    private void getPlatformCouponVOs(int platformCanUse) {
        mViewModel.getPlatformCouponVOs(platformCanUse);
    }

    private void getBusinessCoupons(int businessCanUse) {
        mViewModel.getBusinessCoupons(businessCanUse);
    }

    private void exchangeCoupon(String couponCode) {
        mViewModel.exchangeCoupon(couponCode);
    }
}