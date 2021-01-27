package com.xxjy.jyyh.ui.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.adapter.SearchIntegralAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivitySearchResultBinding;
import com.xxjy.jyyh.dialog.SelectDistanceDialog;
import com.xxjy.jyyh.dialog.SelectOilNumDialog;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilNumBean;
import com.xxjy.jyyh.ui.oil.OilViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.xxjy.jyyh.app.App.getContext;

public class SearchResultActivity extends BindingActivity<ActivitySearchResultBinding, SearchViewModel> implements OnRefreshLoadMoreListener {

    private List<OilEntity.StationsBean> mOilList = new ArrayList<>();
    private List<String> mIntegralList = new ArrayList<>();

    private String mType; // 1是油站搜索 2是权益搜索
    private String mContent; // 1是油站搜索 2是权益搜索

    private SelectDistanceDialog selectDistanceDialog;
    private SelectOilNumDialog selectOilNumDialog;
    private int distance = 50;
    private OilViewModel mOilViewModel;
    private int pageNum = 1;
    private int pageSize = 10;
    private boolean firstDistanceOrPrice = true;
    private String mCheckOilGasId = "92";
    private OilStationListAdapter mOilListAdapter;


    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, false);

        mType = getIntent().getStringExtra("type");
        mContent = getIntent().getStringExtra("content");

        mOilViewModel = ViewModelProviders.of(this).get(OilViewModel.class);

        if (TextUtils.equals("1", mType)) {
            mBinding.tab1Tv.setText("50km内");
            mBinding.tab2Tv.setText("92#");
            mBinding.tab3Tv.setText("距离优先");

            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            mOilListAdapter = new OilStationListAdapter(R.layout.adapter_oil_station_list, mOilList);
            mBinding.recyclerView.setAdapter(mOilListAdapter);
        } else {
            mBinding.tab1Tv.setText("综合");
            mBinding.tab2Tv.setText("价格");
            mBinding.tab3Tv.setText("销量");

            for (int i = 0; i < 20; i++) {
                mIntegralList.add("");
            }
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            SearchIntegralAdapter integralAdapter = new SearchIntegralAdapter(R.layout.adapter_search_integral, mIntegralList);
            mBinding.recyclerView.setAdapter(integralAdapter);
        }

        getOilStations();
    }

    @Override
    protected void initListener() {
        mBinding.tab1.setOnClickListener(this::onViewClicked);
        mBinding.tab2.setOnClickListener(this::onViewClicked);
        mBinding.tab3.setOnClickListener(this::onViewClicked);
        mBinding.refreshView.setOnRefreshLoadMoreListener(this);
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
                    selectDistanceDialog.show();
                    selectDistanceDialog.setOnItemClickedListener((adapter, view1, position, distanceEntity) -> {
                        distance = distanceEntity.getDistance();
                        mBinding.tab1Tv.setText(distanceEntity.getTitle());

                        loadData(false);
                    });
                }else {

                }
                break;
            case R.id.tab2:
                if (TextUtils.equals("1", mType)){
                    getOilNums();
                }else {

                }
                break;
            case R.id.tab3:
                if (TextUtils.equals("1", mType)){
                    if (firstDistanceOrPrice) {
                        firstDistanceOrPrice = false;
                        mBinding.tab3Tv.setText("价格优先");
                    } else {
                        firstDistanceOrPrice = true;
                        mBinding.tab3Tv.setText("距离优先");
                    }
                    loadData(false);
                }else {

                }

                break;
        }
    }

    private void loadData(boolean isLoadMore) {
        if (TextUtils.equals("1", mType)){
            if (isLoadMore) {
                pageNum++;
                getOilStations();

            } else {
                pageNum = 1;
                getOilStations();

            }
        }else {

        }

    }

    private void getOilNums() {
        mOilViewModel.getOilNums();
    }

    private void getOilStations() {
        mOilViewModel.getOilStations(UserConstants.getLatitude(), UserConstants.getLongitude(),
                mCheckOilGasId, firstDistanceOrPrice ? "1" : "2",
                distance == -1 ? null : String.valueOf(distance * 1000),
                String.valueOf(pageNum), String.valueOf(pageSize), mContent, "sb");
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
}