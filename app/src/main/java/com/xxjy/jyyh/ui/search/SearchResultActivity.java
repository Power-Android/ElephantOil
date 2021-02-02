package com.xxjy.jyyh.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.adapter.SearchIntegralAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivitySearchResultBinding;
import com.xxjy.jyyh.dialog.SelectDistanceDialog;
import com.xxjy.jyyh.dialog.SelectOilNumDialog;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilNumBean;
import com.xxjy.jyyh.entity.ProductBean;
import com.xxjy.jyyh.room.DBInstance;
import com.xxjy.jyyh.ui.oil.OilDetailActivity;
import com.xxjy.jyyh.ui.oil.OilViewModel;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.LoginHelper;

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
    private String mCheckOilGasId = "";
    private OilStationListAdapter mOilListAdapter;
    private String integralType = "1";//权益type 1 是 综合  2 是价格 3 是销量
    private SearchIntegralAdapter mIntegralAdapter;

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, false);

        mType = getIntent().getStringExtra("type");
        mContent = getIntent().getStringExtra("content");

        mBinding.searchEt.setText(mContent);

        mOilViewModel = new ViewModelProvider(this).get(OilViewModel.class);

        if (TextUtils.equals("1", mType)) {
            mBinding.tab1Tv.setText("距离不限");
            mBinding.tab2Tv.setText("油号不限");
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
                        Intent intent = new Intent(SearchResultActivity.this, OilDetailActivity.class);
                        intent.putExtra(Constants.GAS_STATION_ID, data.get(position).getGasId());
                        startActivity(intent);
                    }
                });
            });

            getOilStations(UserConstants.getLatitude(), UserConstants.getLongitude(), mCheckOilGasId,
                    firstDistanceOrPrice ? "1" : "2", distance == -1 ? null : String.valueOf(distance * 1000),
                    String.valueOf(pageNum), String.valueOf(pageSize), mContent, "sb");
        } else {
            mBinding.tab1Tv.setText("综合");
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
                    LoginHelper.login(this, new LoginHelper.CallBack() {
                        @Override
                        public void onLogin() {
                            WebViewActivity.openRealUrlWebActivity(SearchResultActivity.this,
                                    data.get(position).getLink());
                        }
                    });

                }
            });

            getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
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
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                if (TextUtils.equals("1", mType)) {
                    if (selectDistanceDialog == null) {
                        selectDistanceDialog = new SelectDistanceDialog(
                                this, mBinding.tabLayout, mBinding.getRoot());
                    }
                    selectDistanceDialog.setSelectPosition(5);
                    selectDistanceDialog.show();
                    selectDistanceDialog.setOnItemClickedListener((adapter, view1, position, distanceEntity) -> {
                        distance = distanceEntity.getDistance();
                        mBinding.tab1Tv.setText(distanceEntity.getTitle());

                        loadData(false);
                    });
                } else {
                    pageNum = 1;
                    getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
                }
                break;
            case R.id.tab2:
                if (TextUtils.equals("1", mType)) {
                    getOilNums();
                } else {
                    pageNum = 1;
                    integralType = "2";
                    getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
                }
                break;
            case R.id.tab3:
                if (TextUtils.equals("1", mType)) {
                    if (firstDistanceOrPrice) {
                        firstDistanceOrPrice = false;
                        mBinding.tab3Tv.setText("价格优先");
                    } else {
                        firstDistanceOrPrice = true;
                        mBinding.tab3Tv.setText("距离优先");
                    }
                    loadData(false);
                } else {
                    pageNum = 1;
                    integralType = "3";
                    getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
                }
            case R.id.search_tv:
                mContent = mBinding.searchEt.getText().toString().trim();
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
                } else {
                    DBInstance.getInstance().insertIntegralData(mContent);
                    pageNum = 1;
                    getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
                }
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }

    private void getOilNums() {
        mOilViewModel.getOilNums();
    }

    private void getOilStations(String appLatitude, String appLongitude, String oilNo, String orderBy,
                                String distance, String pageNum, String pageSize, String gasName, String method) {
        mOilViewModel.getOilStations(appLatitude, appLongitude, oilNo, orderBy, distance, pageNum, pageSize, gasName, method);
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
            selectOilNumDialog.setData(oilNumBeans);
            selectOilNumDialog.show();
            selectOilNumDialog.setOnItemClickedListener((adapter, view, position, oilNum, checkOilGasId) -> {
                mCheckOilGasId = checkOilGasId;
                mBinding.tab2Tv.setText(oilNum);
                selectOilNumDialog.setCheckData(mCheckOilGasId);
                loadData(false);
            });
        });

        mOilViewModel.oilStationLiveData.observe(this, dataStations -> {
            if (dataStations != null && dataStations.getStations() != null && dataStations.getStations().size() > 0) {
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
            } else {
                mBinding.noResultLayout.setVisibility(View.VISIBLE);
            }

        });

        mViewModel.intergraLiveData.observe(this, productBeans -> {
            if (productBeans != null && productBeans.size() > 0) {
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
            } else {
                mBinding.noResultLayout.setVisibility(View.VISIBLE);
            }
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
            }
            getOilStations(UserConstants.getLatitude(), UserConstants.getLongitude(), mCheckOilGasId,
                    firstDistanceOrPrice ? "1" : "2", distance == -1 ? null : String.valueOf(distance * 1000),
                    String.valueOf(pageNum), String.valueOf(pageSize), mContent, "sb");

        } else {
            if (isLoadMore) {
                pageNum++;
            } else {
                pageNum = 1;
            }
            getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
        }
    }
}