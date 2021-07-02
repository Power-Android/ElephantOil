package com.xxjy.jyyh.adapter;

import android.text.Html;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author power
 * @date 2021/7/2 2:31 下午
 * @project ElephantOil
 * @description:
 */
public class OilTagDescAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public OilTagDescAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, String item) {
        TextView contentTv = helper.getView(R.id.item_content_tv);
        contentTv.setText(Html.fromHtml(item));
    }
}
