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
 * @date 1/21/21 9:11 PM
 * @project ElephantOil
 * @description:
 */
public class OilNumAdapter extends BaseQuickAdapter<OilEntity.StationsBean.OilPriceListBean, BaseViewHolder> {

    public OilNumAdapter(int layoutResId, @Nullable List<OilEntity.StationsBean.OilPriceListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilEntity.StationsBean.OilPriceListBean item) {
        helper.getView(R.id.item_oil_type_tv).setSelected(item.isSelected());
        helper.setText(R.id.item_oil_type_tv, item.getOilName());
    }
}
