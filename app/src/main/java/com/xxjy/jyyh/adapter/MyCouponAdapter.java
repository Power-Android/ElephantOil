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
import com.xxjy.jyyh.utils.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MyCouponAdapter extends BaseMultiItemQuickAdapter<CouponBean, BaseViewHolder> {

    public MyCouponAdapter(@Nullable List<CouponBean> data) {
        super(data);
        addItemType(0, R.layout.adapter_my_coupon);
        addItemType(1, R.layout.adapter_my_coupon_integral);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CouponBean item) {

        switch (helper.getItemViewType()) {
            case 0:
                helper.setText(R.id.item_coupon_amount, Util.formatDouble(Double.parseDouble(item.getAmountReduce())))
                        .setText(R.id.item_use_range_tv, TextUtils.isEmpty(item.getAmount()) || Double.parseDouble(item.getAmount()) == 0 ? "无门槛" : String.format("满%s元可用", Util.formatDouble(Double.parseDouble(item.getAmount()))))
                        .setText(R.id.item_title_tv, item.getName())
                        .setText(R.id.item_coupon_date, String.format("%s - %s", item.getStartTime(), item.getEndTime()));
                helper.addOnClickListener(R.id.use_view);
                switch (item.getStatus()) {
                    //0真正可用 1已用 2过期  3时间未到 4 金额未达到
                    case 0:
                        helper.getView(R.id.mask_view).setVisibility(View.GONE);
                        helper.getView(R.id.status_view).setVisibility(View.GONE);
                        helper.getView(R.id.use_view).setVisibility(View.VISIBLE);
                        break;
                    case 3:
                    case 4:
                        helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                        helper.getView(R.id.status_view).setVisibility(View.GONE);
                        helper.getView(R.id.use_view).setVisibility(View.GONE);
                        break;
                    case 1:
                        helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                        helper.getView(R.id.status_view).setVisibility(View.VISIBLE);
                        ((ImageView) helper.getView(R.id.status_view)).setImageResource(R.drawable.ic_coupon_used);
                        helper.getView(R.id.use_view).setVisibility(View.GONE);
                        break;
                    case 2:
                        helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                        helper.getView(R.id.status_view).setVisibility(View.VISIBLE);
                        ((ImageView) helper.getView(R.id.status_view)).setImageResource(R.drawable.ic_coupon_expired);
                        helper.getView(R.id.use_view).setVisibility(View.GONE);
                        break;
                    default:
                        helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                        helper.getView(R.id.status_view).setVisibility(View.GONE);
                        helper.getView(R.id.use_view).setVisibility(View.GONE);
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
                helper.addOnClickListener(R.id.use_view);
                switch (item.getStatus()) {
                    //0真正可用 1已用 2过期  3时间未到 4 金额未达到
                    case 0:
                        helper.getView(R.id.mask_view).setVisibility(View.GONE);
                        helper.getView(R.id.status_view).setVisibility(View.GONE);
                        helper.getView(R.id.use_view).setVisibility(View.VISIBLE);
                        break;
                    case 3:
                    case 4:
                        helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                        helper.getView(R.id.status_view).setVisibility(View.GONE);
                        helper.getView(R.id.use_view).setVisibility(View.GONE);
                        break;
                    case 1:
                        helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                        helper.getView(R.id.status_view).setVisibility(View.VISIBLE);
                        ((ImageView) helper.getView(R.id.status_view)).setImageResource(R.drawable.ic_coupon_used);
                        helper.getView(R.id.use_view).setVisibility(View.GONE);
                        break;
                    case 2:
                        helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                        helper.getView(R.id.status_view).setVisibility(View.VISIBLE);
                        ((ImageView) helper.getView(R.id.status_view)).setImageResource(R.drawable.ic_coupon_expired);
                        helper.getView(R.id.use_view).setVisibility(View.GONE);
                        break;
                    default:
                        helper.getView(R.id.mask_view).setVisibility(View.VISIBLE);
                        helper.getView(R.id.status_view).setVisibility(View.GONE);
                        helper.getView(R.id.use_view).setVisibility(View.GONE);
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
//        helper.setGone(R.id.desc_tv, !TextUtils.isEmpty(item.getDescription()));
//        helper.setText(R.id.desc_content, "使用说明"+item.getDescription());

//        helper.getView(R.id.desc_ll).setVisibility(View.GONE);
//        helper.getView(R.id.desc_tv).setOnClickListener(view -> {
//            int visibility = helper.getView(R.id.desc_ll).getVisibility();
//            if (visibility == 0){
//                helper.getView(R.id.desc_ll).setVisibility(View.GONE);
//            }else if (visibility == 8){
//                helper.getView(R.id.desc_ll).setVisibility(View.VISIBLE);
//            }
//        });

        if(TextUtils.isEmpty(item.getDescription())){
            helper.getView(R.id.down_view).setVisibility(View.INVISIBLE);
            helper.getView(R.id.desc_content).setVisibility(View.INVISIBLE);
            helper.getView(R.id.desc_content2).setVisibility(View.GONE);
        }else{
            if(item.getDescription().length()>16){
                helper.setText(R.id.desc_content, "使用说明："+item.getDescription().substring(0,15));
                helper.setText(R.id.desc_content2, "使用说明："+item.getDescription());
                helper.getView(R.id.down_view).setVisibility(View.VISIBLE);

                helper.getView(R.id.down_view).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if( helper.getView(R.id.desc_content2).getVisibility()==View.VISIBLE){
                            helper.getView(R.id.desc_content).setVisibility(View.VISIBLE);
                            helper.getView(R.id.desc_content2).setVisibility(View.GONE);
//                    helper.getView(R.id.down_view).setVisibility(View.VISIBLE);
                        }else{
                            helper.getView(R.id.desc_content).setVisibility(View.INVISIBLE);
                            helper.getView(R.id.desc_content2).setVisibility(View.VISIBLE);
//                    helper.getView(R.id.down_view).setVisibility(View.VISIBLE);
                        }
                    }
                });
                helper.getView(R.id.desc_ll).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if( helper.getView(R.id.desc_content2).getVisibility()==View.VISIBLE){
                            helper.getView(R.id.desc_content).setVisibility(View.VISIBLE);
                            helper.getView(R.id.desc_content2).setVisibility(View.GONE);
//                    helper.getView(R.id.down_view).setVisibility(View.VISIBLE);
                        }else{
                            helper.getView(R.id.desc_content).setVisibility(View.INVISIBLE);
                            helper.getView(R.id.desc_content2).setVisibility(View.VISIBLE);
//                    helper.getView(R.id.down_view).setVisibility(View.VISIBLE);
                        }
                    }
                });
            }else{
                helper.setText(R.id.desc_content, "使用说明："+item.getDescription());
                helper.getView(R.id.down_view).setVisibility(View.INVISIBLE);
                helper.getView(R.id.desc_content2).setVisibility(View.GONE);
            }
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
