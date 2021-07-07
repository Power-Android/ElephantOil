package com.xxjy.jyyh.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.CarServeCategoryBean;
import com.xxjy.jyyh.entity.CarTypeBean;

import java.util.List;

public class SelectCarTypeAdapter extends BaseQuickAdapter<CarTypeBean, BaseViewHolder> {

    public SelectCarTypeAdapter(int layoutResId, @Nullable List<CarTypeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CarTypeBean item) {
        ImageView view = helper.getView(R.id.item_name);

        switch (item.getValue()){
            case 1:
                view.setBackgroundResource(R.drawable.selector_car_type);
                break;
            case 2:
                view.setBackgroundResource(R.drawable.selector_car_suv_type);
                break;
            default:
                view.setBackgroundResource(R.drawable.selector_car_type);
                break;
        }
        view.setSelected(item.isChecked());
    }
}
