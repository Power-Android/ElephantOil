package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.IntegralHistoryEntity;

import java.util.List;

/**
 * @author power
 * @date 1/31/21 11:32 AM
 * @project ElephantOil
 * @description:
 */
public class IntegralHistoryAdapter extends BaseQuickAdapter<IntegralHistoryEntity, BaseViewHolder> {

    public IntegralHistoryAdapter(int layoutResId, @Nullable List<IntegralHistoryEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, IntegralHistoryEntity item) {
        helper.setText(R.id.item_title_tv, item.getIntegralName());
    }
}
