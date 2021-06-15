package com.xxjy.jyyh.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.IntegralOrderBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;

import java.util.List;

public class IntegralOrderListAdapter extends BaseQuickAdapter<RefuelOrderBean, BaseViewHolder> {

    public IntegralOrderListAdapter(int layoutResId, @Nullable List<RefuelOrderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RefuelOrderBean item) {

        helper.setText(R.id.item_time_view, item.getCreateTime())
                .setText(R.id.item_status_view, item.getStatusName())
                .setText(R.id.item_price_view, (TextUtils.isEmpty(item.getFinalIntegral()) || Double.parseDouble(item.getFinalIntegral()) == 0d) ? "支付金额:" + item.getFinalPayment() + "元" : "支付金额" + item.getFinalIntegral() + "积分+" + item.getFinalPayment() + "元")
                .setText(R.id.item_title_view, item.getProductName());
        helper.addOnClickListener(R.id.cancel_order_view, R.id.continue_pay_view,R.id.go_coupon_view,R.id.go_detail_view);
        switch (item.getStatus()) {
            case 0:
                helper.getView(R.id.bt_layout).setVisibility(View.VISIBLE);
                helper.getView(R.id.bottom_layout_2).setVisibility(View.GONE);
                break;
            case 1:
                helper.getView(R.id.bt_layout).setVisibility(View.GONE);
                helper.getView(R.id.bottom_layout_2).setVisibility(View.VISIBLE);
                if(item.getTrialType()==2){
                    helper.getView(R.id.go_coupon_view).setVisibility(View.VISIBLE);
                }else{
                    helper.getView(R.id.go_coupon_view).setVisibility(View.GONE);
                }

                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                helper.getView(R.id.bt_layout).setVisibility(View.GONE);
                helper.getView(R.id.bottom_layout_2).setVisibility(View.GONE);
                break;
        }
    }
}
