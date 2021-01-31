package com.xxjy.jyyh.ui.pay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.IntegralExchangeAdapter;
import com.xxjy.jyyh.adapter.PayResultBannerAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityRefuelingPayResultBinding;
import com.xxjy.jyyh.entity.PayResultEntity;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.order.OrderDetailsActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class RefuelingPayResultActivity extends BindingActivity<ActivityRefuelingPayResultBinding,
        RefuelingPayResultViewModel> {


    private List<PayResultEntity.ActiveParamsBean.BannerBean> data = new ArrayList<>();
    private PayResultBannerAdapter payResultBannerAdapter;
    private String mOrderNo;
    private String mOrderPayNo;

    //    private IntegralExchangeAdapter integralExchangeAdapter;
    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("支付结果");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.setStatusBarColor(this, Color.parseColor("#1676FF"));
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);

        mOrderNo = getIntent().getStringExtra("orderNo");
        mOrderPayNo = getIntent().getStringExtra("orderPayNo");

        payResultBannerAdapter = new PayResultBannerAdapter(R.layout.item_pay_result_banner, data);
        mBinding.bannerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.bannerRecyclerView.setAdapter(payResultBannerAdapter);
        payResultBannerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<PayResultEntity.ActiveParamsBean.BannerBean> data = adapter.getData();
                WebViewActivity.openRealUrlWebActivity(RefuelingPayResultActivity.this, data.get(position).getLink());
            }
        });

//        integralExchangeAdapter = new IntegralExchangeAdapter(R.layout.adapter_integral_exchange,data);
//        mBinding.recommendRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
//        mBinding.recommendRecyclerView.setAdapter(integralExchangeAdapter);

        mViewModel.getPayResult(mOrderNo, mOrderPayNo);
    }

    @Override
    protected void initListener() {
        mBinding.goOrderView.setOnClickListener(this::onViewClicked);
        mBinding.goHomeView.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_order_view:
                Intent intent = new Intent(this, OrderDetailsActivity.class);
                intent.putExtra(OrderDetailsActivity.ORDER_ID, mOrderNo);
                startActivity(intent);
                break;
            case R.id.go_home_view:
                startActivity(new Intent(this, MainActivity.class));
                finish();
        }

    }

    @Override
    protected void dataObservable() {
        mViewModel.payResultLiveData.observe(this, resultEntity -> {
            switch (resultEntity.getResult()){
                case 0://支付失败
                    mBinding.tagIv.setVisibility(View.GONE);
                    mBinding.statusTv.setText("支付失败");
                    mBinding.statusTv.setTextColor(getResources().getColor(R.color.color_34));
                    mBinding.fallMoney.setText(resultEntity.getMsg());
                    mBinding.tagView.setText("预计下单可获得");
                    mBinding.integralTv.setText(resultEntity.getIntegral()+"");
                    mBinding.integralAll.setText(resultEntity.getIntegralBalance()+"");
                    mBinding.goOrderView.setVisibility(View.GONE);
                    mBinding.tryAgainView.setVisibility(View.VISIBLE);
                    payResultBannerAdapter.setNewData(resultEntity.getActiveParams().getBanner());
                    break;
                case 1://支付成功
                    mBinding.tagIv.setVisibility(View.VISIBLE);
                    mBinding.statusTv.setText("支付成功");
                    mBinding.statusTv.setTextColor(getResources().getColor(R.color.color_30));
                    mBinding.fallMoney.setText("本单优惠¥" + resultEntity.getDiscountAmount());
                    mBinding.tagView.setText("本单获得");
                    mBinding.integralTv.setText(resultEntity.getIntegral()+"");
                    mBinding.integralAll.setText(resultEntity.getIntegralBalance()+"");
                    mBinding.goOrderView.setVisibility(View.VISIBLE);
                    mBinding.tryAgainView.setVisibility(View.GONE);
                    payResultBannerAdapter.setNewData(resultEntity.getActiveParams().getBanner());
                    break;
                case 2://待支付
                    break;
            }
        });
    }
}