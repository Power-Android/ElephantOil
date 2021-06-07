package com.xxjy.jyyh.ui.pay;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.BarUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityPayResultBinding;
import com.xxjy.jyyh.utils.toastlib.Toasty;

public class CarServePayResultActivity extends BindingActivity<ActivityPayResultBinding, CarServePayResultViewModel> {

    private String mOrderNo;
    private String mOrderPayNo;

    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("支付结果");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);

        mOrderNo = getIntent().getStringExtra("orderNo");
        mOrderPayNo = getIntent().getStringExtra("orderPayNo");
    }

    @Override
    protected void initListener() {
        mBinding.checkView.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_view:
                getPayResult();
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.payResultLiveData.observe(this, data -> {
            if (data != null) {
                if(data.getProductParams()!=null){
                    if(data.getProductParams().getType()==2){
                        mBinding.goEquityOrderView.setVisibility(View.VISIBLE);

                    }else{
                        mBinding.goEquityOrderView.setVisibility(View.GONE);
                    }
                }
                switch (data.getResult()) {
                    case 0://处理中
                        mBinding.payingStatusView.setText("处理中...");
                        mBinding.checkView.setVisibility(View.INVISIBLE);
                        break;
                    case 1://支付成功
                        getOrderInfo();
                        break;
                    case 2://支付失败
                        mBinding.payingLayout.setVisibility(View.GONE);
                        mBinding.payFailLayout.setVisibility(View.VISIBLE);
                        break;
                    case 3://已取消
                        mBinding.payingLayout.setVisibility(View.GONE);
                        mBinding.payFailLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

        });
        mViewModel.orderLiveData.observe(this,data->{

        });
    }

    private void getPayResult() {
        mViewModel.getPayResult(mOrderNo, mOrderPayNo);
    }
    private void getOrderInfo(){
        mViewModel.getOrderInfo(mOrderNo);
    }

    public static void openPayResultPage(Activity activity, String orderNo, String orderPayNo) {
        Intent intent = new Intent(activity, CarServePayResultActivity.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("orderPayNo", orderPayNo);
        activity.startActivity(intent);
    }

    public static void openPayResultPage(Activity activity, String orderNo, String orderPayNo,
                                         boolean isLocalLife, boolean isAppPay) {
        Intent intent = new Intent(activity, CarServePayResultActivity.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("orderPayNo", orderPayNo);
        intent.putExtra("isLocalLife", isLocalLife);
        intent.putExtra("isAppPay", isAppPay);
        activity.startActivity(intent);
    }
}