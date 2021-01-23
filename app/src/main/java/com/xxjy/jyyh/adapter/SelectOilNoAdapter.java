package com.xxjy.jyyh.adapter;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.OilNumCheckEntity;

import java.util.List;

/**
 * @author power
 * @date 12/10/20 2:28 PM
 * @project RunElephant
 * @description:
 */
public class SelectOilNoAdapter extends BaseMultiItemQuickAdapter<OilNumCheckEntity, BaseViewHolder> {
    public static final int TYPETIELE = 1;
    public static final int TYPECONTENT = 2;
    private String mOilId;
    private int index = -1;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public SelectOilNoAdapter(List<OilNumCheckEntity> data, String oilId) {
        super(data);
        this.mOilId = oilId;
        addItemType(TYPETIELE, R.layout.adapter_oil_num_title);
        addItemType(TYPECONTENT, R.layout.adapter_oil_num_content);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, OilNumCheckEntity item) {
        if (!TextUtils.isEmpty(mOilId) && item.getOilListBeen() != null && mOilId.equals(item.getOilListBeen().getId() + "")) {
            index = helper.getAdapterPosition();
        }
        switch (item.getItemType()){
            case TYPETIELE:
//                helper.setText(R.id.title, item.getType());
                break;
            case TYPECONTENT:
//                helper.setText(R.id.oilNumber, item.getOilListBeen().getOilNum());
//                helper.getView(R.id.oilNumber).setSelected(index == helper.getAdapterPosition());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
