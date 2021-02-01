package com.xxjy.jyyh.ui.home;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.HomeExchangeAdapter;
import com.xxjy.jyyh.adapter.HomeOftenAdapter;
import com.xxjy.jyyh.adapter.OilStationFlexAdapter;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.constants.BannerPositionConstants;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.constants.PayTypeConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.FragmentHomeBinding;
import com.xxjy.jyyh.dialog.GasStationLocationTipsDialog;
import com.xxjy.jyyh.dialog.LocationTipsDialog;
import com.xxjy.jyyh.dialog.NavigationDialog;
import com.xxjy.jyyh.dialog.OilAmountDialog;
import com.xxjy.jyyh.dialog.OilCouponDialog;
import com.xxjy.jyyh.dialog.OilGunDialog;
import com.xxjy.jyyh.dialog.OilNumDialog;
import com.xxjy.jyyh.dialog.OilPayDialog;
import com.xxjy.jyyh.dialog.OilTipsDialog;
import com.xxjy.jyyh.dialog.ReceiveRewardDialog;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.EventEntity;
import com.xxjy.jyyh.entity.HomeProductEntity;
import com.xxjy.jyyh.entity.OfentEntity;
import com.xxjy.jyyh.entity.OilDistanceEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilPayTypeEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.entity.PayOrderParams;
import com.xxjy.jyyh.entity.ProductBean;
import com.xxjy.jyyh.entity.TaskBean;
import com.xxjy.jyyh.http.Response;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.integral.BannerViewModel;
import com.xxjy.jyyh.ui.oil.OilDetailActivity;
import com.xxjy.jyyh.ui.pay.PayQueryActivity;
import com.xxjy.jyyh.ui.pay.PayResultActivity;
import com.xxjy.jyyh.ui.pay.RefuelingPayResultActivity;
import com.xxjy.jyyh.ui.search.SearchActivity;
import com.xxjy.jyyh.ui.web.WeChatWebPayActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.GlideUtils;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.WXSdkManager;
import com.xxjy.jyyh.utils.locationmanger.MapIntentUtils;
import com.xxjy.jyyh.utils.symanager.ShanYanManager;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RectangleIndicator;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.ThreadUtils.cancel;
import static com.blankj.utilcode.util.ThreadUtils.runOnUiThread;

/**
 * @author power
 * @date 1/21/21 11:45 AM
 * @project ElephantOil
 * @description:
 */
public class HomeFragment extends BindingFragment<FragmentHomeBinding, HomeViewModel> implements OnRefreshLoadMoreListener {
    private List<OilEntity.StationsBean.CzbLabelsBean> mOilTagList = new ArrayList<>();
    private List<OfentEntity> mOftenList = new ArrayList<>();
    private List<HomeProductEntity.FirmProductsVoBean> mExchangeList = new ArrayList<>();
    private OilNumDialog mOilNumDialog;
    private OilGunDialog mOilGunDialog;
    private OilAmountDialog mOilAmountDialog;
    private OilTipsDialog mOilTipsDialog;
    private OilPayDialog mOilPayDialog;
    private OilCouponDialog mOilCouponDialog;
    private double mLng, mLat;
    private OilEntity.StationsBean mStationsBean;
    private OilStationFlexAdapter mFlexAdapter;
    private HomeExchangeAdapter mExchangeAdapter;
    private boolean isShouldAutoOpenWeb = false;    //标记是否应该自动打开浏览器进行支付
    //是否需要跳转支付确认页
    private boolean shouldJump = false;
    private PayOrderEntity mPayOrderEntity;
    private boolean isFar = false;//油站是否在距离内
    private GasStationLocationTipsDialog mGasStationTipsDialog;
    private LocationTipsDialog mLocationTipsDialog;

