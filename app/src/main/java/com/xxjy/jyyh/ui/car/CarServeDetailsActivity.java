package com.xxjy.jyyh.ui.car;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabView;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.CarServeProjectListAdapter;
import com.xxjy.jyyh.adapter.SelectCarServeClassAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityCarServeDetailsBinding;
import com.xxjy.jyyh.dialog.CarServeCouponDialog;
import com.xxjy.jyyh.dialog.NavigationDialog;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.CarServeCouponBean;
import com.xxjy.jyyh.entity.CarServeCouponListBean;
import com.xxjy.jyyh.entity.CarServeProductsBean;
import com.xxjy.jyyh.entity.CardStoreInfoVoBean;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingConstant;
import com.xxjy.jyyh.utils.locationmanger.MapIntentUtils;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CarServeDetailsActivity extends BindingActivity<ActivityCarServeDetailsBinding, CarServeViewModel> {
    private List<String> classData;
    private List<String> bannerData = new ArrayList<>();
    private List<CarServeProductsBean> serveData = new ArrayList<>();
    private Map<String, List<CarServeProductsBean>> productCategory;
    private CarServeProjectListAdapter carServeProjectListAdapter;
    private CardStoreInfoVoBean mCardStoreInfoVo;

    private CarServeCouponDialog mCarServeCouponDialog;
    private CarServeCouponListBean mCarServeCouponListBean;
    private long selectCouponId = 0;


    private String storeNo;
    private double distance;

    private CarServeCouponBean selectCarServeCouponBean;
    private CarServeProductsBean selectCarServeProductsBean;

    private SelectCarServeClassAdapter selectCarServeClassAdapter;

    private int selectClassPosition = 0;

    @Override
    protected void onRestart() {
        super.onRestart();
        getStoreInfo();
    }

    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backView);


        storeNo = getIntent().getStringExtra("no");
        distance = getIntent().getDoubleExtra("distance", 0d);

        mBinding.serveDataRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        carServeProjectListAdapter = new CarServeProjectListAdapter(R.layout.adapter_car_serve_project_list, serveData);
        mBinding.serveDataRecyclerview.setAdapter(carServeProjectListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBinding.tabServeClassView.setLayoutManager(linearLayoutManager);

        getStoreInfo();


    }

    @Override
    protected void initListener() {
        mBinding.backView.setOnClickListener(this::onViewClicked);
        mBinding.buyView.setOnClickListener(this::onViewClicked);
        mBinding.phoneView.setOnClickListener(this::onViewClicked);
        mBinding.navigationView.setOnClickListener(this::onViewClicked);
        mBinding.couponLayout.setOnClickListener(this::onViewClicked);
        mBinding.bannerView.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.indicatorView.setText(position + 1 + "/" + bannerData.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        carServeProjectListAdapter.setOnSelectListener(data -> {
            selectCarServeProductsBean = data;
            mBinding.floatLayout.removeAllViews();
            addTagView(data.getProductAttribute().getCarTypeName(), mBinding.floatLayout);
            addTagView(data.getProductAttribute().getHasAppointment() == 0 ? "无需电话预约" : "需提前电话预约", mBinding.floatLayout);
            if (data.getProductAttribute().getHasNowRefund() == 1) {
                addTagView("随时退", mBinding.floatLayout);
            }
            if (data.getProductAttribute().getExpires() != 0) {
                addTagView(data.getProductAttribute().getExpires() + "天有效", mBinding.floatLayout);
            }
            mBinding.decView.setText(data.getDescription());
            getUsableCoupon(selectCarServeProductsBean.getChildCategoryId()==0?selectCarServeProductsBean.getCategoryId():selectCarServeProductsBean.getChildCategoryId());

        });
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_view:
                finish();
                break;
            case R.id.buy_view:
                if (mCardStoreInfoVo == null || selectCarServeProductsBean == null) {
                    return;
                }
                if (carServeProjectListAdapter.getSelectData().getCategoryId()!=1) {
                    selectCarServeCouponBean = null;
                }
                CarServeConfirmOrderActivity.openPage(this, mCardStoreInfoVo, selectCarServeProductsBean, selectCarServeCouponBean,true);
                break;
            case R.id.phone_view:
                if (mCardStoreInfoVo == null) {
                    return;
                }
                Uri phoneUri = Uri.parse("tel:" + mCardStoreInfoVo.getPhone());
                Intent intent2 = new Intent(Intent.ACTION_DIAL, phoneUri);
                if (intent2.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent2);
                }
                break;
            case R.id.navigation_view:
                if (mCardStoreInfoVo == null) {

                    return;
                }
                if (MapIntentUtils.isPhoneHasMapNavigation()) {
                    NavigationDialog navigationDialog = new NavigationDialog(this,
                            mCardStoreInfoVo.getLatitude(), mCardStoreInfoVo.getLongitude(),
                            mCardStoreInfoVo.getStoreName());
                    navigationDialog.show();
                } else {
                    showToastWarning("您当前未安装地图软件，请先安装");
                }
                break;
            case R.id.coupon_layout:
                if (!(mCarServeCouponListBean !=null&&mCarServeCouponListBean.getRecords()!=null&&mCarServeCouponListBean.getRecords().size()>0)) {
                    return;
                }

                if (mCarServeCouponDialog == null) {
                    mCarServeCouponDialog = new CarServeCouponDialog(this, selectCouponId);
                }
                mCarServeCouponDialog.dispatchData(mCarServeCouponListBean.getRecords(), selectCouponId);
                mCarServeCouponDialog.show();
                mCarServeCouponDialog.setOnItemClickedListener(new CarServeCouponDialog.OnItemClickedListener() {
                    @Override
                    public void onOilCouponClick(BaseQuickAdapter adapter, View view, int position) {
                        selectCarServeCouponBean = mCarServeCouponListBean.getRecords().get(position);
                        selectCouponId = mCarServeCouponListBean.getRecords().get(position).getId();
                        mBinding.couponNameView.setText(mCarServeCouponListBean.getRecords().get(position).getTitle());
                    }

                    @Override
                    public void onNoCouponClick() {
                        selectCouponId = 0;
                        mBinding.couponNameView.setText("选择优惠券");
                        selectCarServeCouponBean = null;
                    }
                });
                break;
        }

    }

    @Override
    protected void dataObservable() {
        mViewModel.storeLiveData.observe(this, data -> {
            mCardStoreInfoVo = data.getCardStoreInfoVo();
            if (mCardStoreInfoVo != null) {
                mBinding.shopNameView.setText(mCardStoreInfoVo.getStoreName());
                mBinding.shopAddressView.setText(mCardStoreInfoVo.getAddress());
                mBinding.shopHoursDistanceView.setText(String.format("营业时间：%s - %s | 距离您大约:%.2fKM", mCardStoreInfoVo.getOpenStart(), mCardStoreInfoVo.getEndStart(), distance / 1000d));
                bannerData.clear();
                bannerData.add(mCardStoreInfoVo.getStorePicture());
                initBanner();
            }
            productCategory = data.getProductCategory();
            if (data.getProductCategory() != null) {
                classData = new ArrayList<>(data.getProductCategory().keySet());
                if(classData.size()>0){
                    initTab();
                    carServeProjectListAdapter.setNewData(data.getProductCategory().get(classData.get(0)));
                    carServeProjectListAdapter.setSelectPosition(0);
                    selectClassPosition = 0;
                    if (carServeProjectListAdapter.getSelectData().getCategoryId()!=1) {
                        mBinding.couponLayout.setVisibility(View.GONE);
                    } else {
                        getUsableCoupon(carServeProjectListAdapter.getSelectData().getChildCategoryId()==0?carServeProjectListAdapter.getSelectData().getCategoryId():carServeProjectListAdapter.getSelectData().getChildCategoryId());
                    }
                }


            }

        });

        mViewModel.usableCouponLiveData.observe(this, data -> {
            mCarServeCouponListBean = data;
            if (data.getRecords() != null && data.getRecords().size() > 0) {
                if (carServeProjectListAdapter.getSelectData().getCategoryId()!=1) {
                    mBinding.couponLayout.setVisibility(View.GONE);
                } else {
                        mBinding.couponLayout.setVisibility(View.VISIBLE);
                        mBinding.couponNameView.setText(data.getRecords().get(0).getTitle());
                        selectCouponId = data.getRecords().get(0).getId();
                        selectCarServeCouponBean = data.getRecords().get(0);
                }
            } else {
                if (carServeProjectListAdapter.getSelectData().getCategoryId()!=1) {
                    mBinding.couponLayout.setVisibility(View.GONE);
                }else{
                    mBinding.couponLayout.setVisibility(View.VISIBLE);
                    mBinding.couponNameView.setText("暂无优惠券");
                }

            }

        });
    }

    private void changeCouponLayout(String productType) {
        for (CarServeCouponBean bean : mCarServeCouponListBean.getRecords()) {
            if (TextUtils.equals(productType, bean.getCouponType())) {
                mBinding.couponLayout.setVisibility(View.VISIBLE);
                break;
            }
        }

    }

    private void initBanner() {
        mBinding.bannerView.setVisibility(View.VISIBLE);
        //banner
        mBinding.bannerView.setAdapter(new BannerImageAdapter<String>(bannerData) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                Glide.with(holder.imageView)
                        .load(data)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.bg_banner_loading)
                                .error(R.drawable.ic_car_serve_store_image))
                        .into(holder.imageView);
            }
        }).addBannerLifecycleObserver(this)
                .setIndicator(new RectangleIndicator(this));
        mBinding.indicatorView.setText(1 + "/" + bannerData.size());
    }

    private void initTab() {
        selectCarServeClassAdapter = new SelectCarServeClassAdapter(R.layout.adapter_select_car_serve_class, classData);
        selectCarServeClassAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                selectCarServeClassAdapter.setSelectPosition(position);
                carServeProjectListAdapter.setNewData(productCategory.get(classData.get(position)));
                carServeProjectListAdapter.setSelectPosition(0);
                selectClassPosition = position;
                if (carServeProjectListAdapter.getSelectData().getCategoryId()!=1) {
                    mBinding.couponLayout.setVisibility(View.GONE);
                } else {
                    getUsableCoupon(carServeProjectListAdapter.getSelectData().getChildCategoryId()==0?carServeProjectListAdapter.getSelectData().getCategoryId():carServeProjectListAdapter.getSelectData().getChildCategoryId());
                }


            }
        });
        mBinding.tabServeClassView.setAdapter(selectCarServeClassAdapter);
    }

    private void addTagView(String content, QMUIFloatLayout floatLayout) {
        TextView textView = new TextView(this);
        int textViewPadding = QMUIDisplayHelper.dp2px(this, 4);
        int textViewPadding2 = QMUIDisplayHelper.dp2px(this, 2);
        textView.setPadding(textViewPadding, textViewPadding2, textViewPadding, textViewPadding2);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
        textView.setTextColor(Color.parseColor("#FF593E"));
        textView.setBackgroundResource(R.drawable.shape_stroke_station_tag_3);
        textView.setText(content);
        floatLayout.addView(textView);
    }


    private void getStoreInfo() {
        mViewModel.getStoreDetails(storeNo);
    }

    private void getUsableCoupon(int categoryId) {
        mViewModel.getUsableCoupon(categoryId);
    }

}