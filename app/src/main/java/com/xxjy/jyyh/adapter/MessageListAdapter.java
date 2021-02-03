package com.xxjy.jyyh.adapter;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.ArticleBean;

import java.util.List;
import java.util.function.BinaryOperator;

public class MessageListAdapter extends BaseQuickAdapter<ArticleBean, BaseViewHolder> {

    private boolean mIsOrderNotice = false;

    public MessageListAdapter(int layoutResId, @Nullable List<ArticleBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ArticleBean item) {
        if (mIsOrderNotice) {
            helper.getView(R.id.line_view).setVisibility(View.GONE);
            helper.getView(R.id.bottom_layout).setVisibility(View.GONE);
            helper.getView(R.id.article_time).setVisibility(View.GONE);
            helper.setTextColor(R.id.article_title, Color.parseColor("#1676FF"));
            helper.setText(R.id.article_title, item.getTitle())
                    .setText(R.id.article_content, item.getContent());
        } else {
            helper.getView(R.id.line_view).setVisibility(View.VISIBLE);
            helper.getView(R.id.bottom_layout).setVisibility(View.VISIBLE);
            helper.getView(R.id.article_time).setVisibility(View.VISIBLE);
            helper.setTextColor(R.id.article_title, Color.parseColor("#323334"));
            helper.setText(R.id.article_title, item.getTitle())
                    .setText(R.id.article_time, item.getCreateTime())
                    .setText(R.id.article_content, item.getSummary());

        }

    }

    public void setType(boolean isOrderNotice) {
        this.mIsOrderNotice = isOrderNotice;
//        notifyDataSetChanged();
    }
}
