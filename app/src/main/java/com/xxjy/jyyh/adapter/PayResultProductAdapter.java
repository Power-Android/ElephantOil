package com.xxjy.jyyh.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SpanUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.PayResultProductBean;
import com.xxjy.jyyh.entity.RedeemEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PayResultProductAdapter extends BaseQuickAdapter<PayResultProductBean, BaseViewHolder> {

    public PayResultProductAdapter(@Nullable List<PayResultProductBean> data) {
        super(R.layout.adapter_pay_result_product,data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, PayResultProductBean item) {
        Glide.with(mContext).load(item.getProductImg())
                .apply(new RequestOptions().error(R.drawable.default_img_bg)).into((ImageView) helper.getView(R.id.item_img_iv));
        helper.setText(R.id.item_name_tv, item.getName())
                .setText(R.id.item_price_tv, "¥"+item.getRedeemPrice());
        SpanUtils.with(helper.getView(R.id.item_orin_price_tv)).append("¥"+item.getCostPrice()).setStrikethrough().create();

    }

}
