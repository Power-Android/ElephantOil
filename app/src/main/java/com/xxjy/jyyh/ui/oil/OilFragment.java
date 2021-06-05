package com.xxjy.jyyh.ui.oil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.CarServeListAdapter;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.adapter.SelecBusinessStatusAdapter;
import com.xxjy.jyyh.adapter.TopLineAdapter;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.FragmentOilBinding;
import com.xxjy.jyyh.dialog.CustomerServiceDialog;
import com.xxjy.jyyh.dialog.NavigationDialog;
import com.xxjy.jyyh.dialog.SelectAreaDialog;
import com.xxjy.jyyh.dialog.SelectBusinessStatusDialog;
import com.xxjy.jyyh.dialog.SelectDistanceDialog;
import com.xxjy.jyyh.dialog.SelectOilNumDialog;
import com.xxjy.jyyh.dialog.SelectProductCategoryDialog;
import com.xxjy.jyyh.dialog.SelectSortDialog;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.CarServeStoreBean;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.car.CarServeDetailsActivity;
import com.xxjy.jyyh.ui.home.HomeViewModel;
import com.xxjy.jyyh.ui.msg.MessageCenterActivity;
import com.xxjy.jyyh.ui.search.SearchActivity;
import com.xxjy.jyyh.ui.setting.AboutUsActivity;
import com.xxjy.jyyh.ui.setting.SettingActivity;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.eventtrackingmanager.EventTrackingManager;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingConstant;
import com.xxjy.jyyh.utils.locationmanger.MapIntentUtils;
import com.xxjy.jyyh.wight.SettingLayout;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RectangleIndicator;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/21/21 11:52 AM
 * @project ElephantOil
 * @description:
 */
public class OilFragment extends BindingFragment<FragmentOilBinding, OilViewModel> {

    private HomeViewModel mHomeViewModel;

    public static OilFragment getInstance() {
        return new OilFragment();
    }

    private final String[] titles = new String[]{"加油特惠", "洗车特惠"};
    private OilStationListAdapter adapter;
    private CarServeListAdapter carServeAdapter;
    private List<OilEntity.StationsBean> data = new ArrayList<>();
    private List<CarServeStoreBean> carServeData = new ArrayList<>();
    private SelectOilNumDialog selectOilNumDialog;
    private SelectDistanceDialog selectDistanceDialog;
    private SelectSortDialog mSelectSortDialog;
    private String mCheckOilGasId = "全部";
    private int distance = 50;
    private String firstDistanceOrPrice = "0";
    private int pageNum = 1;
    private int pageSize = 10;
    private double mLng, mLat;
    private boolean isOilServe = true;
    private boolean isScrollTop = false;

    private CustomerServiceDialog customerServiceDialog;

    //-------------车服start----------------
    private SelectAreaDialog mSelectAreaDialog;
    private SelectBusinessStatusDialog mSelectBusinessStatusDialog;
    private SelectProductCategoryDialog mSelectProductCategoryDialog;

