package com.xxjy.jyyh.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.AreaBean;

import java.util.List;

public class SelectAreaAdapter extends BaseQuickAdapter<AreaBean, BaseViewHolder> {

    public SelectAreaAdapter(int layoutResId, @Nullable List<AreaBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, AreaBean item) {
        TextView textView = helper.getView(R.id.item_name);
        textView.setText(item.getArea());
        textView.setSelected(item.isChecked());
    }
}
