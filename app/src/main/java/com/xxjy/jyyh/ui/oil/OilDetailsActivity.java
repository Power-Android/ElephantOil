package com.xxjy.jyyh.ui.oil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.alipay.sdk.app.PayTask;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.OilCheckedAdapter;
import com.xxjy.jyyh.adapter.OilGunAdapter;
import com.xxjy.jyyh.adapter.OilNumAdapter;
import com.xxjy.jyyh.adapter.OilStationFlexAdapter;
import com.xxjy.jyyh.adapter.OilTypeAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.constants.PayTypeConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityOilDetailsBinding;
import com.xxjy.jyyh.dialog.GasStationLocationTipsDialog;
import com.xxjy.jyyh.dialog.LocationTipsDialog;
import com.xxjy.jyyh.dialog.NavigationDialog;
import com.xxjy.jyyh.dialog.OilAmountDialog;
import com.xxjy.jyyh.dialog.OilCouponDialog;
import com.xxjy.jyyh.dialog.OilGunDialog;
import com.xxjy.jyyh.dialog.OilPayDialog;
import com.xxjy.jyyh.dialog.OilTipsDialog;
import com.xxjy.jyyh.dialog.PriceDescriptionDialog;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.entity.OilEntity;
import com.xxjy.jyyh.entity.OilPayTypeEntity;
import com.xxjy.jyyh.entity.OilTypeEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.home.HomeViewModel;
import com.xxjy.jyyh.ui.pay.RefuelingPayResultActivity;
import com.xxjy.jyyh.ui.web.WeChatWebPayActivity;
import com.xxjy.jyyh.utils.OilUtils;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.WXSdkManager;
import com.xxjy.jyyh.utils.locationmanger.MapIntentUtils;
import com.xxjy.jyyh.utils.pay.IPayListener;
import com.xxjy.jyyh.utils.pay.PayHelper;
import com.xxjy.jyyh.utils.pay.PayListenerUtils;
import com.xxjy.jyyh.utils.toastlib.Toasty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OilDetailsActivity extends BindingActivity<ActivityOilDetailsBinding, OilViewModel> {
    private List<OilEntity.StationsBean.CzbLabelsBean> mTagList = new ArrayList<>();
    private List<OilEntity.StationsBean.OilPriceListBean> mOilNumList = new ArrayList<>();//油号列表
    private List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> mOilGunList = new ArrayList<>();//油枪列表
    private OilTypeAdapter mOilTypeAdapter;
    private OilNumAdapter mOilNumAdapter;
    private OilGunAdapter mOilGunAdapter;
    private int  mOilGunPosition = 0;
    private int  mOilNoPosition;
    private boolean isSelectGun;
    private HomeViewModel mHomeViewModel;
    private double mLng, mLat;
    private String mGasId;
    private OilEntity.StationsBean mStationsBean;
    private OilStationFlexAdapter mFlexAdapter;
    private boolean isFar = false;//油站是否在距离内
    private boolean isPay = false;//油站是否在距离内 是否显示继续支付按钮
    private GasStationLocationTipsDialog mGasStationTipsDialog;
    private LocationTipsDialog mLocationTipsDialog;
    private String mOilNo;
    private List<OilTypeEntity> mOilTypeList = new ArrayList<>();//油类型
    private List<OilEntity.StationsBean.OilPriceListBean> mOilNumGasData = new ArrayList<>(); //汽油
    private List<OilEntity.StationsBean.OilPriceListBean> mOilNumDieselData = new ArrayList<>();  //柴油
    private List<OilEntity.StationsBean.OilPriceListBean> mOilNumNaturalData = new ArrayList<>(); //天然气

    private PriceDescriptionDialog priceDescriptionDialog;

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backIv, true);
        BusUtils.register(this);

        mGasId = getIntent().getStringExtra(Constants.GAS_STATION_ID);
        mOilNo = getIntent().getStringExtra(Constants.OIL_NUMBER_ID);

        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        //首页油站
        mViewModel.getOilDetail(mGasId, mLat, mLng);

        //标签列表
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager.setAlignItems(AlignItems.FLEX_START);
        mBinding.oilTagRecyclerView.setLayoutManager(flexboxLayoutManager);
        mFlexAdapter = new OilStationFlexAdapter(R.layout.adapter_oil_detail_tag, mTagList);
        mBinding.oilTagRecyclerView.setAdapter(mFlexAdapter);

        //油类型列表
        mBinding.oilTypeRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mOilTypeAdapter = new OilTypeAdapter(R.layout.adapter_oil_num_layout, mOilTypeList);
        mBinding.oilTypeRecyclerView.setAdapter(mOilTypeAdapter);
        mOilTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<OilTypeEntity> data = adapter.getData();
            //清空油类型选中状态
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelect(false);
            }
            data.get(position).setSelect(true);
            adapter.notifyDataSetChanged();

            //清空油号选中状态，并默认第一个被选中
            List<OilEntity.StationsBean.OilPriceListBean> oilPriceList = data.get(position).getOilPriceList();
            for (int i = 0; i < oilPriceList.size(); i++) {
                oilPriceList.get(i).setSelected(false);
            }
            oilPriceList.get(0).setSelected(true);
            mOilNoPosition = 0;
            mOilGunPosition = 0;
            isSelectGun = false;
            mOilNumAdapter.setNewData(oilPriceList);
            //切换类型时清空枪号选中状态,清空已选择列表的枪号
            for (int i = 0; i < mStationsBean.getOilPriceList().size(); i++) {
                for (int j = 0; j < mStationsBean.getOilPriceList().get(i).getGunNos().size(); j++) {
                    mStationsBean.getOilPriceList().get(i).getGunNos().get(j).setSelected(false);
                }
            }

            mOilGunAdapter.setNewData(oilPriceList.get(mOilGunPosition).getGunNos());

            mBinding.oilLiterTv.setText("" + oilPriceList.get(0).getPriceYfq());
            mBinding.oilNumTv.setText(oilPriceList.get(0).getOilName()+ " :");
            mBinding.oilPriceTv.setText("" + oilPriceList.get(0).getPriceGun());
            mBinding.oilNationalPriceTv.setText("" + oilPriceList.get(0).getPriceOfficial());
            mOilNoPosition = 0;
        });

        //油号列表
        mBinding.oilNumRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mOilNumAdapter = new OilNumAdapter(R.layout.adapter_oil_num_layout, mOilNumList);
        mBinding.oilNumRecyclerView.setAdapter(mOilNumAdapter);
        mOilNumAdapter.setOnItemClickListener((adapter, view, position) -> {
            UiUtils.canClickViewStateDelayed(view, 1000);
            List<OilEntity.StationsBean.OilPriceListBean> data = adapter.getData();
            //清空油号选中状态
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelected(false);
            }
            data.get(position).setSelected(true);
            adapter.notifyDataSetChanged();
            //切换油号时清空枪号选中状态,清空已选择列表的枪号
            for (int i = 0; i < mStationsBean.getOilPriceList().size(); i++) {
                for (int j = 0; j < mStationsBean.getOilPriceList().get(i).getGunNos().size(); j++) {
                    mStationsBean.getOilPriceList().get(i).getGunNos().get(j).setSelected(false);
                }
            }

            mBinding.oilLiterTv.setText("" + data.get(position).getPriceYfq());
            mBinding.oilNumTv.setText(data.get(position).getOilName() + " :");
            mBinding.oilPriceTv.setText("" + data.get(position).getPriceGun());
            mBinding.oilNationalPriceTv.setText("" + data.get(position).getPriceOfficial());

            mOilNoPosition = position;
            mOilGunPosition = 0;
            isSelectGun = false;
            mOilGunAdapter.setNewData(data.get(position).getGunNos());
            mOilGunAdapter.notifyDataSetChanged();
        });

        //枪号列表
        mBinding.oilGunRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mOilGunAdapter = new OilGunAdapter(R.layout.adapter_oil_num_layout, mOilGunList);
        mBinding.oilGunRecyclerView.setAdapter(mOilGunAdapter);
        mOilGunAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setSelected(false);
            }
            data.get(position).setSelected(true);
            adapter.notifyDataSetChanged();

            mOilGunPosition = position;
            isSelectGun = true;
        });
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
                        mHomeViewModel.getLocation();
                    }

                    @Override
                    public void onDenied() {
                        mViewModel.getOilDetail(mGasId, mLat, mLng);
                    }
                })
                .request();
    }


    @Override
    protected void initListener() {
        mBinding.backIv.setOnClickListener(this::onViewClicked);
        mBinding.oilNavigationTv.setOnClickListener(this::onViewClicked);
        mBinding.queryTv.setOnClickListener(this::onViewClicked);
        mBinding.priceDescriptionLayout.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.oil_navigation_tv:
                if (MapIntentUtils.isPhoneHasMapNavigation()) {
                    NavigationDialog navigationDialog = new NavigationDialog(this,
                            mStationsBean.getStationLatitude(), mStationsBean.getStationLongitude(),
                            mStationsBean.getGasName());
                    navigationDialog.show();
                } else {
                    showToastWarning("您当前未安装地图软件，请先安装");
                }
                break;
            case R.id.query_tv:
                if(!isSelectGun){
                    showToastInfo("请选择枪号");
                }else{
                    if (isFar) {
                        showChoiceOil(mStationsBean.getGasName(), view);
                    } else {
                        Intent intent = new Intent(this, OilOrderActivity.class);
                        intent.putExtra("stationsBean", (Serializable) mStationsBean);
                        intent.putExtra("oilNo", mOilNumAdapter.getData().get(mOilNoPosition).getOilNo());
                        intent.putExtra("gunNo", mOilGunAdapter.getData().get(mOilGunPosition).getGunNo());
                        startActivity(intent);
                    }
                }
                break;
            case R.id.price_description_layout:
                if (priceDescriptionDialog == null) {
                    priceDescriptionDialog = new PriceDescriptionDialog(this);
                }
                priceDescriptionDialog.show(mBinding.priceDescriptionLayout);
                break;
        }
    }

    private void showChoiceOil(String stationName, View view) {
        mGasStationTipsDialog = new GasStationLocationTipsDialog(this, view, stationName);
        mGasStationTipsDialog.showPayBt(isPay);
        mGasStationTipsDialog.setOnClickListener(view1 -> {
            switch (view1.getId()) {
                case R.id.select_agin://重新选择
                    closeDialog();
                    startActivity(new Intent(this,MainActivity.class));
                    finish();
                    break;
                case R.id.navigation_tv://导航过去
                    if (MapIntentUtils.isPhoneHasMapNavigation()) {
                        NavigationDialog navigationDialog = new NavigationDialog(this,
                                mStationsBean.getStationLatitude(), mStationsBean.getStationLongitude(),
                                mStationsBean.getGasName());
                        closeDialog();
                        navigationDialog.show();
                    } else {
                        showToastWarning("您当前未安装地图软件，请先安装");
                    }
                    break;
                case R.id.continue_view://继续支付
                    isFar = false;
                    Intent intent = new Intent(this, OilOrderActivity.class);
                    intent.putExtra("stationsBean", (Serializable) mStationsBean);
                    intent.putExtra("oilNo", mOilNumAdapter.getData().get(mOilNoPosition).getOilNo());
                    intent.putExtra("gunNo", mOilGunAdapter.getData().get(mOilGunPosition).getGunNo());
                    startActivity(intent);
                    break;
            }

        });
        mGasStationTipsDialog.show();
    }

    @Override
    protected void dataObservable() {
        mViewModel.loadingView().observe(this, aBoolean -> {
            if (aBoolean) {
                showLoadingDialog();
            } else {
                dismissLoadingDialog();
            }
        });

        mHomeViewModel.locationLiveData.observe(this, locationEntity -> {
            if (locationEntity.isSuccess()) {
                UserConstants.setLatitude(String.valueOf(locationEntity.getLat()));
                UserConstants.setLongitude(String.valueOf(locationEntity.getLng()));
                DPoint p = new DPoint(locationEntity.getLat(), locationEntity.getLng());
                DPoint p2 = new DPoint(mLat, mLng);
                float distance = CoordinateConverter.calculateLineDistance(p, p2);
                if (distance > 100) {
                    mLng = locationEntity.getLng();
                    mLat = locationEntity.getLat();
                    LogUtils.i("定位成功：" + locationEntity.getLng() + "\n" + locationEntity.getLat());
                    //首页油站
                    mViewModel.getOilDetail(mGasId, mLat, mLng);
                }
            } else {
                mLat = 0;
                mLng = 0;
                mViewModel.getOilDetail(mGasId, mLat, mLng);
//                showFailLocation();
            }
        });

        mViewModel.oilLiveData.observe(this, stationsBean -> {
            mStationsBean = stationsBean;
            mBinding.oilNameTv.setText(mStationsBean.getGasName());
            mBinding.oilTagIv.setVisibility(stationsBean.isIsSign() ? View.VISIBLE : View.INVISIBLE);
            mBinding.oilAddressTv.setText(mStationsBean.getGasAddress());
            mBinding.oilNavigationTv.setText(mStationsBean.getDistance() + "km");
            mBinding.invokeTv.setVisibility(mStationsBean.getIsInvoice() == 0 ? View.VISIBLE : View.GONE);
            mBinding.sepaTv.setVisibility(mStationsBean.getIsInvoice() == 0 ? View.VISIBLE : View.GONE);

            if (mStationsBean.getCzbLabels() != null && mStationsBean.getCzbLabels().size() > 0) {
                mBinding.oilTagLayout.setVisibility(View.VISIBLE);
                mTagList = mStationsBean.getCzbLabels();
                mFlexAdapter.setNewData(mTagList);
                mBinding.oilTagRecyclerView.setVisibility(View.VISIBLE);
                if(mStationsBean.getCzbLabels().size()>3){
                    ViewGroup.LayoutParams lp;
                    lp= mBinding.oilTagLayout.getLayoutParams();
                    lp.height= QMUIDisplayHelper.dpToPx(100);
                    mBinding.oilTagLayout.setLayoutParams(lp);
                }
            } else {
                mBinding.oilTagRecyclerView.setVisibility(View.INVISIBLE);
                mBinding.oilTagLayout.setVisibility(View.GONE);
            }
            //分发数据
            dispatchOilData(stationsBean);

            mHomeViewModel.checkDistance(mStationsBean.getGasId());
        });

        mHomeViewModel.distanceLiveData.observe(this, oilDistanceEntity -> {
            isPay = oilDistanceEntity.isPay();
            if (oilDistanceEntity.isHere()) {
                isFar = false;
            } else {
                isFar = true;
            }
        });
    }

    private void dispatchOilData(OilEntity.StationsBean stationsBean) {
        //清空数据
        mOilTypeList.clear();
        mOilNumGasData.clear();
        mOilNumDieselData.clear();
        mOilNumNaturalData.clear();

        for (OilEntity.StationsBean.OilPriceListBean oilNumBean : stationsBean.getOilPriceList()) {
            if (OilUtils.isOilNumDiesel(oilNumBean)) {//柴油
                mOilNumDieselData.add(oilNumBean);
            } else if (OilUtils.isOilNumNatural(oilNumBean)) {//天然气
                mOilNumNaturalData.add(oilNumBean);
            } else {//汽油
                mOilNumGasData.add(oilNumBean);
            }
        }

        if (!mOilNumGasData.isEmpty()) {
            mOilTypeList.add(new OilTypeEntity("汽油", mOilNumGasData));
        }
        if (!mOilNumDieselData.isEmpty()) {
            mOilTypeList.add(new OilTypeEntity("柴油", mOilNumDieselData));
        }
        if (!mOilNumNaturalData.isEmpty()) {
            mOilTypeList.add(new OilTypeEntity("天然气", mOilNumNaturalData));
        }
        if (TextUtils.isEmpty(mOilNo)) {
            mOilNo = String.valueOf(stationsBean.getOilPriceList().get(0).getOilNo());
        }
        for (int i = 0; i < stationsBean.getOilPriceList().size(); i++) {
            if (String.valueOf(stationsBean.getOilPriceList().get(i).getOilNo()).equals(mOilNo)) {
                Integer oilType = stationsBean.getOilPriceList().get(i).getOilType();
                checkOilType(oilType);
            }
        }

        //已选择列表
        for (int i = 0; i < mOilTypeList.size(); i++) {
            if (mOilTypeList.get(i).isSelect()) {
                List<OilEntity.StationsBean.OilPriceListBean> oilPriceList = mOilTypeList.get(i).getOilPriceList();
                for (int j = 0; j < oilPriceList.size(); j++) {
                    if (String.valueOf(oilPriceList.get(j).getOilNo()).equals(mOilNo)) {
                        mBinding.oilNumTv.setText(oilPriceList.get(j).getOilName() + " :");
                        mBinding.oilLiterTv.setText("" + oilPriceList.get(j).getPriceYfq());
                        mBinding.oilPriceTv.setText("" + oilPriceList.get(j).getPriceGun());
                        mBinding.oilNationalPriceTv.setText("" + oilPriceList.get(j).getPriceOfficial());
                    }
                }
                //更新油号列表
                mOilNumList = mOilTypeList.get(i).getOilPriceList();
                mOilNumAdapter.setNewData(mOilNumList);
            }
        }

        //油号列表
        for (int k = 0; k < mOilNumList.size(); k++) {
            if (TextUtils.equals(String.valueOf(mOilNumList.get(k).getOilNo()), String.valueOf(mOilNo))) {
                mOilNumList.get(k).setSelected(true);
                mOilNoPosition = k;
                //更新油枪列表
                mOilGunList = mOilNumList.get(k).getGunNos();
                mOilGunAdapter.setNewData(mOilGunList);
            }
        }
    }

    private void checkOilType(Integer oilType) {
        if (oilType == 1) {
            for (int j = 0; j < mOilTypeList.size(); j++) {
                if (mOilTypeList.get(j).getOilTypeName().equals("汽油")) {
                    mOilTypeList.get(j).setSelect(true);
                }
            }
        }
        if (oilType == 2) {
            for (int j = 0; j < mOilTypeList.size(); j++) {
                if (mOilTypeList.get(j).getOilTypeName().equals("柴油")) {
                    mOilTypeList.get(j).setSelect(true);
                }
            }
        }
        if (oilType == 3) {
            for (int j = 0; j < mOilTypeList.size(); j++) {
                if (mOilTypeList.get(j).getOilTypeName().equals("天然气")) {
                    mOilTypeList.get(j).setSelect(true);
                }
            }
        }

        mOilTypeAdapter.setNewData(mOilTypeList);
    }

    private void closeDialog() {
        if (mLocationTipsDialog != null) {
            mLocationTipsDialog = null;
        }
        if (mGasStationTipsDialog != null) {
            mGasStationTipsDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusUtils.removeSticky(EventConstants.EVENT_JUMP_PAY_QUERY);
    }
}