package com.xxjy.jyyh.ui.mine;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.MyCouponAdapter;
import com.xxjy.jyyh.adapter.MyViewPagerAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityMyCouponBinding;
import com.xxjy.jyyh.entity.CouponBean;
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
    private final List<View> mList = new ArrayList<>(2);
    private List<CouponBean> data = new ArrayList<>();
    private List<CouponBean> data2 = new ArrayList<>();
    private MyCouponAdapter platformCouponAdapter;
    private MyCouponAdapter businessCouponAdapter;

    private RecyclerView mPlatformRecyclerView;
    private RecyclerView mBusinessRecyclerView;
    private QMUIRoundButton mPlatformCanUseBt;
    private QMUIRoundButton mPlatformNoCanUseBt;
    private QMUIRoundButton mBusinessCanUseBt;
    private QMUIRoundButton mBusinessNoCanUseBt;
    private SmartRefreshLayout mPlatformRefreshView;
    private SmartRefreshLayout mBusinessRefreshView;
    private View mPlatformCoupon;
    private View mBusinessCoupon;
    private View mExchangeCoupon;

    private int platformCanUse = 1;
    private int businessCanUse = 1;

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
                simplePagerTitleView.setmNormalColor(getResources().getColor(R.color.color_BAFF));
                simplePagerTitleView.setmSelectedColor(getResources().getColor(R.color.white));
                simplePagerTitleView.setOnClickListener(v -> mBinding.viewPager.setCurrentItem(index));

                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setLineWidth(UIUtil.dip2px(context, 42));
                indicator.setRoundRadius(UIUtil.dip2px(context, 10));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(getResources().getColor(R.color.color_CBFF));
                return indicator;
            }
        });
        mBinding.indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mBinding.indicator, mBinding.viewPager);
        commonNavigator.setOnClickListener(v -> QMUIKeyboardHelper.hideKeyboard(v));

        mPlatformRecyclerView = mPlatformCoupon.findViewById(R.id.recycler_view);
        mPlatformCanUseBt = mPlatformCoupon.findViewById(R.id.can_use_bt);
        mPlatformNoCanUseBt = mPlatformCoupon.findViewById(R.id.no_can_use_bt);
        mPlatformRefreshView = mPlatformCoupon.findViewById(R.id.refresh_view);
        mPlatformRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        platformCouponAdapter = new MyCouponAdapter(R.layout.adapter_my_coupon, data);
        platformCouponAdapter.setEmptyView(R.layout.empty_layout, mPlatformRecyclerView);
        mPlatformRecyclerView.setAdapter(platformCouponAdapter);


        mBusinessRecyclerView = mBusinessCoupon.findViewById(R.id.recycler_view);
        mBusinessCanUseBt = mBusinessCoupon.findViewById(R.id.can_use_bt);
        mBusinessNoCanUseBt = mBusinessCoupon.findViewById(R.id.no_can_use_bt);
        mBusinessRefreshView = mBusinessCoupon.findViewById(R.id.refresh_view);
        mBusinessRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        businessCouponAdapter = new MyCouponAdapter(R.layout.adapter_my_coupon, data2);
        businessCouponAdapter.setEmptyView(R.layout.empty_layout, mBusinessRecyclerView);
        mBusinessRecyclerView.setAdapter(businessCouponAdapter);

        getPlatformCouponVOs();
        getBusinessCoupons();
    }

    @Override
    protected void initListener() {
//
        mPlatformCanUseBt.setOnClickListener(v -> {
            platformCanUse = 1;
            changeBt(platformCanUse,mPlatformCanUseBt,mPlatformNoCanUseBt);
            getPlatformCouponVOs();

        });
        mPlatformNoCanUseBt.setOnClickListener(v -> {
            platformCanUse = 0;
            changeBt(platformCanUse,mPlatformCanUseBt,mPlatformNoCanUseBt);
            getPlatformCouponVOs();
        });
        mBusinessCanUseBt.setOnClickListener(v -> {
            businessCanUse = 1;
            changeBt(businessCanUse,mBusinessCanUseBt,mBusinessNoCanUseBt);
            getBusinessCoupons();
        });
        mBusinessNoCanUseBt.setOnClickListener(v -> {
            businessCanUse = 0;
            changeBt(businessCanUse,mBusinessCanUseBt,mBusinessNoCanUseBt);
            getBusinessCoupons();
        });

        mPlatformRefreshView.setOnRefreshListener(refreshLayout -> getPlatformCouponVOs());
        mBusinessRefreshView.setOnRefreshListener(refreshLayout -> getBusinessCoupons());
    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {
        mViewModel.platformCouponLiveData.observe(this, data -> {
            platformCouponAdapter.setNewData(data);
        });
        mViewModel.businessCouponLiveData.observe(this, data -> {
            businessCouponAdapter.setNewData(data);
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
    private void changeBt(int type,QMUIRoundButton canUseBt,QMUIRoundButton noCanUseBt) {
        if (type==1) {
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

    private void getPlatformCouponVOs() {
        mViewModel.getPlatformCouponVOs(platformCanUse);
    }
    private void getBusinessCoupons() {
        mViewModel.getBusinessCoupons(businessCanUse);
    }
}