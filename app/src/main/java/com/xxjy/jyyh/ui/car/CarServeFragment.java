package com.xxjy.jyyh.ui.car;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.CarServeListAdapter;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.adapter.SelectCarServeClassAdapter;
import com.xxjy.jyyh.adapter.SelectHomeCarServeClassAdapter;
import com.xxjy.jyyh.adapter.TopLineAdapter;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.constants.BannerPositionConstants;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.SPConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.FragmentCarServeBinding;
import com.xxjy.jyyh.databinding.FragmentOilBinding;
import com.xxjy.jyyh.dialog.CustomerServiceDialog;
import com.xxjy.jyyh.dialog.NavigationDialog;
import com.xxjy.jyyh.dialog.SelectAreaDialog;
import com.xxjy.jyyh.dialog.SelectBusinessStatusDialog;
import com.xxjy.jyyh.dialog.SelectCarTypeDialog;
import com.xxjy.jyyh.dialog.SelectDistanceDialog;
import com.xxjy.jyyh.dialog.SelectOilNumDialog;
import com.xxjy.jyyh.dialog.SelectProductCategoryDialog;
import com.xxjy.jyyh.dialog.SelectSortDialog;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.CarServeCategoryBean;
import com.xxjy.jyyh.entity.CarServeStoreBean;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.RedeemEntity;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.home.HomeViewModel;
import com.xxjy.jyyh.ui.integral.BannerViewModel;
import com.xxjy.jyyh.ui.msg.MessageCenterActivity;
import com.xxjy.jyyh.ui.oil.OilDetailsActivity;
import com.xxjy.jyyh.ui.oil.OilViewModel;
import com.xxjy.jyyh.ui.search.SearchActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.GlideUtils;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.eventtrackingmanager.EventTrackingManager;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingConstant;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingEventConstant;
import com.xxjy.jyyh.utils.locationmanger.MapIntentUtils;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RectangleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/21/21 11:52 AM
 * @project ElephantOil
 * @description:
 */
public class CarServeFragment extends BindingFragment<FragmentCarServeBinding, CarServeViewModel> {

    private HomeViewModel mHomeViewModel;

    public static CarServeFragment getInstance() {
        return new CarServeFragment();
    }

    private CarServeListAdapter carServeAdapter;
    private List<CarServeStoreBean> carServeData = new ArrayList<>();
    private double mLng, mLat;
    private CustomerServiceDialog customerServiceDialog;

    //-------------车服start----------------
    private SelectAreaDialog mSelectAreaDialog;
    private SelectBusinessStatusDialog mSelectBusinessStatusDialog;
    private SelectProductCategoryDialog mSelectProductCategoryDialog;
    private SelectCarTypeDialog mSelectCarTypeDialog;

    private int pageIndex=1;
    private String cityCode;
    private String areaCode;
    private long productCategoryId = -1;
    private int carType = -1;
    private int status=-1;
    private int channel = -1;//101 选中优选
    private BannerViewModel bannerViewModel;
    private OilViewModel oilViewModel;
    private String mDragLink;
    private boolean isTop = false;


