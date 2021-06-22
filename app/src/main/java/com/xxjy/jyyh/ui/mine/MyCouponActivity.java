package com.xxjy.jyyh.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.CarServeCouponAdapter;
import com.xxjy.jyyh.adapter.MyCarServeAdapter;
import com.xxjy.jyyh.adapter.MyCouponAdapter;
import com.xxjy.jyyh.adapter.MyViewPagerAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.databinding.ActivityMyCouponBinding;
import com.xxjy.jyyh.entity.CarCouponEntity;
import com.xxjy.jyyh.entity.CarServeCouponBean;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.CouponNumEntity;
import com.xxjy.jyyh.entity.EventEntity;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.oil.CouponOilStationsActivity;
import com.xxjy.jyyh.ui.oil.OilDetailsActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.wight.SettingLayout;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyCouponActivity extends BindingActivity<ActivityMyCouponBinding, MyCouponViewModel> {


    private final String[] titles = new String[]{"平台优惠券", "商家优惠券", "车生活优惠券", "兑换"};
    private final List<View> mList = new ArrayList<>(4);
    private List<CouponBean> data = new ArrayList<>();
    private List<CouponBean> data2 = new ArrayList<>();
    private List<CarCouponEntity.RecordsBean> data3 = new ArrayList<>();
    private MyCouponAdapter platformCouponAdapter;
    private MyCouponAdapter businessCouponAdapter;
    private MyCarServeAdapter carServeCouponAdapter;

    private View mPlatformCoupon;
    private View mBusinessCoupon;
    private View mCarServeCoupon;
    private View mExchangeCoupon;

    private RecyclerView mPlatformRecyclerView;
    private RecyclerView mBusinessRecyclerView;
    private RecyclerView mCarServeRecyclerView;
    private SmartRefreshLayout mPlatformRefreshView;
    private SmartRefreshLayout mBusinessRefreshView;
    private SmartRefreshLayout mCarServeRefreshView;


    private QMUIRoundButton convertBtn;
    private EditText convertEdit;

    private int platformCanUse = 1;
    private int businessCanUse = 1;

    private int viewPosition = 0;
    private CouponNumEntity mCouponNumEntity1;
    private CouponNumEntity mCouponNumEntity2;

    //可使用 1 待时用 2 已使用/过期 3

    @Override
    protected void initView() {
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
        mBinding.titleLayout.tvTitle.setText("我的优惠券");
        mBinding.titleLayout.tbToolbar.setBackgroundColor(getResources().getColor(R.color.white));
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);
        BusUtils.register(this);
        mViewModel.getCouponNum1(1);
        mViewModel.getCouponNum2(2);

        mPlatformCoupon = View.inflate(this, R.layout.coupon_platform, null);
        mBusinessCoupon = View.inflate(this, R.layout.coupon_business, null);
        mCarServeCoupon = View.inflate(this, R.layout.coupon_car_serve, null);
        mExchangeCoupon = View.inflate(this, R.layout.coupon_exchange, null);
        mList.add(mPlatformCoupon);
        mList.add(mBusinessCoupon);
        mList.add(mCarServeCoupon);
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
                simplePagerTitleView.setmNormalColor(getResources().getColor(R.color.color_34));
                simplePagerTitleView.setmSelectedColor(getResources().getColor(R.color.colorAccent));
                simplePagerTitleView.setTextViewSize(13,13);
                simplePagerTitleView.setSelectedStyle(true);
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
                indicator.setBackgroundResource(R.drawable.shape_indicator);
                indicator.setColors(getResources().getColor(R.color.colorAccent));
                return indicator;
            }
        });
        mBinding.indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mBinding.indicator, mBinding.viewPager);
        commonNavigator.setOnClickListener(v -> QMUIKeyboardHelper.hideKeyboard(v));

        mPlatformRecyclerView = mPlatformCoupon.findViewById(R.id.recycler_view);
        mPlatformRefreshView = mPlatformCoupon.findViewById(R.id.refresh_view);
        mPlatformRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        platformCouponAdapter = new MyCouponAdapter( data);
        platformCouponAdapter.setEmptyView(R.layout.empty_coupon_layout, mPlatformRecyclerView);
        mPlatformRecyclerView.setAdapter(platformCouponAdapter);

        mBusinessRecyclerView = mBusinessCoupon.findViewById(R.id.recycler_view);
        mBusinessRefreshView = mBusinessCoupon.findViewById(R.id.refresh_view);
        mBusinessRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        businessCouponAdapter = new MyCouponAdapter(data2);
        businessCouponAdapter.setEmptyView(R.layout.empty_coupon_layout, mBusinessRecyclerView);
        mBusinessRecyclerView.setAdapter(businessCouponAdapter);

        mCarServeRecyclerView = mCarServeCoupon.findViewById(R.id.recycler_view);
        mCarServeRefreshView = mCarServeCoupon.findViewById(R.id.refresh_view);
        mCarServeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        carServeCouponAdapter = new MyCarServeAdapter(R.layout.adapter_my_car_coupon, data3);
        carServeCouponAdapter.setEmptyView(R.layout.empty_coupon_layout, mCarServeRecyclerView);
        mCarServeRecyclerView.setAdapter(carServeCouponAdapter);

        convertBtn = mExchangeCoupon.findViewById(R.id.convert_btn);
        convertEdit = mExchangeCoupon.findViewById(R.id.convert_edit);
        getPlatformCouponVOs(platformCanUse);
        getBusinessCoupons(businessCanUse);
        getCarServeCoupons();
    }

    @Override
    protected void initListener() {
        mPlatformRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mViewModel.getCouponNum1(1);
                mViewModel.getCouponNum2(2);
                if (viewPosition == 0){
                    getPlatformCouponVOs(platformCanUse);
                }else if (viewPosition == 1){
                    getBusinessCoupons(businessCanUse);
                }else if (viewPosition == 2){

                }
            }
        });
        mBusinessRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (viewPosition == 0){
                    getPlatformCouponVOs(platformCanUse);
                }else if (viewPosition == 1){
                    getBusinessCoupons(businessCanUse);
                }else if (viewPosition == 2){

                }
            }
        });

        mCarServeRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                getCarServeCoupons();
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
//                                startActivity(new Intent(MyCouponActivity.this, OilDetailActivity.class)
//                                        .putExtra(Constants.GAS_STATION_ID, strs[0]));
                                startActivity(new Intent(MyCouponActivity.this, OilDetailsActivity.class)
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

        carServeCouponAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.item_use_tv){
                MainActivity.openMainActAndClearTaskJump(MyCouponActivity.this, 2);
                finish();
            }
        });

        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPosition = position;
                switch (position) {
                    case 0:
                        mBinding.statusLayout.setVisibility(View.VISIBLE);
                        mBinding.useableView.setText(String.format("可使用(%s张)",
                                TextUtils.isEmpty(mCouponNumEntity1.getUse()) ? 0 : mCouponNumEntity1.getUse()));
                        mBinding.toBeUseView.setText(String.format("待使用(%s张)", TextUtils.isEmpty(mCouponNumEntity1.getWait()) ? 0 : mCouponNumEntity1.getWait()));
                        break;
                    case 1:
                        mBinding.statusLayout.setVisibility(View.VISIBLE);
                        mBinding.useableView.setText(String.format("可使用(%s张)",
                                TextUtils.isEmpty(mCouponNumEntity2.getUse()) ? 0 : mCouponNumEntity2.getUse()));
                        mBinding.toBeUseView.setText(String.format("待使用(%s张)",
                                TextUtils.isEmpty(mCouponNumEntity2.getWait()) ? 0 : mCouponNumEntity2.getWait()));
                        break;
                    case 2:
                        mBinding.statusLayout.setVisibility(View.GONE);
                        break;
                    case 3:
                        mBinding.statusLayout.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.statusLayout.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.useable_view:
                    setStatusData(1);
                    break;
                case R.id.to_be_use_view:
                    setStatusData(2);
                    break;
                case R.id.used_view:
                    setStatusData(0);
                    break;
            }
        });

        mBinding.statusLayout.check(R.id.useable_view);

    }

    private void setStatusData(int status){
        switch (viewPosition){
            case 0:
                platformCanUse = status;
                getPlatformCouponVOs(status);
                break;
            case 1:
                businessCanUse = status;
                getBusinessCoupons(status);
                break;
            case 2:
                getCarServeCoupons();
                break;
        }
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

            platformCouponAdapter.setNewData(data);


        });
        mViewModel.businessCouponLiveData.observe(this, data -> {
            mBusinessRefreshView.finishRefresh();

            businessCouponAdapter.setNewData(data);

        });

        mViewModel.carServeCouponLiveData.observe(this, carCouponEntity -> {
            mCarServeRefreshView.finishRefresh();
            if (carCouponEntity.getRecords() != null && carCouponEntity.getRecords().size() > 0){
                carServeCouponAdapter.setNewData(carCouponEntity.getRecords());
            }
        });

        mViewModel.exchangeCouponLiveData.observe(this, data -> {
            if (data.getCode() == 1) {
                showToastSuccess("兑换成功");
            } else {
                showToastError("兑换失败");
            }
        });

        mViewModel.couponNumLiveData1.observe(this, couponNumEntity -> {
            mCouponNumEntity1 = couponNumEntity;
            mBinding.useableView.setText(String.format("可使用(%s张)", mCouponNumEntity1.getUse()));
            mBinding.toBeUseView.setText(String.format("待使用(%s张)", mCouponNumEntity1.getWait()));
        });

        mViewModel.couponNumLiveData2.observe(this, couponNumEntity -> {
            mCouponNumEntity2 = couponNumEntity;
        });
    }


    private void getPlatformCouponVOs(int platformCanUse) {
        mViewModel.getPlatformCouponVOs(platformCanUse);
    }

    private void getBusinessCoupons(int businessCanUse) {
        mViewModel.getBusinessCoupons(businessCanUse);
    }

    private void getCarServeCoupons(){
        mViewModel.carServeCoupon();
    }

    private void exchangeCoupon(String couponCode) {
        mViewModel.exchangeCoupon(couponCode);
    }
}