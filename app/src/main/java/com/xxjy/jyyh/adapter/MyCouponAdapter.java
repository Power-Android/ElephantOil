package com.xxjy.jyyh.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.utils.GlideUtils;

import java.util.List;

public class MyCouponAdapter extends BaseMultiItemQuickAdapter<CouponBean, BaseViewHolder> {

    public MyCouponAdapter(int layoutResId, @Nullable List<CouponBean> data) {
        super(data);
        addItemType(0, R.layout.adapter_my_coupon);
        addItemType(1, R.layout.adapter_my_coupon_integral);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CouponBean item) {

        switch (helper.getItemViewType()) {
            case 0:
                helper.setText(R.id.item_coupon_amount, item.getAmountReduce())
                        .setText(R.id.item_use_range_tv, TextUtils.isEmpty(item.getAmount()) || Double.parseDouble(item.getAmount()) == 0 ? "无门槛" : String.format("满%s元可用", item.getAmount()))
                        .setText(R.id.item_title_tv, item.getName())
                        .setText(R.id.item_coupon_date, String.format("%s - %s", item.getStartTime(), item.getEndTime()));
                GlideUtils.loadImage(mContext, item.getImgUrl(), helper.getView(R.id.image_view));

                switch (item.getStatus()) {
                    //0真正可用 1已用 2过期  3时间未到 4 金额未达到
                    case 0:
                    case 3:
                    case 4:
                        helper.getView(R.id.mask_view).setVisibility(View.GONE);
                        helper.getView(R.id.status_view).setVisibility(View.GONE);
                        break;
                    case 1:
                        helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                        helper.getView(R.id.status_view).setVisibility(View.VISIBLE);
                        ((ImageView) helper.getView(R.id.status_view)).setImageResource(R.drawable.ic_coupon_used);
                        break;
                    case 2:
                        helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                        helper.getView(R.id.status_view).setVisibility(View.VISIBLE);
                        ((ImageView) helper.getView(R.id.status_view)).setImageResource(R.drawable.ic_coupon_expired);
                        break;
//            case 3:
//                helper.getView(R.id.mask_view).setVisibility(View.GONE);
//                helper.getView(R.id.status_view).setVisibility(View.GONE);
//                break;
//            case 4:
//                helper.getView(R.id.mask_view).setVisibility(View.GONE);
//                helper.getView(R.id.status_view).setVisibility(View.GONE);
//                break;
                }
                break;
            case 1:

                SpanUtils.with(helper.getView(R.id.item_coupon_amount))
                        .append(format(item.getPointReduce()))
                        .setFontSize(16, true)
                        .append("积分")
                        .setFontSize(13, true)
                        .create();
//                helper.setText(R.id.item_coupon_amount,format(item.getPointReduce()))
                helper.setText(R.id.item_use_range_tv, TextUtils.isEmpty(item.getPoint()) || Double.parseDouble(item.getPoint()) == 0 ? "无门槛" : String.format("满%s积分可用", item.getPoint()))
                        .setText(R.id.item_title_tv, item.getName())
                        .setText(R.id.item_coupon_date, String.format("%s - %s", item.getStartTime(), item.getEndTime()));
                GlideUtils.loadImage(mContext, item.getImgUrl(), helper.getView(R.id.image_view));

                switch (item.getStatus()) {
                    //0真正可用 1已用 2过期  3时间未到 4 金额未达到
                    case 0:
                    case 3:
                    case 4:
                        helper.getView(R.id.mask_view).setVisibility(View.GONE);
                        helper.getView(R.id.status_view).setVisibility(View.GONE);
                        break;
                    case 1:
                        helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                        helper.getView(R.id.status_view).setVisibility(View.VISIBLE);
                        ((ImageView) helper.getView(R.id.status_view)).setImageResource(R.drawable.ic_coupon_used);
                        break;
                    case 2:
                        helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                        helper.getView(R.id.status_view).setVisibility(View.VISIBLE);
                        ((ImageView) helper.getView(R.id.status_view)).setImageResource(R.drawable.ic_coupon_expired);
                        break;
//            case 3:
//                helper.getView(R.id.mask_view).setVisibility(View.GONE);
//                helper.getView(R.id.status_view).setVisibility(View.GONE);
//                break;
//            case 4:
//                helper.getView(R.id.mask_view).setVisibility(View.GONE);
//                helper.getView(R.id.status_view).setVisibility(View.GONE);
//                break;
                }

                break;
        }


    }


    private String format(String content) {
        try {
            if (TextUtils.isEmpty(content)) {
                return "0";
            }
            int n = Integer.parseInt(content);
            if (n >= 10000) {
                if (n % 10000 == 0) {
                    return n / 10000 + "万";
                } else {
                    return n / 10000d + "万";
                }
            } else {
                return content;
            }

        } catch (NumberFormatException e) {
            return content;
        }

    }
}
