package com.xxjy.jyyh.ui.car;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.CarServeProjectListAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityCarServeDetailsBinding;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingConstant;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.ArrayList;
import java.util.List;

public class CarServeDetailsActivity extends BindingActivity<ActivityCarServeDetailsBinding,CarServeViewModel> {
    private QMUITabBuilder tabBuilder;
    private List<String> classData=new ArrayList<>();
    private List<String> bannerData=new ArrayList<>();
    private List<String> serveData = new ArrayList<>();
    private CarServeProjectListAdapter adapter;
    @Override
    protected void initView() {
        setTransparentStatusBar(mBinding.backView);
        for (int i=0;i<10;i++) {
            addTagView("需提前电话预约",mBinding.floatLayout);

        }
        classData.add("洗车");
        classData.add("美容养护");
        initTab();
        bannerData.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F017dfd55e1437b6ac7251df8a7ebfc.jpg%40900w_1l_2o_100sh.jpg&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625037236&t=dcfdf1f90f9b58ffc614ba3a2a76782b");
        bannerData.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01f34459b7972ca801211d25f906c9.png%401280w_1l_2o_100sh.png&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625037236&t=d668f495f08040804891ca4f2d4359ce");
        bannerData.add("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0109f2578883a70000018c1b5aaea8.JPG%401280w_1l_2o_100sh.jpg&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625037236&t=2d1243477166700071fef9a5909418f1");
        initBanner();

        serveData.add("111111111");
        serveData.add("111111111");
        serveData.add("111111111");
        serveData.add("111111111");
        serveData.add("111111111");
        serveData.add("111111111");
        initServe();
    }

    @Override
    protected void initListener() {
        mBinding.backView.setOnClickListener(this::onViewClicked);
        mBinding.buyView.setOnClickListener(this::onViewClicked);
        mBinding.bannerView.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.indicatorView.setText(position+1+"/"+bannerData.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back_view:
                finish();
                break;
            case R.id.buy_view:
startActivity(new Intent(this,CarServeConfirmOrderActivity.class));
                break;
        }

    }

    @Override
    protected void dataObservable() {

    }
    private void initServe(){
        mBinding.serveDataRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter= new CarServeProjectListAdapter(R.layout.adapter_car_serve_project_list,serveData);
        mBinding.serveDataRecyclerview.setAdapter(adapter);


    }
    private void initBanner(){
        mBinding.bannerView.setVisibility(View.VISIBLE);
        //banner
        mBinding.bannerView.setAdapter(new BannerImageAdapter<String>(bannerData) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                Glide.with(holder.imageView)
                        .load(data)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.bg_banner_loading)
                                .error(R.drawable.bg_banner_error))
                        .into(holder.imageView);
            }
        }).addBannerLifecycleObserver(this)
                .setIndicator(new RectangleIndicator(this));
        mBinding.indicatorView.setText(1+"/"+bannerData.size());
    }

    private void initTab() {
        mBinding.tabServeClassView.reset();
        mBinding.tabServeClassView.notifyDataChanged();
        if (tabBuilder != null) {
            tabBuilder = null;
        }
        tabBuilder = mBinding.tabServeClassView.tabBuilder().setGravity(Gravity.CENTER);
        tabBuilder.setTextSize(QMUIDisplayHelper.sp2px(this, 15), QMUIDisplayHelper.sp2px(this, 15));
        tabBuilder.setColor(Color.parseColor("#313233"), Color.parseColor("#1676FF"));
        tabBuilder.setTypeface(Typeface.DEFAULT_BOLD, Typeface.DEFAULT_BOLD);
//        mBinding.tabView.getTabCount();
        for (String str : classData) {
            mBinding.tabServeClassView.addTab(tabBuilder.setText(str).build(this));
        }

        int space = QMUIDisplayHelper.dp2px(this, 25);
        mBinding.tabServeClassView.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(this, 2), false, true));
        mBinding.tabServeClassView.setItemSpaceInScrollMode(space);
        mBinding.tabServeClassView.setPadding(space, 0, space, 0);
        mBinding.tabServeClassView.setMode(QMUITabSegment.MODE_SCROLLABLE);
        mBinding.tabServeClassView.setScrollContainer(true);
        mBinding.tabServeClassView.selectTab(0);
        mBinding.tabServeClassView.notifyDataChanged();
    }
    private void addTagView( String content, QMUIFloatLayout floatLayout) {
        TextView textView = new TextView(this);
        int textViewPadding = QMUIDisplayHelper.dp2px(this, 4);
        int textViewPadding2 = QMUIDisplayHelper.dp2px(this, 2);
        textView.setPadding(textViewPadding, textViewPadding2, textViewPadding, textViewPadding2);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        textView.setBackgroundResource(R.drawable.shape_stroke_station_tag);
        textView.setText(content);
        floatLayout.addView(textView);
    }
}