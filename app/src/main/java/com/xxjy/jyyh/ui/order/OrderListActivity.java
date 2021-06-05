package com.xxjy.jyyh.ui.order;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.IntegralOrderListAdapter;
import com.xxjy.jyyh.adapter.LocalLifeOrderListAdapter;
import com.xxjy.jyyh.adapter.MyViewPagerAdapter;
import com.xxjy.jyyh.adapter.RefuelOrderListAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityOrderListBinding;
import com.xxjy.jyyh.entity.IntegralOrderBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;
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

public class OrderListActivity extends BindingActivity<ActivityOrderListBinding, OrderListViewModel> {
    private final String[] titles = new String[]{"支付订单", "退款订单"};
    private final List<View> mViewList = new ArrayList<>(2);
    private RefuelOrderListAdapter refuelOrderAdapter;
    private RefuelOrderListAdapter refundOrderAdapter;
    private IntegralOrderListAdapter integralOrderAdapter;
    //    private LocalLifeOrderListAdapter localLifeOrderListAdapter;
    private List<RefuelOrderBean> refuelOrderData = new ArrayList<>();
    private List<RefuelOrderBean> integralOrderData = new ArrayList<>();
    private boolean isOilOrder = true;

    private int status = -1;
    private int payStatus = -1;
    private int pageNum = 1;
    private int pageSize = 10;
    private int pageNum2 = 1;
    private int pageNum3 = 1;

    private View paymentOrderLayout;
    private View refundOrderLayout;

    private RecyclerView mPaymentOrderRecyclerView;
    private RecyclerView mRefundOrderRecyclerView;

    private SmartRefreshLayout mPaymentOrderRefreshView;
    private SmartRefreshLayout mRefundOrderRefreshView;

    private QMUITabSegment paymentTabView;
    private QMUITabSegment refundTabView;

    private OrderDetailsViewModel orderDetailsViewModel;

    private int mPosition = -1;

    private int type = 0;


    @Override
    protected void initView() {
        mBinding.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
        type = getIntent().getIntExtra("type", 0);


        paymentOrderLayout = View.inflate(this, R.layout.order_view_layout, null);
        refundOrderLayout = View.inflate(this, R.layout.order_view_layout, null);
//        localLifeOrderLayout = View.inflate(this, R.layout.refuel_order_list, null);
        mViewList.add(paymentOrderLayout);
        mViewList.add(refundOrderLayout);
//        mViewList.add(localLifeOrderLayout);
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(titles, mViewList);
        mBinding.viewPager.setOffscreenPageLimit(1);
        mBinding.viewPager.setAdapter(viewPagerAdapter);

        mPaymentOrderRecyclerView = paymentOrderLayout.findViewById(R.id.recycler_view);
        mPaymentOrderRefreshView = paymentOrderLayout.findViewById(R.id.refresh_view);
        paymentTabView = paymentOrderLayout.findViewById(R.id.tab_view);
        mPaymentOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        refuelOrderAdapter = new RefuelOrderListAdapter(R.layout.adapter_order_layout, refuelOrderData);
        mPaymentOrderRecyclerView.setAdapter(refuelOrderAdapter);
        refuelOrderAdapter.setEmptyView(R.layout.empty_layout, mPaymentOrderRecyclerView);


        mRefundOrderRecyclerView = refundOrderLayout.findViewById(R.id.recycler_view);
        mRefundOrderRefreshView = refundOrderLayout.findViewById(R.id.refresh_view);
        refundTabView = refundOrderLayout.findViewById(R.id.tab_view);
        mRefundOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        refundOrderAdapter = new RefuelOrderListAdapter(R.layout.adapter_order_layout, integralOrderData);
        mRefundOrderRecyclerView.setAdapter(refundOrderAdapter);
        refundOrderAdapter.setEmptyView(R.layout.empty_layout, mRefundOrderRecyclerView);

        initTab();
        refuelOrderList();
        orderRefundList();
//        integralOrderList();
//        lifeOrderList();
        orderDetailsViewModel = new ViewModelProvider(this).get(OrderDetailsViewModel.class);
        mBinding.viewPager.setCurrentItem(type);
    }

