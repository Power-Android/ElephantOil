package com.xxjy.jyyh.ui.integral;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.HomeExchangeAdapter;
import com.xxjy.jyyh.adapter.IntegralExchangeAdapter;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.databinding.FragmentIntegralBinding;
import com.xxjy.jyyh.dialog.LocationTipsDialog;
import com.xxjy.jyyh.dialog.WithdrawalTipsDialog;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.ProductBean;
import com.xxjy.jyyh.entity.ProductClassBean;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RectangleIndicator;

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

    private IntegralExchangeAdapter adapter;
    private List<ProductBean> productData = new ArrayList<>();
    private WithdrawalTipsDialog withdrawalTipsDialog;
    private List<ProductClassBean> classData = new ArrayList<>();

    private int categoryId;
    private int pageNum = 1;
    private int pageSize = 10;
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            requestPermission();
//        }
//    }


    @Override
    protected void onVisible() {
        super.onVisible();
        getBannerOfPostion();
        queryProductCategorys();
    }

    @Override
    protected void initView() {
        mBinding.topBarLayout.updateBottomDivider(0, 0, 0, 0);
        initTab();

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new IntegralExchangeAdapter(R.layout.adapter_integral_exchange, productData);
        mBinding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter, view, position) -> WebViewActivity.openWebActivity((MainActivity) getActivity(),((ProductBean)(adapter.getData().get(position))).getLink()));
        adapter.setEmptyView(R.layout.empty_layout, mBinding.recyclerView);

        mBinding.refreshview.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                queryProducts();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                getBannerOfPostion();
                queryProductCategorys();
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
        mViewModel.bannersLiveData.observe(this, data -> {
            //banner
            mBinding.banner.setAdapter(new BannerImageAdapter<BannerBean>(data) {
                @Override
                public void onBindView(BannerImageHolder holder, BannerBean data, int position, int size) {
                    Glide.with(holder.imageView)
                            .load(data.getImgUrl())
                            .apply(new RequestOptions()
                                    .error(R.drawable.default_img_bg))
                            .into(holder.imageView);
                    holder.imageView.setOnClickListener(v -> {
                        WebViewActivity.openWebActivity((MainActivity) getActivity(), data.getLink());
                    });
                }
            }).addBannerLifecycleObserver(this)
                    .setIndicator(new RectangleIndicator(mContext));
        });

        mViewModel.productCategorysLiveData.observe(this, data -> {
            classData.clear();
            classData.addAll(data);
            initTab();
        });
        mViewModel.productLiveData.observe(this, data -> {

                if (data != null && data.size() > 0) {
                    if (pageNum == 1) {
                        adapter.setNewData(data);
                        mBinding.refreshview.setEnableLoadMore(true);
                        mBinding.refreshview.finishRefresh(true);
                    } else {
                        adapter.addData(data);
                        mBinding.refreshview.finishLoadMore(true);
                    }
                } else {
                    mBinding.refreshview.finishLoadMoreWithNoMoreData();
                }
        });
    }

    private void initTab() {
        mBinding.tabView.reset();
        mBinding.tabView.notifyDataChanged();
        QMUITabBuilder tabBuilder = mBinding.tabView.tabBuilder().setGravity(Gravity.CENTER);
        tabBuilder.setTextSize(QMUIDisplayHelper.sp2px(getContext(), 15), QMUIDisplayHelper.sp2px(getContext(), 15));
        tabBuilder.setColor(Color.parseColor("#313233"), Color.parseColor("#1676FF"));
        tabBuilder.setTypeface(Typeface.DEFAULT_BOLD, Typeface.DEFAULT_BOLD);
        for (ProductClassBean str : classData) {
            mBinding.tabView.addTab(tabBuilder.setText(str.getClassName()).build(getContext()));
        }
        int space = QMUIDisplayHelper.dp2px(getContext(), 15);
        mBinding.tabView.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(getContext(), 2), false, true));
        mBinding.tabView.setItemSpaceInScrollMode(space);
        mBinding.tabView.setPadding(space, 0, space, 0);
        mBinding.tabView.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mBinding.tabView.setScrollContainer(true);
        mBinding.tabView.selectTab(0);
        mBinding.tabView.notifyDataChanged();
        mBinding.tabView.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTabSelected(int index) {
                pageNum = 1;
                categoryId = classData.get(index).getId();
                queryProducts();

            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {

            }
        });

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

    private void getBannerOfPostion() {
        mViewModel.getBannerOfPostion();
    }

    private void queryProductCategorys() {
        mViewModel.queryProductCategorys();
    }

    private void queryProducts() {
        mBinding.refreshview.setEnableRefresh(true);
        mBinding.refreshview.setEnableLoadMore(false);
        mBinding.refreshview.setNoMoreData(false);
        mViewModel.queryProducts(categoryId, pageNum, pageSize);
    }
}
