package com.xxjy.jyyh.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.adapter.SearchIntegralAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivitySearchResultBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends BindingActivity<ActivitySearchResultBinding, SearchViewModel> {

    private List<String> mOilList = new ArrayList<>();
    private List<String> mIntegralList = new ArrayList<>();

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, false);

        String type = getIntent().getStringExtra("type");

        if (TextUtils.equals("1", type)){
            mBinding.tab1Tv.setText("30km内");
            mBinding.tab2Tv.setText("92#");
            mBinding.tab3Tv.setText("距离优先");

            for (int i = 0; i < 20; i++) {
                mOilList.add("");
            }
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            OilStationListAdapter oilListAdapter = new OilStationListAdapter(R.layout.adapter_oil_station_list, mOilList);
            mBinding.recyclerView.setAdapter(oilListAdapter);
        }else {
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
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {

    }
}