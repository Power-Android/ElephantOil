package com.xxjy.jyyh.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.MyViewPagerAdapter;
import com.xxjy.jyyh.adapter.SearchHistoryAdapter;
import com.xxjy.jyyh.adapter.SearchRecommendAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivitySearchBinding;
import com.xxjy.jyyh.wight.SettingLayout;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BindingActivity<ActivitySearchBinding, SearchViewModel> {
    private final String[] titles = new String[]{"搜油站", "搜权益"};
    private final List<View> mList = new ArrayList<>(2);
    private View mOilView;
    private View mInterestView;
    private List<String> mOilRecommendList = new ArrayList<>();
    private List<String> mOilHistoryList = new ArrayList<>();
    private List<String> mInterestRecommendList = new ArrayList<>();
    private List<String> mInterestHistoryList = new ArrayList<>();

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.toolbar, false);
        initMagicIndicator();
    }

    private void initMagicIndicator() {
        mOilView = View.inflate(this, R.layout.oil_recommend_layout, null);
        mInterestView = View.inflate(this, R.layout.integral_interest_layout, null);
        mList.add(mOilView);
        mList.add(mInterestView);
        initRecommendView();
        initInterestView();
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(titles, mList);
        mBinding.viewPager.setOffscreenPageLimit(1);
        mBinding.viewPager.setAdapter(adapter);

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        int padding = 70;
        commonNavigator.setLeftPadding(padding);
        commonNavigator.setRightPadding(padding);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SettingLayout simplePagerTitleView = new SettingLayout(context);
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setTextSize(20);
                simplePagerTitleView.setmNormalColor(getResources().getColor(R.color.color_BAFF));
                simplePagerTitleView.setmSelectedColor(getResources().getColor(R.color.white));
                simplePagerTitleView.setOnClickListener(v -> mBinding.viewPager.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setLineWidth(UIUtil.dip2px(context, 42));
                indicator.setRoundRadius(UIUtil.dip2px(context, 10));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(getResources().getColor(R.color.color_CBFF));
                return indicator;
            }
        });
        mBinding.indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mBinding.indicator, mBinding.viewPager);
    }

    /**
     * 初始化搜油站view
     */
    private void initRecommendView() {
        for (int i = 0; i < 10; i++) {
            mOilRecommendList.add("");
        }
        RecyclerView oilRecommendRecyclerView = mOilView.findViewById(R.id.oil_recommend_recycler_view);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        oilRecommendRecyclerView.setLayoutManager(flexboxLayoutManager);
        SearchRecommendAdapter oilRecommendAdapter =
                new SearchRecommendAdapter(R.layout.adapter_search_tag, mOilRecommendList);
        oilRecommendRecyclerView.setAdapter(oilRecommendAdapter);

        for (int i = 0; i < 10; i++) {
            mOilHistoryList.add("");
        }
        RecyclerView oilHistoryRecyclerView = mOilView.findViewById(R.id.oil_history_recycler_view);
        FlexboxLayoutManager flexboxLayoutManager1 = new FlexboxLayoutManager(this);
        flexboxLayoutManager1.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager1.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager1.setAlignItems(AlignItems.FLEX_START);
        oilHistoryRecyclerView.setLayoutManager(flexboxLayoutManager1);
        SearchHistoryAdapter oilHistoryAdapter =
                new SearchHistoryAdapter(R.layout.adapter_search_tag, mOilHistoryList);
        oilHistoryRecyclerView.setAdapter(oilHistoryAdapter);

        mOilView.findViewById(R.id.oil_history_delete_iv).setOnClickListener(this::onViewClicked);
    }

    /**
     * 初始化搜权益view
     */
    private void initInterestView() {
        for (int i = 0; i < 10; i++) {
            mInterestRecommendList.add("");
        }
        RecyclerView interestRecommendRecyclerView = mInterestView.findViewById(R.id.interest_recycler_view);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        interestRecommendRecyclerView.setLayoutManager(flexboxLayoutManager);
        SearchRecommendAdapter interestRecommendAdapter =
                new SearchRecommendAdapter(R.layout.adapter_search_tag, mInterestRecommendList);
        interestRecommendRecyclerView.setAdapter(interestRecommendAdapter);

        for (int i = 0; i < 10; i++) {
            mInterestHistoryList.add("");
        }
        RecyclerView interestHistoryRecyclerView = mInterestView.findViewById(R.id.interest_history_recycler_view);
        FlexboxLayoutManager flexboxLayoutManager1 = new FlexboxLayoutManager(this);
        flexboxLayoutManager1.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager1.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager1.setAlignItems(AlignItems.FLEX_START);
        interestHistoryRecyclerView.setLayoutManager(flexboxLayoutManager1);
        SearchHistoryAdapter interestHistoryAdapter =
                new SearchHistoryAdapter(R.layout.adapter_search_tag, mInterestHistoryList);
        interestHistoryRecyclerView.setAdapter(interestHistoryAdapter);

        mInterestView.findViewById(R.id.interest_history_delete_iv).setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void initListener() {
        mBinding.searchTv.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.search_tv:
                Intent intent = new Intent(this, SearchResultActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.oil_history_delete_iv://油站的历史记录删除
                break;
            case R.id.interest_history_delete_iv://权益的历史记录删除
                break;
        }
    }

    @Override
    protected void dataObservable() {

    }
}