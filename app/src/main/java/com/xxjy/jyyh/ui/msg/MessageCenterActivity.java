package com.xxjy.jyyh.ui.msg;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout;
import com.qmuiteam.qmui.widget.tab.QMUIBasicTabSegment;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.MessageListAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityMessageCenterBinding;
import com.xxjy.jyyh.entity.ArticleBean;
import com.xxjy.jyyh.entity.ArticleListBean;
import com.xxjy.jyyh.ui.web.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class MessageCenterActivity extends BindingActivity<ActivityMessageCenterBinding, MessageCenterViewModel> {

    private List<ArticleBean> data = new ArrayList<>();
    private MessageListAdapter messageListAdapter;

    private int pageNum = 1;
    private int pageSize = 10;
    private boolean isSystemNotice = true;

    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("消息中心");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);
        initTab();

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageListAdapter = new MessageListAdapter(R.layout.item_msg_artical_lists, data);
        mBinding.recyclerView.setAdapter(messageListAdapter);
        messageListAdapter.setEmptyView(R.layout.empty_layout, mBinding.recyclerView);
        messageListAdapter.setOnItemClickListener((adapter, view, position) -> WebViewActivity.openRealUrlWebActivity(MessageCenterActivity.this,((ArticleBean)adapter.getItem(position)).getUrl()));
        mBinding.refreshview.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNum++;
                if (isSystemNotice) {
                    getArticles();
                } else {
                    getNotices();
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNum = 1;
                if (isSystemNotice) {
                    getArticles();
                } else {
                    getNotices();
                }
            }
        });

        if (isSystemNotice) {
            getArticles();
        } else {
            getNotices();
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {
        mViewModel.articlesLiveData.observe(this, data -> {
            if (data != null &&data.getList()!=null&& data.getList().size() > 0) {
                if (pageNum == 1) {
                    messageListAdapter.setNewData(data.getList());
                    mBinding.refreshview.setEnableLoadMore(true);
                    mBinding.refreshview.finishRefresh(true);
                } else {
                    messageListAdapter.addData(data.getList());
                    mBinding.refreshview.finishLoadMore(true);
                }
            } else {
                if(pageNum==1){
                    messageListAdapter.setNewData(null);
                }
                mBinding.refreshview.finishLoadMoreWithNoMoreData();
            }
        });
        mViewModel.noticesLiveData.observe(this, data -> {
            if (data != null &&data.getList()!=null&& data.getList().size() > 0) {
                if (pageNum == 1) {
                    messageListAdapter.setNewData(data.getList());
                    mBinding.refreshview.setEnableLoadMore(true);
                    mBinding.refreshview.finishRefresh(true);
                } else {
                    messageListAdapter.addData(data.getList());
                    mBinding.refreshview.finishLoadMore(true);
                }
            } else {
                if(pageNum==1){
                    messageListAdapter.setNewData(null);
                }

                mBinding.refreshview.finishLoadMoreWithNoMoreData();
            }
        });
    }

    private void initTab() {
        QMUITabBuilder tabBuilder = mBinding.tabView.tabBuilder().setGravity(Gravity.CENTER);
        tabBuilder.setTextSize(QMUIDisplayHelper.sp2px(this, 16), QMUIDisplayHelper.sp2px(this, 17));
        tabBuilder.setColor(Color.parseColor("#403636"), Color.parseColor("#1676FF"));
        mBinding.tabView.addTab(tabBuilder.setText("系统消息").build(this));
        mBinding.tabView.addTab(tabBuilder.setText("订单通知").build(this));

        int space = QMUIDisplayHelper.dp2px(this, 10);
        mBinding.tabView.setIndicator(new QMUITabIndicator(QMUIDisplayHelper.dp2px(this, 2), false, true));
        mBinding.tabView.setItemSpaceInScrollMode(space);
        mBinding.tabView.setPadding(space, 0, space, 0);
        mBinding.tabView.setMode(QMUITabSegment.MODE_FIXED);
        mBinding.tabView.selectTab(0);
        mBinding.tabView.notifyDataChanged();
        mBinding.tabView.addOnTabSelectedListener(new QMUIBasicTabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                pageNum = 1;
                mBinding.refreshview.setEnableRefresh(true);
                mBinding.refreshview.setEnableLoadMore(false);
                mBinding.refreshview.setNoMoreData(false);
                if (index == 0) {
                    isSystemNotice = true;
                    getArticles();
                } else {
                    isSystemNotice = false;
                    getNotices();
                }
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

    private void getArticles() {
        mViewModel.getArticles(pageNum, pageSize);
    }

    private void getNotices() {
        mViewModel.getNotices(pageNum, pageSize);
    }
}