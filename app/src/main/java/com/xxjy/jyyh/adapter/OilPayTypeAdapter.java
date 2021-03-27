package com.xxjy.jyyh.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.constants.PayTypeConstants;
import com.xxjy.jyyh.entity.OilPayTypeEntity;
import java.util.List;

/**
 * @author power
 * @date 1/23/21 2:52 PM
 * @project ElephantOil
 * @description:
 */
public class OilPayTypeAdapter extends BaseQuickAdapter<OilPayTypeEntity, BaseViewHolder> {

    private int selectPosition = 0;

    public OilPayTypeAdapter(int layoutResId, @Nullable List<OilPayTypeEntity> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilPayTypeEntity item) {
        switch (item.getId()) {
            case PayTypeConstants.PAY_TYPE_WEIXIN://微信H5
                helper.setImageResource(R.id.item_pay_type_iv, R.drawable.wechat_icon)
                        .setText(R.id.item_title_tv, "微信支付");
                break;
            case PayTypeConstants.PAY_TYPE_WEIXIN_APP://微信H5
                helper.setImageResource(R.id.item_pay_type_iv, R.drawable.wechat_icon)
                        .setText(R.id.item_title_tv, "微信支付");
                break;
            case PayTypeConstants.PAY_TYPE_WEIXIN_GZH://微信公众号
                helper.setImageResource(R.id.item_pay_type_iv, R.drawable.wechat_icon)
                        .setText(R.id.item_title_tv, "微信支付");
                break;
            case PayTypeConstants.PAY_TYPE_WEIXIN_XCX://微信小程序
                helper.setImageResource(R.id.item_pay_type_iv, R.drawable.ic_wechat_apple)
                        .setText(R.id.item_title_tv, "小程序支付");
                break;
            case PayTypeConstants.PAY_TYPE_ZHIFUBAO://支付宝支付
                helper.setImageResource(R.id.item_pay_type_iv, R.drawable.alipay_icon)
                        .setText(R.id.item_title_tv, "支付宝支付");
            case PayTypeConstants.PAY_TYPE_ZHIFUBAO_APP://支付宝支付
                helper.setImageResource(R.id.item_pay_type_iv, R.drawable.alipay_icon)
                        .setText(R.id.item_title_tv, "支付宝支付");
                break;
            case PayTypeConstants.PAY_TYPE_UNIONPAY:
                helper.setImageResource(R.id.item_pay_type_iv, R.drawable.quick_pass_icon)
                        .setText(R.id.item_title_tv, "银联云闪付");
                break;
        }
        if (!TextUtils.isEmpty(item.getActMes())) {
            helper.getView(R.id.dec_view).setVisibility(View.VISIBLE);
            helper.setText(R.id.dec_view, item.getActMes());
        } else {
            helper.getView(R.id.dec_view).setVisibility(View.INVISIBLE);

        }
        if (selectPosition == helper.getAdapterPosition()) {
            item.setSelect(true);
            helper.setImageResource(R.id.item_check_box, R.drawable.ic_checked);
        } else {
            item.setSelect(false);
            helper.setImageResource(R.id.item_check_box, R.drawable.ic_uncheck);
        }

    }


    public void setCheckItem(int position) {
        this.selectPosition = position;
        notifyDataSetChanged();
    }
}
