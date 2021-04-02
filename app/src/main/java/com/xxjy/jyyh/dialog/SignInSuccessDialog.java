package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.blankj.utilcode.util.SpanUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.databinding.DialogOilMonthRuleLayoutBinding;
import com.xxjy.jyyh.databinding.DialogSignInSuccessLayoutBinding;
import com.xxjy.jyyh.entity.SignInBean;
import com.xxjy.jyyh.entity.SignInDayBean;
import com.xxjy.jyyh.entity.SignInResultBean;

/**
 * @author power
 * @date 3/25/21 4:56 PM
 * @project ElephantOil
 * @description:
 */
public class SignInSuccessDialog extends QMUIFullScreenPopup {
    private Context mContext;
    private final DialogSignInSuccessLayoutBinding mBinding;

    public SignInSuccessDialog(Context context, BaseActivity activity) {
        super(context);
        this.mContext = context;
        mBinding = DialogSignInSuccessLayoutBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_sign_in_success_layout, null));
        init();
    }

    private void init() {
        addView(mBinding.getRoot());
        closeBtn(false);
        onBlankClick(new OnBlankClickListener() {
            @Override
            public void onBlankClick(QMUIFullScreenPopup popup) {
                popup.dismiss();
            }
        });
        skinManager(QMUISkinManager.defaultInstance(mContext));
        mBinding.closeView.setOnClickListener(v ->{
            dismiss();
        });
        mBinding.view4.setOnClickListener(v -> {
            dismiss();
        });
    }



    public void setData(SignInResultBean bean, SignInBean signInBean) {
        mBinding.integralTv.setText(bean.getIntegral()+"");
        if (bean.isSendCouponFlag()) {
            mBinding.view1.setVisibility(View.GONE);
            mBinding.view2.setVisibility(View.GONE);
            mBinding.view5.setVisibility(View.VISIBLE);
            mBinding.view4.setVisibility(View.VISIBLE);
            SpanUtils.with(mBinding.view5)
                    .append("恭喜！您额外获得了")
                    .append(bean.getCouponAmount() + "元加油券")
                    .setForegroundColor(Color.parseColor("#FF593E"))
                    .create();
        } else {
            mBinding.view1.setVisibility(View.VISIBLE);
            mBinding.view2.setVisibility(View.VISIBLE);
            mBinding.view5.setVisibility(View.GONE);
            mBinding.view4.setVisibility(View.GONE);
            mBinding.view2.setText(String.format("您已签到 %d 天", bean.getSignDays()));
            SpanUtils.with(mBinding.view1)
                    .append("本周签到满 "+signInBean.getList().size() + " 天，将额外获得 ")
                    .setForegroundColor(Color.parseColor("#1676FF"))
                    .append(signInBean.getCouponAmount() + " 元加油券")
                    .setForegroundColor(Color.parseColor("#FF593E"))
                    .create();
        }

    }


    public interface OnItemClickedListener {
        void onQueryClick();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
