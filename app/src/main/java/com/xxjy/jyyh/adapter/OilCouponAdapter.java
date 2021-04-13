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
import com.xxjy.jyyh.utils.StringUtils;
import com.xxjy.jyyh.utils.Util;

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
        helper.setText(R.id.item_coupon_amount, StringUtils.checkPointDouble(item.getAmountReduce()))
                .setText(R.id.item_use_range_tv, TextUtils.isEmpty(item.getAmount()) ||
                        Double.parseDouble(item.getAmount()) == 0 ? "无门槛" : String.format("满%s元可用", Util.formatDouble(Double.parseDouble(item.getAmount()))))
                .setText(R.id.item_title_tv, item.getName())
                .setText(R.id.item_coupon_date, String.format("%s - %s", item.getStartTime(), item.getEndTime()))
                .addOnClickListener(R.id.rootView);
        GlideUtils.loadImage(mContext, item.getImgUrl(), helper.getView(R.id.image_view));
        if (item.isSelected()) {
            helper.getView(R.id.line_layout).setVisibility(View.VISIBLE);
            helper.getView(R.id.separate).setBackground(mContext.getResources().getDrawable(R.drawable.ic_coupon_line_sel));
        } else {
            helper.getView(R.id.line_layout).setVisibility(View.GONE);
            helper.getView(R.id.separate).setBackground(mContext.getResources().getDrawable(R.drawable.ic_coupon_line));
        }
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
            case 5:
                helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                helper.getView(R.id.status_view).setVisibility(View.GONE);
                helper.getView(R.id.rootView).setEnabled(false);
                break;
            default:
                helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                helper.getView(R.id.status_view).setVisibility(View.GONE);
                helper.getView(R.id.rootView).setEnabled(false);
                break;
        }
    }

    public void setChecked(String id) {
        for (int i = 0; i < getData().size(); i++) {
            if (id.equals(getData().get(i).getId())) {
                getData().get(i).setSelected(true);
            } else {
                getData().get(i).setSelected(false);
            }
        }
        notifyDataSetChanged();
    }
}
