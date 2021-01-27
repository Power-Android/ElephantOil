package com.xxjy.jyyh.ui.mine;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.MineTabAdapter;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.FragmentMineBinding;
import com.xxjy.jyyh.ui.msg.MessageCenterActivity;
import com.xxjy.jyyh.ui.order.OrderListActivity;
import com.xxjy.jyyh.ui.setting.SettingActivity;
import com.xxjy.jyyh.utils.LoginHelper;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.PropertyReference0Impl;

public class MineFragment extends BindingFragment<FragmentMineBinding, MineViewModel> {
    public static MineFragment getInstance() {
        return new MineFragment();
    }


    private List<String> tabs=new ArrayList<>();


    @Override
    protected void onVisible() {
        super.onVisible();
        if (UserConstants.getIsLogin()){
            queryUserInfo();
        }else{

        }
    }

    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.topLayout);
        tabs.add("11111");
        tabs.add("11111");
        tabs.add("11111");
        tabs.add("11111");
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        MineTabAdapter mineTabAdapter = new MineTabAdapter(R.layout.adapter_mine_tab,tabs);
        mBinding.recyclerView.setAdapter(mineTabAdapter);


//        queryUserInfo();

    }

    @Override
    protected void initListener() {
        mBinding.equityOrderLayout.setOnClickListener(this::onViewClicked);
        mBinding.refuelingOrderLayout.setOnClickListener(this::onViewClicked);
        mBinding.messageCenterView.setOnClickListener(this::onViewClicked);
        mBinding.settingView.setOnClickListener(this::onViewClicked);
        mBinding.nameView.setOnClickListener(this::onViewClicked);
        mBinding.photoView.setOnClickListener(this::onViewClicked);
        mBinding.myCouponLayout.setOnClickListener(this::onViewClicked);

    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.equity_order_layout:
                getActivity().startActivity(new Intent(getContext(), OrderListActivity.class));
                break;
            case R.id.refueling_order_layout:
                getActivity().startActivity(new Intent(getContext(), OrderListActivity.class));
                break;
            case R.id.message_center_view:
                getActivity().startActivity(new Intent(getContext(), MessageCenterActivity.class));
                break;
            case R.id.setting_view:
                getActivity().startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.my_coupon_layout:
                getActivity().startActivity(new Intent(getContext(), MyCouponActivity.class));
                break;
            case R.id.name_view:
            case R.id.photo_view:
                LoginHelper.login(mContext, new LoginHelper.CallBack() {
                    @Override
                    public void onLogin() {

                    }
                });
                break;
        }

    }

    @Override
    protected void dataObservable() {

        mViewModel.userLiveData.observe(this,data ->{

        });
    }


    private void queryUserInfo(){
        mViewModel.queryUserInfo();
    }
}
