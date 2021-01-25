package com.xxjy.jyyh.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
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
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.FragmentHomeBinding;
import com.xxjy.jyyh.dialog.GasStationLocationTipsDialog;
import com.xxjy.jyyh.dialog.OilAmountDialog;
import com.xxjy.jyyh.dialog.OilCouponDialog;
import com.xxjy.jyyh.dialog.OilGunDialog;
import com.xxjy.jyyh.dialog.OilNumDialog;
import com.xxjy.jyyh.dialog.OilPayDialog;
import com.xxjy.jyyh.dialog.OilTipsDialog;
import com.xxjy.jyyh.ui.oil.OilDetailActivity;
import com.xxjy.jyyh.ui.search.SearchActivity;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.symanager.ShanYanManager;

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
    private OilGunDialog mOilGunDialog;
    private OilAmountDialog mOilAmountDialog;
    private OilTipsDialog mOilTipsDialog;
    private OilPayDialog mOilPayDialog;
    private OilCouponDialog mOilCouponDialog;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getBaseActivity().setTransparentStatusBar();
            mBinding.toolbar.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
        }
    }

    @Override
    protected void initView() {
        getBaseActivity().setTransparentStatusBar();
        mBinding.toolbar.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0);
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
        if (mOilTagList.size() > 0) {
            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
            flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
            flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
            mBinding.tagRecyclerView.setLayoutManager(flexboxLayoutManager);
            OilStationFlexAdapter flexAdapter =
                    new OilStationFlexAdapter(R.layout.adapter_oil_station_tag, mOilTagList);
            mBinding.tagRecyclerView.setAdapter(flexAdapter);
            mBinding.tagRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mBinding.tagRecyclerView.setVisibility(View.INVISIBLE);
        }
        mBinding.oilOriginalPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        mBinding.otherOilTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //常去油站
        mOftenList.add("• 我最近常去：");
        mOftenList.add("光华路加油站、");
        mOftenList.add("成都石油花园加油站、");
        mOftenList.add("光华路加油站");
        if (mOftenList.size() > 0) {
            FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(mContext);
            flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
            flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
            flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
            mBinding.oftenOilRecyclerView.setLayoutManager(flexboxLayoutManager);
            HomeOftenAdapter oftenAdapter =
                    new HomeOftenAdapter(R.layout.adapter_often_layout, mOftenList);
            mBinding.oftenOilRecyclerView.setAdapter(oftenAdapter);
            mBinding.oftenOilRecyclerView.setVisibility(View.VISIBLE);
        } else {
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
            if (mOilGunDialog != null){
                mOilGunDialog.show();
            }
        });

        mOilGunDialog = new OilGunDialog(mContext);
        mOilGunDialog.setOnItemClickedListener((adapter, view, position) -> {
            if (mOilAmountDialog != null){
                mOilAmountDialog.show();
            }
        });

        mOilAmountDialog = new OilAmountDialog(mContext);
        mOilAmountDialog.setOnItemClickedListener(new OilAmountDialog.OnItemClickedListener() {
            @Override
            public void onOilAmountClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onOilDiscountClick(BaseQuickAdapter adapter, View view, int position) {
                if (mOilCouponDialog != null){
                    mOilCouponDialog.show();
                }
            }

            @Override
            public void onCreateOrder(View view) {
                if (mOilTipsDialog != null){
                    mOilTipsDialog.show(view);
                }
            }
        });

        mOilCouponDialog = new OilCouponDialog(mContext);
        mOilCouponDialog.setOnItemClickedListener(new OilCouponDialog.OnItemClickedListener() {
            @Override
            public void onOilCouponClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onNoCouponClick() {

            }
        });

        mOilTipsDialog = new OilTipsDialog(mContext);
        mOilTipsDialog.setOnItemClickedListener(view -> {
            mOilTipsDialog.dismiss();
            if (mOilPayDialog != null){
                mOilPayDialog.show();
            }
        });

        mOilPayDialog = new OilPayDialog(mContext);
        mOilPayDialog.setOnItemClickedListener(new OilPayDialog.OnItemClickedListener() {
            @Override
            public void onOilPayTypeClick(BaseQuickAdapter adapter, View view, int position) {
                GasStationLocationTipsDialog gasStationLocationTipsDialog = new GasStationLocationTipsDialog(getContext(),mBinding.getRoot(),"成都加油站");
                gasStationLocationTipsDialog.show();
            }

            @Override
            public void onCloseAllClick() {
                mOilAmountDialog.dismiss();
                mOilGunDialog.dismiss();
                mOilNumDialog.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initListener() {
        mBinding.scrollView.setOnScrollChangeListener(
                (View.OnScrollChangeListener) (view, x, y, oldX, oldY) -> {
                    if (y > BarUtils.getStatusBarHeight()) {
                        mBinding.toolbar.setBackgroundColor(Color.parseColor("#1676FF"));
                        mBinding.locationIv.setVisibility(View.INVISIBLE);
                        mBinding.addressTv.setVisibility(View.INVISIBLE);
                        mBinding.titleTv.setVisibility(View.VISIBLE);
                        mBinding.searchIv.setImageResource(R.drawable.icon_search_white);
                    } else {
                        mBinding.toolbar.setBackgroundColor(Color.parseColor("#F5F5F5"));
                        mBinding.locationIv.setVisibility(View.VISIBLE);
                        mBinding.addressTv.setVisibility(View.VISIBLE);
                        mBinding.titleTv.setVisibility(View.INVISIBLE);
                        mBinding.searchIv.setImageResource(R.drawable.icon_search);
                    }
                });
        mBinding.quickOilTv.setOnClickListener(this::onViewClicked);
        mBinding.homeQuickOilRl.setOnClickListener(this::onViewClicked);
        mBinding.searchIv.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.quick_oil_tv:
                if (mOilNumDialog != null) {
                    mOilNumDialog.show();
                }
                break;
            case R.id.home_quick_oil_rl:
                LoginHelper.login(mContext, new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        startActivity(new Intent(mContext, OilDetailActivity.class));
                    }
                });
                break;
            case R.id.search_iv:
                startActivity(new Intent(mContext, SearchActivity.class));
                break;
        }
    }

    @Override
    protected void dataObservable() {

    }
}
