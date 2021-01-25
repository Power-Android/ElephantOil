package com.xxjy.jyyh.ui.pay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.IntegralExchangeAdapter;
import com.xxjy.jyyh.adapter.PayResultBannerAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityRefuelingPayResultBinding;
import com.xxjy.jyyh.ui.order.OrderDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class RefuelingPayResultActivity extends BindingActivity<ActivityRefuelingPayResultBinding,RefuelingPayResultViewModel> {


    private List<String> data=new ArrayList<>();
    private PayResultBannerAdapter payResultBannerAdapter;
    private IntegralExchangeAdapter integralExchangeAdapter;
    @Override
    protected void initView() {
        mBinding.topLayout.setTitle("支付结果").setTextColor(Color.parseColor("#FFFFFF"));
        mBinding.topLayout.updateBottomDivider(0,0,0,0);
        mBinding.topLayout.addLeftImageButton(R.drawable.arrow_back_white,
                R.id.qmui_topbar_item_left_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        data.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1765232562,2313752077&fm=26&gp=0.jpg");
        data.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1292020288,3194758620&fm=26&gp=0.jpg");
        payResultBannerAdapter = new PayResultBannerAdapter(R.layout.item_pay_result_banner,data);
        mBinding.bannerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.bannerRecyclerView.setAdapter(payResultBannerAdapter);

        integralExchangeAdapter = new IntegralExchangeAdapter(R.layout.adapter_integral_exchange,data);
        mBinding.recommendRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mBinding.recommendRecyclerView.setAdapter(integralExchangeAdapter);


    }

    @Override
    protected void initListener() {
mBinding.goOrderView.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.go_order_view:
                startActivity(new Intent(this, OrderDetailsActivity.class));
                break;
        }

    }

    @Override
    protected void dataObservable() {

    }
}