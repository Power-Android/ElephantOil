package com.xxjy.jyyh.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.DistanceEntity;

import java.util.List;

public class OilDistanceAdapter extends BaseQuickAdapter<DistanceEntity, BaseViewHolder> {

    public OilDistanceAdapter(int layoutResId, @Nullable List<DistanceEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DistanceEntity item) {
        TextView textView = helper.getView(R.id.item_name);
        textView.setText(item.getTitle());
        textView.setSelected(item.isChecked());
    }
}
