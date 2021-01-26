package com.xxjy.jyyh.ui.integral;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.view.Gravity;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.HomeExchangeAdapter;
import com.xxjy.jyyh.adapter.IntegralExchangeAdapter;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.databinding.FragmentIntegralBinding;
import com.xxjy.jyyh.dialog.LocationTipsDialog;
import com.xxjy.jyyh.dialog.WithdrawalTipsDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * @author power
 * @date 1/21/21 11:51 AM
 * @project ElephantOil
 * @description:
 */
public class IntegralFragment extends BindingFragment<FragmentIntegralBinding, IntegralViewModel> {
    public static IntegralFragment getInstance() {
        return new IntegralFragment();
    }

    private String[] tabData = new String[]{"推荐兑换", "试听权益", "出行权益", "图书", "美妆", "出行权益", "出行权益"};
    private IntegralExchangeAdapter adapter;
    private List<String> data = new ArrayList<>();
    private WithdrawalTipsDialog withdrawalTipsDialog;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requestPermission();
        }
    }

    @Override
    protected void initView() {
        mBinding.topBarLayout.updateBottomDivider(0, 0, 0, 0);
        initTab();
        for (int i = 0; i < 50; i++) {
            data.add("111111111");
        }

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new IntegralExchangeAdapter(R.layout.adapter_integral_exchange, data);
        mBinding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }

    @Override
    protected void initListener() {
        mBinding.integralView.setOnClickListener(this::onViewClicked);

    }

    @Override
    protected void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.integral_view:
                if (withdrawalTipsDialog == null) {
                    withdrawalTipsDialog = new WithdrawalTipsDialog(getContext(), mBinding.getRoot());
                }
                withdrawalTipsDialog.show();
                break;
        }
    }

    @Override
    protected void dataObservable() {

    }

    private void initTab() {
        QMUITabBuilder tabBuilder = mBinding.tabView.tabBuilder().setGravity(Gravity.CENTER);
        tabBuilder.setTextSize(QMUIDisplayHelper.sp2px(getContext(), 15), QMUIDisplayHelper.sp2px(getContext(), 15));
        tabBuilder.setColor(Color.parseColor("#313233"), Color.parseColor("#1676FF"));
        tabBuilder.setTypeface(Typeface.DEFAULT_BOLD, Typeface.DEFAULT_BOLD);
        for (String str : tabData) {
            mBinding.tabView.addTab(tabBuilder.setText(str).build(getContext()));
        }
        int space = QMUIDisplayHelper.dp2px(getContext(), 15);
        mBinding.tabView.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(getContext(), 2), false, true));
        mBinding.tabView.setItemSpaceInScrollMode(space);
        mBinding.tabView.setPadding(space, 0, space, 0);
        mBinding.tabView.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mBinding.tabView.setScrollContainer(true);
        mBinding.tabView.selectTab(0);
        mBinding.tabView.notifyDataChanged();
    }

    private void requestPermission() {
        PermissionUtils.permission(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                    }

                    @Override
                    public void onDenied() {
                        LocationTipsDialog locationTipsDialog = new LocationTipsDialog(getContext(), mBinding.getRoot());
                        locationTipsDialog.show();
                    }
                })
                .request();
    }
}
