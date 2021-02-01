package com.xxjy.jyyh.ui.oil;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnMultiListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.adapter.TopLineAdapter;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.FragmentOilBinding;
import com.xxjy.jyyh.dialog.CustomerServiceDialog;
import com.xxjy.jyyh.dialog.NavigationDialog;
import com.xxjy.jyyh.dialog.SelectDistanceDialog;
import com.xxjy.jyyh.dialog.SelectOilNumDialog;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.DistanceEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.msg.MessageCenterActivity;
import com.xxjy.jyyh.ui.search.SearchActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.StatusBarUtil;
import com.xxjy.jyyh.utils.locationmanger.MapIntentUtils;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RectangleIndicator;

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
    private List<OilEntity.StationsBean> data = new ArrayList<>();
    private SelectOilNumDialog selectOilNumDialog;
    private SelectDistanceDialog selectDistanceDialog;
    private String mCheckOilGasId = "92";
    private int distance = 50;
    private boolean firstDistanceOrPrice = true;
    private int pageNum = 1;
    private int pageSize = 10;

    private CustomerServiceDialog customerServiceDialog;


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){

        }
    }

    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.parentLayout);
        mBinding.refreshview.setEnableLoadMore(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OilStationListAdapter(R.layout.adapter_oil_station_list, data);
        mBinding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter, view, position) -> {

            LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                @Override
                public void onLogin() {
                    List<OilEntity.StationsBean> data = adapter.getData();
                    Intent intent = new Intent(mContext, OilDetailActivity.class);
                    intent.putExtra(Constants.GAS_STATION_ID, data.get(position).getGasId());
                    startActivity(intent);
                }
            });

        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                List<OilEntity.StationsBean> data = adapter.getData();
                LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        switch (view.getId()){
                            case R.id.navigation_ll:
                                if (MapIntentUtils.isPhoneHasMapNavigation()) {
                                    NavigationDialog navigationDialog = new NavigationDialog(getBaseActivity(),
                                            data.get(position).getStationLatitude(), data.get(position).getStationLongitude(),
                                            data.get(position).getGasName());
                                    navigationDialog.show();
                                } else {
                                    showToastWarning("您当前未安装地图软件，请先安装");
                                }
                                break;
                        }

                    }
                });
            }
        });

        mBinding.msgBanner.setAdapter(new TopLineAdapter(new ArrayList<>(), true))
                .setOrientation(Banner.VERTICAL)
                .setUserInputEnabled(false);
        getOrderNews();
        loadData(false);

        getBanners();
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

        mBinding.refreshview.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData(true);

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getOrderNews();
                getBanners();
                loadData(false);

            }
        });
        mBinding.oilSortLayout.setOnClickListener(this::onViewClicked);
        mBinding.oilSelectLayout.setOnClickListener(this::onViewClicked);
        mBinding.oilDistancePriceLayout.setOnClickListener(this::onViewClicked);
        mBinding.customerServiceView.setOnClickListener(this::onViewClicked);
        mBinding.messageCenterView.setOnClickListener(this::onViewClicked);
        mBinding.topSearchLayout.setOnClickListener(this::onViewClicked);
        mBinding.searchLayout.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_layout:
            case R.id.top_search_layout:
                startActivity(new Intent(getContext(), SearchActivity.class));
                break;
            case R.id.oil_sort_layout:

                getOilNums();

                break;
            case R.id.oil_select_layout:
                if (selectDistanceDialog == null) {
                    selectDistanceDialog = new SelectDistanceDialog(getContext(), mBinding.popupLayout, mBinding.getRoot());
                }
                selectDistanceDialog.show();
                selectDistanceDialog.setOnItemClickedListener((adapter, view1, position, distanceEntity) -> {
                    distance = distanceEntity.getDistance();
                    mBinding.oilSelectDistanceTv.setText(distanceEntity.getTitle());

                    loadData(false);
                });
                break;
            case R.id.oil_distance_price_layout:
                if (firstDistanceOrPrice) {
                    firstDistanceOrPrice = false;
                    mBinding.oilSelectDistanceFirstTv.setText("价格优先");
                } else {
                    firstDistanceOrPrice = true;
                    mBinding.oilSelectDistanceFirstTv.setText("距离优先");

                }

                loadData(false);
                break;
            case R.id.customer_service_view:
                LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        if(customerServiceDialog==null){
                            customerServiceDialog = new CustomerServiceDialog(getBaseActivity());
                        }
                        customerServiceDialog.show(view);
                    }
                });

                break;
            case R.id.message_center_view:
                LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {
                        getActivity().startActivity(new Intent(getContext(), MessageCenterActivity.class));
                    }
                });

                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.orderNewsLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                mBinding.msgBanner.setDatas(data);
                mBinding.newsLayout.setVisibility(View.VISIBLE);
            } else {
                mBinding.newsLayout.setVisibility(View.INVISIBLE);
            }
        });
        mViewModel.oilNumLiveData.observe(this, data -> {
            if (selectOilNumDialog == null) {
                selectOilNumDialog = new SelectOilNumDialog(getContext(), mCheckOilGasId, mBinding.popupLayout, mBinding.getRoot());
            }
            selectOilNumDialog.setData(data);
            selectOilNumDialog.show();
            selectOilNumDialog.setOnItemClickedListener((adapter, view, position, oilNum, checkOilGasId) -> {
                mCheckOilGasId = checkOilGasId;
                mBinding.oilSortOilNumTv.setText(oilNum);
                selectOilNumDialog.setCheckData(mCheckOilGasId);
                loadData(false);
            });
        });
        mViewModel.bannersLiveData.observe(this, data -> {
            if(data!=null&&data.size()>0){
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
                            LoginHelper.login(getContext(), new LoginHelper.CallBack() {
                                @Override
                                public void onLogin() {
                                    NaviActivityInfo.disPathIntentFromUrl((MainActivity)getActivity(),data.getLink());
                                }
                            });

//                            WebViewActivity.openWebActivity((MainActivity) getActivity(), data.getLink());
                        });
                    }
                }).addBannerLifecycleObserver(this)
                        .setIndicator(new RectangleIndicator(mContext));
            }else{
                mBinding.banner.setVisibility(View.GONE);
            }


        });

        mViewModel.signOilStationLiveData.observe(this, signStations -> {
            adapter.setNewData(signStations.getStations());
            mBinding.recyclerView.scrollToPosition(0);
            mBinding.refreshview.setEnableRefresh(true);
            mBinding.refreshview.setEnableLoadMore(false);
            mBinding.refreshview.setNoMoreData(false);
            getOilStations();
        });
        mViewModel.oilStationLiveData.observe(this, dataStations -> {
            if (dataStations != null && dataStations.getStations() != null && dataStations.getStations().size() > 0) {
                if (pageNum == 1) {
                    adapter.addData(dataStations.getStations());
                    mBinding.refreshview.setEnableLoadMore(true);
                    mBinding.refreshview.finishRefresh(true);
                }else{
                    adapter.addData(dataStations.getStations());
                    mBinding.refreshview.finishLoadMore(true);
                }


            } else {
                mBinding.refreshview.finishLoadMoreWithNoMoreData();
            }

        });
    }

    private void loadData(boolean isLoadMore) {
        if (isLoadMore) {
            pageNum++;
            getOilStations();


        }else{
            pageNum=1;
            getSignOilStations();

        }

    }

    private void getOrderNews() {
        mViewModel.getOrderNews();
    }

    private void getOilNums() {
        mViewModel.getOilNums();
    }

    private void getOilStations() {
        mViewModel.getOilStations(UserConstants.getLatitude(), UserConstants.getLongitude(),
                mCheckOilGasId, firstDistanceOrPrice ? "1" : "2",
                distance == -1 ? null : String.valueOf(distance * 1000),
                String.valueOf(pageNum), String.valueOf(pageSize), null, null);
    }

    private void getSignOilStations() {
        mViewModel.getSignOilStations(UserConstants.getLatitude(), UserConstants.getLongitude());
    }

    private void getBanners() {
        mViewModel.getBanners();
    }
}
