package com.xxjy.jyyh.ui.order;


import android.graphics.Color;
import android.view.Gravity;
import android.view.View;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityOrderListBinding;

public class OrderListActivity extends BindingActivity<ActivityOrderListBinding, OrderListViewModel> {


    @Override
    protected void initView() {
        mBinding.topLayout.setTitle("测试");
        mBinding.topLayout.addLeftImageButton( R.drawable.arrow_back_black,
                R.id.qmui_topbar_item_left_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initTab();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {

    }

    private void initTab() {
        QMUITabBuilder tabBuilder = mBinding.tabView.tabBuilder().setGravity(Gravity.CENTER);
        tabBuilder.setTextSize(QMUIDisplayHelper.sp2px(this, 16), QMUIDisplayHelper.sp2px(this, 17));
        mBinding.tabView.addTab(tabBuilder.setText("全部").build(this));
        mBinding.tabView.addTab(tabBuilder.setText("待支付").build(this));
        tabBuilder.setColor(Color.parseColor("#403636"), Color.parseColor("#1676FF"));
        int space = QMUIDisplayHelper.dp2px(this, 10);
        mBinding.tabView.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(this, 2), false, true));
        mBinding.tabView.setItemSpaceInScrollMode(space);
        mBinding.tabView.setPadding(space, 0, space, 0);
        mBinding.tabView.setMode(QMUITabSegment.MODE_FIXED);
        mBinding.tabView.selectTab(0);
        mBinding.tabView.notifyDataChanged();
    }
}