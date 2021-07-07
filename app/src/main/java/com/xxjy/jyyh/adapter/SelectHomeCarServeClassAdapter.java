package com.xxjy.jyyh.adapter;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.CarServeCategoryBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author power
 * @date 2021/6/7 2:47 下午
 * @project ElephantOil
 * @description:
 */
public class SelectHomeCarServeClassAdapter extends BaseQuickAdapter<CarServeCategoryBean, BaseViewHolder> {

    private int selectPosition=0;
    public SelectHomeCarServeClassAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<CarServeCategoryBean> data) {
        super(layoutResId, data);
    }

    public void setSelectPosition(int position){
        selectPosition=position;
        notifyDataSetChanged();
    }
    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, CarServeCategoryBean item) {
        helper.setText(R.id.title_tv, item.getName());
        if(helper.getAdapterPosition()==selectPosition){
            helper.getView(R.id.title_tv).setBackgroundResource(R.drawable.shape_checked_bt);
            helper.setTextColor(R.id.title_tv, Color.parseColor("#FFFFFF"));
        }else{
            helper.getView(R.id.title_tv).setBackground(null);
            helper.setTextColor(R.id.title_tv, Color.parseColor("#424242"));
        }
    }

}
