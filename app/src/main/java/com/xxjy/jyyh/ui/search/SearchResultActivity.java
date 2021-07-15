package com.xxjy.jyyh.ui.search;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.CarServeListAdapter;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.adapter.SearchIntegralAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivitySearchResultBinding;
import com.xxjy.jyyh.dialog.NavigationDialog;
import com.xxjy.jyyh.dialog.SelectAreaDialog;
import com.xxjy.jyyh.dialog.SelectBusinessStatusDialog;
import com.xxjy.jyyh.dialog.SelectCarTypeDialog;
import com.xxjy.jyyh.dialog.SelectDistanceDialog;
import com.xxjy.jyyh.dialog.SelectOilNumDialog;
import com.xxjy.jyyh.dialog.SelectProductCategoryDialog;
import com.xxjy.jyyh.entity.CarServeStoreBean;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.ProductBean;
import com.xxjy.jyyh.room.DBInstance;
import com.xxjy.jyyh.ui.car.CarServeDetailsActivity;
import com.xxjy.jyyh.ui.car.CarServeViewModel;
import com.xxjy.jyyh.ui.oil.OilDetailsActivity;
import com.xxjy.jyyh.ui.oil.OilViewModel;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.eventtrackingmanager.EventTrackingManager;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingConstant;
import com.xxjy.jyyh.utils.locationmanger.MapIntentUtils;

import java.util.ArrayList;
import java.util.List;

import static com.xxjy.jyyh.app.App.getContext;

public class SearchResultActivity extends BindingActivity<ActivitySearchResultBinding, SearchViewModel> implements OnRefreshLoadMoreListener {

    private List<OilEntity.StationsBean> mOilList = new ArrayList<>();
    private List<ProductBean> mIntegralList = new ArrayList<>();

    private String mType; // 1是油站搜索 2是权益搜索
    private String mContent; // 1是油站搜索 2是权益搜索

    private SelectDistanceDialog selectDistanceDialog;
    private SelectOilNumDialog selectOilNumDialog;
    private int distance = -1;
    private OilViewModel mOilViewModel;
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean firstDistanceOrPrice = true;
    private String mCheckOilGasId = "全部";
    private OilStationListAdapter mOilListAdapter;
    private String integralType = "1";//权益type 1 是 综合  2 是价格 3 是销量
    private SearchIntegralAdapter mIntegralAdapter;


