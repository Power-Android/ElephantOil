package com.xxjy.jyyh.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.HomeExchangeAdapter;
import com.xxjy.jyyh.adapter.HomeMenuAdapter;
import com.xxjy.jyyh.adapter.HomeOftenAdapter;
import com.xxjy.jyyh.adapter.HomeOftenCarsAdapter;
import com.xxjy.jyyh.adapter.HomeTopLineAdapter;
import com.xxjy.jyyh.adapter.MyViewPagerAdapter;
import com.xxjy.jyyh.adapter.OilGunAdapter;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.adapter.OilStationFlexAdapter;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.constants.BannerPositionConstants;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.constants.SPConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.FragmentHomeBinding;
import com.xxjy.jyyh.databinding.HomeCarCardLayoutBinding;
import com.xxjy.jyyh.databinding.HomeOilCardLayoutBinding;
import com.xxjy.jyyh.dialog.CarServiceDialog;
import com.xxjy.jyyh.dialog.GasStationLocationTipsDialog;
import com.xxjy.jyyh.dialog.LocationTipsDialog;
import com.xxjy.jyyh.dialog.NavigationDialog;
import com.xxjy.jyyh.dialog.OilMonthRuleDialog;
import com.xxjy.jyyh.dialog.OilNumDialog;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.CarServeStoreBean;
import com.xxjy.jyyh.entity.CarServeStoreDetailsBean;
import com.xxjy.jyyh.entity.CardStoreInfoVoBean;
import com.xxjy.jyyh.entity.EventEntity;
import com.xxjy.jyyh.entity.HomeMenuEntity;
import com.xxjy.jyyh.entity.HomeProductEntity;
import com.xxjy.jyyh.entity.OfentEntity;
import com.xxjy.jyyh.entity.OftenCarsEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilTypeEntity;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.car.CarServeDetailsActivity;
import com.xxjy.jyyh.ui.integral.BannerViewModel;
import com.xxjy.jyyh.ui.mine.MineViewModel;
import com.xxjy.jyyh.ui.oil.OilDetailsActivity;
import com.xxjy.jyyh.ui.oil.OilOrderActivity;
import com.xxjy.jyyh.ui.oil.OilViewModel;
import com.xxjy.jyyh.ui.pay.RefuelingPayResultActivity;
import com.xxjy.jyyh.ui.search.SearchActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.eventtrackingmanager.EventTrackingManager;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingConstant;
import com.xxjy.jyyh.utils.locationmanger.MapIntentUtils;
import com.xxjy.jyyh.wight.SettingLayout;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RectangleIndicator;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/21/21 11:45 AM
 * @project ElephantOil
 * @description:
 */
