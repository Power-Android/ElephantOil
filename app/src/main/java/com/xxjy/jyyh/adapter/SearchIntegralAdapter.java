package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.ProductBean;
import com.xxjy.jyyh.utils.GlideUtils;

import java.util.List;

/**
 * @author power
 * @date 1/25/21 3:15 PM
 * @project ElephantOil
 * @description:
 */
public class SearchIntegralAdapter extends BaseQuickAdapter<ProductBean, BaseViewHolder> {

    public SearchIntegralAdapter(int layoutResId, @Nullable List<ProductBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ProductBean item) {
        GlideUtils.loadImage(mContext, item.getProductImg(), helper.getView(R.id.item_img_iv));
        helper.setText(R.id.item_title_tv, item.getName());
        if (item.getRedeemPrice() > 0){
            helper.setText(R.id.item_integral_tv, item.getRedeemPoint() +
                    "积分 + " + item.getRedeemPrice() + "元");
        }else {
            helper.setText(R.id.item_integral_tv, item.getRedeemPoint() +
                    "积分");
        }
    }
}
