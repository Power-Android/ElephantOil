package com.xxjy.jyyh.ui.oil;

import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.databinding.FragmentOilBinding;
import com.xxjy.jyyh.dialog.SelectOilNumDialog;
import com.xxjy.jyyh.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/21/21 11:52 AM
 * @project ElephantOil
 * @description:
 */
public class OilFragment extends BindingFragment<FragmentOilBinding, OilViewModel> {
    public static OilFragment getInstance() {
        return new OilFragment();
    }

    private OilStationListAdapter adapter;
    private List<String> data = new ArrayList<>();
    private SelectOilNumDialog selectOilNumDialog;

    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.parentLayout);


        for (int i = 0; i < 50; i++) {
            data.add("111111111");
        }

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OilStationListAdapter(R.layout.adapter_oil_station_list, data);
        mBinding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });

    }

    @Override
    protected void initListener() {
        mBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    mBinding.searchLayout.setVisibility(View.VISIBLE);
                    mBinding.popupLayout.setBackgroundColor(Color.parseColor("#1676FF"));
                    mBinding.oilSelectDistanceTv.setTextColor(Color.parseColor("#FFFFFF"));
                    mBinding.oilSortOilNumTv.setTextColor(Color.parseColor("#FFFFFF"));
                    mBinding.oilSelectDistanceFirstTv.setTextColor(Color.parseColor("#FFFFFF"));
                    mBinding.image1.setImageResource(R.drawable.icon_down_arrow_white);
                    mBinding.image2.setImageResource(R.drawable.icon_down_arrow_white);
                    mBinding.image3.setImageResource(R.drawable.icon_down_arrow_white);
                } else {
                    mBinding.searchLayout.setVisibility(View.GONE);
                    mBinding.popupLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));
                    mBinding.oilSelectDistanceTv.setTextColor(Color.parseColor("#323334"));
                    mBinding.oilSortOilNumTv.setTextColor(Color.parseColor("#323334"));
                    mBinding.oilSelectDistanceFirstTv.setTextColor(Color.parseColor("#323334"));
                    mBinding.image1.setImageResource(R.drawable.icon_down_arrow);
                    mBinding.image2.setImageResource(R.drawable.icon_down_arrow);
                    mBinding.image3.setImageResource(R.drawable.icon_down_arrow);
                }


            }
        });

        mBinding.oilSortLayout.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.oil_sort_layout:
                if(selectOilNumDialog==null){
                    selectOilNumDialog = new SelectOilNumDialog(getContext(),view);
            }
                selectOilNumDialog.show();


                break;
        }
    }

    @Override
    protected void dataObservable() {

    }
}