    private BannerViewModel bannerViewModel;

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
            PayQueryActivity.openPayQueryActivity(getActivity(),
                    orderEntity.getOrderPayNo(), orderEntity.getOrderNo());
            closeDialog();
        }
    }

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getBaseActivity().setTransparentStatusBar();
            mBinding.toolbar.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showJump(mPayOrderEntity);
    }

    @Override
    protected void initView() {
        getBaseActivity().setTransparentStatusBar();
        mBinding.toolbar.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
        BusUtils.register(this);
        bannerViewModel = new ViewModelProvider(this).get(BannerViewModel.class);
        if (Double.parseDouble(UserConstants.getLongitude()) != 0 && Double.parseDouble(UserConstants.getLatitude()) != 0) {
            mLat = Double.parseDouble(UserConstants.getLatitude());
            mLng = Double.parseDouble(UserConstants.getLongitude());
            //首页油站
            mViewModel.getHomeOil(mLat, mLng);
        }
        //请求定位权限
        requestPermission();

        //油站标签
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        mBinding.tagRecyclerView.setLayoutManager(flexboxLayoutManager);
        mFlexAdapter = new OilStationFlexAdapter(R.layout.adapter_oil_station_tag, mOilTagList);
        mBinding.tagRecyclerView.setAdapter(mFlexAdapter);

        //智能优选title
        SpanUtils.with(mBinding.descTv)
                .append("小象加油")
                .setForegroundColor(getResources().getColor(R.color.color_76FF))
                .append("根据您当前的位置智能优选")
                .setForegroundColor(getResources().getColor(R.color.color_6A))
                .create();

        //积分豪礼
        mBinding.exchangeRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        mExchangeAdapter = new HomeExchangeAdapter(R.layout.adapter_home_exchange, mExchangeList);
        mBinding.exchangeRecyclerView.setAdapter(mExchangeAdapter);
        mExchangeAdapter.setOnItemClickListener((adapter, view, position) -> {
            LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                @Override
                public void onLogin() {
                    NaviActivityInfo.disPathIntentFromUrl((MainActivity)getActivity(),((HomeProductEntity.FirmProductsVoBean) (adapter.getData().get(position))).getLink());
//                    WebViewActivity.openWebActivity((MainActivity) getActivity(), ((HomeProductEntity.FirmProductsVoBean) (adapter.getData().get(position))).getLink());
                }
            });
        });

        mViewModel.getHomeProduct();
        loadBanner();
        initWebViewClient();
    }

    private void loadBanner(){
        bannerViewModel.getBannerOfPostion(BannerPositionConstants.HOME_BANNER).observe(this, data -> {
            if(data!=null&&data.size()>0){
                mBinding.banner.setVisibility(View.VISIBLE);
                //banner
                mBinding.banner.setAdapter(new BannerImageAdapter<BannerBean>(data) {
                    @Override
                    public void onBindView(BannerImageHolder holder, BannerBean data, int position, int size) {
                        Glide.with(holder.imageView)
                                .load(data.getImgUrl())
                                .apply(new RequestOptions()
                                        .error(R.drawable.default_img_bg))
                                .into(holder.imageView);
                        holder.imageView.setOnClickListener(v -> {
                            WebViewActivity.openWebActivity((MainActivity) getActivity(), data.getLink());
                        });
                    }
                }).addBannerLifecycleObserver(this)
                        .setIndicator(new RectangleIndicator(mContext));
            }else{
                mBinding.banner.setVisibility(View.GONE);
            }


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
                        mViewModel.getLocation();
                    }

                    @Override
                    public void onDenied() {
                        mLat = 0;
                        mLng = 0;
                        mViewModel.getHomeOil(mLat, mLng);
                        showFailLocation();
//                        showToastWarning("权限被拒绝，部分产品功能将无法使用！");
                    }
                })
                .request();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initListener() {
        mBinding.scrollView.setOnScrollChangeListener(
                (View.OnScrollChangeListener) (view, x, y, oldX, oldY) -> {
                    if (y > BarUtils.getStatusBarHeight()) {
                        mBinding.toolbar.setBackgroundColor(Color.parseColor("#1676FF"));
                        mBinding.locationIv.setVisibility(View.INVISIBLE);
                        mBinding.addressTv.setVisibility(View.INVISIBLE);
                        mBinding.titleTv.setVisibility(View.VISIBLE);
                        mBinding.searchIv.setImageResource(R.drawable.icon_search_white);
                    } else {
                        mBinding.toolbar.setBackgroundColor(Color.parseColor("#F5F5F5"));
                        mBinding.locationIv.setVisibility(View.VISIBLE);
                        mBinding.addressTv.setVisibility(View.VISIBLE);
                        mBinding.titleTv.setVisibility(View.INVISIBLE);
                        mBinding.searchIv.setImageResource(R.drawable.icon_search);
                    }
                });
        mBinding.quickOilTv.setOnClickListener(this::onViewClicked);
        mBinding.homeQuickOilRl.setOnClickListener(this::onViewClicked);
        mBinding.searchIv.setOnClickListener(this::onViewClicked);
        mBinding.awardTv.setOnClickListener(this::onViewClicked);
        mBinding.otherOilTv.setOnClickListener(this::onViewClicked);
        mBinding.oilNavigationTv.setOnClickListener(this::onViewClicked);
        mBinding.locationIv.setOnClickListener(this::onViewClicked);
        mBinding.addressTv.setOnClickListener(this::onViewClicked);

        mBinding.refreshView.setOnRefreshLoadMoreListener(this);
        mBinding.goIntegralView.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.quick_oil_tv:
                LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        if (mStationsBean != null && mStationsBean.getOilPriceList().size() > 0) {
                            showNumDialog(mStationsBean);
                        } else {
                            showToastWarning("暂无加油信息~");
                        }
                    }
                });

                break;
            case R.id.home_quick_oil_rl:
                LoginHelper.login(mContext, () -> {
                    if (mStationsBean != null) {
                        Intent intent = new Intent(mContext, OilDetailActivity.class);
                        intent.putExtra(Constants.GAS_STATION_ID, mStationsBean.getGasId());
                        startActivity(intent);
                    }
                });
                break;
            case R.id.search_iv:
                startActivity(new Intent(mContext, SearchActivity.class));
                break;
