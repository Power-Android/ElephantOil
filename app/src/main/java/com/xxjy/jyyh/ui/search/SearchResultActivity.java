package com.xxjy.jyyh.ui.search;

import android.content.Intent;
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
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.adapter.SearchIntegralAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivitySearchResultBinding;
import com.xxjy.jyyh.dialog.SelectDistanceDialog;
import com.xxjy.jyyh.dialog.SelectOilNumDialog;
import com.xxjy.jyyh.dialog.SelectSortDialog;
import com.xxjy.jyyh.entity.OilEntity;
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
    private String mCheckOilGasId = "全部";
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
                        Intent intent = new Intent(SearchResultActivity.this, OilDetailActivity.class);
                        intent.putExtra(Constants.GAS_STATION_ID, data.get(position).getGasId());
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

        } else {
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
        mOilViewModel.getOilStations1(appLatitude, appLongitude, oilNo, orderBy, distance, pageNum, pageSize, gasName, method);
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

        } else {
            if (isLoadMore) {
                pageNum++;
            } else {
                pageNum = 1;
                mBinding.refreshView.finishRefresh();
            }
            getIntegrals(mContent, integralType, String.valueOf(pageNum), String.valueOf(pageSize));
        }
    }
}