package com.xxjy.jyyh.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.CarServeStoreBean;
import com.xxjy.jyyh.entity.OilEntity;

import java.util.List;


public class CarServeListAdapter extends BaseQuickAdapter<CarServeStoreBean, BaseViewHolder> {

    public CarServeListAdapter(int layoutResId, @Nullable List<CarServeStoreBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CarServeStoreBean item) {



        Glide.with(mContext)
                .load(item.getCardStoreInfoVo().getStorePicture())
//                .override(AdaptScreenUtils.pt2Px(67),AdaptScreenUtils.pt2Px(67))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .apply(new RequestOptions()
                        .error(R.drawable.default_img_bg)
                )
                .into((ImageView) helper.getView(R.id.item_oil_img));
        helper.setText(R.id.item_name_tv, item.getCardStoreInfoVo().getStoreName())
                .setText(R.id.item_address_tv, item.getCardStoreInfoVo().getAddress())
                .setText(R.id.item_navigation_tv, String.format("%.2f", item.getCardStoreInfoVo().getDistance() / 1000d) + "KM")
                .setText(R.id.item_business_hours_tv, "营业时间：每天" + item.getCardStoreInfoVo().getOpenStart() + " - " + item.getCardStoreInfoVo().getEndStart())
                .addOnClickListener(R.id.navigation_ll);
        helper.setVisible(R.id.shop_status_view, item.getCardStoreInfoVo().getStatus() == 0 ? false : true);

        ((QMUIFloatLayout) helper.getView(R.id.float_layout)).removeAllViews();
        if (item.getCardStoreInfoVo().getCategoryNameList() != null && item.getCardStoreInfoVo().getCategoryNameList().size() > 0) {
            for (String lab :
                    item.getCardStoreInfoVo().getCategoryNameList()) {
                addTagView(mContext, lab,
                        (QMUIFloatLayout) helper.getView(R.id.float_layout));
            }
            helper.getView(R.id.float_layout).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.float_layout).setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.navigation_ll);


    }

    private void addTagView(Context context, String content, QMUIFloatLayout floatLayout) {
        TextView textView = new TextView(context);
        int textViewPadding = QMUIDisplayHelper.dp2px(context, 4);
        int textViewPadding2 = QMUIDisplayHelper.dp2px(context, 2);
        textView.setPadding(textViewPadding, textViewPadding2, textViewPadding, textViewPadding2);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
        textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        textView.setBackgroundResource(R.drawable.shape_stroke_station_tag);
        textView.setText(content);
        floatLayout.addView(textView);
    }
}
