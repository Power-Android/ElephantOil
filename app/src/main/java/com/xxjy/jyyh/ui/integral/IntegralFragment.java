package com.xxjy.jyyh.ui.integral;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.NumberUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.IntegralExchangeAdapter;
import com.xxjy.jyyh.adapter.SignInAdapter;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.constants.BannerPositionConstants;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.FragmentIntegralBinding;
import com.xxjy.jyyh.dialog.CustomerServiceDialog;
import com.xxjy.jyyh.dialog.LocationTipsDialog;
import com.xxjy.jyyh.dialog.OilMonthRuleDialog;
import com.xxjy.jyyh.dialog.SignInRuleDialog;
import com.xxjy.jyyh.dialog.SignInSuccessDialog;
import com.xxjy.jyyh.dialog.WithdrawalTipsDialog;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.ProductBean;
import com.xxjy.jyyh.entity.ProductClassBean;
import com.xxjy.jyyh.entity.SignInBean;
import com.xxjy.jyyh.entity.SignInDayBean;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.msg.MessageCenterActivity;
import com.xxjy.jyyh.ui.search.SearchActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.GlideUtils;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RectangleIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author power
 * @date 1/21/21 11:51 AM
 * @project ElephantOil
 * @description:
 */
public class IntegralFragment extends BindingFragment<FragmentIntegralBinding, IntegralViewModel> {

    private SignInAdapter mSignInAdapter;
    private SignInSuccessDialog mSignInSuccessDialog;

    public static IntegralFragment getInstance() {
        return new IntegralFragment();
    }

    private IntegralExchangeAdapter adapter;
    private List<ProductBean> productData = new ArrayList<>();
    private List<ProductClassBean> classData = new ArrayList<>();
    private QMUITabBuilder tabBuilder;

    private int categoryId;
    private int pageNum = 1;
    private int pageSize = 10;

    private BannerViewModel bannerViewModel1;
    private BannerViewModel bannerViewModel2;

