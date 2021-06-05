package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author power
 * @date 2021/5/31 3:36 下午
 * @project ElephantOil
 * @description:
 */
public class HomeMenuAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public HomeMenuAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, String item) {

    }
}
