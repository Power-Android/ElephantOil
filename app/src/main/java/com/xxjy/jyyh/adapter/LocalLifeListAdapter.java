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

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.OilEntity;

import java.util.List;


public class LocalLifeListAdapter extends BaseQuickAdapter<OilEntity.StationsBean, BaseViewHolder> {

    public LocalLifeListAdapter(int layoutResId, @Nullable List<OilEntity.StationsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilEntity.StationsBean item) {

//
//        if (!item.isIsSign()) {
//            helper.setTextColor(R.id.item_name_tv, Color.parseColor("#323334"))
//                    .setTextColor(R.id.item_address_tv, Color.parseColor("#A0A0A0"))
//                    .setTextColor(R.id.item_original_tv, Color.parseColor("#A0A0A0"))
//                    .setTextColor(R.id.item_navigation_tv, Color.parseColor("#555555"))
//                    .setTextColor(R.id.item_address_tv, Color.parseColor("#A0A0A0"));
//            helper.getView(R.id.parent_layout).setBackground(null);
//            ((QMUIFloatLayout) helper.getView(R.id.float_layout)).removeAllViews();
//            helper.getView(R.id.type_view).setVisibility(View.GONE);
//        } else {
//            helper.setTextColor(R.id.item_name_tv, Color.parseColor("#1676FF"))
//                    .setTextColor(R.id.item_address_tv, Color.parseColor("#5478AC"))
//                    .setTextColor(R.id.item_original_tv, Color.parseColor("#5478AC"))
//                    .setTextColor(R.id.item_navigation_tv, Color.parseColor("#323334"))
//                    .setTextColor(R.id.item_address_tv, Color.parseColor("#5478AC"));
//            ((QMUIFloatLayout) helper.getView(R.id.float_layout)).removeAllViews();
//
//            helper.getView(R.id.parent_layout).setBackgroundResource(R.drawable.ic_oil_station_bg);
//            helper.getView(R.id.type_view).setVisibility(View.VISIBLE);
//
//        }

//        ((TextView) helper.getView(R.id.item_original_tv)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        Glide.with(mContext)
                .load(item.getGasTypeImg())
                .override(AdaptScreenUtils.pt2Px(67),AdaptScreenUtils.pt2Px(67))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .apply(new RequestOptions()
                        .error(R.drawable.default_img_bg)
                )
                .into((ImageView) helper.getView(R.id.item_oil_img));
        helper.setText(R.id.item_name_tv, item.getGasName())
                .setText(R.id.item_address_tv, item.getGasAddress())
                .setText(R.id.item_navigation_tv, item.getDistance() + "KM")
//                .setText(R.id.item_actual_tv, item.getPriceYfq())
//                .setText(R.id.oil_num_view, item.getOilName())
//                .setText(R.id.item_original_tv, "¥" + item.getPriceGun())
                .addOnClickListener(R.id.navigation_ll);


        ((QMUIFloatLayout) helper.getView(R.id.float_layout)).removeAllViews();
        if (item.getCzbLabels() != null&&item.getCzbLabels().size()>0) {
            for (OilEntity.StationsBean.CzbLabelsBean lab :
                    item.getCzbLabels()) {
                addTagView(mContext, lab.getTagIndexDescription(),
                        (QMUIFloatLayout) helper.getView(R.id.float_layout));
            }
            helper.getView(R.id.float_layout).setVisibility(View.VISIBLE);
        }else{
        helper.getView(R.id.float_layout).setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.navigation_ll);

    }

    private void addTagView(Context context, String content, QMUIFloatLayout floatLayout) {
        TextView textView = new TextView(context);
        int textViewPadding = QMUIDisplayHelper.dp2px(context, 2);
        int textViewPadding2 = QMUIDisplayHelper.dp2px(context, 2);
        textView.setPadding(textViewPadding, textViewPadding2, textViewPadding, textViewPadding2);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
        textView.setTextColor(ContextCompat.getColor(context, R.color.color_6431));
        textView.setBackgroundResource(R.drawable.shape_soild_station_tag_red);

        textView.setText(content);
        floatLayout.addView(textView);
    }
}
