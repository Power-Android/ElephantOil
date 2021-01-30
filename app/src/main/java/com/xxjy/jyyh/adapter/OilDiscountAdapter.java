package com.xxjy.jyyh.adapter;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.NumberUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.OilDiscountEntity;

import java.util.List;

/**
 * @author power
 * @date 1/22/21 2:23 PM
 * @project ElephantOil
 * @description:
 */
public class OilDiscountAdapter extends BaseQuickAdapter<OilDiscountEntity, BaseViewHolder> {

    public OilDiscountAdapter(int layoutResId, @Nullable List<OilDiscountEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilDiscountEntity item) {
        TextView discountTv = helper.getView(R.id.item_discount_tv);
        switch (helper.getAdapterPosition()) {
            case 0://直降优惠
                helper.setImageResource(R.id.item_img_iv, R.drawable.icon_price_fall)
                        .setGone(R.id.item_balance_tv, false)
                        .setText(R.id.item_discount_tv, item.getFallAmount() > 0 ?
                                "-￥" + item.getFallAmount() : "请选择加油金额")
                        .setTextColor(R.id.item_discount_tv, item.getFallAmount() > 0 ?
                                mContext.getResources().getColor(R.color.color_27) :
                                mContext.getResources().getColor(R.color.color_B1))
                        .setText(R.id.item_title_tv, "直降优惠");
                discountTv.setCompoundDrawables(null, null, null, null);
                break;
            case 1://平台优惠券
                helper.setImageResource(R.id.item_img_iv, R.drawable.icon_vip_package)
                        .setGone(R.id.item_balance_tv, false)
                        .setText(R.id.item_title_tv, "平台优惠券");
                if (item.getPlatformDesc().equals("请选择优惠券")){
                    helper.setText(R.id.item_discount_tv, item.getPlatformDesc())
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_27));
                }else if (item.getPlatformDesc().equals("暂无可用优惠券")){
                    helper.setText(R.id.item_discount_tv, item.getPlatformDesc())
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_B1));
                }else {
                    helper.setText(R.id.item_discount_tv, item.getPlatformDesc())
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_B1));
                }
                setDrawable(discountTv, R.drawable.arrow_right_icon);
                break;
            case 2://商家优惠券
                helper.setImageResource(R.id.item_img_iv, R.drawable.icon_elephant_coupon)
                        .setGone(R.id.item_balance_tv, false)
                        .setText(R.id.item_title_tv, "商家优惠券");
                if (item.getPlatformDesc().equals("请选择优惠券")){
                    helper.setText(R.id.item_discount_tv, item.getBusinessDesc())
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_27));
                }else if (item.getPlatformDesc().equals("暂无可用优惠券")){
                    helper.setText(R.id.item_discount_tv, item.getPlatformDesc())
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_B1));
                } else {
                    helper.setText(R.id.item_discount_tv, item.getBusinessDesc())
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_B1));
                }
                setDrawable(discountTv, R.drawable.arrow_right_icon);
                break;
            case 3://余额
                if (item.getBalance() > 0) {
                    helper.setImageResource(R.id.item_img_iv, R.drawable.icon_balance)
                            .setText(R.id.item_balance_tv, "￥" + NumberUtils.format(item.getBalance(), 2))
                            .setText(R.id.item_title_tv, "余额")
                            .setText(R.id.item_discount_tv, "本次可抵扣￥" +
                                    NumberUtils.format(item.getBalanceDiscount(), 2))
                            .setTextColor(R.id.item_discount_tv,
                                    mContext.getResources().getColor(R.color.color_27))
                            .setChecked(R.id.item_switch_tv, true)
                            .setGone(R.id.item_switch_tv, true)
                            .setEnabled(R.id.item_switch_tv, true);
                } else {
                    helper.setImageResource(R.id.item_img_iv, R.drawable.icon_balance)
                            .setText(R.id.item_balance_tv, "")
                            .setText(R.id.item_title_tv, "余额")
                            .setText(R.id.item_discount_tv, "暂无余额")
                            .setTextColor(R.id.item_discount_tv,
                                    mContext.getResources().getColor(R.color.color_B1))
                            .setChecked(R.id.item_switch_tv, false)
                            .setGone(R.id.item_switch_tv, false)
                            .setEnabled(R.id.item_switch_tv, false);
                }
                discountTv.setCompoundDrawables(null, null, null, null);
                helper.addOnClickListener(R.id.item_switch_tv);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    private void setDrawable(TextView textView, int drawableId) {
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
        textView.setCompoundDrawablePadding(SizeUtils.dp2px(5));
    }
}
