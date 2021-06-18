package com.xxjy.jyyh.adapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.OftenCarsEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author power
 * @date 2021/6/18 2:04 下午
 * @project ElephantOil
 * @description:
 */
public class HomeOftenCarsAdapter extends BaseQuickAdapter<OftenCarsEntity, BaseViewHolder> {

    public HomeOftenCarsAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<OftenCarsEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, OftenCarsEntity item) {
        helper.setTextColor(R.id.item_title_tv, helper.getAdapterPosition() == 0 ?
                mContext.getResources().getColor(R.color.color_34) :
                mContext.getResources().getColor(R.color.color_76FF));
        TextView titleTv = helper.getView(R.id.item_title_tv);
        if (helper.getAdapterPosition() == 0 || helper.getAdapterPosition() == getItemCount() - 1){
            titleTv.setText(item.getCardStoreInfoVo().getStoreName());
        }else {
            SpanUtils.with(titleTv)
                    .append(item.getCardStoreInfoVo().getStoreName())
                    .setForegroundColor(mContext.getResources().getColor(R.color.color_76FF))
                    .append("、")
                    .setForegroundColor(mContext.getResources().getColor(R.color.color_34))
                    .create();
        }
    }
}
