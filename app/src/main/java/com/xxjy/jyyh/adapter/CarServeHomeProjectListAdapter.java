package com.xxjy.jyyh.adapter;


import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.entity.CarServeProductsBean;
import com.xxjy.jyyh.entity.PosterBean;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.GlideUtils;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.Util;
import com.xxjy.jyyh.utils.eventtrackingmanager.EventTrackingManager;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingConstant;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingEventConstant;

import java.util.List;


public class CarServeHomeProjectListAdapter extends BaseQuickAdapter<CarServeProductsBean, BaseViewHolder> {

    private OnSelectListener mOnSelectListener;
    private PosterBean mPosterBean;

    public CarServeHomeProjectListAdapter(@Nullable List<CarServeProductsBean> data) {
        super(R.layout.adapter_car_serve_home_project_list, data);
    }

    public void setPoster(PosterBean bean) {
        mPosterBean = bean;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CarServeProductsBean item) {

        helper.setText(R.id.name_view, item.getName())
                .setText(R.id.line_price_view, "¥" + Util.formatDouble(Double.parseDouble(item.getLinePrice())))
                .setText(R.id.sale_price_view, "¥" + Util.formatDouble(Double.parseDouble(item.getSalePrice())));
        ((TextView) helper.getView(R.id.line_price_view)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        helper.getView(R.id.buy_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSelectListener != null) {
                    mOnSelectListener.onSelect(item);
                }

            }
        });
        if (mPosterBean != null && (item.getChildCategoryId() == 7 || item.getChildCategoryId() == 8)) {
            helper.getView(R.id.poster_view).setVisibility(View.VISIBLE);
            GlideUtils.loadImage(mContext,mPosterBean.getPic(),(ImageView) helper.getView(R.id.poster_view));
            helper.getView(R.id.poster_view).setOnClickListener(v -> {
                NaviActivityInfo.disPathIntentFromUrl((BaseActivity) mContext,mPosterBean.getUrl());
                EventTrackingManager.getInstance().trackingEvent((BaseActivity)mContext, TrackingConstant.CF_PAGE_HOME, TrackingEventConstant.CF_EVENT_HOME_SERVICE_BANNER);

            });
        } else {
            helper.getView(R.id.poster_view).setVisibility(View.GONE);


        }
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        void onSelect(CarServeProductsBean data);
    }

}
