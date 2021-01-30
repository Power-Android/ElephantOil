package com.xxjy.jyyh.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.CouponBean;
import com.xxjy.jyyh.utils.GlideUtils;

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
        helper.setText(R.id.item_coupon_amount,item.getAmountReduce())
                .setText(R.id.item_use_range_tv,String.format("满%s元可用",item.getAmount()))
                .setText(R.id.item_title_tv,item.getName())
                .setText(R.id.item_coupon_date,String.format("%s 至 %s",item.getStartTime(),item.getEndTime()))
                .addOnClickListener(R.id.rootView);
        GlideUtils.loadImage(mContext,item.getImgUrl(),helper.getView(R.id.image_view));

        switch (item.getStatus()){
            //0真正可用 1已用 2过期  3时间未到 4 金额未达到
            case 0:
                helper.getView(R.id.rootView).setAlpha(1.0f);
                helper.getView(R.id.status_view).setVisibility(View.GONE);
                helper.getView(R.id.rootView).setEnabled(true);
                break;
            case 1:
                helper.getView(R.id.rootView).setAlpha(0.6f);
                helper.getView(R.id.status_view).setVisibility(View.VISIBLE);
                ((ImageView)helper.getView(R.id.status_view)).setImageResource(R.drawable.ic_coupon_used);
                helper.getView(R.id.rootView).setEnabled(false);
                break;
            case 2:
                helper.getView(R.id.rootView).setAlpha(0.6f);
                helper.getView(R.id.status_view).setVisibility(View.VISIBLE);
                ((ImageView)helper.getView(R.id.status_view)).setImageResource(R.drawable.ic_coupon_expired);
                helper.getView(R.id.rootView).setEnabled(false);
                break;
            case 3:
            case 4:
                helper.getView(R.id.rootView).setAlpha(0.6f);
                helper.getView(R.id.status_view).setVisibility(View.GONE);
                helper.getView(R.id.rootView).setEnabled(false);
                break;
        }
    }
}
