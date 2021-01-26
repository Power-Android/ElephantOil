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
        helper.setText(R.id.item_oil_num_tv, item.getOilName());
        helper.itemView.setSelected(item.isSelected());

        //1汽油，2 柴油，3天然气
        switch (item.getOilType()){
            case 1:
                helper.setText(R.id.item_oil_type_tv, "汽油");
                break;
            case 2:
                helper.setText(R.id.item_oil_type_tv, "柴油");
                break;
            case 3:
                helper.setText(R.id.item_oil_type_tv, "天然气");
                break;
        }
    }
}
