package com.xxjy.jyyh.ui.oil;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alipay.sdk.app.PayTask;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilCheckedAdapter;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.adapter.OilStationFlexAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.constants.PayTypeConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityOilDetailBinding;
import com.xxjy.jyyh.dialog.GasStationLocationTipsDialog;
import com.xxjy.jyyh.dialog.LocationTipsDialog;
import com.xxjy.jyyh.dialog.NavigationDialog;
import com.xxjy.jyyh.dialog.OilAmountDialog;
import com.xxjy.jyyh.dialog.OilCouponDialog;
import com.xxjy.jyyh.dialog.OilGunDialog;
import com.xxjy.jyyh.dialog.OilPayDialog;
import com.xxjy.jyyh.dialog.OilTipsDialog;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilPayTypeEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.ui.home.HomeViewModel;
import com.xxjy.jyyh.ui.pay.PayQueryActivity;
import com.xxjy.jyyh.ui.pay.RefuelingPayResultActivity;
import com.xxjy.jyyh.ui.web.WeChatWebPayActivity;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.WXSdkManager;
import com.xxjy.jyyh.utils.locationmanger.MapIntentUtils;

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
    //是否需要跳转支付确认页
    private boolean shouldJump = false;
    private PayOrderEntity mPayOrderEntity;
    private boolean isFar = false;//油站是否在距离内
    private GasStationLocationTipsDialog mGasStationTipsDialog;
    private LocationTipsDialog mLocationTipsDialog;
    private String mOilNo;

    /**
     * @param orderEntity 消息事件：支付后跳转支付确认页
     */
    @BusUtils.Bus(tag = EventConstants.EVENT_JUMP_PAY_QUERY, sticky = true)
    public void onEvent(PayOrderEntity orderEntity) {
        showJump(orderEntity);
    }

    private void showJump(PayOrderEntity orderEntity) {
        if (orderEntity == null) return;
        if (shouldJump) {
            shouldJump = false;
            PayQueryActivity.openPayQueryActivity(this,
                    orderEntity.getOrderPayNo(), orderEntity.getOrderNo());
            closeDialog();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showJump(mPayOrderEntity);
    }

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, false);
        BusUtils.register(this);

        mGasId = getIntent().getStringExtra(Constants.GAS_STATION_ID);
        mOilNo = getIntent().getStringExtra(Constants.OIL_NUMBER_ID);

        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

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
//        mBinding.oilCheckRecyclerView.setLayoutManager(
//                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        FlexboxLayoutManager flexboxLayoutManager1 = new FlexboxLayoutManager(this);
        flexboxLayoutManager1.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager1.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager1.setAlignItems(AlignItems.FLEX_START);
        mBinding.oilCheckRecyclerView.setLayoutManager(flexboxLayoutManager1);
        mOilCheckedAdapter = new OilCheckedAdapter(R.layout.adapter_oil_checked, mOilCheckedList);
        mBinding.oilCheckRecyclerView.setAdapter(mOilCheckedAdapter);

        //油号列表
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mOilNumAdapter = new OilNumAdapter(R.layout.adapter_oil_num_layout, mOilNumList);
        mBinding.recyclerView.setAdapter(mOilNumAdapter);
        mOilNumAdapter.setOnItemClickListener((adapter, view, position) -> {
            UiUtils.canClickViewStateDelayed(view, 1000);
            List<OilEntity.StationsBean.OilPriceListBean> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelected(false);
            }
            data.get(position).setSelected(true);
            adapter.notifyDataSetChanged();
            mBinding.oilLiterTv.setText("¥" + data.get(position).getPriceYfq() + "/L");
            mBinding.oilNumTv.setText(data.get(position).getOilName());
            mOilCheckedList.set(0, data.get(position).getOilName() +
                    getOilKind(data.get(position).getOilType() + ""));
            mOilCheckedAdapter.notifyDataSetChanged();
            showGunDialog(mStationsBean, position);
        });

        initWebViewClient();
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
        mBinding.oilNavigationTv.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.oil_navigation_tv:
                if (MapIntentUtils.isPhoneHasMapNavigation()) {
                    NavigationDialog navigationDialog = new NavigationDialog(this,
                            mStationsBean.getStationLatitude(), mStationsBean.getStationLongitude(),
                            mStationsBean.getGasName());
                    navigationDialog.show();
                } else {
                    showToastWarning("您当前未安装地图软件，请先安装");
                }
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.loadingView().observe(this, aBoolean -> {
            if (aBoolean) {
                showLoadingDialog();
            } else {
                dismissLoadingDialog();
            }
        });

        mHomeViewModel.locationLiveData.observe(this, locationEntity -> {
            if (locationEntity.isSuccess()) {
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
            } else {
                mLat = 0;
                mLng = 0;
                mViewModel.getOilDetail(mGasId, mLat, mLng);
                showFailLocation();
            }
        });

        mViewModel.oilLiveData.observe(this, stationsBean -> {
            mStationsBean = stationsBean;
            mBinding.oilNameTv.setText(mStationsBean.getGasName());
            mBinding.oilTagIv.setVisibility(stationsBean.isIsSign() ? View.VISIBLE : View.GONE);
            mBinding.oilAddressTv.setText(mStationsBean.getGasAddress());
            mBinding.oilLiterTv.setText("¥" + mStationsBean.getOilPriceList().get(0).getPriceYfq() + "/L");
            mBinding.oilNumTv.setText(mStationsBean.getOilPriceList().get(0).getOilName());
            mBinding.oilNavigationTv.setText(mStationsBean.getDistance() + "km");
            mBinding.invokeTv.setVisibility(mStationsBean.getIsInvoice() == 0 ? View.VISIBLE : View.GONE);

            if (mStationsBean.getCzbLabels() != null && mStationsBean.getCzbLabels().size() > 0) {
                mTagList = mStationsBean.getCzbLabels();
                mFlexAdapter.setNewData(mTagList);
                mBinding.oilTagRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mBinding.oilTagRecyclerView.setVisibility(View.INVISIBLE);
            }

            //已选择列表
            mOilCheckedList.clear();


            //已选择列表   油号列表
            mOilNumList = mStationsBean.getOilPriceList();
            if (TextUtils.isEmpty(mOilNo)){
                mOilCheckedList.add(mStationsBean.getOilPriceList().get(0).getOilName() +
                        getOilKind(mStationsBean.getOilPriceList().get(0).getOilType() + ""));
                mOilCheckedAdapter.setNewData(mOilCheckedList);

                mOilNumList.get(0).setSelected(true);
            }else {
                for (int i = 0; i < mOilNumList.size(); i++) {
                    if (TextUtils.equals(mOilNo, String.valueOf(mOilNumList.get(i).getOilNo()))){
                        mOilCheckedList.add(mStationsBean.getOilPriceList().get(i).getOilName() +
                                getOilKind(mStationsBean.getOilPriceList().get(i).getOilType() + ""));

                        mOilCheckedAdapter.setNewData(mOilCheckedList);
                        mOilNumList.get(i).setSelected(true);
                    }
                }
            }
            mOilNumAdapter.setNewData(mOilNumList);

            mHomeViewModel.checkDistance(mStationsBean.getGasId());
        });

        //支付结果回调
        mHomeViewModel.payOrderLiveData.observe(this, payOrderEntity -> {
            if (payOrderEntity.getResult() == 0) {//支付未完成
                switch (payOrderEntity.getPayType()) {
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
                //                BusUtils.postSticky(EventConstants.EVENT_JUMP_PAY_QUERY, payOrderEntity);
                mPayOrderEntity = payOrderEntity;
                shouldJump = true;
            } else if (payOrderEntity.getResult() == 1) {//支付成功
                jumpToPayResultAct(payOrderEntity.getOrderPayNo(), payOrderEntity.getOrderNo());
            } else {
                showToastWarning(payOrderEntity.getMsg());
            }
        });

        mHomeViewModel.distanceLiveData.observe(this, oilDistanceEntity -> {
            if (oilDistanceEntity.isHere()) {
                isFar = false;
            } else {
                isFar = true;
            }
        });
    }

    private void showGunDialog(OilEntity.StationsBean stationsBean, int oilNoPosition) {
        //枪号dialog
        mOilGunDialog = new OilGunDialog(this, stationsBean, oilNoPosition);
        mOilGunDialog.setOnItemClickedListener(new OilGunDialog.OnItemClickedListener() {
            @Override
            public void onOilGunClick(BaseQuickAdapter adapter, View view, int position) {
                if (isFar) {
                    showChoiceOil(stationsBean.getGasName(), view, position, stationsBean, oilNoPosition, adapter);
                } else {
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
                }
            }

            @Override
            public void closeAll() {
                closeDialog();
            }
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
//                showTipsDialog(stationsBean, oilNoPosition, gunNoPosition, orderId, payAmount, view);
                showPayDialog(stationsBean, oilNoPosition, gunNoPosition, orderId, payAmount);
            }

            @Override
            public void closeAll() {
                closeDialog();
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
//                GasStationLocationTipsDialog gasStationLocationTipsDialog =
//                        new GasStationLocationTipsDialog(OilDetailActivity.this,
//                                mBinding.getRoot(), "成都加油站");
//                gasStationLocationTipsDialog.show();
                List<OilPayTypeEntity> data = adapter.getData();
                for (OilPayTypeEntity item : data) {
                    item.setSelect(false);
                }
                data.get(position).setSelect(true);
                adapter.notifyDataSetChanged();
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

    private void showFailLocation() {
        mLocationTipsDialog = new LocationTipsDialog(this, mBinding.titleTv);
        mLocationTipsDialog.setOnClickListener(view -> {
            switch (view.getId()) {
                case R.id.location_agin:
                    requestPermission();
                    break;
                case R.id.all_oil:
                    BusUtils.postSticky(EventConstants.EVENT_CHANGE_FRAGMENT);
                    break;
            }
        });
        mLocationTipsDialog.show();
    }

    private void showChoiceOil(String stationName, View view, int position, OilEntity.StationsBean stationsBean, int oilNoPosition, BaseQuickAdapter adapter) {
        mGasStationTipsDialog = new GasStationLocationTipsDialog(this, view, stationName);
        mGasStationTipsDialog.setOnClickListener(view1 -> {
            switch (view1.getId()) {
                case R.id.select_agin://重新选择
                    closeDialog();
                    break;
                case R.id.navigation_tv://导航过去
                    if (MapIntentUtils.isPhoneHasMapNavigation()) {
                        NavigationDialog navigationDialog = new NavigationDialog(this,
                                mStationsBean.getStationLatitude(), mStationsBean.getStationLongitude(),
                                mStationsBean.getGasName());
                        closeDialog();
                        navigationDialog.show();
                    } else {
                        showToastWarning("您当前未安装地图软件，请先安装");
                    }
                    break;
                case R.id.continue_view://继续支付
                    isFar = false;
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
                    break;
            }

        });
        mGasStationTipsDialog.show();
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
        if (mOilGunDialog != null) {
            mOilGunDialog.dismiss();
            mOilGunDialog = null;
        }
        if (mOilAmountDialog != null) {
            mOilAmountDialog.dismiss();
            mOilAmountDialog = null;
        }
        if (mOilCouponDialog != null) {
            mOilCouponDialog.dismiss();
            mOilCouponDialog = null;
        }
        if (mOilTipsDialog != null) {
            mOilTipsDialog.dismiss();
            mOilTipsDialog = null;
        }
        if (mOilPayDialog != null) {
            mOilPayDialog.dismiss();
            mOilPayDialog = null;
        }
        if (mLocationTipsDialog != null) {
            mLocationTipsDialog = null;
        }
        if (mGasStationTipsDialog != null) {
            mGasStationTipsDialog = null;
        }

        //关掉以后重新刷新数据,否则再次打开时上下选中不一致
        mViewModel.getOilDetail(mGasId, mLat, mLng);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusUtils.removeSticky(EventConstants.EVENT_JUMP_PAY_QUERY);
    }

    protected void initWebViewClient() {
        mBinding.payWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView webView, String url) {
                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true;
                }

                isShouldAutoOpenWeb = false;
                /**
                 * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
                 */
                final PayTask task = new PayTask(OilDetailActivity.this);
                boolean isIntercepted = task.payInterceptorWithUrl(url, true,
                        result -> runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                jumpToPayResultAct(result.getResultCode(), result.getResultCode());
                            }
                        }));

                /**
                 * 判断是否成功拦截
                 * 若成功拦截，则无需继续加载该URL；否则继续加载
                 */
                if (!isIntercepted) {   //如果不使用sdk直接将url抛出到浏览器
                    UiUtils.openPhoneWebUrl(OilDetailActivity.this, url);
                }
                return true;
            }
        });
    }
}