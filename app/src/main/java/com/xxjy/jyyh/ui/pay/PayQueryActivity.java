package com.xxjy.jyyh.ui.pay;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityPayQueryBinding;

public class PayQueryActivity extends BindingActivity<ActivityPayQueryBinding, PayResultViewModel> {

    private String mOrderNo;
    private String mOrderPayNo;

    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("确认支付结果");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);

        mOrderNo = getIntent().getStringExtra("orderNo");
        mOrderPayNo = getIntent().getStringExtra("orderPayNo");
    }

    @Override
    protected void initListener() {
        mBinding.tv1.setOnClickListener(this::onViewClicked);
        mBinding.tv2.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv1://还未支付
            case R.id.tv2://已支付
                jumpToPayResultAct(mOrderPayNo, mOrderNo);
                break;
        }
    }

    private void jumpToPayResultAct(String orderPayNo, String orderNo) {
        if (TextUtils.isEmpty(orderPayNo) && TextUtils.isEmpty(orderNo)) {
            return;
        }
        Intent intent = new Intent(this, RefuelingPayResultActivity.class);
        intent.putExtra("orderPayNo", orderPayNo);
        intent.putExtra("orderNo", orderNo);
        startActivity(intent);
        finish();
    }

    @Override
    protected void dataObservable() {

    }

    public static void openPayQueryActivity(Activity activity, String orderPayNo, String orderNo) {
        Intent intent = new Intent(activity, PayQueryActivity.class);
        intent.putExtra("orderPayNo", orderPayNo);
        intent.putExtra("orderNo", orderNo);
        activity.startActivity(intent);
    }
}