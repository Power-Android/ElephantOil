package com.xxjy.jyyh.adapter;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

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
                if (item.getFallAmount() > 0) {
                    helper.setText(R.id.item_discount_tv, "-¥" + item.getFallAmount());
                } else {
                    helper.setText(R.id.item_discount_tv, item.getFallDesc());
                }
                helper.setImageResource(R.id.item_img_iv, R.drawable.icon_price_fall)
                        .setGone(R.id.item_balance_tv, false)
                        .setGone(R.id.item_title_desc, item.isService())
                        .setTextColor(R.id.item_discount_tv, item.getFallAmount() > 0 ?
                                mContext.getResources().getColor(R.color.color_27) :
                                mContext.getResources().getColor(R.color.color_B1))
                        .setText(R.id.item_title_tv, "优惠合计");
                discountTv.setCompoundDrawables(null, null, null, null);
                helper.addOnClickListener(R.id.item_title_desc);
                break;
            case 1://平台优惠券
                helper.setImageResource(R.id.item_img_iv, R.drawable.icon_vip_package)
                        .setGone(R.id.item_balance_tv, false)
                        .setText(R.id.item_title_tv, "平台优惠券");
                if (item.getPlatformDesc().equals("请选择优惠券")) {
                    helper.setText(R.id.item_discount_tv, item.getPlatformDesc())
                            .setEnabled(R.id.item_view, true)
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_27));
                } else if (item.getPlatformDesc().equals("暂无可用优惠券")) {
                    helper.setText(R.id.item_discount_tv, item.getPlatformDesc())
                            .setEnabled(R.id.item_view, false)
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_B1));
                } else if (item.getPlatformDesc().equals("请选择加油金额")) {
                    helper.setText(R.id.item_discount_tv, item.getPlatformDesc())
                            .setEnabled(R.id.item_view, false)
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_B1));
                } else {
                    helper.setText(R.id.item_discount_tv, item.getPlatformDesc())
                            .setEnabled(R.id.item_view, true)
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_27));
                }
                if (!TextUtils.isEmpty(item.getSwell())){//有膨胀券
                    helper.setText(R.id.item_swell_tv, "可兑换此油站" + item.getSwell() + "元券")
                            .setGone(R.id.item_swell_tv, true);
                    discountTv.setCompoundDrawables(null, null, null, null);
                }else {
                    helper.setGone(R.id.item_swell_tv, false);
                    setDrawable(discountTv, R.drawable.arrow_right_icon);
                }
                break;
            case 2://商家优惠券
                helper.setImageResource(R.id.item_img_iv, R.drawable.icon_elephant_coupon)
                        .setGone(R.id.item_balance_tv, false)
                        .setText(R.id.item_title_tv, "商家优惠券(与直降优惠不同享)");
                if (item.getBusinessDesc().equals("请选择优惠券")) {
                    helper.setText(R.id.item_discount_tv, item.getBusinessDesc())
                            .setEnabled(R.id.item_view, true)
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_27));
                } else if (item.getBusinessDesc().equals("暂无可用优惠券")) {
                    helper.setText(R.id.item_discount_tv, item.getBusinessDesc())
                            .setEnabled(R.id.item_view, false)
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_B1));
                } else if (item.getBusinessDesc().equals("请选择加油金额")) {
                    helper.setText(R.id.item_discount_tv, item.getBusinessDesc())
                            .setEnabled(R.id.item_view, false)
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_B1));
                } else {
                    helper.setText(R.id.item_discount_tv, item.getBusinessDesc())
                            .setEnabled(R.id.item_view, true)
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_27));
                }
                setDrawable(discountTv, R.drawable.arrow_right_icon);
                break;
            case 3://优惠立减
                ConstraintLayout view = helper.getView(R.id.item_view);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (!TextUtils.equals("0", item.getDiscount())){
                    layoutParams.height = SizeUtils.dp2px(32f); // 根据具体需求场景设置
                    layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    view.setVisibility(View.VISIBLE);
                    helper.setText(R.id.item_title_tv, "优惠立减")
                            .setImageResource(R.id.item_img_iv, R.drawable.icon_elephant_discount)
                            .setText(R.id.item_discount_tv, "-￥" + item.getDiscount())
                            .setTextColor(R.id.item_discount_tv, mContext.getResources().getColor(R.color.color_27))
                            .setGone(R.id.item_balance_tv, false);
                    discountTv.setCompoundDrawables(null, null, null, null);
                }else {
                    view.setVisibility(View.GONE);
                    layoutParams.height = 0;
                    layoutParams.width = 0;
                }
                view.setLayoutParams(layoutParams);
                break;
            case 4://余额
                if (item.getBalance() > 0) {
                    helper.setImageResource(R.id.item_img_iv, R.drawable.icon_balance)
                            .setText(R.id.item_balance_tv, "¥" + NumberUtils.format(item.getBalance(), 2))
                            .setText(R.id.item_title_tv, "余额")
                            .setText(R.id.item_discount_tv, item.isUseBill() ? "本次可抵扣¥" +
                                    NumberUtils.format(item.getBalanceDiscount(), 2) : "")
                            .setTextColor(R.id.item_discount_tv,
                                    mContext.getResources().getColor(R.color.color_27))
                            .setGone(R.id.item_switch_tv, true)
                            .setEnabled(R.id.item_switch_tv, true);
                } else {
                    helper.setImageResource(R.id.item_img_iv, R.drawable.icon_balance)
                            .setText(R.id.item_balance_tv, "")
                            .setText(R.id.item_title_tv, "余额")
                            .setText(R.id.item_discount_tv, "暂无余额")
                            .setTextColor(R.id.item_discount_tv,
                                    mContext.getResources().getColor(R.color.color_B1))
                            .setGone(R.id.item_switch_tv, false)
                            .setEnabled(R.id.item_switch_tv, false);
                }
                helper.getView(R.id.item_switch_tv).setSelected(item.isUseBill());
                discountTv.setCompoundDrawables(null, null, null, null);
                helper.addOnClickListener(R.id.item_switch_tv);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private void setDrawable(TextView textView, int drawableId) {
        Drawable drawable = mContext.getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
        textView.setCompoundDrawablePadding(SizeUtils.dp2px(5));
    }
}
