package com.xxjy.jyyh.adapter;


import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.entity.CarServeProductsBean;
import com.xxjy.jyyh.entity.PosterBean;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.GlideUtils;
import com.xxjy.jyyh.utils.Util;

import java.util.List;


public class CarServeProjectListAdapter extends BaseQuickAdapter<CarServeProductsBean, BaseViewHolder> {

    private int mSelectPosition = 0;
    private OnSelectListener mOnSelectListener;
    private PosterBean mPosterBean;
    public CarServeProjectListAdapter(int layoutResId, @Nullable List<CarServeProductsBean> data) {
        super(layoutResId, data);
    }
    public void setPoster(PosterBean bean) {
        mPosterBean = bean;
        notifyDataSetChanged();
    }

    public void setSelectPosition(int position) {
        mSelectPosition = position;
        notifyDataSetChanged();
        if(mOnSelectListener!=null){
            mOnSelectListener.onSelect(getItem(position));
        }
    }
    public CarServeProductsBean getSelectData(){
        return getItem(mSelectPosition);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CarServeProductsBean item) {

        Glide.with(mContext)
                .load(item.getCover())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_car_default_product)
                        .error(R.drawable.ic_car_default_product)
                )
                .into((ImageView) helper.getView(R.id.img_iv));
        helper.setText(R.id.name_view, item.getName())
                .setText(R.id.line_price_view, "¥" + Util.formatDouble(Double.parseDouble(item.getLinePrice())))
                .setText(R.id.sale_price_view, "¥" + Util.formatDouble(Double.parseDouble(item.getSalePrice())));
        ((TextView) helper.getView(R.id.line_price_view)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if (mSelectPosition == helper.getAdapterPosition()) {
            helper.setImageResource(R.id.check_box, R.drawable.ic_checked);
        } else {
            helper.setImageResource(R.id.check_box, R.drawable.ic_uncheck);
        }
        helper.getView(R.id.parent_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lastPosition = mSelectPosition;
                mSelectPosition = helper.getAdapterPosition();
                helper.setImageResource(R.id.check_box, R.drawable.ic_checked);
                notifyItemChanged(lastPosition);
                if(mOnSelectListener!=null){
                    mOnSelectListener.onSelect(item);
                }

            }
        });

        if (mPosterBean != null && (item.getChildCategoryId() == 7 || item.getChildCategoryId() == 8)) {
            helper.getView(R.id.poster_view).setVisibility(View.VISIBLE);
            GlideUtils.loadImage(mContext,mPosterBean.getPic(),(ImageView) helper.getView(R.id.poster_view));
            helper.getView(R.id.poster_view).setOnClickListener(v -> {
                WebViewActivity.openWebActivity((BaseActivity) mContext,mPosterBean.getUrl());
            });
        } else {
            helper.getView(R.id.poster_view).setVisibility(View.GONE);


        }
    }
    public void setOnSelectListener(OnSelectListener onSelectListener){
        mOnSelectListener =onSelectListener;
    }
    
    public interface OnSelectListener{
       void onSelect(CarServeProductsBean data);
    }

}
