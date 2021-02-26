package com.xxjy.jyyh.ui.order;

import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityLifeOrderDetailsBinding;

public class LifeOrderDetailsActivity extends BindingActivity<ActivityLifeOrderDetailsBinding,OrderDetailsViewModel> {


    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("订单详情");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);
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
}