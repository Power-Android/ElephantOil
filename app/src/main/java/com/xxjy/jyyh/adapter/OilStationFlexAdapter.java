package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;

import java.util.List;

/**
 * @author power
 * @date 12/3/20 11:46 AM
 * @project RunElephant
 * @description:
 */
public class OilStationFlexAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public OilStationFlexAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.item_title_tv, item);
    }
}
