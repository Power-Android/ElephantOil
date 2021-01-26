package com.xxjy.jyyh.ui.oil;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnMultiListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilStationListAdapter;
import com.xxjy.jyyh.adapter.TopLineAdapter;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.FragmentOilBinding;
import com.xxjy.jyyh.dialog.SelectDistanceDialog;
import com.xxjy.jyyh.dialog.SelectOilNumDialog;
import com.xxjy.jyyh.entity.DistanceEntity;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.utils.StatusBarUtil;
import com.youth.banner.Banner;

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

    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.parentLayout);



        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OilStationListAdapter(R.layout.adapter_oil_station_list, data);
        mBinding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });

        mBinding.msgBanner.setAdapter(new TopLineAdapter(new ArrayList<>(), true))
                .setOrientation(Banner.VERTICAL)
                .setUserInputEnabled(false);
        getOrderNews();
loadData(false);
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

        mBinding.refreshview.setOnLoadMoreListener(refreshLayout -> {
//            mBinding.refreshview.setEnableLoadMore(false);
            loadData(true);

        });
        mBinding.refreshview.setOnRefreshListener(refreshLayout -> {
//            mBinding.refreshview.setEnableRefresh(false);
            loadData(false);

        });

        mBinding.oilSortLayout.setOnClickListener(this::onViewClicked);
        mBinding.oilSelectLayout.setOnClickListener(this::onViewClicked);
        mBinding.oilDistancePriceLayout.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.oil_sort_layout:

                getOilNums();

                break;
            case R.id.oil_select_layout:
                if (selectDistanceDialog == null) {
                    selectDistanceDialog = new SelectDistanceDialog(getContext(), mBinding.popupLayout, mBinding.getRoot());
                }
                selectDistanceDialog.show();
                selectDistanceDialog.setOnItemClickedListener((adapter, view1, position, distanceEntity) -> {
                    distance=distanceEntity.getDistance();
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
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.orderNewsLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                mBinding.msgBanner.setDatas(data);
                mBinding.newsLayout.setVisibility(View.VISIBLE);
            } else {
                mBinding.newsLayout.setVisibility(View.GONE);
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
            });
        });

        mViewModel.oilStationLiveData.observe(this, dataStations -> {


           if(dataStations==null||dataStations.getStations()==null||dataStations.getStations().size()==0){
             if(pageNum==1){
                 mBinding.refreshview.finishRefreshWithNoMoreData();
             }else{
                 mBinding.refreshview.finishLoadMoreWithNoMoreData();
             }
           }else{
               if(pageNum==1){
                   data.clear();
                   mBinding.refreshview.finishRefresh();
//                   mBinding.refreshview.setEnableRefresh(true);
               }else{
                   mBinding.refreshview.finishLoadMore();
//                   mBinding.refreshview.setEnableLoadMore(true);
               }
               data.addAll(dataStations.getStations());
               pageNum++;
           }

        });
    }

    private void loadData(boolean isLoadMore){
        if(isLoadMore){
            getOilStations(UserConstants.getLatitude(), UserConstants.getLongitude(), mCheckOilGasId, firstDistanceOrPrice ? "1" : "2", distance==-1?null:String.valueOf(distance*1000), String.valueOf(pageNum), String.valueOf(pageSize));

        }else{
            pageNum=1;
            getOilStations(UserConstants.getLatitude(), UserConstants.getLongitude(), mCheckOilGasId, firstDistanceOrPrice ? "1" : "2", distance==-1?null:String.valueOf(distance*1000), String.valueOf(pageNum), String.valueOf(pageSize));

        }

    }

    private void getOrderNews() {
        mViewModel.getOrderNews();
    }

    private void getOilNums() {
        mViewModel.getOilNums();
    }

    private void getOilStations(String appLatitude, String appLongitude, String oilNo, String orderBy, String distance, String pageNum, String pageSize) {
        mViewModel.getOilStations(appLatitude,
                appLongitude, oilNo, orderBy, distance, pageNum, pageSize);
    }
}
