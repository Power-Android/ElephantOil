package com.xxjy.jyyh.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.utils.GlideUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * @author power
 * @date 1/25/21 2:40 PM
 * @project ElephantOil
 * @description:
 */
public class OilCouponAdapter extends BaseQuickAdapter<CouponBean, BaseViewHolder> {

    public OilCouponAdapter(int layoutResId, @Nullable List<CouponBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CouponBean item) {
        helper.setText(R.id.item_coupon_amount, formatDouble(Double.parseDouble(item.getAmountReduce())))
                .setText(R.id.item_use_range_tv, TextUtils.isEmpty(item.getAmount()) ||
                        Double.parseDouble(item.getAmount()) == 0 ? "无门槛" : String.format("满%s元可用", item.getAmount()))
                .setText(R.id.item_title_tv, item.getName())
                .setText(R.id.item_coupon_date, String.format("%s - %s", item.getStartTime(), item.getEndTime()))
                .addOnClickListener(R.id.rootView);
        GlideUtils.loadImage(mContext, item.getImgUrl(), helper.getView(R.id.image_view));

        switch (item.getStatus()) {
            //0真正可用 1已用 2过期  3时间未到 4 金额未达到
            case 0:
                helper.getView(R.id.mask_view).setVisibility(View.GONE);
                helper.getView(R.id.status_view).setVisibility(View.GONE);
                helper.getView(R.id.rootView).setEnabled(true);
                break;
            case 1:
                helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                helper.getView(R.id.status_view).setVisibility(View.VISIBLE);
                ((ImageView) helper.getView(R.id.status_view)).setImageResource(R.drawable.ic_coupon_used);
                helper.getView(R.id.rootView).setEnabled(false);
                break;
            case 2:
                helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                helper.getView(R.id.status_view).setVisibility(View.VISIBLE);
                ((ImageView) helper.getView(R.id.status_view)).setImageResource(R.drawable.ic_coupon_expired);
                helper.getView(R.id.rootView).setEnabled(false);
                break;
            case 3:
            case 4:
                helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                helper.getView(R.id.status_view).setVisibility(View.GONE);
                helper.getView(R.id.rootView).setEnabled(false);
                break;
        }
    }

    private String formatDouble(double d) {
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        double num = bg.doubleValue();
        if (Math.round(num) - num == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }
}
