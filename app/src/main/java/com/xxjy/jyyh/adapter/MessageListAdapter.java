package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.ArticleBean;

import java.util.List;

public class MessageListAdapter extends BaseQuickAdapter<ArticleBean, BaseViewHolder> {

    public MessageListAdapter(int layoutResId, @Nullable List<ArticleBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ArticleBean item) {
        helper.setText(R.id.article_title, item.getTitle())
                .setText(R.id.article_time, item.getCreateTime())
                .setText(R.id.article_content, item.getSummary());
    }
}
