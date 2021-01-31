package com.xxjy.jyyh.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.HomeProductEntity;

import java.util.List;

/**
 * @author power
 * @date 1/21/21 5:47 PM
 * @project ElephantOil
 * @description:
 */
public class HomeExchangeAdapter extends BaseQuickAdapter<HomeProductEntity.FirmProductsVoBean, BaseViewHolder> {

    public HomeExchangeAdapter(int layoutResId, @Nullable List<HomeProductEntity.FirmProductsVoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, HomeProductEntity.FirmProductsVoBean item) {
        Glide.with(mContext).load(item.getProductImg()).into((ImageView) helper.getView(R.id.item_img_iv));
        helper.setText(R.id.item_title_tv, item.getName())
                .setText(R.id.item_integral_tv, item.getRedeemPrice() == 0 ?
                        item.getRedeemPoint() + "积分" :
                        item.getRedeemPoint() + "积分 + " + item.getRedeemPrice() + "元")
                .setText(R.id.sell_num_view, "已兑换" + item.getSalesNum() + "件");
    }
}