    @Override
    protected void initListener() {
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                type = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPaymentOrderRefreshView.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                refuelOrderList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                refuelOrderList();

            }
        });
        mRefundOrderRefreshView.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum2++;
                orderRefundList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum2 = 1;
                orderRefundList();

            }
        });
        refuelOrderAdapter.setOnItemClickListener((adapter, view, position) -> {
            OrderDetailsActivity.openPage(this, ((RefuelOrderBean) adapter.getItem(position)).getOrderId());
        });
        refuelOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mPosition = position;
                switch (view.getId()) {
                    case R.id.continue_pay_view:
                        OrderDetailsActivity.openPage(OrderListActivity.this, ((RefuelOrderBean) adapter.getItem(position)).getOrderId(), true);
                        break;
                    case R.id.cancel_order_view:
                        isOilOrder = true;
                        cancelOrder(((RefuelOrderBean) adapter.getItem(position)).getOrderId());
                        break;
                }
            }
        });
        refundOrderAdapter.setOnItemClickListener((adapter, view, position) -> {
            OrderDetailsActivity.openPageByRefund(this, ((RefuelOrderBean) adapter.getItem(position)).getId(),true);
        });
        refundOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mPosition = position;
                switch (view.getId()) {
                    case R.id.continue_pay_view:
                        OrderDetailsActivity.openPage(OrderListActivity.this, ((RefuelOrderBean) adapter.getItem(position)).getOrderId(), true);
                        break;
                    case R.id.cancel_order_view:
                        isOilOrder = true;
                        cancelOrder(((RefuelOrderBean) adapter.getItem(position)).getOrderId());
                        break;
                }
            }
        });

