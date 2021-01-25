package com.xxjy.jyyh.ui.mine;

import android.content.Intent;
import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.databinding.FragmentMineBinding;
import com.xxjy.jyyh.ui.msg.MessageCenterActivity;
import com.xxjy.jyyh.ui.order.OrderListActivity;
import com.xxjy.jyyh.ui.setting.SettingActivity;

/**
 * @author power
 * @date 1/21/21 11:53 AM
 * @project ElephantOil
 * @description:
 */
public class MineFragment extends BindingFragment<FragmentMineBinding, MineViewModel> {
    public static MineFragment getInstance() {
        return new MineFragment();
    }

    @Override
    protected void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.topLayout);
    }

    @Override
    protected void initListener() {
        mBinding.equityOrderLayout.setOnClickListener(this::onViewClicked);
        mBinding.refuelingOrderLayout.setOnClickListener(this::onViewClicked);
        mBinding.messageCenterView.setOnClickListener(this::onViewClicked);
        mBinding.settingView.setOnClickListener(this::onViewClicked);

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
        }

    }

    @Override
    protected void dataObservable() {

    }
}