public class HomeFragment extends BindingFragment<FragmentHomeBinding, HomeViewModel> implements
        OnRefreshLoadMoreListener {
    private List<OilEntity.StationsBean.CzbLabelsBean> mOilTagList = new ArrayList<>();
    private List<OfentEntity> mOftenList = new ArrayList<>();
    private List<HomeProductEntity.FirmProductsVoBean> mExchangeList = new ArrayList<>();
    private OilNumDialog mOilNumDialog;
    private double mLng, mLat;
    private OilEntity.StationsBean mStationsBean;
    private OilStationFlexAdapter mFlexAdapter;
    private HomeExchangeAdapter mExchangeAdapter;
    private boolean isFar = false;//油站是否在距离内
    private boolean isPay = false;//油站是否在距离内 是否显示继续支付按钮
    private GasStationLocationTipsDialog mGasStationTipsDialog;
    private LocationTipsDialog mLocationTipsDialog;
    private BannerViewModel bannerViewModel;
    private int mOilGunPosition = -1;
    private int mOilNo;
    private boolean isShowAmount = false;
    private String couponId = "";
    private OilMonthRuleDialog mOilMonthRuleDialog;
    private MineViewModel mineViewModel;
    private String[] titles = new String[]{"油站", "车服"};
    private List<View> mList = new ArrayList<>();
    private View mOilView;
    private View mCarView;
    private HomeOilCardLayoutBinding mOilCardBinding;
    private HomeCarCardLayoutBinding mCarCardBinding;
    private List<HomeMenuEntity> menuList = new ArrayList<>();
    private HomeMenuAdapter mHomeMenuAdapter;
    private CardStoreInfoVoBean mCardStoreInfoVo;
    private MyViewPagerAdapter mPagerAdapter;
    private CommonNavigator mCommonNavigator;
    private CarServiceDialog mCarServiceDialog;
    private CarServeStoreDetailsBean mStoreRecordVo;
    private boolean isIntegral = true;
    private List<OftenCarsEntity> mCarOftenList = new ArrayList<>();


    //猎人码跳转
    @BusUtils.Bus(tag = EventConstants.EVENT_JUMP_HUNTER_CODE, sticky = true)
    public void onHunterEvent(String oilId) {
        if (!TextUtils.isEmpty(oilId)) {
            Constants.HUNTER_GAS_ID = oilId;
        }
        getHomeOil();
    }

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getBaseActivity().setTransparentStatusBar();
//            mBinding.toolbar.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);

//            requestPermission();
//            mViewModel.getLocation();
            getLocation();

            if (mStationsBean != null) {
                EventTrackingManager.getInstance().tracking(mContext, getBaseActivity(), String.valueOf(++Constants.PV_ID),
                        TrackingConstant.HOME_MAIN, "", "gas_id=" + mStationsBean.getGasId());
            }

            mViewModel.getRefuelJob();
            if (UserConstants.getIsLogin()) {
                mViewModel.getOftenOils();
                mViewModel.getOftenCars();
            }
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        mViewModel.getRefuelJob();
        if (PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            mBinding.noLocationLayout.setVisibility(View.GONE);
//            mBinding.recommendStationLayout.setVisibility(View.VISIBLE);
//            mViewModel.getLocation();
            getLocation();
            if (UserConstants.getIsLogin()) {
                mViewModel.getOftenOils();
                mViewModel.getOftenCars();
            }
        } else {
            if (TextUtils.isEmpty(Constants.HUNTER_GAS_ID)) {//是否显示首页卡片
                mOilCardBinding.noLocationLayout.setVisibility(View.VISIBLE);
                mOilCardBinding.recommendStationLayout.setVisibility(View.GONE);
            } else {
                mOilCardBinding.noLocationLayout.setVisibility(View.GONE);
                mOilCardBinding.recommendStationLayout.setVisibility(View.VISIBLE);
            }
            mOftenList.clear();
            mCarOftenList.clear();
            mBinding.oftenOilRecyclerView.setVisibility(View.GONE);
            mBinding.oftenCarRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void initView() {
        getBaseActivity().setTransparentStatusBar();
//        mBinding.toolbar.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
        BusUtils.register(this);
        bannerViewModel = new ViewModelProvider(this).get(BannerViewModel.class);
        loadBanner();
        //tab
        initMagicIndicator();

//        mViewModel.getLocation();
//        getLocation();
        if (PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            mBinding.noLocationLayout.setVisibility(View.GONE);
//            mBinding.recommendStationLayout.setVisibility(View.VISIBLE);
//            mViewModel.getLocation();
            getLocation();
        } else {
            mOilCardBinding.noLocationLayout.setVisibility(View.VISIBLE);
            mOilCardBinding.recommendStationLayout.setVisibility(View.GONE);
            mCarCardBinding.carNoLocationLayout.setVisibility(View.VISIBLE);
            mCarCardBinding.carLayout.setVisibility(View.GONE);
        }
        getHomeOil();
        //请求定位权限
//        requestPermission();

        //油站标签
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        mOilCardBinding.tagRecyclerView.setLayoutManager(flexboxLayoutManager);
        mFlexAdapter = new OilStationFlexAdapter(R.layout.adapter_oil_station_tag, mOilTagList);
        mOilCardBinding.tagRecyclerView.setAdapter(mFlexAdapter);

        //菜单
        mBinding.menuRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mHomeMenuAdapter = new HomeMenuAdapter(R.layout.adapter_home_menu, menuList);
        mBinding.menuRecyclerView.setAdapter(mHomeMenuAdapter);
        mHomeMenuAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<HomeMenuEntity> data = adapter.getData();
            switch (data.get(position).getType()) {
                case 1://加油优惠
                    BusUtils.postSticky(EventConstants.EVENT_CHANGE_FRAGMENT,
                            new EventEntity(EventConstants.EVENT_TO_OIL_FRAGMENT));
                    break;
                case 2://汽车养护
                    BusUtils.postSticky(EventConstants.EVENT_CHANGE_FRAGMENT,
                            new EventEntity(EventConstants.EVENT_TO_CAR_FRAGMENT));
                    break;
                case 3://幸运转盘
                case 4://加油赚钱
                    WebViewActivity.openRealUrlWebActivity(getBaseActivity(), data.get(position).getUrl());
                    break;
                default:
                    break;
            }
        });
        mViewModel.getMenu();

        //积分豪礼
        mBinding.exchangeRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        mExchangeAdapter = new HomeExchangeAdapter(R.layout.adapter_home_exchange, mExchangeList);
        mBinding.exchangeRecyclerView.setAdapter(mExchangeAdapter);
        mExchangeAdapter.setOnItemClickListener((adapter, view, position) -> {
//            LoginHelper.login(getContext(), new LoginHelper.CallBack() {
//                @Override
//                public void onLogin() {
            TrackingConstant.OIL_MAIN_TYPE = "4";
            NaviActivityInfo.disPathIntentFromUrl((MainActivity) getActivity(), ((HomeProductEntity.FirmProductsVoBean) (adapter.getData().get(position))).getLink());
//                    WebViewActivity.openWebActivity((MainActivity) getActivity(), ((HomeProductEntity.FirmProductsVoBean) (adapter.getData().get(position))).getLink());
//                }
//            });
        });
//        mBinding.otherOilTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        mViewModel.getHomeProduct();
        mOilCardBinding.tagBanner.setAdapter(new HomeTopLineAdapter(new ArrayList<>(), true))
                .setOrientation(Banner.VERTICAL)
                .setUserInputEnabled(false);
        mineViewModel = new ViewModelProvider(this).get(MineViewModel.class);
    }

    private void initMagicIndicator() {
        mOilView = View.inflate(mContext, R.layout.home_oil_card_layout, null);
        mCarView = View.inflate(mContext, R.layout.home_car_card_layout, null);
        mList.add(mOilView);
        mList.add(mCarView);

        mOilCardBinding = HomeOilCardLayoutBinding.bind(mOilView);
        mCarCardBinding = HomeCarCardLayoutBinding.bind(mCarView);

        mPagerAdapter = new MyViewPagerAdapter(titles, mList);
        mBinding.viewPager.setOffscreenPageLimit(1);
        mBinding.viewPager.setAdapter(mPagerAdapter);


        mCommonNavigator = new CommonNavigator(mContext);

        mBinding.indicator.setVisibility(View.VISIBLE);
        mCommonNavigator.setFollowTouch(true);

        mCommonNavigator.setAdjustMode(true);
        int padding = 70;
        mCommonNavigator.setLeftPadding(padding);
        mCommonNavigator.setRightPadding(padding);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SettingLayout simplePagerTitleView = new SettingLayout(context);
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setTextViewSize(17, 17);
                simplePagerTitleView.setSelectedStyle(true);
                simplePagerTitleView.setmNormalColor(getResources().getColor(R.color.color_0202));
                simplePagerTitleView.setmSelectedColor(getResources().getColor(R.color.color_0202));
                simplePagerTitleView.setOnClickListener(v -> mBinding.viewPager.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setLineWidth(UIUtil.dip2px(context, 25));
                indicator.setRoundRadius(UIUtil.dip2px(context, 10));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(getResources().getColor(R.color.color_76FF));
                return indicator;
            }
        });
        mBinding.indicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mBinding.indicator, mBinding.viewPager);

    }

    private void orderMsg() {
        OilViewModel oilViewModel = new ViewModelProvider(this).get(OilViewModel.class);
        oilViewModel.getOrderNews();
        oilViewModel.orderNewsLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                mOilCardBinding.tagBanner.setVisibility(View.VISIBLE);
                mOilCardBinding.tagRecyclerView.setVisibility(View.INVISIBLE);
                mOilCardBinding.tagBanner.setDatas(data);
            } else {
                mOilCardBinding.tagBanner.setVisibility(View.GONE);
            }
        });
    }

    private void queryUserInfo() {
        mineViewModel.queryUserInfo();
    }

    private void getHomeOil() {
//    if (Double.parseDouble(UserConstants.getLongitude()) != 0 && Double.parseDouble(UserConstants.getLatitude()) != 0) {
//        mLat = Double.parseDouble(UserConstants.getLatitude());
//        mLng = Double.parseDouble(UserConstants.getLongitude());

        if ((mLng == 0d || mLat == 0d)) {
            if (TextUtils.isEmpty(Constants.HUNTER_GAS_ID)) {
                mOilCardBinding.recommendStationLayout.setVisibility(View.GONE);
                mOilCardBinding.noLocationLayout.setVisibility(View.VISIBLE);
            }else {
                mViewModel.getHomeCard(mLat, mLng, Constants.HUNTER_GAS_ID);
            }
            mCarCardBinding.carNoLocationLayout.setVisibility(View.VISIBLE);
            mCarCardBinding.carLayout.setVisibility(View.GONE);
        } else {
//            LogUtils.e("2222", "VISIBLE");
//            mBinding.recommendStationLayout.setVisibility(View.VISIBLE);
//            mBinding.noLocationLayout.setVisibility(View.GONE);
            //首页卡片
            mViewModel.getHomeCard(mLat, mLng, Constants.HUNTER_GAS_ID);
        }
//    }
    }

    private void loadBanner() {
        bannerViewModel.getBannerOfPostion(BannerPositionConstants.HOME_BANNER).observe(this, data -> {
            if (data != null && data.size() > 0) {
//                mBinding.topBanner.setVisibility(View.VISIBLE);
                //banner
                mBinding.topBanner
                        .setAdapter(new BannerImageAdapter<BannerBean>(data) {
                            @Override
                            public void onBindView(BannerImageHolder holder, BannerBean data, int position, int size) {
                                Glide.with(holder.imageView)
                                        .load(data.getImgUrl())
                                        .apply(new RequestOptions()
                                                .placeholder(R.drawable.bg_banner_loading)
                                                .error(R.drawable.bg_banner_error))
                                        .into(holder.imageView);
                                holder.imageView.setOnClickListener(v -> {
                                    if (TextUtils.isEmpty(data.getLink())) {
                                        return;
                                    }
                                    if (data.getLink().contains("/monthCard")) {
                                        LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                                            @Override
                                            public void onLogin() {
                                                queryUserInfo();
                                            }
                                        });
                                    } else if (data.getLink().contains("/inviteFriends")) {
                                        LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                                            @Override
                                            public void onLogin() {
                                                WebViewActivity.openWebActivity((MainActivity) getActivity(), data.getLink());
                                            }
                                        });
                                    } else {
                                        TrackingConstant.OIL_MAIN_TYPE = "3";
                                        WebViewActivity.openWebActivity((MainActivity) getActivity(), data.getLink());
                                    }
                                });
                            }
                        })
                        .setIndicator(new RectangleIndicator(mContext))
                        .setIndicatorSpace((int) getResources().getDimension(R.dimen.dp_4))
                        .setIndicatorHeight((int) getResources().getDimension(R.dimen.dp_4))
                        .setIndicatorNormalWidth((int) getResources().getDimension(R.dimen.dp_4))
                        .setIndicatorSelectedWidth((int) getResources().getDimension(R.dimen.dp_8))
                        .setIndicatorNormalColor(getResources().getColor(R.color.color_2D))
                        .setIndicatorSelectedColor(getResources().getColor(R.color.color_76FF))
                        .setIndicatorMargins(new IndicatorConfig.Margins(0, 0,
                                0, (int) getResources().getDimension(R.dimen.dp_55)))
                        .addBannerLifecycleObserver(this);
            } else {
//                mBinding.topBanner.setVisibility(View.GONE);
            }
        });

        bannerViewModel.getBannerOfPostion(BannerPositionConstants.MARKETING_CENTER).observe(this, data -> {
            if (data != null && data.size() > 0) {
                mBinding.banner2.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(data.get(0).getImgUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.bg_banner_loading)
                                .error(R.drawable.bg_banner_error))
                        .into(mBinding.banner2);
                mBinding.banner2.setOnClickListener(v -> {
                    LoginHelper.login(mContext, new LoginHelper.CallBack() {
                        @Override
                        public void onLogin() {
                            TrackingConstant.OIL_MAIN_TYPE = "2";
                            WebViewActivity.openWebActivity((MainActivity) getActivity(), data.get(0).getLink());
                        }
                    });
                });
            } else {
                mBinding.banner2.setVisibility(View.GONE);
            }
        });
    }


    private void requestPermission() {
        PermissionUtils.permission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(@NonNull List<String> granted) {
//                        mViewModel.getLocation();
                        getLocation();
                    }

                    @Override
                    public void onDenied(@NonNull List<String> deniedForever, @NonNull List<String> denied) {
                        if (deniedForever.size() > 0) {
                            //添加确定按钮
                            //添加返回按钮
                            //dialog消失后重新查询权限
                            new AlertDialog.Builder(mContext).setTitle("定位权限被拒绝")//设置对话框标题
                                    .setMessage("定位权限被拒绝，将导致部分功能无法正常使用，需要到设置页面手动授权")
                                    .setPositiveButton("去授权", (dialog, which) -> {//确定按钮的响应事件
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", App.getContext().getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                        dialog.dismiss();
                                    }).setNegativeButton("取消", (dialog, which) -> {//响应事件
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            })
                                    .setOnCancelListener(dialog -> {
                                    }).show();//在按键响应事件中显示此对话框
                        } else {
                            mLat = 0;
                            mLng = 0;
//                            mViewModel.getHomeOil(mLat, mLng, Constants.HUNTER_GAS_ID);
                            getHomeOil();
                            showFailLocation();
//                        showToastWarning("权限被拒绝，部分产品功能将无法使用！");
                        }
                    }
                })
                .request();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initListener() {
//        mBinding.scrollView.setOnScrollChangeListener(
//                (View.OnScrollChangeListener) (view, x, y, oldX, oldY) -> {
//                    if (y > BarUtils.getStatusBarHeight()) {
//                        mBinding.toolbar.setBackgroundColor(Color.parseColor("#1676FF"));
//                        mBinding.locationIv.setVisibility(View.INVISIBLE);
//                        mBinding.addressTv.setVisibility(View.INVISIBLE);
//                        mBinding.titleTv.setVisibility(View.VISIBLE);
//                        mBinding.searchIv.setImageResource(R.drawable.icon_search_white);
//                    } else {
//                        mBinding.toolbar.setBackgroundColor(Color.parseColor("#F5F5F5"));
//                        mBinding.locationIv.setVisibility(View.VISIBLE);
//                        mBinding.addressTv.setVisibility(View.VISIBLE);
//                        mBinding.titleTv.setVisibility(View.INVISIBLE);
//                        mBinding.searchIv.setImageResource(R.drawable.icon_search);
//                    }
//                });
        ClickUtils.applySingleDebouncing(new View[]{mOilCardBinding.goMoreOilView, mCarCardBinding.goMoreCarView}, this::onViewClicked);
        mOilCardBinding.quickOilTv.setOnClickListener(this::onViewClicked);
        mOilCardBinding.oilview.setOnClickListener(this::onViewClicked);
        mOilCardBinding.oilNumTv.setOnClickListener(this::onViewClicked);
        mCarView.findViewById(R.id.quick_car_tv).setOnClickListener(this::onViewClicked);
        mCarView.findViewById(R.id.carview).setOnClickListener(this::onViewClicked);

        mBinding.searchIv.setOnClickListener(this::onViewClicked);
        mBinding.awardTv.setOnClickListener(this::onViewClicked);
        mBinding.otherOilTv.setOnClickListener(this::onViewClicked);
        mOilCardBinding.oilNavigationTv.setOnClickListener(this::onViewClicked);
        mBinding.locationIv.setOnClickListener(this::onViewClicked);
        mBinding.addressTv.setOnClickListener(this::onViewClicked);

        ClickUtils.applySingleDebouncing(new View[]{mOilCardBinding.quickOilTv, mOilCardBinding.oilNumTv}, this::onViewClicked);
        mBinding.refreshView.setEnableLoadMore(false);
        mBinding.refreshView.setOnRefreshLoadMoreListener(this);
        mBinding.goIntegralView.setOnClickListener(this::onViewClicked);
        mBinding.signInIv.setOnClickListener(this::onViewClicked);
        mOilCardBinding.goMoreOilView.setOnClickListener(this::onViewClicked);
        mOilCardBinding.goLocationView.setOnClickListener(this::onViewClicked);
        mCarView.findViewById(R.id.go_more_car_view).setOnClickListener(this::onViewClicked);
        mCarView.findViewById(R.id.car_go_location_view).setOnClickListener(this::onViewClicked);
        mCarView.findViewById(R.id.car_navigation_tv).setOnClickListener(this::onViewClicked);

        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        if (mOftenList.size() > 1){
                            mBinding.oftenOilRecyclerView.setVisibility(View.VISIBLE);
                            mBinding.oftenCarRecyclerView.setVisibility(View.GONE);
                        }else {
                            mBinding.oftenOilRecyclerView.setVisibility(View.GONE);
                            mBinding.oftenCarRecyclerView.setVisibility(View.GONE);
                        }
                        break;
                    case 1:
                        if (mCarOftenList.size() > 1){
                            mBinding.oftenOilRecyclerView.setVisibility(View.GONE);
                            mBinding.oftenCarRecyclerView.setVisibility(View.VISIBLE);
                        }else {
                            mBinding.oftenOilRecyclerView.setVisibility(View.GONE);
                            mBinding.oftenCarRecyclerView.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.quick_oil_tv:
            case R.id.oil_num_tv:
                LoginHelper.login(getContext(), () -> {
                    if (mStationsBean != null && mStationsBean.getOilPriceList().size() > 0) {
                        showNumDialog(mStationsBean);
                    } else {
                        showToastWarning("暂无加油信息~");
                    }
                });

                break;
            case R.id.oilview:
                LoginHelper.login(mContext, () -> {
                    if (mStationsBean != null) {
                        Intent intent = new Intent(mContext, OilDetailsActivity.class);
                        intent.putExtra(Constants.GAS_STATION_ID, mStationsBean.getGasId());
                        intent.putExtra(Constants.OIL_NUMBER_ID, mStationsBean.getOilNo() + "");
                        startActivity(intent);
                    }
                });
                break;
            case R.id.search_iv:
                startActivity(new Intent(mContext, SearchActivity.class));
//                startActivity(new Intent(mContext, SettingActivity.class));
                break;
//            case R.id.award_tv:
//                ReceiveRewardDialog receiveRewardDialog = new ReceiveRewardDialog(getContext());
//                receiveRewardDialog.show(view);

//                break;
            case R.id.other_oil_tv:
            case R.id.go_more_oil_view:
                BusUtils.postSticky(EventConstants.EVENT_CHANGE_FRAGMENT,
                        new EventEntity(EventConstants.EVENT_CHANGE_FRAGMENT));
                break;
            case R.id.oil_navigation_tv:
                LoginHelper.login(getContext(), () -> {
                    if (MapIntentUtils.isPhoneHasMapNavigation()) {
                        NavigationDialog navigationDialog = new NavigationDialog(getBaseActivity(),
                                mStationsBean.getStationLatitude(), mStationsBean.getStationLongitude(),
                                mStationsBean.getGasName());
                        navigationDialog.show();
                    } else {
                        showToastWarning("您当前未安装地图软件，请先安装");
                    }
                });

                break;
            case R.id.car_navigation_tv:
                LoginHelper.login(getContext(), () -> {
                    if (MapIntentUtils.isPhoneHasMapNavigation()) {
                        NavigationDialog navigationDialog = new NavigationDialog(getBaseActivity(),
                                mStoreRecordVo.getCardStoreInfoVo().getLatitude(), mStoreRecordVo.getCardStoreInfoVo().getLongitude(),
                                mStoreRecordVo.getCardStoreInfoVo().getStoreName());
                        navigationDialog.show();
                    } else {
                        showToastWarning("您当前未安装地图软件，请先安装");
                    }
                });
                break;
            case R.id.location_iv:
            case R.id.address_tv:
//                requestPermission();
                break;
            case R.id.go_integral_view:
                BusUtils.postSticky(EventConstants.EVENT_CHANGE_FRAGMENT,
                        new EventEntity(EventConstants.EVENT_TO_INTEGRAL_FRAGMENT));
                break;
            case R.id.sign_in_iv:
                LoginHelper.login(getContext(), () -> {
                    TrackingConstant.OIL_MAIN_TYPE = "5";
                    WebViewActivity.openRealUrlWebActivity(getBaseActivity(), Constants.SIGN_IN_URL);
                });

                break;
            case R.id.car_go_location_view:
            case R.id.go_location_view:
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", App.getContext().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                break;
            case R.id.quick_car_tv://车门店下单
                LoginHelper.login(mContext, () -> {
                    if (mStoreRecordVo != null && mStoreRecordVo.getCardStoreInfoVo() != null) {
                        showCarDialog(mStoreRecordVo);
                    }
                });
                break;
            case R.id.carview://跳转门店详情
                LoginHelper.login(mContext, () -> {
                    Intent intent1 = new Intent(mContext, CarServeDetailsActivity.class);
                    intent1.putExtra("no", mStoreRecordVo.getCardStoreInfoVo().getStoreNo());
                    intent1.putExtra("distance", mStoreRecordVo.getCardStoreInfoVo().getDistance());
                    startActivity(intent1);
                });
                break;
            case R.id.go_more_car_view:
                BusUtils.postSticky(EventConstants.EVENT_CHANGE_FRAGMENT,
                        new EventEntity(EventConstants.EVENT_TO_CAR_FRAGMENT));
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
//                    //首页油站
//                    mViewModel.getHomeOil(mLat, mLng, Constants.HUNTER_GAS_ID);
                getHomeOil();

            }
        });

        //首页卡片
        mViewModel.homeOilLiveData.observe(this, oilEntity -> {
            mStoreRecordVo = oilEntity.getStoreRecordVo();

            if (oilEntity.getHasStore() == 1) {//70km内有门店
                if (mList.size() == 1) {
                    titles = new String[]{"油站", "车服"};
                    mCarView = null;
                    mCarView = View.inflate(mContext, R.layout.home_car_card_layout, null);
                    mCarCardBinding = HomeCarCardLayoutBinding.bind(mCarView);
                    mList.add(mCarView);
                    mCarView.findViewById(R.id.quick_car_tv).setOnClickListener(this::onViewClicked);
                    mCarView.findViewById(R.id.carview).setOnClickListener(this::onViewClicked);
                    mCarView.findViewById(R.id.go_more_car_view).setOnClickListener(this::onViewClicked);
                    mCarView.findViewById(R.id.car_go_location_view).setOnClickListener(this::onViewClicked);
                    mCarView.findViewById(R.id.car_navigation_tv).setOnClickListener(this::onViewClicked);

                    mBinding.viewPager.setNoScroll(true);
                    mCommonNavigator.setAdjustMode(true);
                    mCommonNavigator.notifyDataSetChanged();
                    mPagerAdapter.refreshData(titles, mList);
                    mBinding.viewPager.setAdapter(mPagerAdapter);
                }
                if (oilEntity.getNearest() == 1) {//展示油站
                    mBinding.viewPager.setCurrentItem(0);
                } else {//展示门店
                    mBinding.viewPager.setCurrentItem(1);
                }
            } else {
                if (!PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)){
                    mCarCardBinding.carNoLocationLayout.setVisibility(View.VISIBLE);
                    mCarCardBinding.carLayout.setVisibility(View.GONE);
                }else {
                    titles = new String[]{"油站"};
                    if (mList.size() > 1) {
                        mList.remove(1);
                        mBinding.viewPager.removeViewAt(1);
                        mBinding.viewPager.setNoScroll(false);
                    }
                    mCommonNavigator.setAdjustMode(false);
                    mCommonNavigator.notifyDataSetChanged();
                    mPagerAdapter.refreshData(titles, mList);
                    mBinding.viewPager.setCurrentItem(0);
                }
            }

            if (oilEntity.getHasStore() == 1 && oilEntity.getStoreRecordVo() != null &&
                    oilEntity.getStoreRecordVo().getCardStoreInfoVo() != null) {//最近是否有门店
                //车服卡片
                mCardStoreInfoVo = oilEntity.getStoreRecordVo().getCardStoreInfoVo();
                Glide.with(mContext).load(mCardStoreInfoVo.getStorePicture()).into(mCarCardBinding.carImgIv1);
                mCarCardBinding.carNameTv.setText(mCardStoreInfoVo.getStoreName());
                mCarCardBinding.carAddressTv.setText(mCardStoreInfoVo.getAddress());
                mCarCardBinding.carTimeTv.setText("营业时间：每天" + mCardStoreInfoVo.getOpenStart() + "-" + mCardStoreInfoVo.getEndStart());
                mCarCardBinding.carNavigationTv.setText(String.format("%.2f", mCardStoreInfoVo.getDistance() / 1000d) + "KM");
                mCarCardBinding.floatLayout.setMinimumHeight(QMUIDisplayHelper.dp2px(mContext, 40));
                mCarCardBinding.floatLayout.removeAllViews();
                if (mCardStoreInfoVo.getCategoryNameList() != null && mCardStoreInfoVo.getCategoryNameList().size() > 0) {
                    for (String lab : mCardStoreInfoVo.getCategoryNameList()) {
                        TextView textView = new TextView(mContext);
                        textView.setMinHeight(QMUIDisplayHelper.dp2px(mContext, 19));
                        int textViewPadding = QMUIDisplayHelper.dp2px(mContext, 5);
                        int textViewPadding2 = QMUIDisplayHelper.dp2px(mContext, 3);
                        textView.setPadding(textViewPadding, textViewPadding2, textViewPadding, textViewPadding2);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
                        textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                        textView.setBackgroundResource(R.drawable.shape_stroke_station_tag);
                        textView.setText(lab);
                        mCarCardBinding.floatLayout.addView(textView);
                    }
                    mCarCardBinding.floatLayout.setVisibility(View.VISIBLE);
                } else {
                    mCarCardBinding.floatLayout.setVisibility(View.INVISIBLE);
                }

                if (UserConstants.getIsLogin()) {
                    //常去车服门店
                    mViewModel.getOftenCars();
                }

                mCarCardBinding.carLayout.setVisibility(View.VISIBLE);
                mCarCardBinding.carNoLocationLayout.setVisibility(View.GONE);
            } else {
                mCarCardBinding.carLayout.setVisibility(View.GONE);
                mCarCardBinding.carNoLocationLayout.setVisibility(View.VISIBLE);
            }

            if (oilEntity.getStations() != null && oilEntity.getStations().get(0) != null &&
                    oilEntity.getStations().get(0).getOilPriceList() != null ){
                //油站卡片
                mOilCardBinding.noLocationLayout.setVisibility(View.GONE);
                mOilCardBinding.recommendStationLayout.setVisibility(View.VISIBLE);

                mStationsBean = oilEntity.getStations().get(0);
                Glide.with(mContext).load(mStationsBean.getGasTypeImg()).into(mOilCardBinding.oilImgIv1);
                mOilCardBinding.oilNameTv.setText(mStationsBean.getGasName());
                mOilCardBinding.oilAddressTv.setText(mStationsBean.getGasAddress());
                if (mStationsBean != null) {
                    mOilCardBinding.oilCurrentPriceTv.setText(mStationsBean.getPriceYfq());
                    mOilCardBinding.oilOriginalPriceTv.setText("油站价¥" + mStationsBean.getPriceGun());
                    mOilCardBinding.oilNumTv.setText(mStationsBean.getOilName());


                for (int i = 0; i < mStationsBean.getOilPriceList().size(); i++) {
                    if (mStationsBean.getOilNo().equals(String.valueOf(mStationsBean.getOilPriceList().get(i).getOilNo()))) {
                        mStationsBean.getOilPriceList().get(i).setSelected(true);
                        mOilNo = mStationsBean.getOilPriceList().get(i).getOilNo();
                    }
                }

                mOilCardBinding.oilNavigationTv.setText(mStationsBean.getDistance() + "km");
                mOilCardBinding.oilOriginalPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                if (mStationsBean.getCzbLabels() != null && mStationsBean.getCzbLabels().size() > 0) {
                    mOilTagList = mStationsBean.getCzbLabels();
                    mFlexAdapter.setNewData(mOilTagList);
                    mOilCardBinding.tagRecyclerView.setVisibility(View.VISIBLE);
                    mOilCardBinding.tagBanner.setVisibility(View.GONE);
                } else {
                    LogUtils.e(mStationsBean.toString());
                    mOilCardBinding.tagRecyclerView.setVisibility(View.INVISIBLE);
                    orderMsg();
                }
                //常去油站
                if (UserConstants.getIsLogin()) {
                    mViewModel.getOftenOils();
                }
                //加油任务
                mViewModel.getRefuelJob();
                mViewModel.checkDistance(mStationsBean.getGasId());

                EventTrackingManager.getInstance().tracking(mContext, getBaseActivity(), String.valueOf(++Constants.PV_ID),
                        TrackingConstant.HOME_MAIN, "", "gas_id=" + mStationsBean.getGasId());
                }
            }
        });

        //常去油站
        mViewModel.oftenOilLiveData.observe(this, enetities -> {
            if (enetities != null && enetities.size() > 0) {
                mOftenList.clear();
                mOftenList = enetities;
                mOftenList.add(0, new OfentEntity("我最近常去："));
                FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
                flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
                flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
                flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
                mBinding.oftenOilRecyclerView.setLayoutManager(flexboxLayoutManager);
                HomeOftenAdapter oftenAdapter =
                        new HomeOftenAdapter(R.layout.adapter_often_layout, mOftenList);
                mBinding.oftenOilRecyclerView.setAdapter(oftenAdapter);
                oftenAdapter.setOnItemClickListener((adapter, view, position) ->
                        startActivity(new Intent(getContext(), OilDetailsActivity.class)
                                .putExtra(Constants.GAS_STATION_ID, ((OfentEntity) adapter.getItem(position)).getGasId())));
                if (mBinding.viewPager.getCurrentItem() == 0){
                    mBinding.oftenOilRecyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                mOftenList.clear();
                mBinding.oftenOilRecyclerView.setVisibility(View.GONE);
            }
        });

        //常去车服门店
        mViewModel.oftenCarsLiveData.observe(this, new Observer<List<OftenCarsEntity>>() {
            @Override
            public void onChanged(List<OftenCarsEntity> oftenCarsList) {
                if (oftenCarsList != null && oftenCarsList.size() > 0){
                    mCarOftenList.clear();
                    mCarOftenList = oftenCarsList;
                    OftenCarsEntity oftenCarsEntity = new OftenCarsEntity();
                    OftenCarsEntity.CardStoreInfoVoBean cardStoreInfoVoBean = new OftenCarsEntity.CardStoreInfoVoBean();
                    cardStoreInfoVoBean.setStoreName("我最近常去：");
                    oftenCarsEntity.setCardStoreInfoVo(cardStoreInfoVoBean);
                    mCarOftenList.add(0, oftenCarsEntity);
                    FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
                    flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
                    flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
                    flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
                    mBinding.oftenCarRecyclerView.setLayoutManager(flexboxLayoutManager);
                    HomeOftenCarsAdapter oftenAdapter =
                            new HomeOftenCarsAdapter(R.layout.adapter_often_layout, mCarOftenList);
                    mBinding.oftenCarRecyclerView.setAdapter(oftenAdapter);
                    oftenAdapter.setOnItemClickListener((adapter, view, position) -> {
                        Intent intent1 = new Intent(mContext, CarServeDetailsActivity.class);
                        intent1.putExtra("no", mCarOftenList.get(position).getCardStoreInfoVo().getStoreNo());
                        intent1.putExtra("distance", mCarOftenList.get(position).getCardStoreInfoVo().getDistance());
                        startActivity(intent1);
                    });
                    if (mBinding.viewPager.getCurrentItem() == 1){
                        mBinding.oftenCarRecyclerView.setVisibility(View.VISIBLE);
                    }
                }else {
                    mCarOftenList.clear();
                    mBinding.oftenCarRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        //菜单栏
        mViewModel.menuLiveData.observe(this, new Observer<List<HomeMenuEntity>>() {
            @Override
            public void onChanged(List<HomeMenuEntity> menuEntityList) {
                menuList = menuEntityList;
                mHomeMenuAdapter.setNewData(menuEntityList);
                mHomeMenuAdapter.notifyDataSetChanged();
            }
        });

        //积分豪礼
        mViewModel.productLiveData.observe(this, firmProductsVoBeans -> {
            if (firmProductsVoBeans != null && firmProductsVoBeans.size() > 0) {
                mExchangeAdapter.setNewData(firmProductsVoBeans);
            }
        });

        mViewModel.refuelOilLiveData.observe(this, data -> {
            if (data != null) {
                mBinding.oilTaskCl.setVisibility(View.VISIBLE);
                SpanUtils.with(mBinding.oilTaskDesc)
                        .append("每单加油")
                        .append(data.getAmount() + "元")
                        .setForegroundColor(getResources().getColor(R.color.color_3E3D))
                        .append(" 单单有好礼")
                        .create();
                mBinding.taskCouponTv1.setText(data.getCoupons().get(0).getInfo());
                mBinding.taskCouponTv2.setText(data.getCoupons().get(1).getInfo());
                mBinding.taskCouponTv3.setText(data.getCoupons().get(2).getInfo());
                if (data.getProgress() >= data.getCoupons().get(0).getNumber()) {
                    mBinding.taskNodeIv1.setImageDrawable(getResources().getDrawable(R.drawable.task_node_finish));
                } else {
                    mBinding.taskNodeIv1.setImageDrawable(getResources().getDrawable(R.drawable.task_node_icon));
                }
                if (data.getProgress() >= data.getCoupons().get(1).getNumber()) {
                    mBinding.taskNodeIv2.setImageDrawable(getResources().getDrawable(R.drawable.task_node_finish));
                } else {
                    mBinding.taskNodeIv2.setImageDrawable(getResources().getDrawable(R.drawable.task_node_icon));
                }
                if (data.getProgress() >= data.getCoupons().get(2).getNumber()) {
                    mBinding.taskNodeIv3.setImageDrawable(getResources().getDrawable(R.drawable.task_node_finish));
                } else {
                    mBinding.taskNodeIv3.setImageDrawable(getResources().getDrawable(R.drawable.task_node_icon));
                }

                if (data.getProgress() == 0) {
                    mBinding.progressIn.setVisibility(View.GONE);
                } else {
                    mBinding.progressIn.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams layoutParams = mBinding.progressIn.getLayoutParams();
                    if (data.getProgress() > 0 &&
                            data.getProgress() < data.getCoupons().get(0).getNumber()) {
                        layoutParams.width = (mBinding.taskNodeIv1.getRight() - mBinding.taskNodeIv.getLeft()
                                - mBinding.taskNodeIv1.getWidth() / 2) / 2;
                    } else if (data.getProgress() == data.getCoupons().get(0).getNumber()) {
                        layoutParams.width = mBinding.taskNodeIv1.getRight() - mBinding.taskNodeIv.getLeft()
                                - mBinding.taskNodeIv1.getWidth() / 2;
                    } else if (data.getProgress() > data.getCoupons().get(0).getNumber() &&
                            data.getProgress() < data.getCoupons().get(1).getNumber()) {
                        layoutParams.width = (3 * (mBinding.taskNodeIv2.getRight() - mBinding.taskNodeIv.getLeft()) / 4
                                - mBinding.taskNodeIv2.getWidth() / 2);
                    } else if (data.getProgress() == data.getCoupons().get(1).getNumber()) {
                        layoutParams.width = mBinding.taskNodeIv2.getRight() - mBinding.taskNodeIv.getLeft()
                                - mBinding.taskNodeIv2.getWidth() / 2;
                    } else if (data.getProgress() > data.getCoupons().get(1).getNumber() &&
                            data.getProgress() < data.getCoupons().get(2).getNumber()) {
                        layoutParams.width = (5 * (mBinding.taskNodeIv3.getRight() - mBinding.taskNodeIv.getLeft()) / 6
                                - mBinding.taskNodeIv2.getWidth() / 2);
                    } else if (data.getProgress() >= data.getCoupons().get(2).getNumber()) {
                        layoutParams.width = mBinding.taskNodeIv3.getRight() - mBinding.taskNodeIv.getLeft()
                                - mBinding.taskNodeIv3.getWidth() / 2;
                    } else {
                        mBinding.progressIn.setVisibility(View.GONE);
                    }
                }

                mBinding.taskNumTv1.setText("第" + data.getCoupons().get(0).getNumber() + "单");
                mBinding.taskNumTv2.setText("第" + data.getCoupons().get(1).getNumber() + "单");
                mBinding.taskNumTv3.setText("第" + data.getCoupons().get(2).getNumber() + "单");
                SpanUtils.with(mBinding.oilTaskNum)
                        .append("当前已加油")
                        .append(" " + (Math.max(data.getProgress() - data.getUsable(), 0)) + " ")
                        .setForegroundColor(getResources().getColor(R.color.color_3E3D))
                        .append("单")
                        .create();

                for (int i = 0; i < data.getCoupons().size(); i++) {
                    if (0 == data.getCoupons().get(i).getStatus()) {
                        couponId = String.valueOf(data.getCoupons().get(i).getCouponId());
                        break;
                    }
                }
                if (!data.isButton()) {
                    mBinding.oilTaskGet.setBackground(getResources().getDrawable(R.drawable.shape_task_unclick_14radius));
                    mBinding.oilTaskGet.setText("待领取");
                } else {
                    mBinding.oilTaskGet.setBackground(getResources().getDrawable(R.drawable.oil_task_get_icon));
                    mBinding.oilTaskGet.setText("去领取");
                }
                mBinding.oilTaskGet.setOnClickListener(view -> {
                    if (!UserConstants.getIsLogin()) {
                        showToastInfo("请登录后查看");
                    } else {
                        mViewModel.receiverJobCoupon(data.getId(), couponId);
                    }
                });
                mBinding.taskRuleIv.setOnClickListener(view -> {
                    if (mOilMonthRuleDialog == null) {
                        mOilMonthRuleDialog = new OilMonthRuleDialog(mContext, getBaseActivity(), data.getUrl());
                        mOilMonthRuleDialog.show(view);
                    } else {
                        mOilMonthRuleDialog.show(view);
                    }

                });
            } else {
                mBinding.oilTaskCl.setVisibility(View.GONE);
            }
        });

        mViewModel.distanceLiveData.observe(this, oilDistanceEntity -> {
            isPay = oilDistanceEntity.isPay();
            if (oilDistanceEntity.isHere()) {
                isFar = false;
            } else {
                isFar = true;
            }
        });

        //加油任务领奖
        mViewModel.receiverCouponLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showToastSuccess("领取成功~");
                mViewModel.getRefuelJob();
            }
        });

        mineViewModel.userLiveData.observe(this, data -> {
            if (data.isHasBuyOldMonthCoupon()) {
                WebViewActivity.openWebActivity((MainActivity) getActivity(), Constants.MARKET_ACTIVITIES_URL);
            } else {
                if (TextUtils.isEmpty(data.getMonthCardExpireDate())) {
                    WebViewActivity.openWebActivity((MainActivity) getActivity(), Constants.BUY_MONTH_CARD_URL);
                } else {
                    WebViewActivity.openWebActivity((MainActivity) getActivity(), Constants.BUY_IN_ADVANCE_MONTH_CARD_URL);
                }
            }
        });
    }

    private void getLocation() {
        mViewModel.getLocation();
    }

    private void showNumDialog(OilEntity.StationsBean stationsBean) {
        mOilGunPosition = -1;
        //油号dialog
        mOilNumDialog = new OilNumDialog(mContext, stationsBean);
        mOilNumDialog.setOnItemClickedListener(new OilNumDialog.OnItemClickedListener() {
            @Override
            public void onOilTypeClick(BaseQuickAdapter adapter, View view, int position,
                                       OilNumAdapter oilNumAdapter, OilGunAdapter oilGunAdapter) {
                List<OilTypeEntity> data = adapter.getData();
                isShowAmount = false;
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setSelect(false);
                }
                data.get(position).setSelect(true);
                adapter.notifyDataSetChanged();
                for (int i = 0; i < mStationsBean.getOilPriceList().size(); i++) {
                    for (int j = 0; j < mStationsBean.getOilPriceList().get(i).getGunNos().size(); j++) {
                        mStationsBean.getOilPriceList().get(i).getGunNos().get(j).setSelected(false);
                    }
                }
                List<OilEntity.StationsBean.OilPriceListBean> oilPriceList = data.get(position).getOilPriceList();
                for (int i = 0; i < oilPriceList.size(); i++) {
                    oilPriceList.get(i).setSelected(false);
                }
                oilPriceList.get(0).setSelected(true);
                mOilNo = oilPriceList.get(0).getOilNo();
                mOilGunPosition = 0;
                oilNumAdapter.setNewData(oilPriceList);
                oilGunAdapter.setNewData(oilPriceList.get(mOilGunPosition).getGunNos());
                mOilCardBinding.oilCurrentPriceTv.setText(oilPriceList.get(0).getPriceYfq());
                mOilCardBinding.oilOriginalPriceTv.setText("油站价¥" + oilPriceList.get(0).getPriceGun());
                mOilCardBinding.oilNumTv.setText(oilPriceList.get(0).getOilName());
            }

            @Override
            public void onOilNumClick(BaseQuickAdapter adapter, View view, int position, OilGunAdapter oilGunAdapter) {
                List<OilEntity.StationsBean.OilPriceListBean> data = adapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setSelected(false);
                }
                data.get(position).setSelected(true);
                adapter.notifyDataSetChanged();
                for (int i = 0; i < mStationsBean.getOilPriceList().size(); i++) {
                    for (int j = 0; j < mStationsBean.getOilPriceList().get(i).getGunNos().size(); j++) {
                        mStationsBean.getOilPriceList().get(i).getGunNos().get(j).setSelected(false);
                    }
                }
                mOilNo = data.get(position).getOilNo();
                mOilGunPosition = 0;
                isShowAmount = false;
                oilGunAdapter.setNewData(data.get(position).getGunNos());
                mOilCardBinding.oilCurrentPriceTv.setText(data.get(position).getPriceYfq());
                mOilCardBinding.oilOriginalPriceTv.setText("油站价¥" + data.get(position).getPriceGun());
                mOilCardBinding.oilNumTv.setText(data.get(position).getOilName());
            }

            @Override
            public void onOilGunClick(BaseQuickAdapter adapter, View view, int position) {
                List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> data = adapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setSelected(false);
                }
                data.get(position).setSelected(true);
                adapter.notifyDataSetChanged();
                mOilGunPosition = position;
            }

            @Override
            public void onQuickClick(View view, OilNumAdapter oilNumAdapter, OilGunAdapter oilGunAdapter) {
                if (mOilGunPosition == -1) {
                    showToastInfo("请选择枪号");
                } else {
                    for (int i = 0; i < oilGunAdapter.getData().size(); i++) {
                        if (oilGunAdapter.getData().get(i).isSelected()) {
                            isShowAmount = true;
                        }
                    }
                    if (isShowAmount) {
                        if (isFar) {
                            showChoiceOil(oilNumAdapter, oilGunAdapter, mStationsBean.getGasName(), view);
                        } else {
                            EventTrackingManager.getInstance().tracking(mContext, getBaseActivity(), String.valueOf(++Constants.PV_ID),
                                    TrackingConstant.GAS_GUN_NO, "", "type=1");
                            Intent intent = new Intent(getContext(), OilOrderActivity.class);
                            intent.putExtra("stationsBean", (Serializable) mStationsBean);
                            intent.putExtra("oilNo", mOilNo);
                            intent.putExtra("gunNo", oilGunAdapter.getData().get(mOilGunPosition).getGunNo());
                            startActivity(intent);
                            closeDialog();
                        }
                    } else {
                        showToastInfo("请选择枪号");
                    }
                }
            }

            @Override
            public void closeAll() {
                closeDialog();
            }
        });

        mOilNumDialog.show();
    }

    private void showFailLocation() {
        mLocationTipsDialog = new LocationTipsDialog(mContext, mBinding.locationIv);
        mLocationTipsDialog.setOnClickListener(view -> {
            switch (view.getId()) {
                case R.id.location_agin:
//                    requestPermission();
                    break;
                case R.id.all_oil:
                    BusUtils.postSticky(EventConstants.EVENT_CHANGE_FRAGMENT,
                            new EventEntity(EventConstants.EVENT_CHANGE_FRAGMENT));
                    break;
            }
        });
        mLocationTipsDialog.show();
    }

    private void showChoiceOil(OilNumAdapter oilNumAdapter, OilGunAdapter oilGunAdapter, String stationName, View view) {
        mGasStationTipsDialog = new GasStationLocationTipsDialog(mContext, getBaseActivity(), view, stationName);
        mGasStationTipsDialog.showPayBt(isPay);
        mGasStationTipsDialog.setOnClickListener(view1 -> {
            switch (view1.getId()) {
                case R.id.select_agin://重新选择
                    EventTrackingManager.getInstance().tracking(mContext, getBaseActivity(), String.valueOf(++Constants.PV_ID),
                            TrackingConstant.GAS_FENCE, "", "type=1");
                    closeDialog();
                    break;
                case R.id.navigation_tv://导航过去
                    if (MapIntentUtils.isPhoneHasMapNavigation()) {
                        EventTrackingManager.getInstance().tracking(mContext, getBaseActivity(), String.valueOf(++Constants.PV_ID),
                                TrackingConstant.GAS_FENCE, "", "type=2");
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

//                            showAmountDialog(mStationsBean, oilNumAdapter.getData(),
//                                    mOilNoPosition, mOilGunPosition);
                    EventTrackingManager.getInstance().tracking(mContext, getBaseActivity(), String.valueOf(++Constants.PV_ID),
                            TrackingConstant.GAS_FENCE, "", "type=3");

                    Intent intent = new Intent(getContext(), OilOrderActivity.class);
                    intent.putExtra("stationsBean", (Serializable) mStationsBean);
                    intent.putExtra("oilNo", mOilNo);
                    intent.putExtra("gunNo", oilGunAdapter.getData().get(mOilGunPosition).getGunNo());
                    startActivity(intent);
                    closeDialog();
                    break;
            }
        });
        mGasStationTipsDialog.show();
    }

    private void showCarDialog(CarServeStoreDetailsBean carServeStoreBean) {
        mCarServiceDialog = new CarServiceDialog(mContext, carServeStoreBean);
        mCarServiceDialog.show();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getHomeOil();
        mViewModel.getOftenOils();
        mViewModel.getOftenCars();

        if (mStationsBean != null) {
            mViewModel.checkDistance(mStationsBean.getGasId());
        }
        mViewModel.getMenu();
        mViewModel.getRefuelJob();
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
        if (mLocationTipsDialog != null) {
            mLocationTipsDialog = null;
        }
        if (mGasStationTipsDialog != null) {
            mGasStationTipsDialog = null;
        }
        if (mCarServiceDialog != null) {
            mCarServiceDialog = null;
        }
        isShowAmount = false;
        getHomeOil();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusUtils.removeSticky(EventConstants.EVENT_JUMP_HUNTER_CODE);
    }

}
