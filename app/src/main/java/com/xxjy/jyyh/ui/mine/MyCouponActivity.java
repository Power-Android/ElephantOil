package com.xxjy.jyyh.ui.mine;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.MyCouponAdapter;
import com.xxjy.jyyh.adapter.MyViewPagerAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityMyCouponBinding;
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

public class MyCouponActivity extends BindingActivity<ActivityMyCouponBinding,MyCouponViewModel> {


    private boolean isCanUse=true;
    private final String[] titles = new String[]{"平台优惠券", "商家优惠券","兑换"};
    private final List<View> mList = new ArrayList<>(2);
    private List<String> data=new ArrayList<>();
    private MyCouponAdapter myCouponAdapter;
    private View mPlatformCoupon;
    private View mBusinessCoupon;
    private View mExchangeCoupon;
    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("我的优惠券");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);
//        initTab();
        data.add("1111111111");
        data.add("1111111111");
        data.add("1111111111");
        data.add("1111111111");
        data.add("1111111111");
        data.add("1111111111");
        data.add("1111111111");
//        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        myCouponAdapter = new MyCouponAdapter(R.layout.adapter_my_coupon,data);
//        mBinding.recyclerView.setAdapter(myCouponAdapter);
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
                simplePagerTitleView.setTextViewSize(15,15);
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
    }

    @Override
    protected void initListener() {
//
//        mBinding.canUseBt.setOnClickListener(this::onViewClicked);
//        mBinding.noCanUseBt.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.can_use_bt:
                isCanUse =true;
//                changeBt();
                break;
            case R.id.no_can_use_bt:
                isCanUse =false;
//                changeBt();
                break;
        }

    }

    @Override
    protected void dataObservable() {

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
//    private void changeBt() {
//        if (isCanUse) {
//            mBinding.canUseBt.setBackgroundResource(R.drawable.shape_stroke_blue_15radius);
//            mBinding.canUseBt.setTextColor(Color.parseColor("#1676FF"));
//            mBinding.noCanUseBt.setBackgroundResource(0);
//            mBinding.noCanUseBt.setTextColor(Color.parseColor("#585858"));
//        } else {
//            mBinding.canUseBt.setBackgroundResource(0);
//            mBinding.canUseBt.setTextColor(Color.parseColor("#585858"));
//            mBinding.noCanUseBt.setBackgroundResource(R.drawable.shape_stroke_blue_15radius);
//            mBinding.noCanUseBt.setTextColor(Color.parseColor("#1676FF"));
//        }
//    }
}