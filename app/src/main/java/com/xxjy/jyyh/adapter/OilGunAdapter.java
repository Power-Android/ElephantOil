package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.OilEntity;

import java.util.List;

/**
 * @author power
 * @date 1/22/21 1:11 PM
 * @project ElephantOil
 * @description:
 */
public class OilGunAdapter extends BaseQuickAdapter<OilEntity.StationsBean.OilPriceListBean.GunNosBean, BaseViewHolder> {

    public OilGunAdapter(int layoutResId, @Nullable List<OilEntity.StationsBean.OilPriceListBean.GunNosBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilEntity.StationsBean.OilPriceListBean.GunNosBean item) {
        helper.setText(R.id.item_oil_type_tv, String.valueOf(item.getGunNo()) + "号枪");
        helper.itemView.setSelected(item.isSelected());
    }
}
