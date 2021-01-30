package com.xxjy.jyyh.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.PayResultEntity;
import com.xxjy.jyyh.utils.GlideUtils;

import java.util.List;

public class PayResultBannerAdapter extends BaseQuickAdapter<PayResultEntity.ActiveParamsBean.BannerBean, BaseViewHolder> {

    public PayResultBannerAdapter(int layoutResId, @Nullable List<PayResultEntity.ActiveParamsBean.BannerBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PayResultEntity.ActiveParamsBean.BannerBean item) {
        GlideUtils.loadImage(mContext, item.getImgUrl(), (ImageView) (helper.getView(R.id.tips_iv)));
    }
}
