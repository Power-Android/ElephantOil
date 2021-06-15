package com.xxjy.jyyh.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.NumberUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.adapter.MineTabAdapter;
import com.xxjy.jyyh.base.BindingFragment;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.FragmentMineBinding;
import com.xxjy.jyyh.dialog.CustomerServiceDialog;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.UserBean;
import com.xxjy.jyyh.entity.VipInfoEntity;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.msg.MessageCenterActivity;
import com.xxjy.jyyh.ui.order.CarServeOrderListActivity;
import com.xxjy.jyyh.ui.order.OrderListActivity;
import com.xxjy.jyyh.ui.order.OtherOrderListActivity;
import com.xxjy.jyyh.ui.setting.SettingActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.GlideUtils;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.NotificationsUtils;

import java.util.ArrayList;
import java.util.List;

public class MineFragment extends BindingFragment<FragmentMineBinding, MineViewModel> {

    private String mUserCardId = "";

    public static MineFragment getInstance() {
        return new MineFragment();
    }


    private List<BannerBean> tabs = new ArrayList<>();

    private MineTabAdapter mineTabAdapter;
    private UserBean userBean;
    private CustomerServiceDialog customerServiceDialog;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (UserConstants.getIsLogin()) {
                queryUserInfo();
                getMonthEquityInfo();
                getVipInfo();
            }
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        getBannerOfPostion();
        if (UserConstants.getIsLogin()) {
            queryUserInfo();
            getMonthEquityInfo();
            getVipInfo();
        } else {
            mBinding.photoView.setImageResource(R.drawable.default_img_bg);
            mBinding.nameView.setText("登录注册");
            mBinding.userPhoneView.setText("");
            SpanUtils.with(mBinding.couponView)
                    .setBold()
                    .append("0")
                    .append("张")
                    .setForegroundColor(Color.parseColor("#323334"))
                    .create();
            SpanUtils.with(mBinding.integralView)
                    .setBold()
                    .append("0")
//                    .append("积分")
//                    .setForegroundColor(Color.parseColor("#323334"))
                    .create();
            SpanUtils.with(mBinding.balanceView)
                    .setBold()
                    .append("0")
                    .append("元")
                    .setForegroundColor(Color.parseColor("#323334"))
                    .create();
        }
        if (UserConstants.getGoneIntegral()) {
            mBinding.equityOrderLayout.setVisibility(View.INVISIBLE);
            mBinding.moreServiceLayout.setVisibility(View.GONE);
        } else {
            mBinding.equityOrderLayout.setVisibility(View.VISIBLE);
            mBinding.moreServiceLayout.setVisibility(View.VISIBLE);
        }
        if (NotificationsUtils.isNotificationEnabled(getContext())) {
            UserConstants.setNotificationRemindUserCenter(false);
        } else {
            UserConstants.setNotificationRemindUserCenter(true);
        }
        if (UserConstants.getNotificationRemindUserCenter()) {
            mBinding.noticeLayout.setVisibility(View.VISIBLE);
            mBinding.noticeLayout.setAnimation(AnimationUtils.makeInAnimation(getContext(), true));

        } else {
            mBinding.noticeLayout.setVisibility(View.GONE);
//            mBinding.noticeLayout.setAnimation(AnimationUtils.makeOutAnimation(getContext(), true));

        }
        setHeight(false);
    }

    @Override
    public void initView() {
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.topLayout);
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.noticeLayout);
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.userDataLayout);


        boolean b = UserConstants.getGoneIntegral();
        if (b) {
            mBinding.integralLayout.setVisibility(View.INVISIBLE);
        } else {
            mBinding.integralLayout.setVisibility(View.VISIBLE);
        }

        //是否隐藏余额
        mViewModel.getOsBalance().observe(this, aBoolean -> {
            if (!aBoolean) {
                UserConstants.setGoneBalance(true);
                mBinding.balanceLineView.setVisibility(View.GONE);
                mBinding.balanceLayout.setVisibility(View.GONE);
            } else {
                UserConstants.setGoneBalance(false);
                mBinding.balanceLineView.setVisibility(View.VISIBLE);
                mBinding.balanceLayout.setVisibility(View.VISIBLE);
            }
        });

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mineTabAdapter = new MineTabAdapter(R.layout.adapter_mine_tab, tabs);
        mBinding.recyclerView.setAdapter(mineTabAdapter);
        mineTabAdapter.setOnItemClickListener((adapter, view, position) -> {
            LoginHelper.login(getActivity(), () -> WebViewActivity.openWebActivity((MainActivity) getActivity(), ((BannerBean) (adapter.getItem(position))).getLink()));

        });

        mBinding.refreshview.setOnRefreshListener(refreshLayout -> {
            getBannerOfPostion();
            if (UserConstants.getIsLogin()) {
                queryUserInfo();
                getMonthEquityInfo();
                getVipInfo();
            } else {
                mBinding.refreshview.finishRefresh();
            }
        });

    }

    @Override
    protected void initListener() {
        mBinding.equityOrderLayout.setOnClickListener(this::onViewClicked);
        mBinding.refuelingOrderLayout.setOnClickListener(this::onViewClicked);
        mBinding.localLifeOrderLayout.setOnClickListener(this::onViewClicked);
        mBinding.messageCenterView.setOnClickListener(this::onViewClicked);
        mBinding.customerServiceView.setOnClickListener(this::onViewClicked);
        mBinding.settingView.setOnClickListener(this::onViewClicked);
        mBinding.nameView.setOnClickListener(this::onViewClicked);
        mBinding.photoView.setOnClickListener(this::onViewClicked);
        mBinding.myCouponLayout.setOnClickListener(this::onViewClicked);
        mBinding.balanceLayout.setOnClickListener(this::onViewClicked);
        mBinding.integralLayout.setOnClickListener(this::onViewClicked);
        mBinding.closeNoticeView.setOnClickListener(this::onViewClicked);
        mBinding.openView.setOnClickListener(this::onViewClicked);
        mBinding.receiveBt.setOnClickListener(this::onViewClicked);
        mBinding.vipReceiveBt.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        LoginHelper.login(mContext, new LoginHelper.CallBack() {
            @Override
            public void onLogin() {
                switch (view.getId()) {
                    case R.id.equity_order_layout:
                        getActivity().startActivity(new Intent(getContext(), OtherOrderListActivity.class).putExtra("isIntegral", true));
                        break;
                    case R.id.refueling_order_layout:
                        getActivity().startActivity(new Intent(getContext(), OrderListActivity.class));
                        break;
                    case R.id.local_life_order_layout:
//                        getActivity().startActivity(new Intent(getContext(), OtherOrderListActivity.class).putExtra("isIntegral", false));
                        getActivity().startActivity(new Intent(getContext(), CarServeOrderListActivity.class));
                        break;
                    case R.id.customer_service_view:
                        if (customerServiceDialog == null) {
                            customerServiceDialog = new CustomerServiceDialog(getBaseActivity());
                        }
                        customerServiceDialog.show(view);
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
                    case R.id.integral_layout:
                        if (userBean == null) {
                            return;
                        }
                        WebViewActivity.openRealUrlWebActivity(getBaseActivity(), userBean.getIntegralBillUrl());
                        break;
                    case R.id.balance_layout:
                        if (userBean == null) {
                            return;
                        }
                        WebViewActivity.openRealUrlWebActivity(getBaseActivity(), userBean.getWalletUrl());

                        break;
                    case R.id.close_notice_view:
                        UserConstants.setNotificationRemindUserCenter(false);
                        mBinding.noticeLayout.setVisibility(View.GONE);
                        mBinding.noticeLayout.setAnimation(AnimationUtils.makeOutAnimation(getContext(), true));
                        break;
                    case R.id.open_view:
                        NotificationsUtils.requestNotify(getContext());
                        break;
                    case R.id.receive_bt:
                        if (userBean == null) {
                            return;
                        }
                        WebViewActivity.openRealUrlWebActivity(getBaseActivity(), userBean.getMonthCardBuyUrl());
                        break;
                    case R.id.vip_receive_bt:
                        String url = "https://tcore.qqgyhk.com/membership?id=1";
                        WebViewActivity.openRealUrlWebActivity(getBaseActivity(), url);
                        break;
                }
            }
        });


    }

    @Override
    protected void dataObservable() {

        mViewModel.userLiveData.observe(this, data -> {
            userBean = data;
            mBinding.refreshview.finishRefresh();
            GlideUtils.loadCircleImage(getContext(), data.getHeadImg(), mBinding.photoView);
            mBinding.nameView.setText(data.getPhone());
            mBinding.userPhoneView.setText(data.getPhone());
            mBinding.couponView.setText(data.getCouponsSize());
            mBinding.integralView.setText(NumberUtils.format(Double.parseDouble(data.getIntegralBalance()), 0));
            mBinding.balanceView.setText(data.getBalance());
//            mBinding.userPhoneView.setVisibility(View.VISIBLE);
//            setHeight(!data.isHasBuyOldMonthCoupon());
            SpanUtils.with(mBinding.couponView)

                    .append("" + data.getCouponsSize())
                    .setBold()
                    .append("张")
                    .setForegroundColor(Color.parseColor("#323334"))
                    .create();
            SpanUtils.with(mBinding.integralView)
                    .append("" + NumberUtils.format(Double.parseDouble(data.getIntegralBalance()), 0))
                    .setBold()
//                    .append("积分")
//                    .setForegroundColor(Color.parseColor("#323334"))
                    .create();
            SpanUtils.with(mBinding.balanceView)
                    .append("" + data.getBalance())
                    .setBold()
                    .append("元")
                    .setForegroundColor(Color.parseColor("#323334"))
                    .create();
            if (TextUtils.isEmpty(data.getMonthCardExpireDate())) {

                SpanUtils.with(mBinding.cardMoneyView)
                        .append("到手立省")
                        .append(data.getTotalDiscountPre() + "元")
                        .setForegroundColor(Color.parseColor("#FE1300"))
                        .create();
                mBinding.receiveBt.setText("立即领卡");
            } else {
                SpanUtils.with(mBinding.cardMoneyView2)
                        .append("已省")
                        .append(data.getMonthCardTotalDiscount() + "元")
                        .setForegroundColor(Color.parseColor("#FE1300"))
                        .create();
                mBinding.receiveBt.setText("前往月卡中心");
                mBinding.cardMoneyView.setText("到期时间：" + data.getMonthCardExpireDate());
            }


        });
        mViewModel.bannersLiveData.observe(this, data -> {
            if (data != null && data.size() > 0) {
                mBinding.moreServiceLayout.setVisibility(View.VISIBLE);
                mineTabAdapter.setNewData(data);
                mBinding.refreshview.finishRefresh();
            } else {
                mBinding.moreServiceLayout.setVisibility(View.GONE);

            }

        });
        mViewModel.monthEquityInfoLiveData.observe(this, data -> {
            if (data != null) {
                if (data.isBuymMonthCard()) {
                    mBinding.cardMoneyView2.setVisibility(View.VISIBLE);
                    mBinding.cardMoneyView.setVisibility(View.VISIBLE);
                } else {
                    mBinding.cardMoneyView2.setVisibility(View.INVISIBLE);
                    mBinding.cardMoneyView.setVisibility(View.VISIBLE);
                }

            }

        });

        mViewModel.vipLiveData.observe(this, new Observer<VipInfoEntity>() {
            @Override
            public void onChanged(VipInfoEntity s) {
                setHeight(true);
                if (TextUtils.isEmpty(s.getUserCardId())){
                    mBinding.vipMoneyView2.setVisibility(View.INVISIBLE);
                    mBinding.vipReceiveBt.setImageResource(R.drawable.vip_join_iv);
                }else {
                    mUserCardId = s.getUserCardId();
                    mBinding.vipMoneyView2.setVisibility(View.VISIBLE);
                    mBinding.vipMoneyView.setText("有效期至：" + s.getExpire());
                    mBinding.vipReceiveBt.setImageResource(R.drawable.vip_look_icon);
                }
            }
        });

        mViewModel.vipInfoLiveData.observe(this, new Observer<VipInfoEntity>() {
            @Override
            public void onChanged(VipInfoEntity s) {
                if (!TextUtils.isEmpty(s.getDescription())){
                    mBinding.vipMoneyView.setText(s.getDescription());
                }
            }
        });
    }

    private void setHeight(boolean isHeight) {
        if (isHeight) {
            ViewGroup.LayoutParams params = mBinding.layout1.getLayoutParams();
            params.height = QMUIDisplayHelper.dpToPx(135);
            mBinding.layout1.setLayoutParams(params);
            ViewGroup.LayoutParams params2 = mBinding.bgView.getLayoutParams();
            params2.height = QMUIDisplayHelper.dpToPx(135);
            mBinding.bgView.setLayoutParams(params2);
            mBinding.bgView.setBackgroundResource(R.drawable.bg_mine_top_4);
            mBinding.vipLayout.setVisibility(View.VISIBLE);
        } else {
            ViewGroup.LayoutParams params = mBinding.layout1.getLayoutParams();
            params.height = QMUIDisplayHelper.dpToPx(70);
            mBinding.layout1.setLayoutParams(params);
            ViewGroup.LayoutParams params2 = mBinding.bgView.getLayoutParams();
            params2.height = QMUIDisplayHelper.dpToPx(70);
            mBinding.bgView.setLayoutParams(params2);
            mBinding.bgView.setBackgroundResource(R.drawable.bg_mine_top_2);
            mBinding.vipLayout.setVisibility(View.GONE);
        }
    }

    private void getBannerOfPostion() {
        mViewModel.getBannerOfPostion();
    }

    private void queryUserInfo() {
        mViewModel.queryUserInfo();
    }

    private void getMonthEquityInfo() {
        mViewModel.getMonthEquityInfo();
    }

    private void getVipInfo(){
        mViewModel.getVip();
        if (TextUtils.isEmpty(mUserCardId)){
            mViewModel.getVipInfo();
        }
    }
}
