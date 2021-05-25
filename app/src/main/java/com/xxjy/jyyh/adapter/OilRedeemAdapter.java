package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author power
 * @date 2021/5/24 2:27 下午
 * @project ElephantOil
 * @description:
 */
public class OilRedeemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public OilRedeemAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, String item) {

    }
}