    private CustomerServiceDialog customerServiceDialog;
    private List<SignInDayBean> mSignInList = new ArrayList<>();
    private SignInRuleDialog mSignInRuleDialog;
    private String signInRuleStr;
    private SignInDayBean todaySignInDayBean;
    private SignInBean signInBean;


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getIntegralInfo();
            if (UserConstants.getIsLogin()) {
                queryIntegralBalance();

            } else {
                mBinding.integralView.setText("0");
            }
        }
    }

    @Override
    public void initView() {
        mBinding.topBarLayout.updateBottomDivider(0, 0, 0, 0);
        initTab();
        bannerViewModel1 = new ViewModelProvider(getActivity()).get(BannerViewModel.class);
        bannerViewModel2 = new ViewModelProvider(getActivity()).get(BannerViewModel.class);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new IntegralExchangeAdapter(R.layout.adapter_integral_exchange, productData);
        mBinding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter, view, position) -> {
//            LoginHelper.login(getContext(), new LoginHelper.CallBack() {
//                @Override
//                public void onLogin() {
            WebViewActivity.openWebActivity((MainActivity) getActivity(), ((ProductBean) (adapter.getData().get(position))).getLink());
//                }
//            });


        });
        adapter.setEmptyView(R.layout.empty_layout, mBinding.recyclerView);

        //签到
        mBinding.signInRecycler.setLayoutManager(new GridLayoutManager(mContext, 7));
        mSignInAdapter = new SignInAdapter(R.layout.adapter_sign_in_layout, mSignInList);
        mBinding.signInRecycler.setAdapter(mSignInAdapter);

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
                getIntegralInfo();
                if (UserConstants.getIsLogin()) {
                    queryIntegralBalance();

                }

            }
        });
        getBannerOfPostion();
        queryProductCategorys();
        getIntegralInfo();
        if (UserConstants.getIsLogin()) {
            queryIntegralBalance();
        } else {
            mBinding.integralView.setText("0");
        }
    }

    @Override
    protected void initListener() {
        mBinding.integralView.setOnClickListener(this::onViewClicked);
        mBinding.customerServiceView.setOnClickListener(this::onViewClicked);
        mBinding.messageCenterView.setOnClickListener(this::onViewClicked);
        mBinding.explanationView.setOnClickListener(this::onViewClicked);
        mBinding.searchBar.setOnClickListener(this::onViewClicked);
        mBinding.signInTv.setOnClickListener(this::onViewClicked);
        mBinding.signInRule.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.integral_view:
//                if (withdrawalTipsDialog == null) {
//                    withdrawalTipsDialog = new WithdrawalTipsDialog(getContext(), mBinding.getRoot());
//                }
//                withdrawalTipsDialog.show();
                LoginHelper.login(getContext(), () -> {
                });

                break;
            case R.id.customer_service_view:
                LoginHelper.login(getContext(), () -> {
                    if (customerServiceDialog == null) {
                        customerServiceDialog = new CustomerServiceDialog(getBaseActivity());
                    }
                    customerServiceDialog.show(view);
                });

                break;
            case R.id.message_center_view:
                LoginHelper.login(getContext(), () ->
                        getActivity().startActivity(new Intent(getContext(), MessageCenterActivity.class)));

                break;
            case R.id.explanation_view:
                LoginHelper.login(getContext(), () ->
                        WebViewActivity.openRealUrlWebActivity(getBaseActivity(), Constants.INTEGRAL_EXPLANATION_URL));
                break;
            case R.id.search_bar:
                startActivity(new Intent(mContext, SearchActivity.class).putExtra("type", "integral"));
                break;
            case R.id.sign_in_rule://签到规则
                if (mSignInRuleDialog == null) {
                    mSignInRuleDialog = new SignInRuleDialog(mContext, signInRuleStr);
                    mSignInRuleDialog.show(view);
                } else {
                    mSignInRuleDialog.show(view);
                }
                break;
            case R.id.sign_in_tv://签到

                LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        if (todaySignInDayBean == null) {
                            return;
                        }
                        integralSign(todaySignInDayBean.getDayOfWeek(), todaySignInDayBean.getIntelgral(), todaySignInDayBean.getCouponId());
                    }
                });


                break;
        }
    }

    @Override
    protected void dataObservable() {

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

        mViewModel.integralBalanceLiveData.observe(this, data -> {
            mBinding.integralView.setText(NumberUtils.format(Double.parseDouble(data), 0));
        });

        mViewModel.integralInfoLiveData.observe(this, data -> {
            mSignInList.clear();
            mSignInList.addAll(data.getList());
            mSignInAdapter.refresh();
            mSignInAdapter.notifyDataSetChanged();
            signInRuleStr = data.getSignRule();
            signInBean = data;
            for (SignInDayBean bean : mSignInList) {
                if (bean.isCurrentDayFlag()) {
                    todaySignInDayBean = bean;
                    break;
                }
            }
            if (todaySignInDayBean.isSignFlag()) {
                mBinding.signInTv.setEnabled(false);
                mBinding.signInTv.setText("已签到");
                mBinding.signInTv.setTextColor(Color.parseColor("#A0A0A0"));
            } else {
                mBinding.signInTv.setEnabled(true);
                mBinding.signInTv.setText("立即签到领积分");
                mBinding.signInTv.setTextColor(Color.parseColor("#FFFFFF"));
            }
            SpanUtils.with(mBinding.signinDesc)
                    .append("本周签到 ")
                    .append(data.getList().size() + " ")
                    .setForegroundColor(Color.parseColor("#FF593E"))
                    .append("天，额外获得 ")
                    .append(data.getCouponAmount() + "元加油券")
                    .setForegroundColor(Color.parseColor("#FF593E"))
                    .create();
        });
        mViewModel.integralSignLiveData.observe(this, data -> {
            if (mSignInSuccessDialog == null) {
                mSignInSuccessDialog = new SignInSuccessDialog(mContext, getBaseActivity());
            }
            mSignInSuccessDialog.setData(data,signInBean);
            mSignInSuccessDialog.show(mBinding.signInTv);
            getIntegralInfo();
            queryIntegralBalance();
        });
    }

    private void initTab() {
        mBinding.tabView.reset();
        mBinding.tabView.notifyDataChanged();
        if (tabBuilder != null) {
            tabBuilder = null;
        }
        tabBuilder = mBinding.tabView.tabBuilder().setGravity(Gravity.CENTER);
        tabBuilder.setTextSize(QMUIDisplayHelper.sp2px(getContext(), 15), QMUIDisplayHelper.sp2px(getContext(), 15));
        tabBuilder.setColor(Color.parseColor("#313233"), Color.parseColor("#1676FF"));
        tabBuilder.setTypeface(Typeface.DEFAULT_BOLD, Typeface.DEFAULT_BOLD);
//        mBinding.tabView.getTabCount();
        for (ProductClassBean str : classData) {
            mBinding.tabView.addTab(tabBuilder.setText(str.getClassName()).build(getContext()));
        }

        int space = QMUIDisplayHelper.dp2px(getContext(), 25);
        mBinding.tabView.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(getContext(), 2), false, true));
        mBinding.tabView.setItemSpaceInScrollMode(space);
        mBinding.tabView.setPadding(space, 0, space, 0);
        mBinding.tabView.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mBinding.tabView.setScrollContainer(true);
        mBinding.tabView.selectTab(0);
