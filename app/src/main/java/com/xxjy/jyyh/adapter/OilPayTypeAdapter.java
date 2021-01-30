package com.xxjy.jyyh.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.OilPayTypeEntity;

import java.util.List;

/**
 * @author power
 * @date 1/23/21 2:52 PM
 * @project ElephantOil
 * @description:
 */
public class OilPayTypeAdapter extends BaseQuickAdapter<OilPayTypeEntity, BaseViewHolder> {

    public OilPayTypeAdapter(int layoutResId, @Nullable List<OilPayTypeEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilPayTypeEntity item) {
        switch (item.getId()){
            case "wxwap"://微信H5
                helper.setImageResource(R.id.item_pay_type_iv, R.drawable.wechat_icon)
                        .setText(R.id.item_title_tv, "微信支付");
                break;
            case "wxjsapi"://微信公众号
                helper.setImageResource(R.id.item_pay_type_iv, R.drawable.wechat_icon)
                        .setText(R.id.item_title_tv, "微信支付");
                break;
            case "wxapplet"://微信小程序
                helper.setImageResource(R.id.item_pay_type_iv, R.drawable.ic_wechat_apple)
                        .setText(R.id.item_title_tv, "小程序支付");
                break;
            case "aliwappay"://支付宝支付
                helper.setImageResource(R.id.item_pay_type_iv, R.drawable.alipay_icon)
                        .setText(R.id.item_title_tv, "支付宝支付");
                break;
        }
        helper.setChecked(R.id.item_check_box, item.isSelect());
    }
}
