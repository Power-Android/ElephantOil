package com.xxjy.jyyh.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.IntegralHistoryAdapter;
import com.xxjy.jyyh.adapter.MyViewPagerAdapter;
import com.xxjy.jyyh.adapter.SearchHistoryAdapter;
import com.xxjy.jyyh.adapter.SearchRecommendAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivitySearchBinding;
import com.xxjy.jyyh.dialog.QueryDialog;
import com.xxjy.jyyh.entity.IntegralHistoryEntity;
import com.xxjy.jyyh.entity.RecomdEntity;
import com.xxjy.jyyh.entity.SearchHistoryEntity;
import com.xxjy.jyyh.room.DBInstance;
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
    private List<RecomdEntity> mOilRecommendList = new ArrayList<>();
    private List<SearchHistoryEntity> mOilHistoryList = new ArrayList<>();
    private List<RecomdEntity> mInterestRecommendList = new ArrayList<>();
    private List<IntegralHistoryEntity> mInterestHistoryList = new ArrayList<>();
    private SearchHistoryAdapter mOilHistoryAdapter;
    private IntegralHistoryAdapter mInterestHistoryAdapter;
    private QueryDialog mQueryDialog;
    private SearchRecommendAdapter mOilRecommendAdapter;
    private SearchRecommendAdapter mInterestRecommendAdapter;
    private String mType;

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.toolbar, false);
        mType = getIntent().getStringExtra("type");

        initMagicIndicator();
    }

    private void initMagicIndicator() {
        mOilView = View.inflate(this, R.layout.oil_recommend_layout, null);
        mInterestView = View.inflate(this, R.layout.integral_interest_layout, null);
        mList.add(mOilView);
        mList.add(mInterestView);
        initOilView();
        initIntegralView();

        initDao();
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(titles, mList);
        mBinding.viewPager.setOffscreenPageLimit(1);
        mBinding.viewPager.setAdapter(adapter);

        boolean goneIntegral = UserConstants.getGoneIntegral();

        CommonNavigator commonNavigator = new CommonNavigator(this);
        if (goneIntegral) {
            mBinding.indicator.setVisibility(View.GONE);
            commonNavigator.setFollowTouch(false);
        } else {
            mBinding.indicator.setVisibility(View.VISIBLE);
            commonNavigator.setFollowTouch(true);
        }
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
                simplePagerTitleView.setTextSize(16);
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

        if (!TextUtils.isEmpty(mType) && TextUtils.equals(mType, "integral")){
            mBinding.viewPager.setCurrentItem(1);
            mBinding.searchEt.setHint("搜索权益名称");
        }

        mViewModel.getRecomnd("2");//2油站热门推荐 1权益热门推荐
        mViewModel.getRecomnd("1");
    }

    private void initDao() {
        mOilHistoryList = DBInstance.getInstance().getSearchHistory();
        if (mOilHistoryList != null && mOilHistoryList.size() > 0) {
            mOilHistoryAdapter.setNewData(mOilHistoryList);
            mOilHistoryAdapter.notifyDataSetChanged();
            mOilView.findViewById(R.id.oil_history_title).setVisibility(View.VISIBLE);
            mOilView.findViewById(R.id.oil_history_delete_iv).setVisibility(View.VISIBLE);
        } else {
            mOilView.findViewById(R.id.oil_history_title).setVisibility(View.GONE);
            mOilView.findViewById(R.id.oil_history_delete_iv).setVisibility(View.GONE);
        }
        mInterestHistoryList = DBInstance.getInstance().getSearchIntegralHistory();
        if (mInterestHistoryList != null && mInterestHistoryList.size() > 0) {
            mInterestHistoryAdapter.setNewData(mInterestHistoryList);
            mInterestHistoryAdapter.notifyDataSetChanged();
            mInterestView.findViewById(R.id.interest_history_title).setVisibility(View.VISIBLE);
            mInterestView.findViewById(R.id.interest_history_delete_iv).setVisibility(View.VISIBLE);
        } else {
            mInterestView.findViewById(R.id.interest_history_title).setVisibility(View.GONE);
            mInterestView.findViewById(R.id.interest_history_delete_iv).setVisibility(View.GONE);
        }
    }

    /**
     * 初始化搜油站view
     */
    private void initOilView() {
        RecyclerView oilRecommendRecyclerView = mOilView.findViewById(R.id.oil_recommend_recycler_view);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        oilRecommendRecyclerView.setLayoutManager(flexboxLayoutManager);
        mOilRecommendAdapter = new SearchRecommendAdapter(R.layout.adapter_search_recommend, mOilRecommendList);
        oilRecommendRecyclerView.setAdapter(mOilRecommendAdapter);

        RecyclerView oilHistoryRecyclerView = mOilView.findViewById(R.id.oil_history_recycler_view);
        FlexboxLayoutManager flexboxLayoutManager1 = new FlexboxLayoutManager(this);
        flexboxLayoutManager1.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager1.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager1.setAlignItems(AlignItems.FLEX_START);
        oilHistoryRecyclerView.setLayoutManager(flexboxLayoutManager1);
        mOilHistoryAdapter = new SearchHistoryAdapter(R.layout.adapter_search_tag, mOilHistoryList);
        oilHistoryRecyclerView.setAdapter(mOilHistoryAdapter);
        mOilHistoryAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
            intent.putExtra("type", mBinding.viewPager.getCurrentItem() == 0 ? "1" : "2");
            intent.putExtra("content", ((SearchHistoryEntity) adapter.getItem(position)).getGasName());
            startActivity(intent);
        });

        mOilView.findViewById(R.id.oil_history_delete_iv).setOnClickListener(this::onViewClicked);
    }

    /**
     * 初始化搜权益view
     */
    private void initIntegralView() {
        RecyclerView interestRecommendRecyclerView = mInterestView.findViewById(R.id.interest_recycler_view);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        interestRecommendRecyclerView.setLayoutManager(flexboxLayoutManager);
        mInterestRecommendAdapter = new SearchRecommendAdapter(R.layout.adapter_search_recommend, mInterestRecommendList);
        interestRecommendRecyclerView.setAdapter(mInterestRecommendAdapter);


        RecyclerView interestHistoryRecyclerView = mInterestView.findViewById(R.id.interest_history_recycler_view);
        FlexboxLayoutManager flexboxLayoutManager1 = new FlexboxLayoutManager(this);
        flexboxLayoutManager1.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager1.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager1.setAlignItems(AlignItems.FLEX_START);
        interestHistoryRecyclerView.setLayoutManager(flexboxLayoutManager1);
        mInterestHistoryAdapter = new IntegralHistoryAdapter(R.layout.adapter_search_tag, mInterestHistoryList);
        interestHistoryRecyclerView.setAdapter(mInterestHistoryAdapter);
        mInterestHistoryAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
            intent.putExtra("type", mBinding.viewPager.getCurrentItem() == 0 ? "1" : "2");
            intent.putExtra("content", ((IntegralHistoryEntity) adapter.getItem(position)).getIntegralName());
            startActivity(intent);
        });

        mInterestView.findViewById(R.id.interest_history_delete_iv).setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void initListener() {
        mBinding.backIv.setOnClickListener(this::onViewClicked);
        mBinding.searchTv.setOnClickListener(this::onViewClicked);
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mBinding.searchEt.setHint("搜索油站名称");
                } else {
                    mBinding.searchEt.setHint("搜索权益名称");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.searchEt.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH){
                mBinding.searchTv.callOnClick();
            }
            return false;
        });
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.search_tv:
                if (TextUtils.isEmpty(mBinding.searchEt.getText().toString().trim())) {
                    showToastInfo("请输入搜索内容");
                    return;
                }
                KeyboardUtils.hideSoftInput(this);
                if (mBinding.viewPager.getCurrentItem() == 0) {
                    DBInstance.getInstance().insertData(mBinding.searchEt.getText().toString());
                } else {
                    DBInstance.getInstance().insertIntegralData(mBinding.searchEt.getText().toString());
                }
                initDao();
                Intent intent = new Intent(this, SearchResultActivity.class);
                intent.putExtra("type", mBinding.viewPager.getCurrentItem() == 0 ? "1" : "2");
                intent.putExtra("content", mBinding.searchEt.getText().toString());
                startActivity(intent);
                break;
            case R.id.oil_history_delete_iv://油站的历史记录删除
                mQueryDialog = new QueryDialog(this);
                mQueryDialog.show();
                mQueryDialog.setOnConfirmListener(() -> {
                    DBInstance.getInstance().deleteAllData();
                    mOilHistoryList.clear();
                    mOilView.findViewById(R.id.oil_history_title).setVisibility(View.GONE);
                    mOilView.findViewById(R.id.oil_history_delete_iv).setVisibility(View.GONE);
                    mOilHistoryAdapter.notifyDataSetChanged();
                });
                break;
            case R.id.interest_history_delete_iv://权益的历史记录删除
                mQueryDialog = new QueryDialog(this);
                mQueryDialog.show();
                mQueryDialog.setOnConfirmListener(() -> {
                    DBInstance.getInstance().deleteAllIntegralData();
                    mInterestHistoryList.clear();
                    mInterestView.findViewById(R.id.interest_history_title).setVisibility(View.GONE);
                    mInterestView.findViewById(R.id.interest_history_delete_iv).setVisibility(View.GONE);
                    mInterestHistoryAdapter.notifyDataSetChanged();
                });
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.recomdLiveData.observe(this, recomdEntities -> {
            if (recomdEntities != null && recomdEntities.size() > 0) {
                mOilRecommendAdapter.setNewData(recomdEntities);
            } else {
                mOilView.findViewById(R.id.recommend_title).setVisibility(View.GONE);
            }
        });

        mViewModel.recomdLiveData1.observe(this, recomdEntities -> {
            if (recomdEntities != null && recomdEntities.size() > 0) {
                mInterestRecommendAdapter.setNewData(recomdEntities);
            }else {
                mInterestView.findViewById(R.id.interest_title).setVisibility(View.GONE);
            }
        });
    }
}