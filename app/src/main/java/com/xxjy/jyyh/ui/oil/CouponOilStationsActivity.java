package com.xxjy.jyyh.ui.oil;


import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.LocalLifeOrderListAdapter;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.databinding.ActivityCouponOilStationsBinding;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.ui.search.SearchResultActivity;
import com.xxjy.jyyh.utils.LoginHelper;

import java.util.ArrayList;
import java.util.List;

public class CouponOilStationsActivity extends BindingActivity<ActivityCouponOilStationsBinding,OilViewModel> {

    private int pageSize = 10;
    private int pageNum = 1;
    private String gasIds;
    private List<OilEntity.StationsBean> mOilList = new ArrayList<>();
    private OilStationListAdapter mOilListAdapter;
    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);
        mBinding.titleLayout.tvTitle.setText("适用油站");
        gasIds = getIntent().getStringExtra("gasIds");
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOilListAdapter = new OilStationListAdapter(R.layout.adapter_oil_station_list, mOilList);
        mBinding.recyclerView.setAdapter(mOilListAdapter);
        mOilListAdapter.setEmptyView(R.layout.empty_layout, mBinding.recyclerView);

        getOilStations();
    }

    @Override
    protected void initListener() {
        mBinding.refreshView.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                getOilStations();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum =1;
                getOilStations();
            }
        });
        mOilListAdapter.setOnItemClickListener((adapter, view, position) -> {

            LoginHelper.login(this, new LoginHelper.CallBack() {
                @Override
                public void onLogin() {
                    List<OilEntity.StationsBean> data = adapter.getData();
                    Intent intent = new Intent(CouponOilStationsActivity.this, OilDetailActivity.class);
                    intent.putExtra(Constants.GAS_STATION_ID, data.get(position).getGasId());
                    intent.putExtra(Constants.OIL_NUMBER_ID, data.get(position).getOilNo());
                    startActivity(intent);
                }
            });
        });
    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {
        mViewModel.oilStationLiveData1.observe(this, data -> {
            if (data != null && data.getStations()!=null&&data.getStations().size() > 0) {
                if (pageNum == 1) {
                    mOilListAdapter.setNewData(data.getStations());
                    mBinding.refreshView.setEnableLoadMore(true);
                    mBinding.refreshView.finishRefresh(true);
                } else {
                    mOilListAdapter.addData(data.getStations());
                    mBinding.refreshView.finishLoadMore(true);
                }
            } else {
                if (pageNum == 1) {
                    mOilListAdapter.setNewData(null);
                }
                mBinding.refreshView.finishLoadMoreWithNoMoreData();
            }
        });
    }

    private void getOilStations() {
        mViewModel.getOilStations1("0", "0", "", "",
                "", ""+pageNum, ""+pageSize, "", "",gasIds);
    }
}