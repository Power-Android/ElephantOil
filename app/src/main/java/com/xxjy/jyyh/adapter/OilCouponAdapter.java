package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author power
 * @date 1/25/21 2:40 PM
 * @project ElephantOil
 * @description:
 */
public class OilCouponAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public OilCouponAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        //TODO 不可用优惠券透明度0.7
    }
}
