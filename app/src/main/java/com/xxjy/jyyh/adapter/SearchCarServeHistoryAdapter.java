package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.IntegralHistoryEntity;
import com.xxjy.jyyh.entity.SearchCarServeHistoryEntity;

import java.util.List;

/**
 * @author power
 * @date 1/31/21 11:32 AM
 * @project ElephantOil
 * @description:
 */
public class SearchCarServeHistoryAdapter extends BaseQuickAdapter<SearchCarServeHistoryEntity, BaseViewHolder> {

    public SearchCarServeHistoryAdapter(int layoutResId, @Nullable List<SearchCarServeHistoryEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SearchCarServeHistoryEntity item) {
        helper.setText(R.id.item_title_tv, item.getStoreName());
    }
}
