package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.CarCouponEntity;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

/**
 * @author power
 * @date 2021/6/11 1:21 下午
 * @project ElephantOil
 * @description:
 */
public class MyCarServeAdapter extends BaseQuickAdapter<CarCouponEntity.RecordsBean, BaseViewHolder> {

    public MyCarServeAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<CarCouponEntity.RecordsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, CarCouponEntity.RecordsBean item) {
        helper.setText(R.id.item_title_tv, item.getTitle())
                .setText(R.id.item_status_tv, item.getCouponStatus() == 0 ? "可使用" : "未生效")
                .setText(R.id.item_date_tv, item.getStartTime() + "-" + item.getEndTime())
                .addOnClickListener(R.id.item_use_tv);
        Date date = TimeUtils.string2Date(item.getStartTime());
        long startMill = TimeUtils.date2Millis(date);
        if (startMill - TimeUtils.getNowMills() > 0){
            helper.setText(R.id.item_status_tv, "未生效")
                    .setVisible(R.id.item_use_tv, false);
        }else {
            helper.setText(R.id.item_status_tv, "可使用")
                    .setVisible(R.id.item_use_tv, true);
        }
    }
}
