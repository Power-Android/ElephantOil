package com.xxjy.jyyh.ui.order;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.xxjy.jyyh.adapter.LocalLifeOrderListAdapter;
import com.xxjy.jyyh.adapter.RefuelOrderListAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityOtherOrderListBinding;
import com.xxjy.jyyh.entity.IntegralOrderBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;
import com.xxjy.jyyh.ui.mine.MyCouponActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class OtherOrderListActivity extends BindingActivity<ActivityOtherOrderListBinding, OrderListViewModel> {

    private int pageSize = 10;
    private int pageNum = 1;
    private int payStatus = -1;
    private LocalLifeOrderListAdapter localLifeOrderListAdapter;
    private IntegralOrderListAdapter integralOrderAdapter;
    private List<RefuelOrderBean> refuelOrderData = new ArrayList<>();

    private int mPosition = -1;
    private boolean isIntegral=false;

    private OrderDetailsViewModel orderDetailsViewModel;
    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        isIntegral = getIntent().getBooleanExtra("isIntegral",false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(!isIntegral){
            mBinding.titleLayout.tvTitle.setText("生活订单");
            localLifeOrderListAdapter = new LocalLifeOrderListAdapter(R.layout.adapter_order_layout, refuelOrderData);
            mBinding.recyclerView.setAdapter(localLifeOrderListAdapter);
            localLifeOrderListAdapter.setEmptyView(R.layout.empty_layout, mBinding.recyclerView);
            localLifeOrderListAdapter.setOnItemClickListener((adapter, view, position) -> {
                OrderDetailsActivity.openPage(this, true,((RefuelOrderBean) adapter.getItem(position)).getOrderId());
            });
            localLifeOrderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    mPosition = position;
                    switch (view.getId()) {
                        case R.id.continue_pay_view:
                            OrderDetailsActivity.openPage(OtherOrderListActivity.this, ((RefuelOrderBean) adapter.getItem(position)).getOrderId(), true,true);
                            break;
                        case R.id.cancel_order_view:
                            cancelOrder(((RefuelOrderBean) adapter.getItem(position)).getOrderId());
                            break;
                    }
                }
            });
            lifeOrderList();
        }else{
            mBinding.titleLayout.tvTitle.setText("权益订单");
            integralOrderAdapter = new IntegralOrderListAdapter(R.layout.adapter_integral_order_layout, refuelOrderData);
            mBinding.recyclerView.setAdapter(integralOrderAdapter);
            integralOrderAdapter.setEmptyView(R.layout.empty_layout, mBinding.recyclerView);
            integralOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    mPosition = position;
                    switch (view.getId()) {
                        case R.id.continue_pay_view:
                            WebViewActivity.openRealUrlWebActivity(OtherOrderListActivity.this, ((RefuelOrderBean) adapter.getItem(position)).getLink() + "&showCashier=true");
                            break;
                        case R.id.cancel_order_view:
                            productCancelOrder(((RefuelOrderBean) adapter.getItem(position)).getOrderId());
                            break;
                        case R.id.go_coupon_view:
                            startActivity(new Intent(OtherOrderListActivity.this, MyCouponActivity.class));
                            break;
                        case R.id.go_detail_view:
                            WebViewActivity.openRealUrlWebActivity(OtherOrderListActivity.this, ((RefuelOrderBean) adapter.getItem(position)).getLink());
                            break;
                    }
                }
            });
//            integralOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    WebViewActivity.openRealUrlWebActivity(OtherOrderListActivity.this, ((RefuelOrderBean) adapter.getItem(position)).getLink());
//                }
//            });
            integralOrderList();
        }
        initTab();


        orderDetailsViewModel = new ViewModelProvider(this).get(OrderDetailsViewModel.class);
    }

    @Override
    protected void initListener() {

        mBinding.refreshView.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                if(isIntegral){
                    integralOrderList();
                }else{
                    lifeOrderList();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum =1;
                if(isIntegral){
                    integralOrderList();
                }else{
                    lifeOrderList();
                }

            }
        });
    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {
        mViewModel.integralOrderListLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                if (pageNum == 1) {
                    integralOrderAdapter.setNewData(data);
                    mBinding.refreshView.setEnableLoadMore(true);
                    mBinding.refreshView.finishRefresh(true);
                } else {
                    integralOrderAdapter.addData(data);
                    mBinding.refreshView.finishLoadMore(true);
                }
            } else {
                if (pageNum == 1) {
                    integralOrderAdapter.setNewData(null);
                }
                mBinding.refreshView.finishLoadMoreWithNoMoreData();
            }
        });
        mViewModel.lifeOrderListLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                if (pageNum == 1) {
                    localLifeOrderListAdapter.setNewData(data);
                    mBinding.refreshView.setEnableLoadMore(true);
                    mBinding.refreshView.finishRefresh(true);
                } else {
                    localLifeOrderListAdapter.addData(data);
                    mBinding.refreshView.finishLoadMore(true);
                }
            } else {
                if (pageNum == 1) {
                    localLifeOrderListAdapter.setNewData(null);
                }
                mBinding.refreshView.finishLoadMoreWithNoMoreData();
            }
        });

        orderDetailsViewModel.cancelOrderDetailsLiveData.observe(this, data -> {

                if (data.getCode() == 1) {
                    showToastSuccess("取消成功");
                    ((RefuelOrderBean) localLifeOrderListAdapter.getItem(mPosition)).setStatus(3);
                    ((RefuelOrderBean) localLifeOrderListAdapter.getItem(mPosition)).setStatusName("支付失败");
                    localLifeOrderListAdapter.notifyItemChanged(mPosition);
                } else {
                    showToastError("取消失败");
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

    private void initTab(){
        QMUITabBuilder tabBuilder = mBinding.tabView.tabBuilder().setGravity(Gravity.CENTER);
        tabBuilder.setTextSize(QMUIDisplayHelper.sp2px(this, 13), QMUIDisplayHelper.sp2px(this, 13));
        tabBuilder.setColor(Color.parseColor("#323334"), Color.parseColor("#1676FF"));
        mBinding.tabView.addTab(tabBuilder.setText("全部").build(this));
        mBinding.tabView.addTab(tabBuilder.setText("待支付").build(this));
        mBinding.tabView.addTab(tabBuilder.setText("支付成功").build(this));
        mBinding.tabView.addTab(tabBuilder.setText("支付失败").build(this));

        int space = QMUIDisplayHelper.dp2px(this, 12);
        mBinding.tabView.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(this, 2), false, true));
        mBinding.tabView.setItemSpaceInScrollMode(space);
        mBinding.tabView.setPadding(space, 0, space, 0);
        mBinding.tabView.setMode(QMUITabSegment.MODE_FIXED);
        mBinding.tabView.selectTab(0);
        mBinding.tabView.notifyDataChanged();
        mBinding.tabView.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
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
                if(isIntegral){
                    integralOrderList();
                }else{
                    lifeOrderList();
                }


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

        private void integralOrderList() {
        mViewModel.integralOrderList(payStatus, pageNum, pageSize);
    }

    private void lifeOrderList() {
        mViewModel.lifeOrderList(payStatus, pageNum, pageSize);
    }
    private void productCancelOrder(String orderId) {
        orderDetailsViewModel.productCancelOrder(orderId);
    }
    private void cancelOrder(String orderId) {
        orderDetailsViewModel.cancelOrder(orderId);
    }
}