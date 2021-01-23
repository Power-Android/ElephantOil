package com.xxjy.jyyh.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUIFloatLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundRelativeLayout;
import com.xxjy.jyyh.R;

import java.util.List;

public class OilStationListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public OilStationListAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setTextColor(R.id.item_name_tv, Color.parseColor("#323334"))
                .setTextColor(R.id.item_address_tv, Color.parseColor("#A0A0A0"))
                .setTextColor(R.id.item_navigation_tv, Color.parseColor("#555555"))
                .setTextColor(R.id.item_address_tv, Color.parseColor("#A0A0A0"));
        if (helper.getAdapterPosition() % 2 == 0) {
            helper.getView(R.id.parent_layout).setBackground(null);
            helper.getView(R.id.type_view).setVisibility(View.GONE);

            ((QMUIFloatLayout) helper.getView(R.id.float_layout)).removeAllViews();
            addTagView(mContext, 2, "标签标签标签标签标签标签", (QMUIFloatLayout) helper.getView(R.id.float_layout));
        } else {

            helper.setTextColor(R.id.item_name_tv, Color.parseColor("#1676FF"))
                    .setTextColor(R.id.item_address_tv, Color.parseColor("#5478AC"))
                    .setTextColor(R.id.item_navigation_tv, Color.parseColor("#323334"))
                    .setTextColor(R.id.item_address_tv, Color.parseColor("#5478AC"));
            ((QMUIFloatLayout) helper.getView(R.id.float_layout)).removeAllViews();

            helper.getView(R.id.parent_layout).setBackgroundResource(R.drawable.ic_oil_station_bg);
            helper.getView(R.id.type_view).setVisibility(View.VISIBLE);
            helper.getView(R.id.float_layout);
            ((QMUIFloatLayout) helper.getView(R.id.float_layout)).removeAllViews();

            addTagView(mContext, 1, "标签标签标签标签标签标签", (QMUIFloatLayout) helper.getView(R.id.float_layout));

        }
    }

//    private fun addTypeView(context: Context, content: String, floatLayout: QMUIFloatLayout) {
//        val textView = TextView(context)
//        val textViewPadding = QMUIDisplayHelper.dp2px(context, 4)
//        val textViewPadding2 = QMUIDisplayHelper.dp2px(context, 2)
//        textView.setPadding(textViewPadding, textViewPadding2, textViewPadding, textViewPadding2)
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
//        textView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
//        textView.text = content
//        textView.setBackgroundResource(R.drawable.level_text_bg)
//        floatLayout.addView(textView)
//    }

    private void addTagView(Context context, int type, String content, QMUIFloatLayout floatLayout) {

        TextView textView = new TextView(context);
        int textViewPadding = QMUIDisplayHelper.dp2px(context, 4);
        int textViewPadding2 = QMUIDisplayHelper.dp2px(context, 2);
        textView.setPadding(textViewPadding, textViewPadding2, textViewPadding, textViewPadding2);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
        if (type == 1) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.color_3E));
            textView.setBackgroundResource(R.drawable.shape_soild_station_tag_red);
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
            textView.setBackgroundResource(R.drawable.shape_soild_station_tag);
        }

        textView.setText(content);
        floatLayout.addView(textView);

    }
}
