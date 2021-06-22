package com.xxjy.jyyh.ui.pay;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Html;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.PayResultProductAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityPayResultBinding;
import com.xxjy.jyyh.dialog.NavigationDialog;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.PayResultProductBean;
import com.xxjy.jyyh.ui.car.CarServeDetailsActivity;
import com.xxjy.jyyh.ui.home.HomeViewModel;
import com.xxjy.jyyh.ui.mine.MyCouponActivity;
import com.xxjy.jyyh.ui.oil.OilDetailsActivity;
import com.xxjy.jyyh.ui.order.CarServeOrderListActivity;
import com.xxjy.jyyh.ui.order.OrderListActivity;
import com.xxjy.jyyh.ui.order.OtherOrderListActivity;
import com.xxjy.jyyh.utils.GlideUtils;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.locationmanger.MapIntentUtils;
import com.xxjy.jyyh.utils.toastlib.Toasty;
import com.xxjy.jyyh.wight.MyCountDownTime;

import java.util.ArrayList;
import java.util.List;

public class CarServePayResultActivity extends BindingActivity<ActivityPayResultBinding, CarServePayResultViewModel> {

    private String mOrderNo;
    private String mOrderPayNo;
    private MyCountDownTime mCountDownTime;

    private OilEntity.StationsBean oilStationBean;
    private PayResultProductAdapter payResultProductAdapter;
    private List<PayResultProductBean> payResultProductBeanList = new ArrayList<>();

//    private HomeViewModel homeViewModel;

    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("支付结果");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);

        mOrderNo = getIntent().getStringExtra("orderNo");
        mOrderPayNo = getIntent().getStringExtra("orderPayNo");
        mCountDownTime = MyCountDownTime.getInstence(3 * 1000, 1000);
        mCountDownTime.setOnTimeCountDownListener(new MyCountDownTime.OnTimeCountDownListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                mBinding.payingStatusView.setText("支付结果查询中…\n" + (millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
//                mBinding.payingStatusView.setVisibility(View.GONE);
                mBinding.payingStatusView.setText("支付中，请稍后");
//                mViewModel.getPayResult(mOrderNo, mOrderPayNo);
                mBinding.checkView.setEnabled(true);
                getPayResult();
            }
        });
        mBinding.goHomeView.setText(Html.fromHtml("<u>返回首页 ></u>"));//下划线

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mBinding.productRecyclerview.setLayoutManager(linearLayoutManager);
        payResultProductAdapter = new PayResultProductAdapter(payResultProductBeanList);
        mBinding.productRecyclerview.setAdapter(payResultProductAdapter);

