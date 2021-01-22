package com.xxjy.jyyh.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;

import java.util.List;

/**
 * @author power
 * @date 1/22/21 2:23 PM
 * @project ElephantOil
 * @description:
 */
public class OilDiscountAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public OilDiscountAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        TextView discountTv = helper.getView(R.id.item_discount_tv);
        switch (helper.getAdapterPosition()) {
            case 0://直降优惠
                helper.setImageResource(R.id.item_img_iv, R.drawable.icon_price_fall)
                        .setText(R.id.item_title_tv, "直降优惠");
                discountTv.setCompoundDrawables(null, null, null, null);
                break;
            case 1://会员红包
                helper.setImageResource(R.id.item_img_iv, R.drawable.icon_vip_package)
                        .setText(R.id.item_title_tv, "会员红包");
                setDrawable(discountTv, R.drawable.arrow_right_icon);
                break;
            case 2://小象优惠券
                helper.setImageResource(R.id.item_img_iv, R.drawable.icon_elephant_coupon)
                        .setText(R.id.item_title_tv, "小象优惠券");
                setDrawable(discountTv, R.drawable.arrow_right_icon);
                break;
            case 3://余额
                helper.setImageResource(R.id.item_img_iv, R.drawable.icon_balance)
                        .setText(R.id.item_title_tv, "余额")
                        .setGone(R.id.item_switch_tv, true);
                discountTv.setCompoundDrawables(null, null, null, null);
                break;
        }
    }

    private void setDrawable(TextView textView, int drawableId) {
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
        textView.setCompoundDrawablePadding(SizeUtils.dp2px(5));
    }
}
