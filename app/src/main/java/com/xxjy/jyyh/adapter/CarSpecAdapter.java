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
 * @date 2021/6/7 3:32 下午
 * @project ElephantOil
 * @description:
 */
public class CarSpecAdapter extends BaseQuickAdapter<CarServeProductsBean, BaseViewHolder> {

    public CarSpecAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<CarServeProductsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, CarServeProductsBean item) {
        helper.setText(R.id.item_oil_type_tv, item.getCategoryName());
        helper.getView(R.id.item_oil_type_tv).setSelected(item.isSelect());
    }
}
