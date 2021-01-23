package com.xxjy.jyyh.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivitySearchResultBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends BindingActivity<ActivitySearchResultBinding, SearchViewModel> {

    private List<String> mOilList = new ArrayList<>();

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, false);

        for (int i = 0; i < 20; i++) {
            mOilList.add("");
        }
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OilStationListAdapter oilListAdapter = new OilStationListAdapter(R.layout.adapter_oil_station_list, mOilList);
        mBinding.recyclerView.setAdapter(oilListAdapter);
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