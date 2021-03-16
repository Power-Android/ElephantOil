package com.xxjy.jyyh.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.PrivacyEntity;

import java.util.List;

/**
 * @author power
 * @date 3/16/21 6:39 PM
 * @project ElephantOil
 * @description:
 */
public class PrivacyAdapter extends BaseQuickAdapter<PrivacyEntity, BaseViewHolder> {

    public PrivacyAdapter(int layoutResId, @Nullable List<PrivacyEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PrivacyEntity item) {
        Glide.with(mContext).load(item.getImg()).into((ImageView) helper.getView(R.id.item_img_iv));
        helper.setText(R.id.item_title_tv, item.getTitle())
                .setText(R.id.item_content_tv, item.getContent());
    }
}
