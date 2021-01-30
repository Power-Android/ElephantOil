package com.xxjy.jyyh.adapter;

import android.graphics.Color;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.OfentEntity;

import java.util.List;

/**
 * @author power
 * @date 1/21/21 7:34 PM
 * @project ElephantOil
 * @description:
 */
public class HomeOftenAdapter extends BaseQuickAdapter<OfentEntity, BaseViewHolder> {

    public HomeOftenAdapter(int layoutResId, @Nullable List<OfentEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OfentEntity item) {
        helper.setTextColor(R.id.item_title_tv, helper.getAdapterPosition() == 0 ?
                mContext.getResources().getColor(R.color.color_34) :
                mContext.getResources().getColor(R.color.color_76FF));
        TextView titleTv = helper.getView(R.id.item_title_tv);
        if (helper.getAdapterPosition() == 0 || helper.getAdapterPosition() == getItemCount() - 1){
            titleTv.setText(item.getGasName());
        }else {
            SpanUtils.with(titleTv)
                    .append(item.getGasName())
                    .setForegroundColor(mContext.getResources().getColor(R.color.color_76FF))
                    .append("、")
                    .setForegroundColor(mContext.getResources().getColor(R.color.color_34))
                    .create();
        }
    }
}
