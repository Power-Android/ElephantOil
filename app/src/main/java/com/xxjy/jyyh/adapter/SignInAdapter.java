package com.xxjy.jyyh.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.entity.SignInDayBean;

import java.util.List;

/**
 * @author power
 * @date 3/24/21 3:09 PM
 * @project ElephantOil
 * @description:
 */
public class SignInAdapter extends BaseQuickAdapter<SignInDayBean, BaseViewHolder> {

    private int currentDayPosition = 0;

    public SignInAdapter(int layoutResId, @Nullable List<SignInDayBean> data) {
        super(layoutResId, data);
    }

    public void refresh(){
        currentDayPosition =0;
    }
    @Override
    protected void convert(@NonNull BaseViewHolder helper, SignInDayBean item) {

        helper.setText(R.id.integral_view, "+" + item.getIntelgral())
                .setText(R.id.day_view, item.getWeekStr());

        if (item.isCurrentDayFlag()) {
            currentDayPosition = helper.getAdapterPosition();
            if (item.isSignFlag()) {
                helper.setVisible(R.id.item_receive_tv, false);
            } else {
                helper.setVisible(R.id.item_receive_tv, true);
                helper.setText(R.id.item_receive_tv, "今日可领");
            }
        } else {
            if (!item.isCurrentDayFlag()&&!item.isSignFlag()) {
                if(helper.getAdapterPosition()==currentDayPosition+1&&getData().get(currentDayPosition).isSignFlag()&&getData().get(currentDayPosition).isCurrentDayFlag()){
                    helper.setVisible(R.id.item_receive_tv, true);
                    helper.setText(R.id.item_receive_tv, "明日可领");

                } else{
                    helper.setVisible(R.id.item_receive_tv, false);
                }
            }else{
                helper.setVisible(R.id.item_receive_tv, false);
            }

        }

        if (item.isSignFlag()) {
            if(helper.getAdapterPosition()==getData().size()-1){
                helper.setBackgroundRes(R.id.item_coupon_cl, R.drawable.sign_ready_last_icon);

            }else{
                helper.setBackgroundRes(R.id.item_coupon_cl, R.drawable.sign_ready_icon);
            }

            helper.getView(R.id.integral_view).setAlpha(1f);
        } else {
            if(helper.getAdapterPosition()==getData().size()-1){
                helper.setBackgroundRes(R.id.item_coupon_cl, R.drawable.sign_un_ready_last_icon);

            }else{
                helper.setBackgroundRes(R.id.item_coupon_cl, R.drawable.sign_un_ready_icon);
            }

            helper.getView(R.id.integral_view).setAlpha(0.43f);
        }
    }
}
