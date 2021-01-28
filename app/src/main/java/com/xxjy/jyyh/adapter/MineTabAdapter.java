package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.utils.GlideUtils;

import java.util.List;

public class MineTabAdapter extends BaseQuickAdapter<BannerBean, BaseViewHolder> {

    public MineTabAdapter(int layoutResId, @Nullable List<BannerBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BannerBean item) {
        GlideUtils.loadImage(mContext,item.getImgUrl(),helper.getView(R.id.image_view));
        helper.setText(R.id.title_view,item.getTitle());
    }
}
