package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.CarServeProductsBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author power
 * @date 2021/6/7 3:50 下午
 * @project ElephantOil
 * @description:
 */
public class CarPriceAdapter extends BaseQuickAdapter<CarServeProductsBean, BaseViewHolder> {

    public CarPriceAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<CarServeProductsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, CarServeProductsBean item) {
        helper.setText(R.id.item_orin_price_tv, "¥" + item.getLinePrice())
                .setText(R.id.item_price_tv, "¥" + item.getSalePrice());
        helper.getView(R.id.item_car_price_cl).setSelected(true);
    }
}
