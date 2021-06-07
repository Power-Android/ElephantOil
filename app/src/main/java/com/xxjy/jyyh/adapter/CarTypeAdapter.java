package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.CarTypeEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * @author power
 * @date 2021/6/7 2:47 下午
 * @project ElephantOil
 * @description:
 */
public class CarTypeAdapter extends BaseQuickAdapter<CarTypeEntity, BaseViewHolder> {

    public CarTypeAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<CarTypeEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, CarTypeEntity item) {
        helper.setText(R.id.item_oil_type_tv, item.getTitle());
        helper.getView(R.id.item_oil_type_tv).setSelected(item.isSelect());
    }

    public void setSelector(int selectPosition) {
        for (int i = 0; i < getData().size(); i++) {
            if (selectPosition == i){
                Objects.requireNonNull(getViewByPosition(getRecyclerView(), i, R.id.item_oil_type_tv)).setSelected(true);
            }else {
                Objects.requireNonNull(getViewByPosition(getRecyclerView(), i, R.id.item_oil_type_tv)).setSelected(false);
            }
        }
        notifyDataSetChanged();
    }
}
