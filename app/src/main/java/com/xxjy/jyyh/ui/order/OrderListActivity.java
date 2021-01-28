package com.xxjy.jyyh.ui.order;


import android.graphics.Color;
import android.view.Gravity;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.IntegralOrderListAdapter;
import com.xxjy.jyyh.adapter.MyViewPagerAdapter;
import com.xxjy.jyyh.adapter.RefuelOrderListAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityOrderListBinding;
import com.xxjy.jyyh.entity.IntegralOrderBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends BindingActivity<ActivityOrderListBinding, OrderListViewModel> {
    private final String[] titles = new String[]{"全部", "待支付"};
    private final List<View> mViewList = new ArrayList<>(2);
    private RefuelOrderListAdapter refuelOrderAdapter;
    private IntegralOrderListAdapter integralOrderAdapter;
    private List<RefuelOrderBean> refuelOrderData = new ArrayList<>();
    private List<IntegralOrderBean> integralOrderData = new ArrayList<>();
    private boolean isOilOrder = true;

    private int status = -1;
    private int pageNum = 1;
    private int pageSize = 10;
    private int pageNum2 = 1;

    private View refuelOrderLayout;
    private View integralOrderLayout;

    private RecyclerView mRefuelOrderRecyclerView;
    private RecyclerView mIntegralOrderRecyclerView;
    private SmartRefreshLayout mRefuelOrderRefreshView;
    private SmartRefreshLayout mIntegralOrderRefreshView;


    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("订单列表");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.setStatusBarColor(this, Color.parseColor("#1676FF"));
        initTab();
        refuelOrderLayout = View.inflate(this, R.layout.refuel_order_list, null);
        integralOrderLayout = View.inflate(this, R.layout.refuel_order_list, null);
        mViewList.add(refuelOrderLayout);
        mViewList.add(integralOrderLayout);
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(titles, mViewList);
        mBinding.viewPager.setOffscreenPageLimit(1);
        mBinding.viewPager.setAdapter(viewPagerAdapter);

        mRefuelOrderRecyclerView = refuelOrderLayout.findViewById(R.id.recycler_view);
        mRefuelOrderRefreshView = refuelOrderLayout.findViewById(R.id.refresh_view);
        mRefuelOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        refuelOrderAdapter = new RefuelOrderListAdapter(R.layout.adapter_order_layout, refuelOrderData);
        mRefuelOrderRecyclerView.setAdapter(refuelOrderAdapter);
        refuelOrderAdapter.setEmptyView(R.layout.empty_layout, mRefuelOrderRecyclerView);
        refuelOrderAdapter.setOnItemClickListener((adapter, view, position) -> {
            OrderDetailsActivity.openPage(this, ((RefuelOrderBean)adapter.getItem(position)).getOrderId());
        });
        mIntegralOrderRecyclerView = integralOrderLayout.findViewById(R.id.recycler_view);
        mIntegralOrderRefreshView = integralOrderLayout.findViewById(R.id.refresh_view);
        mIntegralOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        integralOrderAdapter = new IntegralOrderListAdapter(R.layout.adapter_order_layout, integralOrderData);
        mIntegralOrderRecyclerView.setAdapter(integralOrderAdapter);
        integralOrderAdapter.setEmptyView(R.layout.empty_layout, mIntegralOrderRecyclerView);
        refuelOrderList();
        integralOrderList();
    }

    @Override
    protected void initListener() {
        mBinding.interestsBt.setOnClickListener(this::onViewClicked);
        mBinding.oilBt.setOnClickListener(this::onViewClicked);
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    isOilOrder = true;
                } else {
                    isOilOrder = false;
                }
                changeBt();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRefuelOrderRefreshView.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
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
        mIntegralOrderRefreshView.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum2++;
                integralOrderList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum2 = 1;
                integralOrderList();

            }
        });
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.oil_bt:
                isOilOrder = true;
                changeBt();
//                loadData();
                mBinding.viewPager.setCurrentItem(0);
                break;
            case R.id.interests_bt:
                isOilOrder = false;
                changeBt();
//                loadData();
                mBinding.viewPager.setCurrentItem(1);
                break;
        }

    }

    @Override
    protected void dataObservable() {
        mViewModel.refuelOrderListLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                if (pageNum == 1) {
                    refuelOrderAdapter.setNewData(data);
                    mRefuelOrderRefreshView.setEnableLoadMore(true);
                    mRefuelOrderRefreshView.finishRefresh(true);
                } else {
                    refuelOrderAdapter.addData(data);
                    mRefuelOrderRefreshView.finishLoadMore(true);
                }
            } else {
                mRefuelOrderRefreshView.finishLoadMoreWithNoMoreData();
            }
        });
        mViewModel.integralOrderListLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                if (pageNum2 == 1) {
                    integralOrderAdapter.setNewData(data);
                    mIntegralOrderRefreshView.setEnableLoadMore(true);
                    mIntegralOrderRefreshView.finishRefresh(true);
                } else {
                    integralOrderAdapter.addData(data);
                    mIntegralOrderRefreshView.finishLoadMore(true);
                }
            } else {
                mIntegralOrderRefreshView.finishLoadMoreWithNoMoreData();
            }
        });

    }

    private void initTab() {
        QMUITabBuilder tabBuilder = mBinding.tabView.tabBuilder().setGravity(Gravity.CENTER);
        tabBuilder.setTextSize(QMUIDisplayHelper.sp2px(this, 16), QMUIDisplayHelper.sp2px(this, 17));
        tabBuilder.setColor(Color.parseColor("#403636"), Color.parseColor("#1676FF"));
        mBinding.tabView.addTab(tabBuilder.setText("全部").build(this));
        mBinding.tabView.addTab(tabBuilder.setText("待支付").build(this));

        int space = QMUIDisplayHelper.dp2px(this, 10);
        mBinding.tabView.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(this, 2), false, true));
        mBinding.tabView.setItemSpaceInScrollMode(space);
        mBinding.tabView.setPadding(space, 0, space, 0);
        mBinding.tabView.setMode(QMUITabSegment.MODE_FIXED);
        mBinding.tabView.selectTab(0);
        mBinding.tabView.notifyDataChanged();
//        mBinding.tabView.setupWithViewPager(mBinding.viewPager);
        mBinding.tabView.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                if (index == 0) {
                    status = -1;
                } else {
                    status = 0;
                }
                loadData();
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
    }

    private void changeBt() {
        if (isOilOrder) {
            mBinding.oilBt.setBackgroundColor(Color.parseColor("#1676FF"));
            mBinding.oilBt.setTextColor(Color.parseColor("#FFFFFF"));
            mBinding.interestsBt.setBackgroundColor(Color.parseColor("#FFFFFF"));
            mBinding.interestsBt.setTextColor(Color.parseColor("#1676FF"));
        } else {
            mBinding.oilBt.setBackgroundColor(Color.parseColor("#FFFFFF"));
            mBinding.oilBt.setTextColor(Color.parseColor("#1676FF"));
            mBinding.interestsBt.setBackgroundColor(Color.parseColor("#1676FF"));
            mBinding.interestsBt.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }


    private void loadData() {
//        if(isOilOrder){
        pageNum = 1;
        refuelOrderList();
//        }else{
        pageNum2 = 1;
        integralOrderList();
//        }
    }

    private void refuelOrderList() {
        mViewModel.refuelOrderList(status, pageNum, pageSize);
    }

    private void integralOrderList() {
        mViewModel.integralOrderList(status, pageNum2, pageSize);
    }
}