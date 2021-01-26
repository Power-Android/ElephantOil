package com.xxjy.jyyh.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SpanUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.entity.OrderNewsEntity;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;

import java.util.List;

public class TopLineAdapter extends BannerAdapter<OrderNewsEntity, TopLineAdapter.TopLineHolder> {


    private boolean mIsWhite=false;
    public TopLineAdapter(List<OrderNewsEntity> datas,boolean isWhite) {
        super(datas);
        this.mIsWhite=isWhite;
    }

    @Override
    public TopLineHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new TopLineHolder(BannerUtils.getView(parent, R.layout.item_home_message));
    }

    @Override
    public void onBindView(TopLineHolder holder, OrderNewsEntity data, int position, int size) {
        holder.message.setTextColor(App.getContext().getResources().getColor(
                mIsWhite ? R.color.white : R.color.color_34));
        SpanUtils.with(holder.message)
                .append("车主")
                .append(data.getAccount())
                .append("加油")
                .append(String.valueOf(data.getAmount()))
                .append("元，节省")
                .append(String.valueOf(data.getDiscount()))
                .setForegroundColor(App.getContext().getResources().getColor(
                        mIsWhite ? R.color.white : R.color.colorAccent))
                .append("元")
                .create();
    }

    class TopLineHolder extends RecyclerView.ViewHolder{
        public TextView message;

        public TopLineHolder(@NonNull View view){
            super(view);
            message = view.findViewById(R.id.message);
        }
    }
}
