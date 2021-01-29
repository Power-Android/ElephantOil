package com.xxjy.jyyh.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.IntegralOrderBean;
import com.xxjy.jyyh.entity.RefuelOrderBean;

import java.util.List;

public class IntegralOrderListAdapter extends BaseQuickAdapter<IntegralOrderBean, BaseViewHolder> {

    public IntegralOrderListAdapter(int layoutResId, @Nullable List<IntegralOrderBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, IntegralOrderBean item) {

        helper.setText(R.id.item_time_view, item.getCreateTime())
                .setText(R.id.item_status_view, item.getStatusName())
                .setText(R.id.item_price_view, item.getFinalIntegral()+"积分+" + item.getFinalPayment()+"元")
                .setText(R.id.item_title_view, item.getProductName());

        switch (item.getStatus()) {
            case 0:
                helper.getView(R.id.bt_layout).setVisibility(View.VISIBLE);
                break;
            case 1:
                helper.getView(R.id.bt_layout).setVisibility(View.GONE);
                break;
            case 2:
                helper.getView(R.id.bt_layout).setVisibility(View.GONE);
                break;
            case 3:
                helper.getView(R.id.bt_layout).setVisibility(View.GONE);
                break;
            case 4:
                helper.getView(R.id.bt_layout).setVisibility(View.GONE);
                break;
            case 5:
                helper.getView(R.id.bt_layout).setVisibility(View.GONE);
                break;
            case 6:
                helper.getView(R.id.bt_layout).setVisibility(View.GONE);
                break;
        }
    }
}
