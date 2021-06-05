package com.xxjy.jyyh.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.CarServeOrderBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;

import java.util.List;

public class CarServeOrderListAdapter extends BaseQuickAdapter<CarServeOrderBean, BaseViewHolder> {

    public CarServeOrderListAdapter(int layoutResId, @Nullable List<CarServeOrderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CarServeOrderBean item) {

        helper.setText(R.id.item_time_view, item.getExpireTime())
                .setText(R.id.item_status_view, item.getOrderStatusName())
                .setText(R.id.item_price_view, item.getProductPrice() + "元")
                .setText(R.id.item_title_view, item.getProductName())
                .setText(R.id.item_shop_name_view, item.getStoreName());
        helper.addOnClickListener(R.id.cancel_order_view, R.id.continue_pay_view,R.id.refund_view,R.id.check_coupon_code_view);

       // 0：待支付，1：支付成功，2：支付失败，3：已取消，4：已退款，5：退款中 6:退款失败
        switch (item.getOrderStatus()) {
            case 0:
                helper.getView(R.id.bt_layout).setVisibility(View.VISIBLE);
                helper.getView(R.id.bt_2_layout).setVisibility(View.GONE);
                break;
            case 1:
                helper.setText(R.id.item_status_view, item.getVerificationStatusName());
                if(item.getVerificationStatus()==1){
                    helper.getView(R.id.bt_2_layout).setVisibility(View.VISIBLE);
                }else{
                    helper.getView(R.id.bt_2_layout).setVisibility(View.GONE);
                }
                helper.getView(R.id.bt_layout).setVisibility(View.GONE);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                helper.getView(R.id.bt_2_layout).setVisibility(View.GONE);
                helper.getView(R.id.bt_layout).setVisibility(View.GONE);
                break;
        }
    }
}
