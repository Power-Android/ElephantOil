package com.xxjy.jyyh.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.ProductBean;
import com.xxjy.jyyh.utils.GlideUtils;

import java.util.List;

/**
 * @author power
 * @date 1/21/21 5:47 PM
 * @project ElephantOil
 * @description:
 */
public class IntegralExchangeAdapter extends BaseQuickAdapter<ProductBean, BaseViewHolder> {

    public IntegralExchangeAdapter(int layoutResId, @Nullable List<ProductBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ProductBean item) {
//        Glide.with(mContext)
//                .load(item.getProductImg())
////                .override(AdaptScreenUtils.pt2Px(67),AdaptScreenUtils.pt2Px(67))
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                .apply(new RequestOptions()
//                        .error(R.drawable.default_img_bg)
//                )
//                .into((ImageView) helper.getView(R.id.item_img_iv));

        GlideUtils.loadRoundSquareImage(mContext,item.getProductImg(),(ImageView) helper.getView(R.id.item_img_iv));


        helper.setText(R.id.item_title_tv,item.getName())
                .setText(R.id.item_integral_tv, (TextUtils.isEmpty(item.getRedeemPrice())||Double.parseDouble(item.getRedeemPrice())==0d)?(item.getRedeemPoint()+"积分"):(item.getRedeemPoint()+"积分+"+item.getRedeemPrice()+"元"))
                .setText(R.id.sell_num_view,String.format("已兑换%d件",item.getSalesNum()));



    }
}
