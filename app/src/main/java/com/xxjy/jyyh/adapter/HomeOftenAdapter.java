package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;

import java.util.List;

/**
 * @author power
 * @date 1/21/21 7:34 PM
 * @project ElephantOil
 * @description:
 */
public class HomeOftenAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public HomeOftenAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setTextColor(R.id.item_title_tv, helper.getAdapterPosition() == 0 ?
                mContext.getResources().getColor(R.color.color_34) :
                mContext.getResources().getColor(R.color.color_76FF));
        helper.setText(R.id.item_title_tv, item);
    }
}
