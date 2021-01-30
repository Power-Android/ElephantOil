package com.xxjy.jyyh.ui.oil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilCheckedAdapter;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.adapter.OilStationFlexAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.PayTypeConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityOilDetailBinding;
import com.xxjy.jyyh.dialog.GasStationLocationTipsDialog;
import com.xxjy.jyyh.dialog.OilAmountDialog;
import com.xxjy.jyyh.dialog.OilCouponDialog;
import com.xxjy.jyyh.dialog.OilGunDialog;
import com.xxjy.jyyh.dialog.OilPayDialog;
import com.xxjy.jyyh.dialog.OilTipsDialog;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.LocationEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.ui.home.HomeViewModel;
import com.xxjy.jyyh.ui.pay.PayResultActivity;
import com.xxjy.jyyh.ui.pay.RefuelingPayResultActivity;
import com.xxjy.jyyh.ui.web.WeChatWebPayActivity;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.WXSdkManager;

import java.util.ArrayList;
import java.util.List;

public class OilDetailActivity extends BindingActivity<ActivityOilDetailBinding, OilViewModel> {
    private List<OilEntity.StationsBean.CzbLabelsBean> mTagList = new ArrayList<>();
    private List<OilEntity.StationsBean.OilPriceListBean> mOilNumList = new ArrayList<>();
    private List<String> mOilCheckedList = new ArrayList<>(2);
    private OilNumAdapter mOilNumAdapter;
    private OilGunDialog mOilGunDialog;
    private OilAmountDialog mOilAmountDialog;
    private OilCouponDialog mOilCouponDialog;
    private OilTipsDialog mOilTipsDialog;
    private OilPayDialog mOilPayDialog;
    private HomeViewModel mHomeViewModel;
    private double mLng, mLat;
    private String mGasId;
    private OilEntity.StationsBean mStationsBean;
    private OilStationFlexAdapter mFlexAdapter;
    private OilCheckedAdapter mOilCheckedAdapter;
    private boolean isShouldAutoOpenWeb = false;    //标记是否应该自动打开浏览器进行支付


    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, false);
        mGasId = getIntent().getStringExtra(Constants.GAS_STATION_ID);
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        if (Double.parseDouble(UserConstants.getLongitude()) != 0 && Double.parseDouble(UserConstants.getLatitude()) != 0) {
            mLat = Double.parseDouble(UserConstants.getLatitude());
            mLng = Double.parseDouble(UserConstants.getLongitude());
            //首页油站
            mViewModel.getOilDetail(mGasId, mLat, mLng);
        }

        //请求定位权限
        requestPermission();

        //标签列表

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        mBinding.oilTagRecyclerView.setLayoutManager(flexboxLayoutManager);
        mFlexAdapter = new OilStationFlexAdapter(R.layout.adapter_oil_detail_tag, mTagList);
        mBinding.oilTagRecyclerView.setAdapter(mFlexAdapter);

        //已选择列表
        mBinding.oilCheckRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mOilCheckedAdapter = new OilCheckedAdapter(R.layout.adapter_oil_checked, mOilCheckedList);
        mBinding.oilCheckRecyclerView.setAdapter(mOilCheckedAdapter);

        //油号列表
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mOilNumAdapter = new OilNumAdapter(R.layout.adapter_oil_num_layout, mOilNumList);
        mBinding.recyclerView.setAdapter(mOilNumAdapter);
        mOilNumAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<OilEntity.StationsBean.OilPriceListBean> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelected(false);
            }
            data.get(position).setSelected(true);
            adapter.notifyDataSetChanged();
            mBinding.oilLiterTv.setText("￥" + data.get(position).getPriceYfq() + "/L");
            mBinding.oilNumTv.setText(data.get(position).getOilName());
            mOilCheckedList.set(0, data.get(position).getOilName() +
                    getOilKind(data.get(position).getOilType() + ""));
            mOilCheckedAdapter.notifyDataSetChanged();
            showGunDialog(mStationsBean, position);
        });

    }

    private void requestPermission() {
        PermissionUtils.permission(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        mHomeViewModel.getLocation();
                    }

                    @Override
                    public void onDenied() {
                        showToastWarning("权限被拒绝，部分产品功能将无法使用！");
                    }
                })
                .request();
    }


    @Override
    protected void initListener() {
        mBinding.backIv.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mHomeViewModel.locationLiveData.observe(this, locationEntity -> {
            UserConstants.setLatitude(String.valueOf(locationEntity.getLat()));
            UserConstants.setLongitude(String.valueOf(locationEntity.getLng()));
            DPoint p = new DPoint(locationEntity.getLat(), locationEntity.getLng());
            DPoint p2 = new DPoint(mLat, mLng);
            float distance = CoordinateConverter.calculateLineDistance(p, p2);
            if (distance > 100) {
                mLng = locationEntity.getLng();
                mLat = locationEntity.getLat();
                LogUtils.i("定位成功：" + locationEntity.getLng() + "\n" + locationEntity.getLat());
                //首页油站
                mViewModel.getOilDetail(mGasId, mLat, mLng);
            }
        });

        mViewModel.oilLiveData.observe(this, stationsBean -> {
            mStationsBean = stationsBean;
            mBinding.oilNameTv.setText(mStationsBean.getGasName());
            mBinding.oilAddressTv.setText(mStationsBean.getGasAddress());
            mBinding.oilLiterTv.setText("￥" + mStationsBean.getOilPriceList().get(0).getPriceYfq() + "/L");
            mBinding.oilNumTv.setText(mStationsBean.getOilPriceList().get(0).getOilName());
            mBinding.oilNavigationTv.setText(mStationsBean.getDistance() + "km");

            if (mStationsBean.getCzbLabels() != null && mStationsBean.getCzbLabels().size() > 0) {
                mTagList = mStationsBean.getCzbLabels();
                mFlexAdapter.setNewData(mTagList);
                mBinding.oilTagRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mBinding.oilTagRecyclerView.setVisibility(View.INVISIBLE);
            }

            //已选择列表
            mOilCheckedList.clear();
            mOilCheckedList.add(mStationsBean.getOilPriceList().get(0).getOilName() +
                    getOilKind(mStationsBean.getOilPriceList().get(0).getOilType() + ""));
            mOilCheckedAdapter.setNewData(mOilCheckedList);

            //油号列表
            mOilNumList = mStationsBean.getOilPriceList();
            mOilNumList.get(0).setSelected(true);
            mOilNumAdapter.setNewData(mOilNumList);
        });

        //支付结果回调
        mHomeViewModel.payOrderLiveData.observe(this, payOrderEntity -> {
            if (payOrderEntity.getResult() == 0){//支付未完成
                switch (payOrderEntity.getPayType()){
                    case PayTypeConstants.PAY_TYPE_WEIXIN://微信H5
                        WeChatWebPayActivity.openWebPayAct(this, payOrderEntity.getUrl());
                        break;
                    case PayTypeConstants.PAY_TYPE_WEIXIN_XCX://微信小程序
                        WXSdkManager.newInstance().useWXLaunchMiniProgramToPay(this, payOrderEntity.getOrderNo());
                        break;
                    case PayTypeConstants.PAY_TYPE_ZHIFUBAO://支付宝H5
                        boolean urlCanUse = UiUtils.checkZhifubaoSdkCanPayUrl(this,
                                payOrderEntity.getUrl(),
                                h5PayResultModel -> {//直接跳转支付宝
                                    jumpToPayResultAct(payOrderEntity.getOrderPayNo(),
                                            payOrderEntity.getOrderNo());
                                });
                        if (!urlCanUse) {//外部浏览器打开
                            isShouldAutoOpenWeb = true;
                            mBinding.payWebView.loadUrl(payOrderEntity.getUrl());
                            mBinding.payWebView.post(() -> {
                                if (isShouldAutoOpenWeb) {
                                    UiUtils.openPhoneWebUrl(this,
                                            payOrderEntity.getUrl());
                                }
                            });
                        }
                        break;
                }
            }else if (payOrderEntity.getResult() == 1){//支付成功
                jumpToPayResultAct(payOrderEntity.getOrderPayNo(), payOrderEntity.getOrderNo());
            }else {
                showToastWarning(payOrderEntity.getMsg());
            }
        });
    }

    private void showGunDialog(OilEntity.StationsBean stationsBean, int oilNoPosition) {
        //枪号dialog
        mOilGunDialog = new OilGunDialog(this, stationsBean, oilNoPosition);
        mOilGunDialog.setOnItemClickedListener((adapter, view, position) -> {
            List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelected(false);
            }
            data.get(position).setSelected(true);
            adapter.notifyDataSetChanged();
            if (mOilCheckedList.size() <= 1) {
                mOilCheckedList.add(1, data.get(position).getGunNo() + "号枪");
            } else {
                mOilCheckedList.set(1, data.get(position).getGunNo() + "号枪");
            }
            mOilCheckedAdapter.notifyDataSetChanged();
            showAmountDialog(stationsBean, oilNoPosition, position);
        });

        mOilGunDialog.show();
    }

    private void showAmountDialog(OilEntity.StationsBean stationsBean, int oilNoPosition, int gunNoPosition) {
        //快捷金额dialog  请输入加油金额 请选择优惠券 暂无优惠券
        mOilAmountDialog = new OilAmountDialog(this, this, stationsBean,
                oilNoPosition, gunNoPosition);
        mOilAmountDialog.setOnItemClickedListener(new OilAmountDialog.OnItemClickedListener() {
            @Override
            public void onOilDiscountClick(BaseQuickAdapter adapter, View view, int position,
                                           String amount, String oilNo) {
                if (position == 1 || position == 2) {
                    showCouponDialog(stationsBean, amount, oilNoPosition, gunNoPosition, position == 1);
                }
            }

            @Override
            public void onCreateOrder(View view, String orderId, String payAmount) {
                showTipsDialog(stationsBean, oilNoPosition, gunNoPosition, orderId, payAmount, view);
            }
        });

        mOilAmountDialog.show();
    }

    private void showCouponDialog(OilEntity.StationsBean stationsBean, String amount,
                                  int oilNoPosition, int gunNoPosition, boolean isPlat) {
        //优惠券dialog
        mOilCouponDialog = new OilCouponDialog(this, this, amount, stationsBean,
                oilNoPosition, gunNoPosition, isPlat);
        mOilCouponDialog.setOnItemClickedListener(new OilCouponDialog.OnItemClickedListener() {
            @Override
            public void onOilCouponClick(BaseQuickAdapter adapter, View view, int position, boolean isPlat) {
                List<CouponBean> data = adapter.getData();
                mOilAmountDialog.setCouponInfo(data.get(position), isPlat);
                mOilCouponDialog.dismiss();
            }

            @Override
            public void onNoCouponClick(boolean isPlat) {
                mOilAmountDialog.setCouponInfo(null, isPlat);
                mOilCouponDialog.dismiss();
            }
        });

        mOilCouponDialog.show();
    }

    private void showTipsDialog(OilEntity.StationsBean stationsBean, int oilNoPosition,
                                int gunNoPosition, String orderId, String payAmount, View view) {
        //温馨提示dialog
        mOilTipsDialog = new OilTipsDialog(this, this);
        mOilTipsDialog.setOnItemClickedListener(() -> {
            mOilTipsDialog.dismiss();
            //show的时候把订单信息传过去
            showPayDialog(stationsBean, oilNoPosition, gunNoPosition, orderId, payAmount);
        });

        mOilTipsDialog.show(view);
    }

    private void showPayDialog(OilEntity.StationsBean stationsBean, int oilNoPosition,
                               int gunNoPosition, String orderId, String payAmount) {
        //支付dialog
        mOilPayDialog = new OilPayDialog(this, this, stationsBean,
                oilNoPosition, gunNoPosition, orderId, payAmount);

        mOilPayDialog.setOnItemClickedListener(new OilPayDialog.OnItemClickedListener() {
            @Override
            public void onOilPayTypeClick(BaseQuickAdapter adapter, View view, int position) {
                GasStationLocationTipsDialog gasStationLocationTipsDialog =
                        new GasStationLocationTipsDialog(OilDetailActivity.this,
                                mBinding.getRoot(), "成都加油站");
                gasStationLocationTipsDialog.show();
            }

            @Override
            public void onCloseAllClick() {
                closeDialog();
            }

            @Override
            public void onPayOrderClick(String payType, String orderId, String payAmount) {
                mHomeViewModel.payOrder(payType, orderId, payAmount);
            }
        });

        mOilPayDialog.show();
    }

    private String getOilKind(String oilType) {
        switch (oilType) {
            case "1":
                return "汽油";
            case "2":
                return "柴油";
            case "3":
                return "天然气";
        }
        return "";
    }

    private void jumpToPayResultAct(String orderPayNo, String orderNo) {
        if (TextUtils.isEmpty(orderPayNo) && TextUtils.isEmpty(orderNo)) {
            return;
        }
        Intent intent = new Intent(this, RefuelingPayResultActivity.class);
        intent.putExtra("orderPayNo", orderPayNo);
        intent.putExtra("orderNo", orderNo);
        startActivity(intent);
        closeDialog();
    }

    private void closeDialog() {
        mOilGunDialog.dismiss();
        mOilAmountDialog.dismiss();
        mOilPayDialog.dismiss();

        mOilGunDialog = null;
        mOilAmountDialog = null;
        mOilPayDialog = null;

        //关掉以后重新刷新数据,否则再次打开时上下选中不一致
        mViewModel.getOilDetail(mGasId, mLat, mLng);
    }
}