    private int pageIndex=1;
    private String cityCode;
    private String areaCode;
    private long productCategoryId = -1;
    private int status=0;
    //-----------车服end--------------
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                mBinding.locationView.setVisibility(View.GONE);
                BarUtils.addMarginTopEqualStatusBarHeight(mBinding.parentLayout);
            } else {
                if (Constants.OPEN_LOCATION_VISIBLE) {
                    mBinding.locationView.setVisibility(View.VISIBLE);
                    BarUtils.addMarginTopEqualStatusBarHeight(mBinding.locationImageView);
                    BarUtils.addMarginTopEqualStatusBarHeight(mBinding.closeView);
                    BarUtils.addMarginTopEqualStatusBarHeight(mBinding.openView);
                } else {
                    mBinding.locationView.setVisibility(View.GONE);
                    BarUtils.addMarginTopEqualStatusBarHeight(mBinding.parentLayout);
                }
            }
        }
    }


    @Override
    protected void onVisible() {
        super.onVisible();
        if (PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            mBinding.locationView.setVisibility(View.GONE);
            BarUtils.addMarginTopEqualStatusBarHeight(mBinding.parentLayout);
        } else {
            if (Constants.OPEN_LOCATION_VISIBLE) {
                mBinding.locationView.setVisibility(View.VISIBLE);
                BarUtils.addMarginTopEqualStatusBarHeight(mBinding.locationImageView);
                BarUtils.addMarginTopEqualStatusBarHeight(mBinding.closeView);
                BarUtils.addMarginTopEqualStatusBarHeight(mBinding.openView);
            } else {
                mBinding.locationView.setVisibility(View.GONE);
                BarUtils.addMarginTopEqualStatusBarHeight(mBinding.parentLayout);
            }
        }
        mHomeViewModel.getLocation();
        EventTrackingManager.getInstance().tracking(mContext, getBaseActivity(), String.valueOf(++Constants.PV_ID),
                TrackingConstant.GAS_LIST, "", "", "", TrackingConstant.OIL_MAIN_TYPE);
    }

    @Override
    public void initView() {
        if (PermissionUtils.isGranted(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            mBinding.locationView.setVisibility(View.GONE);
            BarUtils.addMarginTopEqualStatusBarHeight(mBinding.parentLayout);
        } else {
            mBinding.locationView.setVisibility(View.VISIBLE);
            BarUtils.addMarginTopEqualStatusBarHeight(mBinding.locationImageView);
            BarUtils.addMarginTopEqualStatusBarHeight(mBinding.closeView);
            BarUtils.addMarginTopEqualStatusBarHeight(mBinding.openView);
        }


        mBinding.refreshview.setEnableLoadMore(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OilStationListAdapter(R.layout.adapter_oil_station_list, data);
        carServeAdapter = new CarServeListAdapter(R.layout.adapter_car_serve_list, carServeData);
        mBinding.recyclerView.setAdapter(adapter);

        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        adapter.setOnItemClickListener((adapter, view, position) -> {

            LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                @Override
                public void onLogin() {
                    List<OilEntity.StationsBean> data = adapter.getData();
//                    Intent intent = new Intent(mContext, OilDetailActivity.class);
                    Intent intent = new Intent(mContext, OilDetailsActivity.class);
                    intent.putExtra(Constants.GAS_STATION_ID, data.get(position).getGasId());
                    intent.putExtra(Constants.OIL_NUMBER_ID, data.get(position).getOilNo());
                    startActivity(intent);
                }
            });

        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<OilEntity.StationsBean> data = adapter.getData();
                LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        switch (view.getId()) {
                            case R.id.navigation_ll:
                                if (MapIntentUtils.isPhoneHasMapNavigation()) {
                                    NavigationDialog navigationDialog = new NavigationDialog(getBaseActivity(),
                                            data.get(position).getStationLatitude(), data.get(position).getStationLongitude(),
                                            data.get(position).getGasName());
                                    navigationDialog.show();
                                } else {
                                    showToastWarning("您当前未安装地图软件，请先安装");
                                }
                                break;
                        }

                    }
                });
            }
        });
        carServeAdapter.setOnItemClickListener((adapter, view, position) -> {

            LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                @Override
                public void onLogin() {
                    List<CarServeStoreBean> data = adapter.getData();
//                    Intent intent = new Intent(mContext, OilDetailActivity.class);
                    Intent intent = new Intent(mContext, CarServeDetailsActivity.class);
                    intent.putExtra("no", data.get(position).getCardStoreInfoVo().getStoreNo());
                    intent.putExtra("distance",data.get(position).getCardStoreInfoVo().getDistance());
                    startActivity(intent);
                }
            });

        });
        carServeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<CarServeStoreBean> data = adapter.getData();
                LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        switch (view.getId()) {
                            case R.id.navigation_ll:
                                if (MapIntentUtils.isPhoneHasMapNavigation()) {
                                    NavigationDialog navigationDialog = new NavigationDialog(getBaseActivity(),
                                            data.get(position).getCardStoreInfoVo().getLatitude(), data.get(position).getCardStoreInfoVo().getLongitude(),
                                            data.get(position).getCardStoreInfoVo().getStoreName());
                                    navigationDialog.show();
                                } else {
                                    showToastWarning("您当前未安装地图软件，请先安装");
                                }
                                break;
                        }

                    }
                });
            }
        });

        mBinding.msgBanner.setAdapter(new TopLineAdapter(new ArrayList<>(), true))
                .setOrientation(Banner.VERTICAL)
                .setUserInputEnabled(false);


        getOrderNews();
        loadData(false);

        getBanners();
        //110100
        cityCode = UserConstants.getCityCode();
        getAreaList(cityCode);
        getProductCategory();
        loadCarServeData(false);
    }

    @Override
    protected void initListener() {
        mBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    isScrollTop = true;
                    mBinding.searchLayout.setVisibility(View.VISIBLE);
                    mBinding.search2Layout.setVisibility(View.VISIBLE);
                    mBinding.tabLayout.setBackgroundColor(Color.parseColor("#00000000"));
//                    mBinding.popupLayout.setBackgroundColor(Color.parseColor("#00000000"));
//                    mBinding.oilSelectDistanceTv.setTextColor(Color.parseColor("#FFFFFF"));
//                    mBinding.oilSortOilNumTv.setTextColor(Color.parseColor("#FFFFFF"));
//                    mBinding.oilSelectDistanceFirstTv.setTextColor(Color.parseColor("#FFFFFF"));
//                    mBinding.image1.setImageResource(R.drawable.icon_down_arrow_white);
//                    mBinding.image2.setImageResource(R.drawable.icon_down_arrow_white);
//                    mBinding.image3.setImageResource(R.drawable.icon_down_arrow_white);
//
                } else {
                    isScrollTop = false;
                    mBinding.searchLayout.setVisibility(View.GONE);
                    mBinding.search2Layout.setVisibility(View.GONE);
                    mBinding.tabLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));

