package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author power
 * @date 1/23/21 7:30 PM
 * @project ElephantOil
 * @description:
 */
public class SearchRecommendAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SearchRecommendAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {

    }


}
