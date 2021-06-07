package com.xxjy.jyyh.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.HomeMenuEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author power
 * @date 2021/5/31 3:36 下午
 * @project ElephantOil
 * @description:
 */
public class HomeMenuAdapter extends BaseQuickAdapter<HomeMenuEntity, BaseViewHolder> {
    public HomeMenuAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<HomeMenuEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, HomeMenuEntity item) {
        Glide.with(mContext)
                .load(item.getIcon())
                .apply(new RequestOptions()
                .error(R.drawable.default_img_bg))
                .into((ImageView) helper.getView(R.id.item_img_iv));
        helper.setText(R.id.item_title_tv, item.getTitle());
    }
}
