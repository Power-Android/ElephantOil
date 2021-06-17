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
import com.xxjy.jyyh.entity.RedeemEntity;
import com.xxjy.jyyh.utils.Util;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author power
 * @date 2021/5/24 2:27 下午
 * @project ElephantOil
 * @description:
 */
public class OilRedeemAdapter extends BaseMultiItemQuickAdapter<RedeemEntity.ProductOilGasListBean, BaseViewHolder> {
    public static final int TYPENORMAL = 1;
    public static final int TYPECAR = 2;
    public static final int TYPENORMAL1 = 3;
    public static final int TYPENORMAL2 = 4;

    public OilRedeemAdapter(@Nullable List<RedeemEntity.ProductOilGasListBean> data) {
        super(data);
        addItemType(TYPENORMAL, R.layout.adapter_oil_redeem);
        addItemType(TYPECAR, R.layout.adapter_oil_redeem_car);
        addItemType(TYPENORMAL1, R.layout.adapter_oil_redeem);
        addItemType(TYPENORMAL2, R.layout.adapter_oil_redeem);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, RedeemEntity.ProductOilGasListBean item) {
        Glide.with(mContext).load(item.getProductImg())
                .apply(new RequestOptions().error(R.drawable.default_img_bg)).into((ImageView) helper.getView(R.id.item_img_iv));
        helper.setText(R.id.item_name_tv, item.getName())
                .setText(R.id.item_desc_tv, item.getProductDetail())
                .setText(R.id.item_price_tv, "¥"+ Util.formatDouble(Double.parseDouble(item.getRedeemPrice())));
        SpanUtils.with(helper.getView(R.id.item_orin_price_tv)).append("¥"+Util.formatDouble(Double.parseDouble(item.getCostPrice()))).setStrikethrough().create();

        helper.setImageResource(R.id.item_check_box, item.isSelected() ? R.drawable.redeem_selected_icon : R.drawable.redeem_unslect_icon);
    }

    public void setSelected(int position){
        getItem(position).setSelected(!getItem(position).isSelected());
        notifyDataSetChanged();
    }
}
