package com.xxjy.jyyh.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.AreaBean;
import com.xxjy.jyyh.entity.CarServeCategoryBean;

import java.util.List;

public class SelectProductCategoryAdapter extends BaseQuickAdapter<CarServeCategoryBean, BaseViewHolder> {

    public SelectProductCategoryAdapter(int layoutResId, @Nullable List<CarServeCategoryBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CarServeCategoryBean item) {
        TextView textView = helper.getView(R.id.item_name);
        textView.setText(item.getName());
        textView.setSelected(item.isChecked());
    }
}
