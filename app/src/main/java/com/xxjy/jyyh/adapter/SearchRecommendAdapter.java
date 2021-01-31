package com.xxjy.jyyh.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.entity.RecomdEntity;
import com.xxjy.jyyh.ui.web.WebViewActivity;

import java.util.List;

/**
 * @author power
 * @date 1/23/21 7:30 PM
 * @project ElephantOil
 * @description:
 */
public class SearchRecommendAdapter extends BaseQuickAdapter<RecomdEntity, BaseViewHolder> {

    public SearchRecommendAdapter(int layoutResId, @Nullable List<RecomdEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RecomdEntity item) {
        helper.setText(R.id.item_title_tv, item.getName());
        helper.itemView.setOnClickListener(view -> WebViewActivity.openRealUrlWebActivity((BaseActivity) mContext, item.getLink()));
    }


}
