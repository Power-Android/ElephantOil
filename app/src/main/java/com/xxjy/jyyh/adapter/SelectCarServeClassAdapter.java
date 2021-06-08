package com.xxjy.jyyh.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author power
 * @date 2021/6/7 2:47 下午
 * @project ElephantOil
 * @description:
 */
public class SelectCarServeClassAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int selectPosition=0;
    public SelectCarServeClassAdapter(int layoutResId, @Nullable @org.jetbrains.annotations.Nullable List<String> data) {
        super(layoutResId, data);
    }

    public void setSelectPosition(int position){
        selectPosition=position;
        notifyDataSetChanged();
    }
    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, String item) {
        helper.setText(R.id.title_tv, item);
        if(helper.getAdapterPosition()==selectPosition){
            helper.getView(R.id.line_view).setVisibility(View.VISIBLE);
        }else{
            helper.getView(R.id.line_view).setVisibility(View.INVISIBLE);
        }
    }

}
