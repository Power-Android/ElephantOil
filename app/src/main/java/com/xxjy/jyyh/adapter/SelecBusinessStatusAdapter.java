package com.xxjy.jyyh.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.BusinessStatusBean;
import com.xxjy.jyyh.entity.DistanceEntity;

import java.util.List;

public class SelecBusinessStatusAdapter extends BaseQuickAdapter<BusinessStatusBean, BaseViewHolder> {

    public SelecBusinessStatusAdapter(int layoutResId, @Nullable List<BusinessStatusBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BusinessStatusBean item) {
        TextView textView = helper.getView(R.id.item_name);
        textView.setText(item.getName());
        textView.setSelected(item.isChecked());
    }
}
