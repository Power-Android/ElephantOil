package com.xxjy.jyyh.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.OilEntity;

import java.util.List;

/**
 * @author power
 * @date 12/3/20 11:46 AM
 * @project RunElephant
 * @description:
 */
public class OilStationFlexAdapter extends BaseQuickAdapter<OilEntity.StationsBean.CzbLabelsBean, BaseViewHolder> {

    public OilStationFlexAdapter(int layoutResId, @Nullable List<OilEntity.StationsBean.CzbLabelsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilEntity.StationsBean.CzbLabelsBean item) {

        if (!TextUtils.isEmpty(item.getTagDescription())){
//            helper.itemView.setVisibility(View.GONE);
            helper.setText(R.id.item_title_tv, item.getTagDescription());
        }else{
            helper.setText(R.id.item_title_tv, item.getTagName());
        }
    }
}
