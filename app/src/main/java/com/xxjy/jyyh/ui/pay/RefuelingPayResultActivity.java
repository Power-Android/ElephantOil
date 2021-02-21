package com.xxjy.jyyh.ui.pay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.HomeExchangeAdapter;
import com.xxjy.jyyh.adapter.PayResultBannerAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.databinding.ActivityRefuelingPayResultBinding;
import com.xxjy.jyyh.entity.HomeProductEntity;
import com.xxjy.jyyh.entity.PayResultEntity;
import com.xxjy.jyyh.ui.home.HomeViewModel;
import com.xxjy.jyyh.ui.order.OrderListActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.wight.MyCountDownTime;

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
    private MyCountDownTime mCountDownTime;
//    private MyCountDownTime mCountDownTime2;
    // 线程标志
    private boolean isStop = false;
    private boolean isFirst = true;

    private Thread changeBgThread;

    @Override
    protected void initView() {
        mBinding.tbToolbar.setNavigationOnClickListener(v -> finish());
//        BarUtils.setStatusBarColor(this, Color.parseColor("#1676FF"));
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.tbToolbar);
        mCountDownTime = MyCountDownTime.getInstence(3 * 1000, 1000);
//        mCountDownTime2 = MyCountDownTime.getInstence(3 * 1000, 1000);
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
        mBinding.timeView.setText(TimeUtils.date2String(TimeUtils.getNowDate(), "MM月dd日 HH:mm:ss"));
//        mBinding.tryAgainView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        mBinding.tryAgainView.getPaint().setAntiAlias(true);

        mHomeViewModel.getHomeProduct();
        mCountDownTime.setOnTimeCountDownListener(new MyCountDownTime.OnTimeCountDownListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.waitTimeView.setText("支付结果查询中…\n" + (millisUntilFinished / 1000) + "s");
//                mBinding.timeView.setText(TimeUtils.date2String(TimeUtils.getNowDate(), "MM月dd日 HH:mm:ss"));
            }

            @Override
            public void onFinish() {
                mBinding.waitTimeView.setVisibility(View.GONE);
//                mBinding.timeView.setText(TimeUtils.date2String(TimeUtils.getNowDate(), "MM月dd日 HH:mm:ss"));
                mViewModel.getPayResult(mOrderNo, mOrderPayNo);
            }
        });
