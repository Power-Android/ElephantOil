package com.xxjy.jyyh.ui.home;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
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
import com.xxjy.jyyh.dialog.ReceiveRewardDialog;
import com.xxjy.jyyh.entity.OilEntity;
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
public class HomeFragment extends BindingFragment<FragmentHomeBinding, HomeViewModel> implements OnRefreshLoadMoreListener {
    private List<OilEntity.StationsBean.CzbLabelsBean> mOilTagList = new ArrayList<>();
    private List<String> mOftenList = new ArrayList<>();
    private List<String> mExchangeList = new ArrayList<>();
    private OilNumDialog mOilNumDialog;
    private OilGunDialog mOilGunDialog;
    private OilAmountDialog mOilAmountDialog;
    private OilTipsDialog mOilTipsDialog;
    private OilPayDialog mOilPayDialog;
    private OilCouponDialog mOilCouponDialog;
    private double mLng, mLat;

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

        if (Double.parseDouble(UserConstants.getLongitude()) != 0 && Double.parseDouble(UserConstants.getLatitude()) != 0) {
            mLat = Double.parseDouble(UserConstants.getLatitude());
            mLng = Double.parseDouble(UserConstants.getLongitude());
            //首页油站
            mViewModel.getHomeOil(mLat, mLng);
        }
        //请求定位权限
        requestPermission();

        //智能优选title
        SpanUtils.with(mBinding.descTv)
                .append("小象加油")
                .setForegroundColor(getResources().getColor(R.color.color_76FF))
                .append("根据您当前的位置智能优选")
                .setForegroundColor(getResources().getColor(R.color.color_6A))
                .create();

        //积分豪礼
        for (int i = 0; i < 6; i++) {
            mExchangeList.add("");
        }
        mBinding.exchangeRecyclerView.setLayoutManager(
                new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        HomeExchangeAdapter exchangeAdapter = new HomeExchangeAdapter(R.layout.adapter_home_exchange, mExchangeList);
        mBinding.exchangeRecyclerView.setAdapter(exchangeAdapter);

    }

    private void requestPermission() {
        PermissionUtils.permission(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        mViewModel.getLocation();
                    }

                    @Override
                    public void onDenied() {
                        showToastWarning("权限被拒绝，部分产品功能将无法使用！");
                    }
                })
                .request();
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
        mBinding.awardTv.setOnClickListener(this::onViewClicked);

        mBinding.refreshView.setOnRefreshLoadMoreListener(this);
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
            case R.id.award_tv:
                ReceiveRewardDialog receiveRewardDialog = new ReceiveRewardDialog(getContext());
                receiveRewardDialog.show(view);
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.locationLiveData.observe(this, locationEntity -> {
            UserConstants.setLatitude(String.valueOf(locationEntity.getLat()));
            UserConstants.setLongitude(String.valueOf(locationEntity.getLng()));
            DPoint p = new DPoint(locationEntity.getLat(), locationEntity.getLng());
            DPoint p2 = new DPoint(mLat, mLng);
            mBinding.addressTv.setText(locationEntity.getAddress());
            float distance = CoordinateConverter.calculateLineDistance(p, p2);
            if (distance > 100) {
                mLng = locationEntity.getLng();
                mLat = locationEntity.getLat();
                LogUtils.i("定位成功：" + locationEntity.getLng() + "\n" + locationEntity.getLat());
                //首页油站
                mViewModel.getHomeOil(mLat, mLng);

            }
        });

        //优选油站
        mViewModel.homeOilLiveData.observe(this, oilEntity -> {
            OilEntity.StationsBean stationsBean = oilEntity.getStations().get(0);
            Glide.with(mContext).load(stationsBean.getGasTypeImg()).into(mBinding.oilImgIv);
            mBinding.oilNameTv.setText(stationsBean.getGasName());
            mBinding.oilAddressTv.setText(stationsBean.getGasAddress());
            mBinding.oilCurrentPriceTv.setText(stationsBean.getPriceYfq());
            mBinding.oilOriginalPriceTv.setText("油站价￥"+stationsBean.getPriceOfficial());
            mBinding.oilNumTv.setText(stationsBean.getOilName());
            mBinding.oilNavigationTv.setText(stationsBean.getDistance()+"km");
            mBinding.oilOriginalPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            mBinding.otherOilTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            if (stationsBean.getCzbLabels() != null){
                mOilTagList = stationsBean.getCzbLabels();
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
            }

            //初始化加油弹框
            initOilDialog(stationsBean);
            //常去油站
            if (UserConstants.getIsLogin()){
                mViewModel.getOftenOils();
            }
            //加油任务
            mViewModel.getRefuelJob(stationsBean.getGasId());
        });

        //常去油站
        mViewModel.oftenOilLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
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
            }
        });
    }

    private void initOilDialog(OilEntity.StationsBean stationsBean) {
        //油号dialog
        mOilNumDialog = new OilNumDialog(mContext, stationsBean);
        mOilNumDialog.setOnItemClickedListener((adapter, view, position) -> {
            List<OilEntity.StationsBean.OilPriceListBean> data = adapter.getData();
            if (mOilGunDialog != null){
                data.get(position).setSelected(true);
                mOilGunDialog.show(data.get(position).getGunNos());
            }
        });

        //枪号dialog
        mOilGunDialog = new OilGunDialog(mContext);
        mOilGunDialog.setOnItemClickedListener((adapter, view, position) -> {
            if (mOilAmountDialog != null){
                mOilAmountDialog.show();
            }
        });

        //快捷金额dialog
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

        //优惠券dialog
        mOilCouponDialog = new OilCouponDialog(mContext);
        mOilCouponDialog.setOnItemClickedListener(new OilCouponDialog.OnItemClickedListener() {
            @Override
            public void onOilCouponClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onNoCouponClick() {

            }
        });

        //温馨提示dialog
        mOilTipsDialog = new OilTipsDialog(mContext);
        mOilTipsDialog.setOnItemClickedListener(view -> {
            mOilTipsDialog.dismiss();
            if (mOilPayDialog != null){
                mOilPayDialog.show();
            }
        });

        //支付dialog
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

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
