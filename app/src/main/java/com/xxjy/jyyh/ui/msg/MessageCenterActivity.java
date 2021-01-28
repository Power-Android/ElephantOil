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
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.MessageListAdapter;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityMessageCenterBinding;

import java.util.ArrayList;
import java.util.List;

public class MessageCenterActivity extends BindingActivity<ActivityMessageCenterBinding, MessageCenterViewModel> {

    private List<String> data = new ArrayList<>();
    private MessageListAdapter messageListAdapter;
    private QMUIPullLayout.PullAction mPullAction;

    @Override
    protected void initView() {
//        mBinding.topLayout.setTitle("消息中心");
//        mBinding.topLayout.addLeftImageButton(R.drawable.arrow_back_black,
//                R.id.qmui_topbar_item_left_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        mBinding.titleLayout.tvTitle.setText("消息中心");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);
        initTab();

        data = new ArrayList<>();
        data.add("aaaa");
        data.add("aaaa");
        data.add("aaaa");
        data.add("aaaa");
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageListAdapter = new MessageListAdapter(R.layout.item_msg_artical_lists, data);
        mBinding.recyclerView.setAdapter(messageListAdapter);

        mBinding.pullLayout.setActionListener(new QMUIPullLayout.ActionListener() {
            @Override
            public void onActionTriggered(@NonNull QMUIPullLayout.PullAction pullAction) {
                mPullAction = pullAction;
                if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_TOP) {
                    data.clear();
                    data.add("aaaa");
                    data.add("aaaa");
                    data.add("aaaa");
                    data.add("aaaa");
                    messageListAdapter.notifyDataSetChanged();
                } else if (pullAction.getPullEdge() == QMUIPullLayout.PULL_EDGE_BOTTOM) {
                    data.add("aaaa");
                    data.add("aaaa");
                    data.add("aaaa");
                    data.add("aaaa");
                    messageListAdapter.notifyDataSetChanged();

                }
            }
        });
        messageListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {

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
    }
}