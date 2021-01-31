package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.SearchHistoryEntity;

import java.util.List;

/**
 * @author power
 * @date 1/23/21 7:44 PM
 * @project ElephantOil
 * @description:
 */
public class SearchHistoryAdapter extends BaseQuickAdapter<SearchHistoryEntity, BaseViewHolder> {

    public SearchHistoryAdapter(int layoutResId, @Nullable List<SearchHistoryEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchHistoryEntity item) {
        helper.setText(R.id.item_title_tv, item.getGasName());
    }
}
