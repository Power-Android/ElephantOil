package com.xxjy.jyyh.ui.order;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;

import androidx.lifecycle.ViewModelProvider;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.BarUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.PayTypeConstants;
import com.xxjy.jyyh.databinding.ActivityOrderDetailsBinding;
import com.xxjy.jyyh.dialog.CustomerServiceDialog;
import com.xxjy.jyyh.dialog.SelectPayDialog;
import com.xxjy.jyyh.entity.OilPayTypeEntity;
import com.xxjy.jyyh.entity.PayOrderEntity;
import com.xxjy.jyyh.entity.RefuelOrderBean;
import com.xxjy.jyyh.ui.home.HomeViewModel;
import com.xxjy.jyyh.ui.pay.PayQueryActivity;
import com.xxjy.jyyh.ui.pay.RefuelingPayResultActivity;
import com.xxjy.jyyh.ui.web.WeChatWebPayActivity;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.WXSdkManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDetailsActivity extends BindingActivity<ActivityOrderDetailsBinding, OrderDetailsViewModel> {

    public static final String ORDER_ID = "order_id";
    public static final String CONTINUE_PAY = "continue_pay";

    private QMUIPopup mNormalPopup;

    private String orderId;
    private boolean isContinuePay = false;
    private SelectPayDialog mOilPayDialog;

    private HomeViewModel homeViewModel;
    //是否需要跳转支付确认页
    private boolean shouldJump = false;
    private PayOrderEntity mPayOrderEntity;
    private boolean isShouldAutoOpenWeb = false;    //标记是否应该自动打开浏览器进行支付

    private RefuelOrderBean refuelOrderBean;


    @Override
    protected void initView() {
        mBinding.titleLayout.tvTitle.setText("订单详情");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);
//        mBinding.useMethodView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        mBinding.useMethodView.getPaint().setAntiAlias(true);
        orderId = getIntent().getStringExtra(ORDER_ID);
        isContinuePay = getIntent().getBooleanExtra(CONTINUE_PAY, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        refuelOrderDetails();
        initWebViewClient();
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
                    CustomerServiceDialog customerServiceDialog = new CustomerServiceDialog(this);
                    customerServiceDialog.show(view);

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
                if (refuelOrderBean != null) {
                    showPayDialog(refuelOrderBean.getProductName(), refuelOrderBean.getOrderId(), refuelOrderBean.getPayAmount());
                }
                break;
            case R.id.cancel_view:
                cancelOrder();
                break;
        }
    }

    @Override
    protected void dataObservable() {
        mViewModel.refuelOrderDetailsLiveData.observe(this, data -> {
            refuelOrderBean = data;
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

            if (isContinuePay && data.getStatus() == 0) {
                isContinuePay=false;
                showPayDialog(refuelOrderBean.getProductName(), refuelOrderBean.getOrderId(), refuelOrderBean.getPayAmount());
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

        //支付结果回调
        homeViewModel.payOrderLiveData.observe(this, payOrderEntity -> {
            if (payOrderEntity.getResult() == 0) {//支付未完成
                switch (payOrderEntity.getPayType()) {
                    case PayTypeConstants.PAY_TYPE_WEIXIN://微信H5
                        WeChatWebPayActivity.openWebPayAct(this, payOrderEntity.getUrl());
                        break;
                    case PayTypeConstants.PAY_TYPE_WEIXIN_XCX://微信小程序
                        WXSdkManager.newInstance().useWXLaunchMiniProgramToPay(this, payOrderEntity.getOrderNo());
                        break;
                    case PayTypeConstants.PAY_TYPE_ZHIFUBAO://支付宝H5
                        boolean urlCanUse = UiUtils.checkZhifubaoSdkCanPayUrl(this,
                                payOrderEntity.getUrl(),
                                h5PayResultModel -> {//直接跳转支付宝
                                    jumpToPayResultAct(payOrderEntity.getOrderPayNo(),
                                            payOrderEntity.getOrderNo());
                                });
                        if (!urlCanUse) {//外部浏览器打开
                            isShouldAutoOpenWeb = true;
                            mBinding.payWebView.loadUrl(payOrderEntity.getUrl());
                            mBinding.payWebView.post(() -> {
                                if (isShouldAutoOpenWeb) {
                                    UiUtils.openPhoneWebUrl(OrderDetailsActivity.this,
                                            payOrderEntity.getUrl());
                                }
                            });
                        }
                        break;
                }
//                BusUtils.postSticky(EventConstants.EVENT_JUMP_PAY_QUERY, payOrderEntity);
                mPayOrderEntity = payOrderEntity;
                shouldJump = true;
            } else if (payOrderEntity.getResult() == 1) {//支付成功
                jumpToPayResultAct(payOrderEntity.getOrderPayNo(), payOrderEntity.getOrderNo());
            } else {
                showToastWarning(payOrderEntity.getMsg());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showJump(mPayOrderEntity);
        refuelOrderDetails();
    }

    private void showPayDialog(String title, String orderId, String payAmount) {
        //支付dialog
        mOilPayDialog = new SelectPayDialog(this, title, orderId, payAmount);
        mOilPayDialog.setOnItemClickedListener(new SelectPayDialog.OnItemClickedListener() {
            @Override
            public void onOilPayTypeClick(BaseQuickAdapter adapter, View view, int position) {
                List<OilPayTypeEntity> data = adapter.getData();
                for (OilPayTypeEntity item : data) {
                    item.setSelect(false);
                }
                data.get(position).setSelect(true);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCloseAllClick() {
                closeDialog();
            }

            @Override
            public void onPayOrderClick(String payType, String orderId, String payAmount) {
                homeViewModel.payOrder(payType, orderId, payAmount);
            }
        });

        mOilPayDialog.show();
    }

    protected void initWebViewClient() {
        mBinding.payWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(final WebView webView, String url) {
                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true;
                }

                isShouldAutoOpenWeb = false;
                /**
                 * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
                 */
                final PayTask task = new PayTask(OrderDetailsActivity.this);
                boolean isIntercepted = task.payInterceptorWithUrl(url, true,
                        result -> runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                jumpToPayResultAct(result.getResultCode(), result.getResultCode());
                            }
                        }));

                /**
                 * 判断是否成功拦截
                 * 若成功拦截，则无需继续加载该URL；否则继续加载
                 */
                if (!isIntercepted) {   //如果不使用sdk直接将url抛出到浏览器
                    UiUtils.openPhoneWebUrl(OrderDetailsActivity.this, url);
                }
                return true;
            }
        });
    }

    private void showJump(PayOrderEntity orderEntity) {
        if (orderEntity == null) return;
        if (shouldJump) {
            shouldJump = false;
            PayQueryActivity.openPayQueryActivity(this,
                    orderEntity.getOrderPayNo(), orderEntity.getOrderNo());
            closeDialog();
        }
    }

    private void closeDialog() {
        if (mOilPayDialog != null) {
            mOilPayDialog.dismiss();
            mOilPayDialog = null;
        }
    }

    private void jumpToPayResultAct(String orderPayNo, String orderNo) {
        if (TextUtils.isEmpty(orderPayNo) && TextUtils.isEmpty(orderNo)) {
            return;
        }
        Intent intent = new Intent(this, RefuelingPayResultActivity.class);
        intent.putExtra("orderPayNo", orderPayNo);
        intent.putExtra("orderNo", orderNo);
        startActivity(intent);
        closeDialog();
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

    public static void openPage(BaseActivity activity, String orderId, boolean isContinuePay) {
        Intent intent = new Intent(activity, OrderDetailsActivity.class);
        intent.putExtra(ORDER_ID, orderId);
        intent.putExtra(CONTINUE_PAY, isContinuePay);
        activity.startActivity(intent);
    }

}