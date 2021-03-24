package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.NumberUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.OilDefaultPriceEntity;

import java.util.List;

/**
 * @author power
 * @date 1/22/21 3:45 PM
 * @project ElephantOil
 * @description:
 */
public class OilAmountAdapter extends BaseQuickAdapter<OilDefaultPriceEntity.DefaultAmountBean, BaseViewHolder> {

    public OilAmountAdapter(int layoutResId, @Nullable List<OilDefaultPriceEntity.DefaultAmountBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilDefaultPriceEntity.DefaultAmountBean item) {
        helper.itemView.setSelected(item.isSelected());
        helper.setText(R.id.item_amount_tv, NumberUtils.format(Float.parseFloat(item.getAmount()), 0))
                .setText(R.id.item_discount_tv, "优惠¥" + (item.isSelected() ? item.getAlltotalDiscountAmount() : item.getTotalDiscountAmount()));
        if (item.isSelected()){
            helper.setTextColor(R.id.item_money_tag, mContext.getResources().getColor(R.color.color_76FF))
                    .setTextColor(R.id.item_amount_tv, mContext.getResources().getColor(R.color.color_76FF))
                    .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_76FF));
        }else {
            helper.setTextColor(R.id.item_money_tag, mContext.getResources().getColor(R.color.color_34))
                    .setTextColor(R.id.item_amount_tv, mContext.getResources().getColor(R.color.color_34))
                    .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_50));
        }
    }
}