    private SelectHomeCarServeClassAdapter selectCarServeClassAdapter;
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
        EventTrackingManager.getInstance().tracking(getBaseActivity(), String.valueOf(++Constants.PV_ID),
                TrackingConstant.GAS_LIST, "", "", "", TrackingConstant.OIL_MAIN_TYPE,null);
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
        carServeAdapter = new CarServeListAdapter(R.layout.adapter_car_serve_list, carServeData);
        mBinding.recyclerView.setAdapter(carServeAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBinding.tabServeClassView.setLayoutManager(linearLayoutManager);

        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        carServeAdapter.setOnItemClickListener((adapter, view, position) -> {

            LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                @Override
                public void onLogin() {
                    List<CarServeStoreBean> data = adapter.getData();
//                    Intent intent = new Intent(mContext, OilDetailActivity.class);
                    Intent intent = new Intent(mContext, CarServeDetailsActivity.class);
                    intent.putExtra("no", data.get(position).getCardStoreInfoVo().getStoreNo());
                    intent.putExtra("distance",data.get(position).getCardStoreInfoVo().getDistance());
                    intent.putExtra("channel",data.get(position).getCardStoreInfoVo().getChannel());
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
                                            data.get(position).getCardStoreInfoVo().getAddress());
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


        bannerViewModel = new ViewModelProvider(this).get(BannerViewModel.class);
        oilViewModel = new ViewModelProvider(this).get(OilViewModel.class);


//        getBanners();
        //110100
        cityCode = UserConstants.getCityCode();
        carType = UserConstants.getCarType();
        getAreaList(cityCode);
        getProductCategory();
        getCarType();
        getPoster();
        loadCarServeData(false);
        if (UserConstants.getIsLogin()){
            oilViewModel.getDragViewInfo();
        }

        EventTrackingManager.getInstance().tracking(getBaseActivity(),TrackingConstant.CF_PAGE_HOME,"");
    }

    @Override
    protected void initListener() {
        mBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    mBinding.search2Layout.setVisibility(View.VISIBLE);
                    mBinding.popupLayout.setBackgroundColor(Color.parseColor("#00000000"));
                    mBinding.carCitySelectTv.setTextColor(Color.parseColor("#FFFFFF"));
                    mBinding.carModelSelectTv.setTextColor(Color.parseColor("#FFFFFF"));
//                    mBinding.carSelectDistanceFirstTv.setTextColor(Color.parseColor("#FFFFFF"));
                    mBinding.carBusinessStatusTv.setTextColor(Color.parseColor("#FFFFFF"));
                    if(channel==-1){
                        mBinding.carOptimizationTv.setTextColor(Color.parseColor("#FE1530"));
                    }else{
                        mBinding.carOptimizationTv.setTextColor(Color.parseColor("#FFFFFF"));
                    }


                    mBinding.carImage1.setImageResource(R.drawable.icon_down_arrow_white);
                    mBinding.carImage5.setImageResource(R.drawable.icon_down_arrow_white);
//                    mBinding.carImage3.setImageResource(R.drawable.icon_down_arrow_white);
                    mBinding.carImage4.setImageResource(R.drawable.icon_down_arrow_white);
                    isTop = true;

                } else {
                    mBinding.search2Layout.setVisibility(View.GONE);
                    mBinding.popupLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));
                    mBinding.carCitySelectTv.setTextColor(Color.parseColor("#323334"));
                    mBinding.carModelSelectTv.setTextColor(Color.parseColor("#323334"));
//                    mBinding.carSelectDistanceFirstTv.setTextColor(Color.parseColor("#323334"));
                    mBinding.carBusinessStatusTv.setTextColor(Color.parseColor("#323334"));
                    if(channel==-1){
                        mBinding.carOptimizationTv.setTextColor(Color.parseColor("#FE1530"));
                    }else{
                        mBinding.carOptimizationTv.setTextColor(Color.parseColor("#323334"));
                    }
                    mBinding.carImage1.setImageResource(R.drawable.icon_down_arrow);
                    mBinding.carImage5.setImageResource(R.drawable.icon_down_arrow);
//                    mBinding.carImage3.setImageResource(R.drawable.icon_down_arrow);
                    mBinding.carImage4.setImageResource(R.drawable.icon_down_arrow);
                    isTop=false;
                }
            }
        });

        mBinding.refreshview.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    loadCarServeData(true);

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                getBanners();
                loadCarServeData(false);

            }
        });
        mBinding.customerServiceView.setOnClickListener(this::onViewClicked);
        mBinding.messageCenterView.setOnClickListener(this::onViewClicked);
        mBinding.topSearchLayout.setOnClickListener(this::onViewClicked);
        mBinding.closeView.setOnClickListener(this::onViewClicked);
        mBinding.openView.setOnClickListener(this::onViewClicked);


        mBinding.carCitySelectLayout.setOnClickListener(this::onViewClicked);
        mBinding.carBusinessStatusLayout.setOnClickListener(this::onViewClicked);
        mBinding.carModelSelectLayout.setOnClickListener(this::onViewClicked);
        mBinding.search2Layout.setOnClickListener(this::onViewClicked);

        mBinding.closeIv.setOnClickListener(this::onViewClicked);
        mBinding.dragView.setOnClickListener(this::onViewClicked);

        mBinding.carOptimizationLayout.setOnClickListener(this::onViewClicked);
        mBinding.carModelSelectLayout.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_2_layout:
            case R.id.top_search_layout:
                startActivity(new Intent(getContext(), SearchActivity.class).putExtra("type","carserve"));
                EventTrackingManager.getInstance().trackingEvent(getBaseActivity(),TrackingConstant.CF_PAGE_HOME, TrackingEventConstant.CF_EVENT_HOME_SEARCH);
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
                EventTrackingManager.getInstance().trackingEvent(getBaseActivity(),TrackingConstant.CF_PAGE_HOME, TrackingEventConstant.CF_EVENT_HOME_CUSTOMER);
                break;
            case R.id.message_center_view:
                LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        getActivity().startActivity(new Intent(getContext(), MessageCenterActivity.class));
                    }
                });
                EventTrackingManager.getInstance().trackingEvent(getBaseActivity(),TrackingConstant.CF_PAGE_HOME, TrackingEventConstant.CF_EVENT_HOME_MESSAGE);
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

            case R.id.car_city_select_layout:

                if (mSelectAreaDialog == null) {
                   return;
                }
                mSelectAreaDialog.show();
                mSelectAreaDialog.setOnItemClickedListener((adapter, view1, position, data) -> {
                    areaCode = data.getAreaCode();
                    mBinding.carCitySelectTv.setText(data.getArea());
                    mSelectAreaDialog.setSelectPosition(position);
                    mBinding.refreshview.resetNoMoreData();
                    loadCarServeData(false);
                });
                EventTrackingManager.getInstance().trackingEvent(getBaseActivity(),TrackingConstant.CF_PAGE_HOME, TrackingEventConstant.CF_EVENT_HOME_DISTRICT_CHOOSE);
                break;
