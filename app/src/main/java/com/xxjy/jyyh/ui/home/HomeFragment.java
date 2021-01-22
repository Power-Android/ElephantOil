package com.xxjy.jyyh.ui.home;

import android.graphics.Paint;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.HomeExchangeAdapter;
import com.xxjy.jyyh.adapter.HomeOftenAdapter;
import com.xxjy.jyyh.adapter.OilStationFlexAdapter;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.databinding.FragmentHomeBinding;
import com.xxjy.jyyh.dialog.OilNumDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/21/21 11:45 AM
 * @project ElephantOil
 * @description:
 */
public class HomeFragment extends BindingFragment<FragmentHomeBinding, HomeViewModel> {
    private List<String> mOilTagList = new ArrayList<>();
    private List<String> mOftenList = new ArrayList<>();
    private List<String> mExchangeList = new ArrayList<>();
    private OilNumDialog mOilNumDialog;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getBaseActivity().setTransparentStatusBar(mBinding.toolbar);
        }
    }

    @Override
    protected void initView() {
        getBaseActivity().setTransparentStatusBar(mBinding.toolbar);
        //智能优选title
        SpanUtils.with(mBinding.descTv)
                .append("小象加油")
                .setForegroundColor(getResources().getColor(R.color.color_76FF))
                .append("根据您当前的位置智能优选")
                .setForegroundColor(getResources().getColor(R.color.color_6A))
                .create();
        //优选油站
        for (int i = 0; i < 3; i++) {
            mOilTagList.add("新客满20L享10L国标价半价");
        }
        if (mOilTagList.size() > 0){
            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
            flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
            flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
            mBinding.tagRecyclerView.setLayoutManager(flexboxLayoutManager);
            OilStationFlexAdapter flexAdapter =
                    new OilStationFlexAdapter(R.layout.adapter_oil_station_tag, mOilTagList);
            mBinding.tagRecyclerView.setAdapter(flexAdapter);
            mBinding.tagRecyclerView.setVisibility(View.VISIBLE);
        }else {
            mBinding.tagRecyclerView.setVisibility(View.INVISIBLE);
        }
        mBinding.oilOriginalPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mBinding.otherOilTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //常去油站
        mOftenList.add("• 我最近常去：");
        mOftenList.add("光华路加油站、");
        mOftenList.add("成都石油花园加油站、");
        mOftenList.add("光华路加油站");
        if (mOftenList.size() > 0){
            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
            flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
            flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
            mBinding.oftenOilRecyclerView.setLayoutManager(flexboxLayoutManager);
            HomeOftenAdapter oftenAdapter =
                    new HomeOftenAdapter(R.layout.adapter_often_layout, mOftenList);
            mBinding.oftenOilRecyclerView.setAdapter(oftenAdapter);
            mBinding.oftenOilRecyclerView.setVisibility(View.VISIBLE);
        }else {
            mBinding.oftenOilRecyclerView.setVisibility(View.GONE);
        }
        //积分豪礼
        for (int i = 0; i < 6; i++) {
            mExchangeList.add("");
        }
        mBinding.exchangeRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        HomeExchangeAdapter exchangeAdapter = new HomeExchangeAdapter(R.layout.adapter_home_exchange, mExchangeList);
        mBinding.exchangeRecyclerView.setAdapter(exchangeAdapter);

        initOilDialog();
    }

    private void initOilDialog() {
        mOilNumDialog = new OilNumDialog(mContext);
        mOilNumDialog.setOnItemClickedListener((adapter, view, position) -> {

        });
    }

    @Override
    protected void initListener() {
        mBinding.quickOilTv.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.quick_oil_tv:
                if (mOilNumDialog != null){
                    mOilNumDialog.show();
                }
                break;
        }
    }

    @Override
    protected void dataObservable() {

    }
}
