package com.xxjy.jyyh.ui.order;


import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.CarServeOrderListAdapter;
import com.xxjy.jyyh.adapter.IntegralOrderListAdapter;
import com.xxjy.jyyh.adapter.LocalLifeOrderListAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityCarServeOrderListBinding;
import com.xxjy.jyyh.dialog.CustomerServiceDialog;
import com.xxjy.jyyh.entity.CarServeOrderBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;
import com.xxjy.jyyh.ui.web.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class CarServeOrderListActivity extends BindingActivity<ActivityCarServeOrderListBinding, OrderListViewModel> {

    private int pageNum = 1;
    private int verificationStatus = -1;
    private int orderStatus = -1;
    private CarServeOrderListAdapter carServeOrderListAdapter;
    private List<CarServeOrderBean> carServeOrderBeanList = new ArrayList<>();

    private int mPosition = -1;

    private CustomerServiceDialog customerServiceDialog;
    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.titleLayout.tvTitle.setText("车生活订单");
        carServeOrderListAdapter = new CarServeOrderListAdapter(R.layout.adapter_car_serve_order_layout, carServeOrderBeanList);
        mBinding.recyclerView.setAdapter(carServeOrderListAdapter);
        carServeOrderListAdapter.setEmptyView(R.layout.empty_layout, mBinding.recyclerView);
        carServeOrderListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    mPosition = position;
                    switch (view.getId()) {
                        case R.id.continue_pay_view:
                            WebViewActivity.openRealUrlWebActivity(CarServeOrderListActivity.this, ((CarServeOrderBean) adapter.getItem(position)).getExDetailLink() + "&showCashier=true");
                            break;
                        case R.id.cancel_order_view:
                            productCancelOrder(((CarServeOrderBean) adapter.getItem(position)).getOrderId());
                            break;
                        case R.id.refund_view:
                            if (customerServiceDialog == null) {
                                customerServiceDialog = new CustomerServiceDialog(CarServeOrderListActivity.this);
                            }
                            customerServiceDialog.show(view);
                            break;
                        case R.id.check_coupon_code_view:
                            WebViewActivity.openRealUrlWebActivity(CarServeOrderListActivity.this, ((CarServeOrderBean) adapter.getItem(position)).getExDetailLink());
                            break;
                    }
                }
            });
        carServeOrderListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    WebViewActivity.openRealUrlWebActivity(CarServeOrderListActivity.this, ((CarServeOrderBean) adapter.getItem(position)).getExDetailLink());
                }
            });
        initTab();


        carServeOrderList();
    }

    @Override
    protected void initListener() {

        mBinding.refreshView.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                carServeOrderList();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                carServeOrderList();

            }
        });
    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {
        mViewModel.carServeOrderListLiveData.observe(this, data -> {
            if (data != null && data.getList()!=null&&data.getList().size() > 0) {
                if (pageNum == 1) {
                    carServeOrderListAdapter.setNewData(data.getList());
                    mBinding.refreshView.setEnableLoadMore(true);
                    mBinding.refreshView.finishRefresh(true);
                } else {
                    carServeOrderListAdapter.addData(data.getList());
                    mBinding.refreshView.finishLoadMore(true);
                }
            } else {
                if (pageNum == 1) {
                    carServeOrderListAdapter.setNewData(null);
                }
                mBinding.refreshView.finishLoadMoreWithNoMoreData();
            }
        });
//        mViewModel.lifeOrderListLiveData.observe(this, data -> {
//            if (data != null && data.size() > 0) {
//                if (pageNum == 1) {
//                    localLifeOrderListAdapter.setNewData(data);
//                    mBinding.refreshView.setEnableLoadMore(true);
//                    mBinding.refreshView.finishRefresh(true);
//                } else {
//                    localLifeOrderListAdapter.addData(data);
//                    mBinding.refreshView.finishLoadMore(true);
//                }
//            } else {
//                if (pageNum == 1) {
//                    localLifeOrderListAdapter.setNewData(null);
//                }
//                mBinding.refreshView.finishLoadMoreWithNoMoreData();
//            }
//        });

//        orderDetailsViewModel.cancelOrderDetailsLiveData.observe(this, data -> {
//
//            if (data.getCode() == 1) {
//                showToastSuccess("取消成功");
//                ((RefuelOrderBean) localLifeOrderListAdapter.getItem(mPosition)).setStatus(3);
//                ((RefuelOrderBean) localLifeOrderListAdapter.getItem(mPosition)).setStatusName("支付失败");
//                localLifeOrderListAdapter.notifyItemChanged(mPosition);
//            } else {
//                showToastError("取消失败");
//            }
//
//
//        });
        mViewModel.cancelCarServeOrderLiveData.observe(this, data -> {
            if (data.getCode() == 1) {
                showToastSuccess("取消成功");
                carServeOrderListAdapter.getItem(mPosition).setOrderStatus(3);
                carServeOrderListAdapter.getItem(mPosition).setOrderStatusName("订单取消");
                carServeOrderListAdapter.notifyItemChanged(mPosition);
            } else {
                showToastError("取消失败");
            }
        });
    }

    private void initTab() {
        QMUITabBuilder tabBuilder = mBinding.tabView.tabBuilder().setGravity(Gravity.CENTER);
        tabBuilder.setTextSize(QMUIDisplayHelper.sp2px(this, 16), QMUIDisplayHelper.sp2px(this, 16));
        tabBuilder.setColor(Color.parseColor("#323334"), Color.parseColor("#1676FF"));
        tabBuilder.setTypeface(Typeface.DEFAULT,Typeface.DEFAULT_BOLD);
        mBinding.tabView.addTab(tabBuilder.setText("全部").build(this));
        mBinding.tabView.addTab(tabBuilder.setText("待使用").build(this));
//        mBinding.tabView.addTab(tabBuilder.setText("待支付").build(this));

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
                switch (index) {
                    case 0:
                        orderStatus = -1;
                        verificationStatus = -1;
                        break;
                    case 1:
                        orderStatus = 1;
                        verificationStatus = 1;
                        break;
                    case 2:
                        orderStatus = 0;
                        verificationStatus = -1;
                        break;
                }
                pageNum = 1;
                carServeOrderList();

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


    private void carServeOrderList() {
        mViewModel.carServeOrderList( orderStatus,verificationStatus, pageNum);
    }


    private void productCancelOrder(String orderId) {
        mViewModel.cancelCarServeOrder(orderId);
    }

}