//        integralOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                mPosition = position;
//                switch (view.getId()) {
//                    case R.id.continue_pay_view:
//                        WebViewActivity.openRealUrlWebActivity(OrderListActivity.this, ((IntegralOrderBean) adapter.getItem(position)).getLink() + "&showCashier=true");
//                        break;
//                    case R.id.cancel_order_view:
//                        productCancelOrder(((IntegralOrderBean) adapter.getItem(position)).getOrderId());
//                        break;
//                }
//            }
//        });
//        integralOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                WebViewActivity.openRealUrlWebActivity(OrderListActivity.this, ((IntegralOrderBean) adapter.getItem(position)).getLink());
//            }
//        });
    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadData();
    }

    @Override
    protected void dataObservable() {
        mViewModel.paymentOrderListLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                if (pageNum == 1) {
                    refuelOrderAdapter.setNewData(data);
                    mPaymentOrderRefreshView.setEnableLoadMore(true);
                    mPaymentOrderRefreshView.finishRefresh(true);
                } else {
                    refuelOrderAdapter.addData(data);
                    mPaymentOrderRefreshView.finishLoadMore(true);
                }
            } else {
                if (pageNum == 1) {
                    refuelOrderAdapter.setNewData(null);
                }
                mPaymentOrderRefreshView.finishLoadMoreWithNoMoreData();
            }
        });
        mViewModel.refundOrderListLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                if (pageNum2 == 1) {
                    refundOrderAdapter.setNewData(data);
                    mRefundOrderRefreshView.setEnableLoadMore(true);
                    mRefundOrderRefreshView.finishRefresh(true);
                } else {
                    refundOrderAdapter.addData(data);
                    mRefundOrderRefreshView.finishLoadMore(true);
                }
            } else {
                if (pageNum2 == 1) {
                    refundOrderAdapter.setNewData(null);
                }
                mRefundOrderRefreshView.finishLoadMoreWithNoMoreData();
            }
        });
        mViewModel.integralOrderListLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                if (pageNum2 == 1) {
                    integralOrderAdapter.setNewData(data);
                    mRefundOrderRefreshView.setEnableLoadMore(true);
                    mRefundOrderRefreshView.finishRefresh(true);
                } else {
                    integralOrderAdapter.addData(data);
                    mRefundOrderRefreshView.finishLoadMore(true);
                }
            } else {
                if (pageNum2 == 1) {
                    integralOrderAdapter.setNewData(null);
                }
                mRefundOrderRefreshView.finishLoadMoreWithNoMoreData();
            }
        });
        mViewModel.lifeOrderListLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                if (pageNum3 == 1) {
                    refuelOrderAdapter.setNewData(data);
                    mRefundOrderRefreshView.setEnableLoadMore(true);
                    mRefundOrderRefreshView.finishRefresh(true);
                } else {
                    refuelOrderAdapter.addData(data);
                    mRefundOrderRefreshView.finishLoadMore(true);
                }
            } else {
                if (pageNum3 == 1) {
                    refuelOrderAdapter.setNewData(null);
                }
                mRefundOrderRefreshView.finishLoadMoreWithNoMoreData();
            }
        });

        orderDetailsViewModel.cancelOrderDetailsLiveData.observe(this, data -> {
            if (isOilOrder) {
                if (data.getCode() == 1) {
                    showToastSuccess("取消成功");
                    ((RefuelOrderBean) refuelOrderAdapter.getItem(mPosition)).setStatus(3);
                    ((RefuelOrderBean) refuelOrderAdapter.getItem(mPosition)).setStatusName("支付失败");
                    refuelOrderAdapter.notifyItemChanged(mPosition);
                } else {
                    showToastError("取消失败");
                }
            } else {
                if (data.getCode() == 1) {
                    showToastSuccess("取消成功");
                    ((RefuelOrderBean) refuelOrderAdapter.getItem(mPosition)).setStatus(3);
                    ((RefuelOrderBean) refuelOrderAdapter.getItem(mPosition)).setStatusName("支付失败");
                    refuelOrderAdapter.notifyItemChanged(mPosition);
                } else {
                    showToastError("取消失败");
                }
            }

        });
        orderDetailsViewModel.productCancelOrderDetailsLiveData.observe(this, data -> {
            if (data.getCode() == 1) {
                showToastSuccess("取消成功");
                ((RefuelOrderBean) integralOrderAdapter.getItem(mPosition)).setStatus(3);
                ((RefuelOrderBean) integralOrderAdapter.getItem(mPosition)).setStatusName("兑换失败");
                integralOrderAdapter.notifyItemChanged(mPosition);
            } else {
                showToastError("取消失败");
            }
        });
    }

    private void initTab() {
        QMUITabBuilder tabBuilder = paymentTabView.tabBuilder().setGravity(Gravity.CENTER);
        tabBuilder.setTextSize(QMUIDisplayHelper.sp2px(this, 13), QMUIDisplayHelper.sp2px(this, 13));
        tabBuilder.setColor(Color.parseColor("#3d3d3d"), Color.parseColor("#1676FF"));
        paymentTabView.addTab(tabBuilder.setText("全部").build(this));
        paymentTabView.addTab(tabBuilder.setText("待支付").build(this));
        paymentTabView.addTab(tabBuilder.setText("支付成功").build(this));
        paymentTabView.addTab(tabBuilder.setText("支付失败").build(this));

        int space = QMUIDisplayHelper.dp2px(this, 12);
        paymentTabView.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(this, 2), false, true));
        paymentTabView.setItemSpaceInScrollMode(space);
        paymentTabView.setPadding(space, 0, space, 0);
        paymentTabView.setMode(QMUITabSegment.MODE_FIXED);
        paymentTabView.selectTab(0);
        paymentTabView.notifyDataChanged();
        paymentTabView.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                switch (index){
                    case 0:
                        payStatus = -1;
                        break;
                    case 1:
                        payStatus = 0;
                        break;
                    case 2:
                        payStatus = 1;
                        break;
                    case 3:
                        payStatus = 2;
                        break;

                }
                pageNum = 1;
                refuelOrderList();

            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {

            }
        });

        QMUITabBuilder tabBuilder2 = refundTabView.tabBuilder().setGravity(Gravity.CENTER);
        tabBuilder2.setTextSize(QMUIDisplayHelper.sp2px(this, 13), QMUIDisplayHelper.sp2px(this, 13));
        tabBuilder2.setColor(Color.parseColor("#3d3d3d"), Color.parseColor("#1676FF"));
        refundTabView.addTab(tabBuilder2.setText("全部").build(this));
        refundTabView.addTab(tabBuilder2.setText("退款中").build(this));
        refundTabView.addTab(tabBuilder2.setText("退款成功").build(this));
        refundTabView.addTab(tabBuilder2.setText("退款失败").build(this));

        refundTabView.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(this, 2), false, true));
        refundTabView.setItemSpaceInScrollMode(space);
        refundTabView.setPadding(space, 0, space, 0);
        refundTabView.setMode(QMUITabSegment.MODE_FIXED);
        refundTabView.selectTab(0);
        refundTabView.notifyDataChanged();
        refundTabView.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                switch (index){
                    case 0:
                        status = -1;
                        break;
                    case 1:
                        status = 0;
                        break;
                    case 2:
                        status = 1;
                        break;
                    case 3:
                        status = 2;
                        break;

                }
                pageNum2 = 1;
                orderRefundList();

            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {

            }
        });

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
//        int padding = 70;
        commonNavigator.setLeftPadding(UIUtil.dip2px(this, 15));
        commonNavigator.setRightPadding(UIUtil.dip2px(this, 15));
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SettingLayout simplePagerTitleView = new SettingLayout(context);
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setTextViewSize(16, 16);
                simplePagerTitleView.setmNormalColor(getResources().getColor(R.color.color_3D));
                simplePagerTitleView.setmSelectedColor(getResources().getColor(R.color.color_34));
                simplePagerTitleView.setSelectedStyle(true);
                simplePagerTitleView.setOnClickListener(v -> {
                    mBinding.viewPager.setCurrentItem(index);
                });

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
                indicator.setColors(getResources().getColor(R.color.colorAccent));
                return indicator;
            }
        });
        mBinding.indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mBinding.indicator, mBinding.viewPager);
        commonNavigator.setOnClickListener(v -> QMUIKeyboardHelper.hideKeyboard(v));
    }


    private void loadData() {
        mPaymentOrderRefreshView.setEnableRefresh(true);
        mPaymentOrderRefreshView.setEnableLoadMore(false);
        mPaymentOrderRefreshView.setNoMoreData(false);

        mRefundOrderRefreshView.setEnableRefresh(true);
        mRefundOrderRefreshView.setEnableLoadMore(false);
        mRefundOrderRefreshView.setNoMoreData(false);
        pageNum = 1;
        refuelOrderList();
        pageNum2 = 1;
        orderRefundList();
//        pageNum3 = 1;
//        lifeOrderList();
    }


    private void refuelOrderList() {
        mViewModel.refuelOrderList(payStatus, pageNum, pageSize);
    }
    private void orderRefundList() {
        mViewModel.orderRefundList(status, pageNum2, pageSize);
    }

//    private void integralOrderList() {
//        mViewModel.integralOrderList(status, pageNum2, pageSize);
//    }

//    private void lifeOrderList() {
//        mViewModel.lifeOrderList(status, pageNum3, pageSize);
//    }

    private void cancelOrder(String orderId) {
        orderDetailsViewModel.cancelOrder(orderId);
    }

    private void productCancelOrder(String orderId) {
        orderDetailsViewModel.productCancelOrder(orderId);
    }
}