    private CarServeViewModel carServeViewModel;
    private CarServeListAdapter carServeAdapter;
    private List<CarServeStoreBean> carServeData = new ArrayList<>();
    private String cityCode;
    private String areaCode;
    private long productCategoryId = -1;
    private int carType = -1;
    private int status=-1;
    private int channel = -1;//101 选中优选
    private SelectAreaDialog mSelectAreaDialog;
    private SelectBusinessStatusDialog mSelectBusinessStatusDialog;
    private SelectProductCategoryDialog mSelectProductCategoryDialog;
    private SelectCarTypeDialog mSelectCarTypeDialog;
    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, false);

        mType = getIntent().getStringExtra("type");
        mContent = getIntent().getStringExtra("content");

        mBinding.searchEt.setText(mContent);

        mOilViewModel = new ViewModelProvider(this).get(OilViewModel.class);
        carServeViewModel =new  ViewModelProvider(this).get(CarServeViewModel.class);
        if (TextUtils.equals("1", mType)) {
            mBinding.tab1Tv.setText("距离不限");
            mBinding.tab2Tv.setText("全部");
            mBinding.tab3Tv.setText("距离优先");
            mBinding.searchEt.setHint("搜索油站名称");
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            mOilListAdapter = new OilStationListAdapter(R.layout.adapter_oil_station_list, mOilList);
            mBinding.recyclerView.setAdapter(mOilListAdapter);
            mOilListAdapter.setOnItemClickListener((adapter, view, position) -> {

                LoginHelper.login(this, new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        List<OilEntity.StationsBean> data = adapter.getData();
//                        Intent intent = new Intent(SearchResultActivity.this, OilDetailActivity.class);
                        Intent intent = new Intent(SearchResultActivity.this, OilDetailsActivity.class);
                        intent.putExtra(Constants.GAS_STATION_ID, data.get(position).getGasId());
                        intent.putExtra(Constants.OIL_NUMBER_ID, data.get(position).getOilNo());
                        startActivity(intent);
                    }
                });
            });

            getOilStations(UserConstants.getLatitude(), UserConstants.getLongitude(), mCheckOilGasId,
                    firstDistanceOrPrice ? "1" : "2", distance == -1 ? null : String.valueOf(distance * 1000),
                    String.valueOf(pageNum), String.valueOf(pageSize), mContent, "sb");

            selectDistanceDialog = new SelectDistanceDialog(
                    this, mBinding.tabLayout, mBinding.getRoot(), false);
            selectDistanceDialog.setSelectPosition(5);

            selectOilNumDialog = new SelectOilNumDialog(getContext(), mCheckOilGasId, mBinding.tabLayout, mBinding.getRoot());

            EventTrackingManager.getInstance().tracking( this,
                    TrackingConstant.SEARCH_LIST,  "type=1");

        } else if(TextUtils.equals("2", mType)){
            mBinding.tab1Tv.setText("综合");
            mBinding.tab1Tv.setTextColor(getResources().getColor(R.color.color_76FF));
            mBinding.tab1Iv.setVisibility(View.GONE);
            mBinding.tab2Tv.setText("价格");
            mBinding.tab3Tv.setText("销量");
            mBinding.tab3Iv.setVisibility(View.GONE);
            mBinding.searchEt.setHint("搜索权益名称");

            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            mIntegralAdapter = new SearchIntegralAdapter(R.layout.adapter_search_integral, mIntegralList);
            mBinding.recyclerView.setAdapter(mIntegralAdapter);
            mIntegralAdapter.setEmptyView(R.layout.empty_layout, mBinding.recyclerView);
            mIntegralAdapter.setOnItemClickListener((adapter, view, position) -> {
                List<ProductBean> data = adapter.getData();
                if (!TextUtils.isEmpty(data.get(position).getLink())) {
                    WebViewActivity.openRealUrlWebActivity(SearchResultActivity.this,
                            data.get(position).getLink());
                }
            });

            getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
            EventTrackingManager.getInstance().tracking(this,
                    TrackingConstant.SEARCH_LIST,  "type=2");
        }else{

            mBinding.searchEt.setHint("搜索店铺名称");
            mBinding.carTabSelectLayout.setVisibility(View.VISIBLE);
            mBinding.tabLayout.setVisibility(View.GONE);

            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            carServeAdapter = new CarServeListAdapter(R.layout.adapter_car_serve_list, carServeData);
            mBinding.recyclerView.setAdapter(carServeAdapter);
            carServeAdapter.setEmptyView(R.layout.empty_layout, mBinding.recyclerView);
            carServeAdapter.setOnItemClickListener((adapter, view, position) -> {
                List<ProductBean> data = adapter.getData();
                if (!TextUtils.isEmpty(data.get(position).getLink())) {
                    WebViewActivity.openRealUrlWebActivity(SearchResultActivity.this,
                            data.get(position).getLink());
                }
            });
            carServeAdapter.setOnItemClickListener((adapter, view, position) -> {

                LoginHelper.login(SearchResultActivity.this, new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        List<CarServeStoreBean> data = adapter.getData();
//                    Intent intent = new Intent(mContext, OilDetailActivity.class);
                        Intent intent = new Intent(SearchResultActivity.this, CarServeDetailsActivity.class);
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
                                        NavigationDialog navigationDialog = new NavigationDialog(SearchResultActivity.this,
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
            cityCode = UserConstants.getCityCode();
            getAreaList(cityCode);
            getProductCategory();
            getCarType();
            loadCarServeData(false);


        }
    }

    @Override
    protected void initListener() {
        mBinding.tab1.setOnClickListener(this::onViewClicked);
        mBinding.tab2.setOnClickListener(this::onViewClicked);
        mBinding.tab3.setOnClickListener(this::onViewClicked);
        mBinding.searchTv.setOnClickListener(this::onViewClicked);
        mBinding.backIv.setOnClickListener(this::onViewClicked);
        mBinding.refreshView.setOnRefreshLoadMoreListener(this);
        mBinding.noResultLayout.setOnClickListener(this::onViewClicked);
        mBinding.searchEt.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                mBinding.searchTv.callOnClick();
            }
            return true;
        });


        mBinding.carCitySelectLayout.setOnClickListener(this::onViewClicked);
        mBinding.carBusinessStatusLayout.setOnClickListener(this::onViewClicked);
        mBinding.carServeSelectLayout.setOnClickListener(this::onViewClicked);

        mBinding.carOptimizationLayout.setOnClickListener(this::onViewClicked);
        mBinding.carModelSelectLayout.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                mBinding.refreshView.setEnableRefresh(true);
                mBinding.refreshView.setEnableLoadMore(true);
                if (TextUtils.equals("1", mType)) {
                    if (selectDistanceDialog == null) {
                        selectDistanceDialog = new SelectDistanceDialog(
                                this, mBinding.tabLayout, mBinding.getRoot(), false);
                    }
                    selectDistanceDialog.show();
                    selectDistanceDialog.setOnItemClickedListener((adapter, view1, position, distanceEntity) -> {
                        distance = distanceEntity.getDistance();
                        mBinding.tab1Tv.setText(distanceEntity.getTitle());

                        loadData(false);
                    });
                } else {
                    pageNum = 1;
                    integralType = "1";
                    mBinding.tab1Tv.setTextColor(getResources().getColor(R.color.color_76FF));
                    mBinding.tab2Tv.setTextColor(getResources().getColor(R.color.color_34));
                    mBinding.tab3Tv.setTextColor(getResources().getColor(R.color.color_34));
                    getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
                }
                break;
            case R.id.tab2:
                mBinding.refreshView.setEnableRefresh(true);
                mBinding.refreshView.setEnableLoadMore(true);
                if (TextUtils.equals("1", mType)) {
                    getOilNums();
                } else {
                    pageNum = 1;
                    integralType = "2";
                    mBinding.tab2Tv.setTextColor(getResources().getColor(R.color.color_76FF));
                    mBinding.tab1Tv.setTextColor(getResources().getColor(R.color.color_34));
                    mBinding.tab3Tv.setTextColor(getResources().getColor(R.color.color_34));
                    getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
                }
                break;
            case R.id.tab3:
                mBinding.refreshView.setEnableRefresh(true);
                mBinding.refreshView.setEnableLoadMore(true);
                if (TextUtils.equals("1", mType)) {
                    if (firstDistanceOrPrice) {
                        firstDistanceOrPrice = false;
//                        mBinding.tab2Tv.setText("92#");
//                        selectOilNumDialog.setCheckData("92");
//                        mCheckOilGasId = "92";
                        mBinding.tab3Tv.setText("价格优先");
                    } else {
                        firstDistanceOrPrice = true;
                        mBinding.tab3Tv.setText("距离优先");
                    }
                    loadData(false);
                } else {
                    pageNum = 1;
                    integralType = "3";
                    mBinding.tab3Tv.setTextColor(getResources().getColor(R.color.color_76FF));
                    mBinding.tab1Tv.setTextColor(getResources().getColor(R.color.color_34));
                    mBinding.tab2Tv.setTextColor(getResources().getColor(R.color.color_34));
                    getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
                }
            case R.id.search_tv:
                mContent = mBinding.searchEt.getText().toString().trim();
                mBinding.refreshView.setEnableRefresh(true);
                mBinding.refreshView.setEnableLoadMore(true);
                if (TextUtils.isEmpty(mContent)) {
                    showToastInfo("请输入搜索内容");
                    return;
                }
                if (TextUtils.equals("1", mType)) {
                    DBInstance.getInstance().insertData(mContent);
                    pageNum = 1;
                    getOilStations(UserConstants.getLatitude(), UserConstants.getLongitude(), mCheckOilGasId,
                            firstDistanceOrPrice ? "1" : "2", distance == -1 ? null : String.valueOf(distance * 1000),
                            String.valueOf(pageNum), String.valueOf(pageSize), mContent, "sb");
                } else if(TextUtils.equals("2",mType)) {
                    DBInstance.getInstance().insertIntegralData(mContent);
                    pageNum = 1;
                    getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
                }else{
                    pageNum=1;
                    getCarServeStoreList();
                }
                break;
            case R.id.back_iv:
                finish();
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
                    mSelectProductCategoryDialog.setSelectPosition(position);
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
                    mBinding.carBusinessStatusTv.setText(data.getName().equals("全部")?"营业状态":data.getName());
                    mSelectBusinessStatusDialog.setSelectPosition(position);
                    loadCarServeData(false);
                });
                break;
            case R.id.car_optimization_layout:
                if(channel==-1){
                    channel=101;
                    mBinding.carOptimizationTv.setTextColor(Color.parseColor("#323334"));
                }else{
                    channel=-1;
                    mBinding.carOptimizationTv.setTextColor(Color.parseColor("#FE1530"));
                }
                loadCarServeData(false);
                break;
            case R.id.car_model_select_layout:
                if (mSelectCarTypeDialog == null) {
                    return;
                }
                mSelectCarTypeDialog.show();

                break;
        }
    }

    private void getOilNums() {
        mOilViewModel.getOilNums();
    }

    private void getOilStations(String appLatitude, String appLongitude, String oilNo, String orderBy,
                                String distance, String pageNum, String pageSize, String gasName, String method) {
        mOilViewModel.getOilStations1(appLatitude, appLongitude, oilNo, orderBy, distance, pageNum, pageSize, gasName, method,"");
    }

    private void getIntegrals(String name, String type, String pageNum, String pageSize) {
        mViewModel.getIntegrals(name, type, pageNum, pageSize);
    }

    @Override
    protected void dataObservable() {
        mOilViewModel.oilNumLiveData.observe(this, oilNumBeans -> {
            if (selectOilNumDialog == null) {
                selectOilNumDialog = new SelectOilNumDialog(getContext(), mCheckOilGasId, mBinding.tabLayout, mBinding.getRoot());
            }
            selectOilNumDialog.setCheckData(mCheckOilGasId);
            selectOilNumDialog.setData(oilNumBeans);
            selectOilNumDialog.show();
            selectOilNumDialog.setOnItemClickedListener(new SelectOilNumDialog.OnItemClickedListener() {
                @Override
                public void onOilNumClick(BaseQuickAdapter adapter, View view, int position, String oilNum, String checkOilGasId) {
                    mCheckOilGasId = checkOilGasId;
                    mBinding.tab2Tv.setText(oilNum);
                    selectOilNumDialog.setCheckData(mCheckOilGasId);
                    loadData(false);
                }

                @Override
                public void onOilNumAllClick(BaseQuickAdapter adapter, View view, String checkOilGasId) {
                    mCheckOilGasId = checkOilGasId;
                    mBinding.tab2Tv.setText("全部");
                    selectOilNumDialog.setCheckData(mCheckOilGasId);
                    loadData(false);
                }
            });
        });

        mOilViewModel.oilStationLiveData1.observe(this, dataStations -> {
            if (dataStations != null && dataStations.getStations() != null) {
                mBinding.noResultLayout.setVisibility(View.GONE);
                if (pageNum == 1) {
                    mOilListAdapter.setNewData(dataStations.getStations());
                    mBinding.refreshView.setEnableLoadMore(true);
                    mBinding.refreshView.finishRefresh(true);
                } else {
                    if (dataStations != null && dataStations.getStations() != null && dataStations.getStations().size() > 0) {
                        mOilListAdapter.addData(dataStations.getStations());
                        mBinding.refreshView.finishLoadMore(true);
                    } else {
                        mBinding.refreshView.finishLoadMoreWithNoMoreData();
                    }
                }
            } else if (pageNum == 1) {
                mBinding.refreshView.finishLoadMoreWithNoMoreData();
                mBinding.noResultLayout.setVisibility(View.VISIBLE);
            } else {
                mBinding.refreshView.finishLoadMoreWithNoMoreData();
            }

        });

        mViewModel.intergraLiveData.observe(this, productBeans -> {
            if (productBeans != null) {
                mBinding.noResultLayout.setVisibility(View.GONE);
                if (pageNum == 1) {
                    mIntegralAdapter.setNewData(productBeans);
                    mBinding.refreshView.setEnableLoadMore(true);
                    mBinding.refreshView.finishLoadMore(true);
                } else {
                    if (productBeans != null && productBeans.size() > 0) {
                        mIntegralAdapter.addData(productBeans);
                        mBinding.refreshView.finishLoadMore(true);
                    } else {
                        mBinding.refreshView.finishLoadMoreWithNoMoreData();
                    }
                }
            } else if (pageNum == 1) {
                mBinding.refreshView.finishLoadMoreWithNoMoreData();
                mBinding.noResultLayout.setVisibility(View.VISIBLE);
            } else {
                mBinding.refreshView.finishLoadMoreWithNoMoreData();
            }
        });

        //车服

        carServeViewModel.cityListLiveData.observe(this,data ->{

            mSelectAreaDialog = new SelectAreaDialog(getContext(),mBinding.carTabSelectLayout, mBinding.getRoot(),data.getAreasList());

        });
        carServeViewModel.productCategoryLiveData.observe(this,data->{
            mSelectProductCategoryDialog = new SelectProductCategoryDialog(getContext(),mBinding.carTabSelectLayout, mBinding.getRoot(),data.getRecords());

        });
        carServeViewModel.storeListLiveData.observe(this, dataStations -> {
            if (dataStations != null && dataStations.getRecords() != null && dataStations.getRecords().size() > 0) {
                if (pageNum == 1) {
                    carServeAdapter.setNewData(dataStations.getRecords());
                    mBinding.refreshView.setEnableLoadMore(true);
                    mBinding.refreshView.finishRefresh(true);
                } else {
                    carServeAdapter.addData(dataStations.getRecords());
                    mBinding.refreshView.finishLoadMore(true);
                }
                mBinding.noResultLayout.setVisibility(View.GONE);
            } else if (pageNum == 1) {
                mBinding.refreshView.finishLoadMoreWithNoMoreData();
                mBinding.noResultLayout.setVisibility(View.VISIBLE);
            } else {
                pageNum--;
                mBinding.refreshView.finishLoadMoreWithNoMoreData();
            }
        });

        carServeViewModel.carTypeLiveData.observe(this,data->{
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
                mBinding.refreshView.resetNoMoreData();
                loadCarServeData(false);
            });
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadData(true);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        loadData(false);
    }

    private void loadData(boolean isLoadMore) {
        if (TextUtils.equals("1", mType)) {
            if (isLoadMore) {
                pageNum++;
            } else {
                pageNum = 1;
                mBinding.refreshView.finishRefresh();
            }
            getOilStations(UserConstants.getLatitude(), UserConstants.getLongitude(), mCheckOilGasId,
                    firstDistanceOrPrice ? "1" : "2", distance == -1 ? null : String.valueOf(distance * 1000),
                    String.valueOf(pageNum), String.valueOf(pageSize), mContent, "sb");

        } else if(TextUtils.equals("2", mType)) {
            if (isLoadMore) {
                pageNum++;
            } else {
                pageNum = 1;
                mBinding.refreshView.finishRefresh();
            }
            getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
        }else{
            loadCarServeData(isLoadMore);
        }
    }

    private void loadCarServeData(boolean isLoadMore) {
        if (isLoadMore) {
            pageNum++;
            getCarServeStoreList();
        } else {
            pageNum = 1;
            getCarServeStoreList();
        }

    }




    private void getAreaList(String cityCode){
        carServeViewModel.getAreaList(cityCode);
    }
    //获取车服服务分类
    private void getProductCategory(){
        carServeViewModel.getProductCategory();
    }
    private void getCarServeStoreList(){
        carServeViewModel.getCarServeStoreList(pageNum, cityCode, areaCode, productCategoryId, status,channel,carType,mContent);
    }

    //获取车型
    private void getCarType(){
        carServeViewModel.getCarType();
    }
}