//            case R.id.car_serve_select_layout:
//                if (mSelectProductCategoryDialog == null) {
//                   return;
//                }
//                mSelectProductCategoryDialog.show();
//                mSelectProductCategoryDialog.setOnItemClickedListener((adapter, view1, position, data) -> {
//                    productCategoryId = data.getId();
//                    mBinding.carServeSelectTv.setText(data.getName());
//                    mSelectProductCategoryDialog.setSelectPosition(position);
//                    mBinding.refreshview.resetNoMoreData();
//                    loadCarServeData(false);
//                });
//                break;
            case R.id.car_model_select_layout:
                if (mSelectCarTypeDialog == null) {
                   return;
                }
                mSelectCarTypeDialog.show();
                EventTrackingManager.getInstance().trackingEvent(getBaseActivity(),TrackingConstant.CF_PAGE_HOME, TrackingEventConstant.CF_EVENT_HOME_CARTYPE);
                break;
            case R.id.car_business_status_layout:
                if (mSelectBusinessStatusDialog == null) {
                    mSelectBusinessStatusDialog = new SelectBusinessStatusDialog(getContext(), mBinding.carTabSelectLayout, mBinding.getRoot());
                }
                mSelectBusinessStatusDialog.show();
                mSelectBusinessStatusDialog.setOnItemClickedListener((adapter, view1, position, data) -> {
                    status= data.getStatus();
                    mBinding.carBusinessStatusTv.setText(data.getName().equals("全部")?"营业状态":data.getName());
                    mSelectBusinessStatusDialog.setSelectPosition(position);
                    mBinding.refreshview.resetNoMoreData();
                    loadCarServeData(false);
                });
                EventTrackingManager.getInstance().trackingEvent(getBaseActivity(),TrackingConstant.CF_PAGE_HOME, TrackingEventConstant.CF_EVENT_HOME_ONLINE_CHOOSE);
                break;
            case R.id.car_optimization_layout:
                if(channel==-1){
                    channel=101;
                    if(isTop){
                        mBinding.carOptimizationTv.setTextColor(Color.parseColor("#FFFFFF"));
                    }else{
                        mBinding.carOptimizationTv.setTextColor(Color.parseColor("#323334"));
                    }

                }else{
                    channel=-1;
                    mBinding.carOptimizationTv.setTextColor(Color.parseColor("#FE1530"));
                }
                loadCarServeData(false);
                EventTrackingManager.getInstance().trackingEvent(getBaseActivity(),TrackingConstant.CF_PAGE_HOME, TrackingEventConstant.CF_EVENT_HOME_SELECT);
                break;

            case R.id.close_iv:
                mBinding.dragView.setVisibility(View.GONE);
                break;
            case R.id.drag_view:
                if (!TextUtils.isEmpty(mDragLink)) {
                    SPUtils.getInstance().put(SPConstants.IS_TODAY, TimeUtils.getNowString());
                    WebViewActivity.openRealUrlWebActivity(getBaseActivity(), mDragLink);
//                    mBinding.dragView.setVisibility(View.GONE);
                }
                break;
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

        bannerViewModel.getBannerOfPostion(BannerPositionConstants.CAR_SERVE_LIST).observe(this, data -> {
            if (data != null) {
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
                            NaviActivityInfo.disPathIntentFromUrl((MainActivity) getActivity(), data.getLink());
                        });
                    }
                }).addBannerLifecycleObserver(this)
                        .setIndicator(new RectangleIndicator(mContext));
            } else {
                mBinding.banner.setVisibility(View.GONE);
            }


        });


        mViewModel.cityListLiveData.observe(this,data ->{

            mSelectAreaDialog = new SelectAreaDialog(getContext(),mBinding.carTabSelectLayout, mBinding.getRoot(),data.getAreasList());

        });
        mViewModel.productCategoryLiveData.observe(this,data->{
//            mSelectProductCategoryDialog = new SelectProductCategoryDialog(getContext(),mBinding.carTabSelectLayout, mBinding.getRoot(),data.getRecords());

        initTab(data.getRecords());
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

        oilViewModel.dragViewLiveData.observe(this, new Observer<RedeemEntity>() {
            @Override
            public void onChanged(RedeemEntity redeemEntity) {
                if (TextUtils.isEmpty(redeemEntity.getImgUrl())) {
                    mBinding.dragView.setVisibility(View.GONE);
                } else {
                    mDragLink = redeemEntity.getLink();
                    Glide.with(mContext).load(redeemEntity.getImgUrl()).into(mBinding.jumpIntegral);
                    String nowMills = SPUtils.getInstance().getString(SPConstants.IS_TODAY);
                    if (!TextUtils.isEmpty(nowMills)) {
                        boolean today = TimeUtils.isToday(nowMills);
                        if (!today) {
                            mBinding.dragView.setVisibility(View.VISIBLE);
                        } else {
                            mBinding.dragView.setVisibility(View.GONE);
                        }
                    } else {
                        mBinding.dragView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        mViewModel.carTypeLiveData.observe(this,data->{
            int n = -1;
            if (UserConstants.getCarType() != -1) {
                for (int i=0;i<data.size();i++) {
                    if(data.get(i).getValue()==UserConstants.getCarType()){
                        n=i;
                        data.get(i).setChecked(true);
                        mBinding.carModelSelectTv.setText(data.get(i).getName());
                    }else{
                        data.get(i).setChecked(false);
                    }
                }
            }

            mSelectCarTypeDialog = new SelectCarTypeDialog(getContext(),mBinding.carTabSelectLayout, mBinding.getRoot(),data);
           if(n!=-1){
               mSelectCarTypeDialog.setSelectPosition(n);
           }else{
               mSelectCarTypeDialog.show();
           }

            mSelectCarTypeDialog.setOnItemClickedListener((adapter, view1, position, bean) -> {
                carType = bean.getValue();
                mBinding.carModelSelectTv.setText(bean.getName());
                mSelectCarTypeDialog.setSelectPosition(position);
                mBinding.refreshview.resetNoMoreData();
                loadCarServeData(false);
            });
        });
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

    private void initTab(List<CarServeCategoryBean> records) {
        records.add(0,new CarServeCategoryBean(-1,"全部",true));

        selectCarServeClassAdapter = new SelectHomeCarServeClassAdapter(R.layout.adapter_select_car_serve_class, records);
        selectCarServeClassAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                selectCarServeClassAdapter.setSelectPosition(position);
                productCategoryId = records.get(position).getId();
                loadCarServeData(false);
                EventTrackingManager.getInstance().trackingEvent(getBaseActivity(),TrackingConstant.CF_PAGE_HOME,
                        TrackingEventConstant.CF_EVENT_HOME_SERVICE_CHOOSE+selectCarServeClassAdapter.getItem(position).getName());

            }
        });
        selectCarServeClassAdapter.setSelectPosition(0);
        mBinding.tabServeClassView.setAdapter(selectCarServeClassAdapter);
    }

//    private void getBanners() {
//        mViewModel.getBanners();
//    }

    private void getAreaList(String cityCode){
        mViewModel.getAreaList(TextUtils.isEmpty(cityCode)?"110100":cityCode);
    }
    //获取车服服务分类
    private void getProductCategory(){
        mViewModel.getProductCategory();
    }
    //获取车型
    private void getCarType(){
        mViewModel.getCarType();
    }
    private void getPoster(){
        mViewModel.getPoster(1).observe(this,data->{
            if(data!=null&&data.size()>0){
                mBinding.banner2.setVisibility(View.VISIBLE);
                GlideUtils.loadImage(getContext(),data.get(0).getPic(),mBinding.banner2);
                mBinding.banner2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NaviActivityInfo.disPathIntentFromUrl(getBaseActivity(),data.get(0).getUrl());
                        EventTrackingManager.getInstance().trackingEvent(getBaseActivity(),TrackingConstant.CF_PAGE_HOME, TrackingEventConstant.CF_EVENT_HOME_BANNER);
                    }
                });
            }else{
                mBinding.banner2.setVisibility(View.GONE);
            }

        });
        mViewModel.getPoster(3).observe(this,data->{
            if(data!=null&&data.size()>0) {
                carServeAdapter.setPoster(data.get(0));
            }
        });
    }


    private void getCarServeStoreList(){
        mViewModel.getCarServeStoreList(pageIndex, TextUtils.isEmpty(cityCode)?"110100":cityCode, areaCode, productCategoryId, status,channel,carType,"");
    }
}