//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


    }

    @Override
    protected void initListener() {
        mBinding.checkView.setOnClickListener(this::onViewClicked);
        mBinding.goOrderView.setOnClickListener(this::onViewClicked);
        mBinding.goHomeView.setOnClickListener(this::onViewClicked);
        mBinding.goEquityOrderView.setOnClickListener(this::onViewClicked);
        mBinding.oilStationAddressNavigationView.setOnClickListener(this::onViewClicked);
        mBinding.useView.setOnClickListener(this::onViewClicked);
        mBinding.goMoreOilView.setOnClickListener(this::onViewClicked);
        mBinding.againView.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_view:
//                getPayResult();
                mCountDownTime.start();
                mBinding.checkView.setEnabled(false);
                break;
            case R.id.go_order_view:
                startActivity(new Intent(this, CarServeOrderListActivity.class));
                finish();
                break;
            case R.id.go_home_view:
                if (UserConstants.getGoneIntegral()) {
                    UiUtils.jumpToHome(this, Constants.TYPE_OIL);
                } else {
                    UiUtils.jumpToHome(this, Constants.TYPE_HOME);
                }
                finish();
                break;
            case R.id.go_equity_order_view:
                startActivity(new Intent(this, MyCouponActivity.class));
                finish();
                break;
            case R.id.car_serve_shop_address_navigation_view:
                if (oilStationBean == null) {
                    return;
                }
                if (MapIntentUtils.isPhoneHasMapNavigation()) {
                    NavigationDialog navigationDialog = new NavigationDialog(this,
                            oilStationBean.getStationLatitude(), oilStationBean.getStationLongitude(),
                            oilStationBean.getGasName());
                    navigationDialog.show();
                } else {
                    showToastWarning("您当前未安装地图软件，请先安装");
                }

                break;
            case R.id.use_view:
                if (oilStationBean == null) {
                    return;
                }
                Intent intent = new Intent(this, OilDetailsActivity.class);
                intent.putExtra(Constants.GAS_STATION_ID, oilStationBean.getGasId());
                intent.putExtra(Constants.OIL_NUMBER_ID, oilStationBean.getOilNo());
                startActivity(intent);
                finish();

                break;
            case R.id.go_more_oil_view:
                UiUtils.jumpToHome(this, Constants.TYPE_OIL);
                finish();
                break;
            case R.id.again_view:
                finish();
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.payResultLiveData.observe(this, data -> {
            if (data != null) {
                if (data.getProductParams() != null) {
                    if (data.getProductParams().getType() == 2) {
                        mBinding.goEquityOrderView.setVisibility(View.VISIBLE);

                    } else {
                        mBinding.goEquityOrderView.setVisibility(View.GONE);
                    }

                    if (data.getProductParams().getHomeOilStations() != null && data.getProductParams().getHomeOilStations().size() > 0) {
                        mBinding.oilLayout.setVisibility(View.VISIBLE);
                        oilStationBean = data.getProductParams().getHomeOilStations().get(0);
                        GlideUtils.loadImage(this, oilStationBean.getGasTypeImg(), mBinding.oilStationImageView);
                        mBinding.oilStationNameView.setText(oilStationBean.getGasName());
                        mBinding.oilStationAddressView.setText(oilStationBean.getGasAddress());
                        mBinding.oilStationAddressNavigationView.setText(String.format("%.2f", oilStationBean.getDistance()) + "KM");
                    } else {
                        mBinding.oilLayout.setVisibility(View.GONE);
                    }


                    if (data.getProductParams().getProductResult() != null && data.getProductParams().getProductResult().size() > 0) {
                        mBinding.productLayout.setVisibility(View.VISIBLE);
                        payResultProductAdapter.setNewData(data.getProductParams().getProductResult());
                    } else {
                        mBinding.productLayout.setVisibility(View.GONE);
                    }

                }
                mBinding.payingStatusView.setText(""+data.getMsg());
                switch (data.getResult()) {
                    case 0://处理中
                        mBinding.checkView.setVisibility(View.VISIBLE);
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
        mViewModel.orderLiveData.observe(this, data -> {


            if (data != null) {
                mBinding.payingLayout.setVisibility(View.GONE);
                mBinding.orderLayout.setVisibility(View.VISIBLE);
                mBinding.productNameView.setText(data.getProductName());
                mBinding.shopNameView.setText(data.getStoreName());
                mBinding.couponCodeView.setText("券码：" + data.getVerificationCode());
                mBinding.couponDescView.setText(data.getCarTypeDesc());
                mBinding.timeView.setText("有效期至:" + data.getPayTime().substring(0, 10) + " - " + data.getExpireTime().substring(0, 10));
                GlideUtils.loadImage(this, data.getQrcodeBase64(), mBinding.qrcodeImageView);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTime != null) {
            mCountDownTime.cancel();
            mCountDownTime = null;
        }
    }

    private void getOilStation(){
        //首页卡片
//        homeViewModel.getHomeCard(Double.parseDouble(UserConstants.getLatitude()),Double.parseDouble( UserConstants.getLongitude()),"");
    }

    private void getPayResult() {
        mViewModel.getPayResult(mOrderNo, mOrderPayNo, UserConstants.getLatitude(), UserConstants.getLongitude());
    }

    private void getOrderInfo() {
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