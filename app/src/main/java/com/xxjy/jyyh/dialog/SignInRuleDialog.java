package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.LayoutInflater;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.databinding.DialogOilMonthRuleLayoutBinding;
import com.xxjy.jyyh.databinding.DialogSignInRuleLayoutBinding;

/**
 * @author power
 * @date 3/18/21 2:34 PM
 * @project ElephantOil
 * @description:
 */
public class SignInRuleDialog extends QMUIFullScreenPopup {
    private Context mContext;
    private String content;
    private final DialogSignInRuleLayoutBinding mBinding;

    public SignInRuleDialog(Context context, String monthCouponRule) {
        super(context);
        this.mContext = context;
        this.content = monthCouponRule;
        mBinding = DialogSignInRuleLayoutBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_sign_in_rule_layout, null));
        init();
        initData();
    }

    private void init() {
        addView(mBinding.getRoot());
        closeBtn(false);
        skinManager(QMUISkinManager.defaultInstance(mContext));

        mBinding.closeIv.setOnClickListener(view -> {
            dismiss();
        });
    }

    private void initData() {

        mBinding.contentView.setText(content);
    }


    public interface OnItemClickedListener {
        void onQueryClick();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