//                    mBinding.popupLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));
//                    mBinding.oilSelectDistanceTv.setTextColor(Color.parseColor("#323334"));
//                    mBinding.oilSortOilNumTv.setTextColor(Color.parseColor("#323334"));
//                    mBinding.oilSelectDistanceFirstTv.setTextColor(Color.parseColor("#323334"));
//                    mBinding.image1.setImageResource(R.drawable.icon_down_arrow);
//                    mBinding.image2.setImageResource(R.drawable.icon_down_arrow);
//                    mBinding.image3.setImageResource(R.drawable.icon_down_arrow);
//
//
//
                }
                changeTab(isScrollTop);
            }
        });

        mBinding.refreshview.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if(isOilServe){
                    loadData(true);
                }else{
                    loadCarServeData(true);
                }


            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getOrderNews();
                getBanners();
                loadData(false);
                loadCarServeData(false);

            }
        });
        mBinding.oilSortLayout.setOnClickListener(this::onViewClicked);
        mBinding.oilSelectLayout.setOnClickListener(this::onViewClicked);
        mBinding.oilDistancePriceLayout.setOnClickListener(this::onViewClicked);
        mBinding.customerServiceView.setOnClickListener(this::onViewClicked);
        mBinding.messageCenterView.setOnClickListener(this::onViewClicked);
        mBinding.topSearchLayout.setOnClickListener(this::onViewClicked);
        mBinding.searchLayout.setOnClickListener(this::onViewClicked);
        mBinding.closeView.setOnClickListener(this::onViewClicked);
        mBinding.openView.setOnClickListener(this::onViewClicked);

        mBinding.tab1View.setOnClickListener(this::onViewClicked);
        mBinding.tab2View.setOnClickListener(this::onViewClicked);

        mBinding.carCitySelectLayout.setOnClickListener(this::onViewClicked);
        mBinding.carBusinessStatusLayout.setOnClickListener(this::onViewClicked);
        mBinding.carServeSelectLayout.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_layout:
            case R.id.top_search_layout:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
            case R.id.oil_sort_layout:

                getOilNums();

                break;
            case R.id.oil_select_layout:
                if (selectDistanceDialog == null) {
                    selectDistanceDialog = new SelectDistanceDialog(getContext(), mBinding.popupLayout, mBinding.getRoot(), true);
                }
                selectDistanceDialog.show();
                selectDistanceDialog.setOnItemClickedListener((adapter, view1, position, distanceEntity) -> {
                    distance = distanceEntity.getDistance();
                    mBinding.oilSelectDistanceTv.setText(distanceEntity.getTitle());

                    loadData(false);
                });
                break;
            case R.id.oil_distance_price_layout:
                if (mSelectSortDialog == null) {
                    mSelectSortDialog = new SelectSortDialog(getContext(), mBinding.popupLayout, mBinding.getRoot());
                }
                mSelectSortDialog.show();
                mSelectSortDialog.setOnItemClickedListener((adapter, view1, position, distanceEntity) -> {
                    firstDistanceOrPrice = distanceEntity.getDistance() + "";
                    mBinding.oilSelectDistanceFirstTv.setText(distanceEntity.getTitle());
                    mSelectSortDialog.setSelectPosition(position);
                    loadData(false);
                });
                break;
            case R.id.customer_service_view:
                LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        if (customerServiceDialog == null) {
                            customerServiceDialog = new CustomerServiceDialog(getBaseActivity());
                        }
                        customerServiceDialog.show(view);
                    }
                });

                break;
            case R.id.message_center_view:
                LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        getActivity().startActivity(new Intent(getContext(), MessageCenterActivity.class));
                    }
                });

                break;
            case R.id.close_view:
                mBinding.locationView.setVisibility(View.GONE);
                BarUtils.addMarginTopEqualStatusBarHeight(mBinding.parentLayout);
                Constants.OPEN_LOCATION_VISIBLE = false;
                break;
            case R.id.open_view:
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", App.getContext().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                break;
            case R.id.tab_1_view:
                isOilServe = true;
                changeTab(isScrollTop);
                mBinding.recyclerView.setAdapter(adapter);
                mBinding.refreshview.resetNoMoreData();
                break;
            case R.id.tab_2_view:
                isOilServe = false;
                changeTab(isScrollTop);
                mBinding.recyclerView.setAdapter(carServeAdapter);
                mBinding.refreshview.resetNoMoreData();
                break;

            case R.id.car_city_select_layout:
                if (mSelectAreaDialog == null) {
                   return;
                }
                mSelectAreaDialog.show();
                mSelectAreaDialog.setOnItemClickedListener((adapter, view1, position, data) -> {
                    areaCode = data.getAreaCode();
                    mBinding.carCitySelectTv.setText(data.getArea());
                    mSelectAreaDialog.setSelectPosition(position);
                    loadCarServeData(false);
                });
                break;
            case R.id.car_serve_select_layout:
                if (mSelectProductCategoryDialog == null) {
                   return;
                }
                mSelectProductCategoryDialog.show();
                mSelectProductCategoryDialog.setOnItemClickedListener((adapter, view1, position, data) -> {
                    productCategoryId = data.getId();
                    mBinding.carServeSelectTv.setText(data.getName());
                    mSelectAreaDialog.setSelectPosition(position);
                    loadCarServeData(false);
                });
                break;
            case R.id.car_business_status_layout:
                if (mSelectBusinessStatusDialog == null) {
                    mSelectBusinessStatusDialog = new SelectBusinessStatusDialog(getContext(), mBinding.carTabSelectLayout, mBinding.getRoot());
                }
                mSelectBusinessStatusDialog.show();
                mSelectBusinessStatusDialog.setOnItemClickedListener((adapter, view1, position, data) -> {
                    status= data.getStatus();
                    mBinding.carBusinessStatusTv.setText(data.getName());
                    mSelectBusinessStatusDialog.setSelectPosition(position);
                    loadCarServeData(false);
                });
                break;
        }
    }

    private void changeTab(boolean isTop) {
        if (isOilServe) {
            if (isTop) {
                mBinding.tab1View.setTextColor(Color.parseColor("#FFFFFF"));
                mBinding.tab1IndexView.setBackgroundResource(R.drawable.shape_tab_index_white);
                mBinding.tab2View.setTextColor(Color.parseColor("#80FFFFFF"));
                mBinding.tab2IndexView.setBackgroundResource(R.drawable.shape_tab_index_white);
            } else {
                mBinding.tab1View.setTextColor(Color.parseColor("#212121"));
                mBinding.tab1IndexView.setBackgroundResource(R.drawable.shape_tab_index);
                mBinding.tab2View.setTextColor(Color.parseColor("#808080"));
                mBinding.tab2IndexView.setBackgroundResource(R.drawable.shape_tab_index);
            }


            mBinding.tab1View.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            mBinding.tab1IndexView.setVisibility(View.VISIBLE);
            mBinding.tab2View.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            mBinding.tab2IndexView.setVisibility(View.INVISIBLE);
            mBinding.tabSelectLayout.setVisibility(View.VISIBLE);
            mBinding.carTabSelectLayout.setVisibility(View.GONE);
        } else {
            if (isTop) {
                mBinding.tab1View.setTextColor(Color.parseColor("#80FFFFFF"));
                mBinding.tab1IndexView.setBackgroundResource(R.drawable.shape_tab_index_white);
                mBinding.tab2View.setTextColor(Color.parseColor("#FFFFFF"));
                mBinding.tab2IndexView.setBackgroundResource(R.drawable.shape_tab_index_white);
            } else {
                mBinding.tab1View.setTextColor(Color.parseColor("#808080"));
                mBinding.tab1IndexView.setBackgroundResource(R.drawable.shape_tab_index);
                mBinding.tab2View.setTextColor(Color.parseColor("#212121"));
                mBinding.tab2IndexView.setBackgroundResource(R.drawable.shape_tab_index);
            }


            mBinding.tab1View.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            mBinding.tab1IndexView.setVisibility(View.INVISIBLE);
            mBinding.tab2View.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            mBinding.tab2IndexView.setVisibility(View.VISIBLE);
            mBinding.tabSelectLayout.setVisibility(View.GONE);
            mBinding.carTabSelectLayout.setVisibility(View.VISIBLE);

        }


    }

    private void screenTab(boolean isTop, boolean isCarServe) {
        if (isTop) {
            mBinding.tab1View.setTextColor(Color.parseColor("#FFFFFF"));
            mBinding.tab1IndexView.setBackgroundResource(R.drawable.shape_tab_index_white);
        } else {
            mBinding.tab1View.setTextColor(Color.parseColor("#FFFFFF"));
            mBinding.tab1IndexView.setBackgroundResource(R.drawable.shape_tab_index_white);
        }


    }

    private void requestPermission() {
        PermissionUtils.permission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        mHomeViewModel.getLocation();
                    }

                    @Override
                    public void onDenied() {
//                        mLat = 0;
//                        mLng = 0;
//                        mViewModel.getHomeOil(mLat, mLng);
//                        showFailLocation();
                        showToastWarning("权限被拒绝，请去设置里开启");
                    }
                })
                .request();
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
            }
        });

        mViewModel.orderNewsLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                mBinding.msgBanner.setDatas(data);
                mBinding.msgIv.setVisibility(View.VISIBLE);
                mBinding.newsLayout.setVisibility(View.VISIBLE);
            } else {
                mBinding.msgIv.setVisibility(View.INVISIBLE);
                mBinding.newsLayout.setVisibility(View.INVISIBLE);
            }
        });
        mViewModel.oilNumLiveData.observe(this, data -> {
            if (selectOilNumDialog == null) {
                selectOilNumDialog = new SelectOilNumDialog(getContext(), mCheckOilGasId, mBinding.popupLayout, mBinding.getRoot());
            }
            selectOilNumDialog.setData(data);
            selectOilNumDialog.show();
            selectOilNumDialog.setOnItemClickedListener(new SelectOilNumDialog.OnItemClickedListener() {
                @Override
                public void onOilNumClick(BaseQuickAdapter adapter, View view, int position, String oilNum, String checkOilGasId) {
                    mCheckOilGasId = checkOilGasId;
                    mBinding.oilSortOilNumTv.setText(oilNum);
                    selectOilNumDialog.setCheckData(mCheckOilGasId);
                    loadData(false);
                }

                @Override
                public void onOilNumAllClick(BaseQuickAdapter adapter, View view, String checkOilGasId) {
                    mCheckOilGasId = checkOilGasId;
                    mBinding.oilSortOilNumTv.setText("全部");
                    selectOilNumDialog.setCheckData(mCheckOilGasId);
                    loadData(false);
                }
            });
        });
        mViewModel.bannersLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                mBinding.banner.setVisibility(View.VISIBLE);
                //banner
                mBinding.banner.setAdapter(new BannerImageAdapter<BannerBean>(data) {
                    @Override
                    public void onBindView(BannerImageHolder holder, BannerBean data, int position, int size) {
                        Glide.with(holder.imageView)
                                .load(data.getImgUrl())
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.bg_banner_loading)
                                        .error(R.drawable.bg_banner_error))
                                .into(holder.imageView);
                        holder.imageView.setOnClickListener(v -> {
//                            LoginHelper.login(getContext(), new LoginHelper.CallBack() {
//                                @Override
//                                public void onLogin() {
                            NaviActivityInfo.disPathIntentFromUrl((MainActivity) getActivity(), data.getLink());
//                                }
//                            });

//                            WebViewActivity.openWebActivity((MainActivity) getActivity(), data.getLink());
                        });
                    }
                }).addBannerLifecycleObserver(this)
                        .setIndicator(new RectangleIndicator(mContext));
            } else {
                mBinding.banner.setVisibility(View.GONE);
            }


        });

        mViewModel.oilStationLiveData.observe(this, dataStations -> {
            if (dataStations != null && dataStations.getStations() != null && dataStations.getStations().size() > 0) {
                if (pageNum == 1) {
                    adapter.setNewData(dataStations.getStations());
                    mBinding.refreshview.setEnableLoadMore(true);
                    mBinding.refreshview.finishRefresh(true);
                } else {
                    adapter.addData(dataStations.getStations());
                    mBinding.refreshview.finishLoadMore(true);
                }
                mBinding.noResultLayout.setVisibility(View.GONE);
            } else if (pageNum == 1) {
                mBinding.refreshview.finishLoadMoreWithNoMoreData();
                mBinding.noResultLayout.setVisibility(View.VISIBLE);
            } else {
                pageIndex--;
                mBinding.refreshview.finishLoadMoreWithNoMoreData();
            }
        });

        mViewModel.cityListLiveData.observe(this,data ->{

            mSelectAreaDialog = new SelectAreaDialog(getContext(),mBinding.carTabSelectLayout, mBinding.getRoot(),data.getAreasList());

        });
        mViewModel.productCategoryLiveData.observe(this,data->{
            mSelectProductCategoryDialog = new SelectProductCategoryDialog(getContext(),mBinding.carTabSelectLayout, mBinding.getRoot(),data.getRecords());

        });
        mViewModel.storeListLiveData.observe(this, dataStations -> {
            if (dataStations != null && dataStations.getRecords() != null && dataStations.getRecords().size() > 0) {
                if (pageIndex == 1) {
                    carServeAdapter.setNewData(dataStations.getRecords());
                    mBinding.refreshview.setEnableLoadMore(true);
                    mBinding.refreshview.finishRefresh(true);
                } else {
                    carServeAdapter.addData(dataStations.getRecords());
                    mBinding.refreshview.finishLoadMore(true);
                }
                mBinding.noResultLayout.setVisibility(View.GONE);
            } else if (pageIndex == 1) {
                mBinding.refreshview.finishLoadMoreWithNoMoreData();
                mBinding.noResultLayout.setVisibility(View.VISIBLE);
            } else {
                pageIndex--;
                mBinding.refreshview.finishLoadMoreWithNoMoreData();
            }
        });
    }

    private void loadData(boolean isLoadMore) {
        if (isLoadMore) {
            pageNum++;
            getOilStations();


        } else {
            pageNum = 1;
            getOilStations();

        }

    }
    private void loadCarServeData(boolean isLoadMore) {
        if (isLoadMore) {
            pageIndex++;
            getCarServeStoreList();


        } else {
            pageIndex = 1;
            getCarServeStoreList();

        }

    }

    private void getOrderNews() {
        mViewModel.getOrderNews();
    }

    private void getOilNums() {
        mViewModel.getOilNums();
    }

    private void getOilStations() {
        mViewModel.getOilStations(UserConstants.getLatitude(), UserConstants.getLongitude(),
                mCheckOilGasId, firstDistanceOrPrice,
                distance == -1 ? null : String.valueOf(distance * 1000),
                String.valueOf(pageNum), String.valueOf(pageSize), null, null);
    }

    private void getBanners() {
        mViewModel.getBanners();
    }

    private void getAreaList(String cityCode){
        mViewModel.getAreaList(cityCode);
    }
    //获取车服服务分类
    private void getProductCategory(){
        mViewModel.getProductCategory();
    }
    private void getCarServeStoreList(){
        mViewModel.getCarServeStoreList(pageIndex, cityCode, areaCode, productCategoryId, status);
    }
}
