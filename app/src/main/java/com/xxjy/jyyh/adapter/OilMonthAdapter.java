package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.NumberUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.MonthCouponEntity;

import java.util.List;

/**
 * @author power
 * @date 3/17/21 3:33 PM
 * @project ElephantOil
 * @description:
 */
public class OilMonthAdapter extends BaseQuickAdapter<MonthCouponEntity.MonthCouponTemplatesBean, BaseViewHolder> {
    private boolean isSelected = false;

    public OilMonthAdapter(int layoutResId, @Nullable List<MonthCouponEntity.MonthCouponTemplatesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MonthCouponEntity.MonthCouponTemplatesBean item) {
        helper.setText(R.id.item_money_tag, NumberUtils.format(Double.parseDouble(item.getAmountReduce()), 0))
                .setText(R.id.item_content_tv, "满" +
                        NumberUtils.format(Double.parseDouble(item.getAmount()), 0)
                        + "可用");
        if (isSelected && item.getMonthCouponDefault()) {
            helper.setImageResource(R.id.item_bg, R.drawable.month_red_select);
            helper.setText(R.id.item_content_tv, "本单可用");
        } else {
            helper.setImageResource(R.id.item_bg, R.drawable.month_red_def);
        }
    }

    public void isSelected(boolean b) {//月度红包是否勾选
        this.isSelected = b;
        notifyDataSetChanged();
    }
}