//            case R.id.award_tv:
//                ReceiveRewardDialog receiveRewardDialog = new ReceiveRewardDialog(getContext());
//                receiveRewardDialog.show(view);

//                break;
            case R.id.other_oil_tv:
                BusUtils.postSticky(EventConstants.EVENT_CHANGE_FRAGMENT,
                        new EventEntity(EventConstants.EVENT_CHANGE_FRAGMENT));
                break;
            case R.id.oil_navigation_tv:

                LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        if (MapIntentUtils.isPhoneHasMapNavigation()) {
                            NavigationDialog navigationDialog = new NavigationDialog(getBaseActivity(),
                                    mStationsBean.getStationLatitude(), mStationsBean.getStationLongitude(),
                                    mStationsBean.getGasName());
                            navigationDialog.show();
                        } else {
                            showToastWarning("您当前未安装地图软件，请先安装");
                        }
                    }
                });

                break;
            case R.id.location_iv:
            case R.id.address_tv:
                requestPermission();
                break;
            case R.id.go_integral_view:
                BusUtils.postSticky(EventConstants.EVENT_CHANGE_FRAGMENT,
                        new EventEntity(EventConstants.EVENT_TO_INTEGRAL_FRAGMENT));
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.locationLiveData.observe(this, locationEntity -> {
                UserConstants.setLatitude(String.valueOf(locationEntity.getLat()));
                UserConstants.setLongitude(String.valueOf(locationEntity.getLng()));
                DPoint p = new DPoint(locationEntity.getLat(), locationEntity.getLng());
                DPoint p2 = new DPoint(mLat, mLng);
                mBinding.addressTv.setText(locationEntity.getAddress());
                float distance = CoordinateConverter.calculateLineDistance(p, p2);
                if (distance > 100) {
                    mLng = locationEntity.getLng();
                    mLat = locationEntity.getLat();
                    LogUtils.i("定位成功：" + locationEntity.getLng() + "\n" + locationEntity.getLat());
                    //首页油站
                    mViewModel.getHomeOil(mLat, mLng);
                }
        });

        //优选油站
        mViewModel.homeOilLiveData.observe(this, oilEntity -> {
            if (oilEntity == null || oilEntity.getStations() == null ||
                    oilEntity.getStations().size() <= 0 || oilEntity.getStations().get(0).getOilPriceList() == null) {
                return;
            }
            mStationsBean = oilEntity.getStations().get(0);
            Glide.with(mContext).load(mStationsBean.getGasTypeImg()).into(mBinding.oilImgIv);
            mBinding.oilNameTv.setText(mStationsBean.getGasName());
            mBinding.oilAddressTv.setText(mStationsBean.getGasAddress());
            if(mStationsBean.getOilPriceList()!=null&&mStationsBean.getOilPriceList().size()>0){
                mBinding.oilCurrentPriceTv.setText(mStationsBean.getOilPriceList().get(0).getPriceYfq());
                mBinding.oilOriginalPriceTv.setText("油站价¥" + mStationsBean.getOilPriceList().get(0).getPriceOfficial());
                mBinding.oilNumTv.setText(mStationsBean.getOilPriceList().get(0).getOilName());
            }

            mBinding.oilNavigationTv.setText(mStationsBean.getDistance() + "km");
            mBinding.oilOriginalPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            mBinding.otherOilTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            if (mStationsBean.getCzbLabels() != null && mStationsBean.getCzbLabels().size() > 0) {
                mOilTagList = mStationsBean.getCzbLabels();
                mFlexAdapter.setNewData(mOilTagList);
                mBinding.tagRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mBinding.tagRecyclerView.setVisibility(View.INVISIBLE);
            }

            //常去油站
            if (UserConstants.getIsLogin()) {
                mViewModel.getOftenOils();
            }
            //加油任务
            mViewModel.getRefuelJob(mStationsBean.getGasId());
//            mViewModel.getRefuelJob("DH000011510");
            mViewModel.checkDistance(mStationsBean.getGasId());
        });

        //常去油站
        mViewModel.oftenOilLiveData.observe(this, enetities -> {
            if (enetities != null && enetities.size() > 0) {
                mOftenList.clear();
                mOftenList = enetities;
                mOftenList.add(0, new OfentEntity("• 我最近常去："));
                FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
                flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
                flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
                flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
                mBinding.oftenOilRecyclerView.setLayoutManager(flexboxLayoutManager);
                HomeOftenAdapter oftenAdapter =
                        new HomeOftenAdapter(R.layout.adapter_often_layout, mOftenList);
                mBinding.oftenOilRecyclerView.setAdapter(oftenAdapter);
                mBinding.oftenOilRecyclerView.setVisibility(View.VISIBLE);
                oftenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        getActivity().startActivity(new Intent(getContext(), OilDetailActivity.class).putExtra(Constants.GAS_STATION_ID, ((OfentEntity) adapter.getItem(position)).getGasId()));
                    }
                });
            } else {
                mBinding.oftenOilRecyclerView.setVisibility(View.GONE);
            }
        });

        //积分豪礼
        mViewModel.productLiveData.observe(this, firmProductsVoBeans -> {
            if (firmProductsVoBeans != null && firmProductsVoBeans.size() > 0) {
                mExchangeAdapter.setNewData(firmProductsVoBeans);
            }
        });

        //支付结果回调
        mViewModel.payOrderLiveData.observe(this, payOrderEntity -> {
            if (payOrderEntity.getResult() == 0) {//支付未完成
                switch (payOrderEntity.getPayType()) {
                    case PayTypeConstants.PAY_TYPE_WEIXIN://微信H5
                        WeChatWebPayActivity.openWebPayAct(getActivity(), payOrderEntity.getUrl());
                        break;
                    case PayTypeConstants.PAY_TYPE_WEIXIN_XCX://微信小程序
                        WXSdkManager.newInstance().useWXLaunchMiniProgramToPay(getBaseActivity(), payOrderEntity.getOrderNo());
                        break;
                    case PayTypeConstants.PAY_TYPE_ZHIFUBAO://支付宝H5
                        boolean urlCanUse = UiUtils.checkZhifubaoSdkCanPayUrl(getActivity(),
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
                                    UiUtils.openPhoneWebUrl(getBaseActivity(),
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
        mViewModel.refuelOilLiveData.observe(this, data -> {
            if(data.getData()!=null){
               List<TaskBean> taskBeans= (List<TaskBean>)data.getData();

            if (taskBeans != null && taskBeans.size() > 0) {
                mBinding.integralRl.setVisibility(View.VISIBLE);
                TaskBean taskBean = taskBeans.get(0);

                SpanUtils.with(mBinding.integralDesc)
                        .append("• 还需加油")
                        .append(taskBean.getnOrderAmount() + "")
                        .setForegroundColor(getResources().getColor(R.color.color_1300))
                        .append("元 可立即领取")
                        .append(taskBean.getSpName())
                        .setForegroundColor(getResources().getColor(R.color.color_1300))
                        .append("(价值" + taskBean.getRedeemPoint() + "积分)")
                        .setFontSize(10, true)

                        .setForegroundColor(getResources().getColor(R.color.color_1300))
                        .create();

                SpanUtils.with(mBinding.orderNumDecView)
                        .append("(约需")
                        .append(taskBean.gettOrderNum() + "")
                        .setForegroundColor(getResources().getColor(R.color.color_34))
                        .append("单，还需完成")
                        .append(taskBean.getnOrderNum() + "")
                        .setForegroundColor(getResources().getColor(R.color.color_34))
                        .append("单，限")
                        .append(taskBean.getGasName())
                        .setForegroundColor(getResources().getColor(R.color.color_34))
                        .append(")")
                        .create();
                GlideUtils.loadImage(getContext(), taskBean.getSpImg(), mBinding.integralIv);
                mBinding.progress.setMax(taskBean.gettOrderNum());
                mBinding.progress.setProgress(taskBean.gettOrderNum() - taskBean.getnOrderAmount());
                if (taskBean.isStatus()) {
                    mBinding.awardTv.setBackgroundResource(R.drawable.shape_receive_6radius);
                    mBinding.awardTv.setClickable(true);
                } else {
                    mBinding.awardTv.setBackgroundResource(R.drawable.shape_gray_6raduis);
                    mBinding.awardTv.setClickable(false);
                }
                mBinding.awardTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebViewActivity.openRealUrlWebActivity(getBaseActivity(), taskBean.getLink());
                    }
                });
            }
            }else{
                mBinding.integralRl.setVisibility(View.GONE);
            }
        });

        mViewModel.distanceLiveData.observe(this, oilDistanceEntity -> {
            if (oilDistanceEntity.isHere()) {
                isFar = false;
            } else {
                isFar = true;
            }
        });

    }

    private void showNumDialog(OilEntity.StationsBean stationsBean) {
        //油号dialog
        mOilNumDialog = new OilNumDialog(mContext, stationsBean);
        mOilNumDialog.setOnItemClickedListener((adapter, view, position) -> {
            List<OilEntity.StationsBean.OilPriceListBean> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelected(false);
            }
            data.get(position).setSelected(true);
            adapter.notifyDataSetChanged();
            mBinding.oilCurrentPriceTv.setText(data.get(position).getPriceYfq());
            mBinding.oilOriginalPriceTv.setText("油站价¥" + data.get(position).getPriceOfficial());
            mBinding.oilNumTv.setText(data.get(position).getOilName());
            showGunDialog(mStationsBean, position);
        });

        mOilNumDialog.show();
    }

    private void showGunDialog(OilEntity.StationsBean stationsBean, int oilNoPosition) {
        //枪号dialog
        mOilGunDialog = new OilGunDialog(mContext, stationsBean, oilNoPosition);
        mOilGunDialog.setOnItemClickedListener((adapter, view, position) -> {
            if (isFar) {
                showChoiceOil(stationsBean.getGasName(), view);
            } else {
                List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> data = adapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setSelected(false);
                }
                data.get(position).setSelected(true);
                adapter.notifyDataSetChanged();

                showAmountDialog(stationsBean, oilNoPosition, position);
            }
        });

        mOilGunDialog.show();
    }

    private void showAmountDialog(OilEntity.StationsBean stationsBean, int oilNoPosition, int gunNoPosition) {
        //快捷金额dialog  请输入加油金额 请选择优惠券 暂无优惠券
        mOilAmountDialog = new OilAmountDialog(mContext, getBaseActivity(), stationsBean,
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
        });

        mOilAmountDialog.show();
    }

    private void showCouponDialog(OilEntity.StationsBean stationsBean, String amount,
                                  int oilNoPosition, int gunNoPosition, boolean isPlat) {
        //优惠券dialog
        mOilCouponDialog = new OilCouponDialog(mContext, getBaseActivity(), amount, stationsBean,
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
        mOilTipsDialog = new OilTipsDialog(mContext, getBaseActivity());
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
        mOilPayDialog = new OilPayDialog(mContext, getBaseActivity(), stationsBean,
                oilNoPosition, gunNoPosition, orderId, payAmount);
        mOilPayDialog.setOnItemClickedListener(new OilPayDialog.OnItemClickedListener() {
            @Override
            public void onOilPayTypeClick(BaseQuickAdapter adapter, View view, int position) {
//                GasStationLocationTipsDialog gasStationLocationTipsDialog =
//                        new GasStationLocationTipsDialog(getContext(), mBinding.getRoot(), "成都加油站");
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
                mViewModel.payOrder(payType, orderId, payAmount);
            }
        });

        mOilPayDialog.show();
    }

    private void showFailLocation() {
        mLocationTipsDialog = new LocationTipsDialog(mContext, mBinding.locationIv);
        mLocationTipsDialog.setOnClickListener(view -> {
            switch (view.getId()) {
                case R.id.location_agin:
                    requestPermission();
                    break;
                case R.id.all_oil:
                    BusUtils.postSticky(EventConstants.EVENT_CHANGE_FRAGMENT,
                            new EventEntity(EventConstants.EVENT_CHANGE_FRAGMENT));
                    break;
            }
        });
        mLocationTipsDialog.show();
    }

    private void showChoiceOil(String stationName, View view) {
        mGasStationTipsDialog = new GasStationLocationTipsDialog(mContext, view, stationName);
        mGasStationTipsDialog.setOnClickListener(view1 -> {
            switch (view1.getId()){
                case R.id.select_agin://重新选择
                    closeDialog();
                    break;
                case R.id.navigation_tv://导航过去
                    if (MapIntentUtils.isPhoneHasMapNavigation()) {
                        NavigationDialog navigationDialog = new NavigationDialog(getBaseActivity(),
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
                    break;
            }

        });
        mGasStationTipsDialog.show();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewModel.getHomeOil(mLat, mLng);
        mViewModel.getOftenOils();
        if (mStationsBean != null){
            mViewModel.getRefuelJob(mStationsBean.getGasId());
            mViewModel.checkDistance(mStationsBean.getGasId());
        }
        mViewModel.getHomeProduct();

        loadBanner();
        refreshLayout.finishRefresh();
    }

    private void jumpToPayResultAct(String orderPayNo, String orderNo) {
        if (TextUtils.isEmpty(orderPayNo) && TextUtils.isEmpty(orderNo)) {
            return;
        }
        Intent intent = new Intent(mContext, RefuelingPayResultActivity.class);
        intent.putExtra("orderPayNo", orderPayNo);
        intent.putExtra("orderNo", orderNo);
        startActivity(intent);
        closeDialog();
    }

    private void closeDialog() {
        if (mOilNumDialog != null) {
            mOilNumDialog.dismiss();
            mOilNumDialog = null;
        }
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
        if (mLocationTipsDialog != null){
            mLocationTipsDialog = null;
        }
        if (mGasStationTipsDialog != null){
            mGasStationTipsDialog = null;
        }

        //关掉以后重新刷新数据,否则再次打开时上下选中不一致
        mViewModel.getHomeOil(mLat, mLng);
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
                final PayTask task = new PayTask(getActivity());
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
                    UiUtils.openPhoneWebUrl(getBaseActivity(), url);
                }
                return true;
            }
        });
    }

}
