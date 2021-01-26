package com.xxjy.jyyh.ui.order;


import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OrderListAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityOrderListBinding;
import com.xxjy.jyyh.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends BindingActivity<ActivityOrderListBinding, OrderListViewModel> {


    private OrderListAdapter adapter;
    private List<String> data;
    private boolean isOilOrder = true;
    private QMUIPullLayout.PullAction mPullAction;

    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("订单列表");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.setStatusBarColor(this,Color.parseColor("#1676FF"));
        initTab();

        data = new ArrayList<>();
        data.add("aaaa");
        data.add("aaaa");
        data.add("aaaa");
        data.add("aaaa");
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderListAdapter(R.layout.adapter_order_layout, data);
        mBinding.recyclerView.setAdapter(adapter);

        mBinding.pullLayout.setActionListener(pullAction -> {
            mPullAction = pullAction;
            if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_TOP) {
                data.clear();
                data.add("aaaa");
                data.add("aaaa");
                data.add("aaaa");
                data.add("aaaa");
                adapter.notifyDataSetChanged();
            } else if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_BOTTOM) {
                data.add("aaaa");
                data.add("aaaa");
                data.add("aaaa");
                data.add("aaaa");
                adapter.notifyDataSetChanged();

            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(OrderListActivity.this, OrderDetailsActivity.class));
            }
        });
    }

    @Override
    protected void initListener() {
        mBinding.interestsBt.setOnClickListener(this::onViewClicked);
        mBinding.oilBt.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.oil_bt:
                isOilOrder = true;
                changeBt();
                break;
            case R.id.interests_bt:
                isOilOrder = false;
                changeBt();
                break;
        }

    }

    @Override
    protected void dataObservable() {

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
}