//        mBinding.tabView.selectTab(0, false, false);
        mBinding.tabView.notifyDataChanged();
        mBinding.tabView.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                pageNum = 1;
                categoryId = classData.get(index).getId();
                mBinding.refreshview.setEnableRefresh(true);
                mBinding.refreshview.setEnableLoadMore(false);
                mBinding.refreshview.setNoMoreData(false);
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
        bannerViewModel1.getBannerOfPostion(BannerPositionConstants.INTEGRAL_HOME_FUNCTION_BANNER).observe(this, data -> {
            mBinding.refreshview.finishRefresh(true);
            if (data != null) {
                if (data.size() == 1) {
                    GlideUtils.loadImage(getContext(), data.get(0).getImgUrl(), mBinding.bannerLeftView);
                    mBinding.bannerRightView.setVisibility(View.INVISIBLE);
                    mBinding.bannerLeftView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NaviActivityInfo.disPathIntentFromUrl((MainActivity) getActivity(), data.get(0).getLink());
                        }
                    });
                } else if (data.size() == 2) {
                    GlideUtils.loadImage(getContext(), data.get(0).getImgUrl(), mBinding.bannerLeftView);
                    GlideUtils.loadImage(getContext(), data.get(1).getImgUrl(), mBinding.bannerRightView);
                    mBinding.bannerLeftView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            LoginHelper.login(getContext(), new LoginHelper.CallBack() {
//                                @Override
//                                public void onLogin() {
                            NaviActivityInfo.disPathIntentFromUrl((MainActivity) getActivity(), data.get(0).getLink());
//                                }
//                            });
//                            WebViewActivity.openRealUrlWebActivity(getBaseActivity(), data.get(0).getLink());
                        }
                    });
                    mBinding.bannerRightView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            LoginHelper.login(getContext(), new LoginHelper.CallBack() {
//                                @Override
//                                public void onLogin() {
                            NaviActivityInfo.disPathIntentFromUrl((MainActivity) getActivity(), data.get(1).getLink());
//                                }
//                            });
                        }
                    });
                } else {
                    mBinding.bannerLeftView.setVisibility(View.GONE);
                    mBinding.bannerRightView.setVisibility(View.GONE);
                }
            }
        });
        bannerViewModel2.getBannerOfPostion(BannerPositionConstants.INTEGRAL_HOME_BANNER).observe(this, data -> {
            mBinding.refreshview.finishRefresh(true);
            if (data != null && data.size() > 0) {
                mBinding.banner.setVisibility(View.VISIBLE);
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
//                            LoginHelper.login(getContext(), new LoginHelper.CallBack() {
//                                @Override
//                                public void onLogin() {
                            NaviActivityInfo.disPathIntentFromUrl((MainActivity) getActivity(), data.getLink());
//                                }
//                            });
//                            WebViewActivity.openWebActivity((MainActivity) getActivity(), data.getLink());
                        });
                    }
                }).addBannerLifecycleObserver(this)
                        .setIndicator(new RectangleIndicator(mContext));
            } else {
                mBinding.banner.setVisibility(View.GONE);

            }

        });


    }

    private void queryProductCategorys() {
        mViewModel.queryProductCategorys();
    }

    private void queryProducts() {

        mViewModel.queryProducts(categoryId, pageNum, pageSize);
    }

    private void queryIntegralBalance() {
        mViewModel.queryIntegralBalance();
    }

    private void getIntegralInfo() {
        mViewModel.getIntegralInfo();
    }

    private void integralSign(int dayOfWeek, int integral, String couponId) {
        mViewModel.integralSign(dayOfWeek, integral, couponId);
    }

}
