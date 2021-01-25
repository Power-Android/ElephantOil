package com.xxjy.jyyh.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.utils.GlideUtils;

import java.util.List;

public class PayResultBannerAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public PayResultBannerAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        GlideUtils.loadImage(item,(ImageView) (helper.getView(R.id.tips_iv)));
    }
}
