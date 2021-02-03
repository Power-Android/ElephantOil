package com.xxjy.jyyh.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;

import java.util.List;

/**
 * @author power
 * @date 1/23/21 12:16 PM
 * @project ElephantOil
 * @description:
 */
public class OilCheckedAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public OilCheckedAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.item_content_tv, item);
    }
}
