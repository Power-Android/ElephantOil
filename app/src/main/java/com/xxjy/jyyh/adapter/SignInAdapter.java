package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;

import java.util.List;

/**
 * @author power
 * @date 3/24/21 3:09 PM
 * @project ElephantOil
 * @description:
 */
public class SignInAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SignInAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        if (helper.getAdapterPosition() == 1){
            helper.setVisible(R.id.item_receive_tv, true);
        }
    }
}
