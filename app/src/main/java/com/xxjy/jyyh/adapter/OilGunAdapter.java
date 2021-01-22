package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;

import java.util.List;

/**
 * @author power
 * @date 1/22/21 1:11 PM
 * @project ElephantOil
 * @description:
 */
public class OilGunAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public OilGunAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setGone(R.id.item_oil_type_tv, false)
                .setText(R.id.item_oil_num_tv, item);
    }
}