//        mCountDownTime.start();
//        mCountDownTime2.setOnTimeCountDownListener(new MyCountDownTime.OnTimeCountDownListener() {
//            @Override
//            public void onTick(long millisUntilFinished) {
////                mBinding.waitTimeView.setText("支付结果查询中…\n" + (millisUntilFinished / 1000) + "s");
////                mBinding.timeView.setText(TimeUtils.date2String(TimeUtils.getNowDate(), "MM月dd日 HH:mm:ss"));
//            }
//
//            @Override
//            public void onFinish() {
////                mBinding.waitTimeView.setVisibility(View.GONE);
////                mBinding.timeView.setText(TimeUtils.date2String(TimeUtils.getNowDate(), "MM月dd日 HH:mm:ss"));
//                mViewModel.getPayResult(mOrderNo, mOrderPayNo);
//            }
//        });

        // 开启新线程，1秒一次更新背景图
        changeBgThread= new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    SystemClock.sleep(500);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if(mBinding!=null){
                                if (isFirst) {
                                    mBinding.topBgLayout.setBackgroundResource(R.drawable.pay_top_bg);
                                } else {
                                    mBinding.topBgLayout.setBackgroundResource(R.drawable.pay_top_bg_2);
                                }
                                mBinding.timeView.setText(TimeUtils.date2String(TimeUtils.getNowDate(), "MM月dd日 HH:mm:ss"));
                                isFirst = !isFirst;
                            }

                        }
                    });
                }
            }
        });
        changeBgThread.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        mCountDownTime2.start();
    }

    @Override
    protected void initListener() {
        mBinding.goOrderView.setOnClickListener(this::onViewClicked);
        mBinding.goHomeView.setOnClickListener(this::onViewClicked);
        mBinding.tv1.setOnClickListener(this::onViewClicked);
        mBinding.tv2.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_order_view:
                startActivity(new Intent(this, OrderListActivity.class).putExtra("type", 0));
                finish();
                break;
            case R.id.go_home_view:
                UiUtils.jumpToHome(this, Constants.TYPE_HOME);
                finish();
            case R.id.tv1:
            case R.id.tv2:
                mCountDownTime.start();
                mBinding.checkBtLayout.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.payResultLiveData.observe(this, resultEntity -> {
            if (Double.parseDouble(resultEntity.getIntegral()) <= 0) {
                mBinding.integralLl.setVisibility(View.INVISIBLE);
            } else {
                mBinding.integralLl.setVisibility(View.VISIBLE);
            }
            switch (resultEntity.getResult()) {
                case 1://支付成功
//                    mBinding.queryStatusView.setVisibility(View.GONE);
//                    mBinding.tryAgainView.setVisibility(View.GONE);
                    mBinding.topLayout.setVisibility(View.VISIBLE);
                    mBinding.integralLl.setVisibility(View.VISIBLE);
                    mBinding.priceLayout2.setVisibility(View.VISIBLE);
                    mBinding.queryStatusView.setVisibility(View.GONE);
                    mBinding.btLayout.setVisibility(View.VISIBLE);
                    mBinding.tvTitle.setText("支付成功");
                    mBinding.decView.setText("支付成功，请和加油员确认您的油机金额");
                    mBinding.fallMoney.setText("¥" + resultEntity.getDiscountAmount());
                    if (resultEntity.getGasParams() != null) {
                        mBinding.numView.setText(resultEntity.getGasParams().getGunNo() + "号枪" + " | " + resultEntity.getGasParams().getOilName());
                        mBinding.stationNameView.setText("加油油站："+resultEntity.getGasParams().getGasName());
                    }
                    mBinding.tagView.setText("本单预计获得");
                    mBinding.payAmountView.setText("¥" + resultEntity.getGasParams().getAmount() + "");
                    mBinding.payAmountView2.setText("¥" + resultEntity.getPayAmount() + "");
                    mBinding.integralTv.setText(resultEntity.getIntegral() + "");
                    mBinding.integralAll.setText(resultEntity.getIntegralBalance() + "");
                    if(resultEntity.getActiveParams()!=null&&resultEntity.getActiveParams().getBanner()!=null){
                        payResultBannerAdapter.setNewData(resultEntity.getActiveParams().getBanner());
                    }

                    break;
                case 0://处理中
                case 2://支付失败
                case 3://已取消
                    mBinding.topLayout.setVisibility(View.GONE);
                    mBinding.integralLl.setVisibility(View.GONE);
                    mBinding.priceLayout2.setVisibility(View.GONE);
                    mBinding.queryStatusView.setVisibility(View.VISIBLE);
                    mBinding.btLayout.setVisibility(View.VISIBLE);
                    mBinding.queryStatusView.setText(resultEntity.getMsg());
                    mBinding.fallMoney.setText("--");
//                    mBinding.decView.setText("请和加油员确认您的油机金额");
//                    mBinding.tagView.setText("预计下单可获得");
//                    mBinding.integralTv.setText(resultEntity.getIntegral() + "");
//                    mBinding.integralAll.setText(resultEntity.getIntegralBalance() + "");
                    if(resultEntity.getActiveParams()!=null&&resultEntity.getActiveParams().getBanner()!=null){
                        payResultBannerAdapter.setNewData(resultEntity.getActiveParams().getBanner());
                    }
                    break;
            }
        });

        mHomeViewModel.productLiveData.observe(this, firmProductsVoBeans -> {
            if (firmProductsVoBeans != null && firmProductsVoBeans.size() > 0) {
                mExchangeAdapter.setNewData(firmProductsVoBeans);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mCountDownTime != null) {
            mCountDownTime.cancel();
            mCountDownTime = null;
        }
//        if (mCountDownTime2 != null) {
//            mCountDownTime2.cancel();
//            mCountDownTime2 = null;
//        }
        // 关闭定时器
        isStop = true;

        super.onDestroy();

    }

    public static void openPayResultPage(Activity activity, String orderNo, String orderPayNo) {
        Intent intent = new Intent(activity, RefuelingPayResultActivity.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("orderPayNo", orderPayNo);
        activity.startActivity(intent);
    }
}