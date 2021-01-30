package com.xxjy.jyyh.ui.order;


import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.blankj.utilcode.util.BarUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityOrderDetailsBinding;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.ui.pay.PayResultActivity;
import com.xxjy.jyyh.ui.pay.RefuelingPayResultActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDetailsActivity extends BindingActivity<ActivityOrderDetailsBinding, OrderDetailsViewModel> {

    public static final String ORDER_ID = "order_id";

    private QMUIPopup mNormalPopup;

    private String orderId;

    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("订单详情");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);
//        mBinding.useMethodView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        mBinding.useMethodView.getPaint().setAntiAlias(true);
        orderId = getIntent().getStringExtra(ORDER_ID);
        refuelOrderDetails();
    }

    @Override
    protected void initListener() {
        mBinding.continuePayView.setOnClickListener(this::onViewClicked);
        mBinding.orderManageLayout.setOnClickListener(this::onViewClicked);
        mBinding.cancelView.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_manage_layout:
                String[] listItems = new String[]{
                        "申请退款",
                        "开取发票",
                };
                List<String> data = new ArrayList<>();

                Collections.addAll(data, listItems);
                ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.adapter_order_manage_layout, data);
                AdapterView.OnItemClickListener onItemClickListener = (adapterView, view1, i, l) -> {
                    if (mNormalPopup != null) {
                        mNormalPopup.dismiss();
                    }
                };
                mNormalPopup = QMUIPopups.listPopup(this,
                        QMUIDisplayHelper.dp2px(this, 70),
                        QMUIDisplayHelper.dp2px(this, 300),
                        adapter,
                        onItemClickListener)
                        .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                        .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                        .arrowSize(QMUIDisplayHelper.dp2px(this, 13),
                                QMUIDisplayHelper.dp2px(this, 7))
                        .shadow(true)
                        .offsetYIfTop(QMUIDisplayHelper.dp2px(this, 5))
                        .skinManager(QMUISkinManager.defaultInstance(this))
                        .radius(QMUIDisplayHelper.dp2px(this, 4))
                        .borderWidth(0)
                        .onDismiss(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                            }
                        })
                        .show(view);

                break;
            case R.id.continue_pay_view:
                startActivity(new Intent(this, RefuelingPayResultActivity.class));
                break;
            case R.id.cancel_view:

//                startActivity(new Intent(this, PayResultActivity.class));
                cancelOrder();
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.refuelOrderDetailsLiveData.observe(this, data -> {
            mBinding.stationNameView.setText(data.getProductName());
            mBinding.statusView.setText(data.getStatusName());
            mBinding.numView.setText(data.getLitre() + "L");
            mBinding.amountView.setText("¥" + data.getAmount());
            mBinding.businessDirectDiscountView.setText("-¥" + data.getCzbDepreciateAmount());
            mBinding.businessDiscountView.setText("-¥" + data.getCzbCouponAmount());
            mBinding.balanceView.setText("-¥" + data.getUsedBalance());
            mBinding.platformDiscountView.setText("-¥" + data.getAmountCoupon());
            mBinding.orderIdView.setText(data.getOrderId());
            mBinding.payTypeView.setText(data.getPayTypeName());
            mBinding.payAmountView.setText("¥" + data.getPayAmount());

            switch (data.getStatus()) {
                case 0:
                    mBinding.orderManageLayout.setVisibility(View.GONE);
                    mBinding.btLayout.setVisibility(View.VISIBLE);
                    mBinding.businessDirectDiscountLayout.setVisibility(View.VISIBLE);
                    mBinding.businessDiscountLayout.setVisibility(View.VISIBLE);
                    mBinding.platformDiscountLayout.setVisibility(View.VISIBLE);
                    mBinding.balanceLayout.setVisibility(View.VISIBLE);
                    mBinding.payTypeLayout.setVisibility(View.GONE);
                    mBinding.payAmountLayout.setVisibility(View.GONE);
                    break;
                case 1:
                    mBinding.orderManageLayout.setVisibility(View.VISIBLE);
                    mBinding.btLayout.setVisibility(View.GONE);
                    mBinding.businessDirectDiscountLayout.setVisibility(View.VISIBLE);
                    mBinding.businessDiscountLayout.setVisibility(View.VISIBLE);
                    mBinding.platformDiscountLayout.setVisibility(View.VISIBLE);
                    mBinding.balanceLayout.setVisibility(View.VISIBLE);
                    mBinding.payTypeLayout.setVisibility(View.VISIBLE);
                    mBinding.payAmountLayout.setVisibility(View.VISIBLE);
                    break;

                case 2:
                    mBinding.orderManageLayout.setVisibility(View.GONE);
                    mBinding.btLayout.setVisibility(View.GONE);
                    mBinding.businessDirectDiscountLayout.setVisibility(View.GONE);
                    mBinding.businessDiscountLayout.setVisibility(View.GONE);
                    mBinding.platformDiscountLayout.setVisibility(View.GONE);
                    mBinding.balanceLayout.setVisibility(View.GONE);
                    mBinding.payTypeLayout.setVisibility(View.GONE);
                    mBinding.payAmountLayout.setVisibility(View.GONE);

                    break;
                default:
                    mBinding.orderManageLayout.setVisibility(View.GONE);
                    mBinding.btLayout.setVisibility(View.GONE);
                    mBinding.businessDirectDiscountLayout.setVisibility(View.GONE);
                    mBinding.businessDiscountLayout.setVisibility(View.GONE);
                    mBinding.platformDiscountLayout.setVisibility(View.GONE);
                    mBinding.balanceLayout.setVisibility(View.GONE);
                    mBinding.payTypeLayout.setVisibility(View.GONE);
                    mBinding.payAmountLayout.setVisibility(View.GONE);
                    break;
            }
        });

        mViewModel.cancelOrderDetailsLiveData.observe(this, data -> {

            if (data.getCode() == 1) {
                showToastSuccess("取消成功");
                refuelOrderDetails();
            } else {
                showToastError("取消失败");
            }

        });
    }


    private void refuelOrderDetails() {
        mViewModel.refuelOrderDetails(orderId);
    }

    private void cancelOrder() {
        mViewModel.cancelOrder(orderId);
    }


    public static void openPage(BaseActivity activity, String orderId) {
        Intent intent = new Intent(activity, OrderDetailsActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        activity.startActivity(intent);
    }

}