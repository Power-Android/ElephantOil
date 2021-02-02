package com.xxjy.jyyh.ui.pay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.BusUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.HomeExchangeAdapter;
import com.xxjy.jyyh.adapter.IntegralExchangeAdapter;
import com.xxjy.jyyh.adapter.PayResultBannerAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.databinding.ActivityRefuelingPayResultBinding;
import com.xxjy.jyyh.entity.EventEntity;
import com.xxjy.jyyh.entity.HomeProductEntity;
import com.xxjy.jyyh.entity.PayResultEntity;
import com.xxjy.jyyh.entity.ProductBean;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.home.HomeViewModel;
import com.xxjy.jyyh.ui.order.OrderDetailsActivity;
import com.xxjy.jyyh.ui.order.OrderListActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.LoginHelper;

import java.util.ArrayList;
import java.util.List;

public class RefuelingPayResultActivity extends BindingActivity<ActivityRefuelingPayResultBinding,
        RefuelingPayResultViewModel> {


    private List<PayResultEntity.ActiveParamsBean.BannerBean> data = new ArrayList<>();
    private List<HomeProductEntity.FirmProductsVoBean> mExchangeList = new ArrayList<>();
    private PayResultBannerAdapter payResultBannerAdapter;
    private String mOrderNo;
    private String mOrderPayNo;
    private HomeExchangeAdapter mExchangeAdapter;
    private HomeViewModel mHomeViewModel;

    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("支付结果");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.setStatusBarColor(this, Color.parseColor("#1676FF"));
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);

        mOrderNo = getIntent().getStringExtra("orderNo");
        mOrderPayNo = getIntent().getStringExtra("orderPayNo");

        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        payResultBannerAdapter = new PayResultBannerAdapter(R.layout.item_pay_result_banner, data);
        mBinding.bannerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.bannerRecyclerView.setAdapter(payResultBannerAdapter);
        payResultBannerAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<PayResultEntity.ActiveParamsBean.BannerBean> data = adapter.getData();
            WebViewActivity.openRealUrlWebActivity(RefuelingPayResultActivity.this, data.get(position).getLink());
        });

        mBinding.recommendRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mExchangeAdapter = new HomeExchangeAdapter(R.layout.adapter_integral_exchange, mExchangeList);
        mBinding.recommendRecyclerView.setAdapter(mExchangeAdapter);
        mExchangeAdapter.setOnItemClickListener((adapter, view, position) -> {
            LoginHelper.login(this, () ->
                    WebViewActivity.openWebActivity(this,
                            ((HomeProductEntity.FirmProductsVoBean) (adapter.getData().get(position))).getLink()));
        });


        mViewModel.getPayResult(mOrderNo, mOrderPayNo);
        mHomeViewModel.getHomeProduct();
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
                startActivity(new Intent(this, OrderListActivity.class).putExtra("type",0));
                finish();
                break;
            case R.id.go_home_view:
                startActivity(new Intent(this, MainActivity.class));
                finish();
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.payResultLiveData.observe(this, resultEntity -> {
            if (Double.parseDouble(resultEntity.getIntegral()) <= 0){
                mBinding.integralLl.setVisibility(View.INVISIBLE);
            }else {
                mBinding.integralLl.setVisibility(View.VISIBLE);
            }
            switch (resultEntity.getResult()){
                case 1://支付成功
                    mBinding.tagIv.setVisibility(View.VISIBLE);
                    mBinding.statusTv.setText("支付成功");
                    mBinding.statusTv.setTextColor(getResources().getColor(R.color.color_30));
                    mBinding.fallMoney.setText("本单优惠¥" + resultEntity.getDiscountAmount());
                    mBinding.tagView.setText("本单预计获得");
                    mBinding.integralTv.setText(resultEntity.getIntegral()+"");
                    mBinding.integralAll.setText(resultEntity.getIntegralBalance()+"");
                    payResultBannerAdapter.setNewData(resultEntity.getActiveParams().getBanner());
                    break;
                case 0://处理中
                case 2://支付失败
                case 3://已取消
                    mBinding.tagIv.setVisibility(View.GONE);
                    mBinding.statusTv.setText(resultEntity.getMsg());
                    mBinding.statusTv.setTextColor(getResources().getColor(R.color.color_34));
                    mBinding.fallMoney.setText("");
                    mBinding.tagView.setText("预计下单可获得");
                    mBinding.integralTv.setText(resultEntity.getIntegral()+"");
                    mBinding.integralAll.setText(resultEntity.getIntegralBalance()+"");
                    payResultBannerAdapter.setNewData(resultEntity.getActiveParams().getBanner());
                    break;
            }
        });

        mHomeViewModel.productLiveData.observe(this, firmProductsVoBeans -> {
            if (firmProductsVoBeans != null && firmProductsVoBeans.size() > 0) {
                mExchangeAdapter.setNewData(firmProductsVoBeans);
            }
        });
    }

    public static void openPayResultPage(Activity activity, String orderNo, String orderPayNo){
        Intent intent = new Intent(activity, RefuelingPayResultActivity.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("orderPayNo", orderPayNo);
        activity.startActivity(intent);
    }
}