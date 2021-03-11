package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.databinding.DialogNotificeTipsLayoutBinding;
import com.xxjy.jyyh.databinding.DialogPriceDescriptionLayoutBinding;


/**
 * @author power
 * @date 1/22/21 5:59 PM
 * @project ElephantOil
 * @description:
 */
public class PriceDescriptionDialog extends QMUIFullScreenPopup {
    private Context mContext;
    private final DialogPriceDescriptionLayoutBinding mBinding;


    public PriceDescriptionDialog(Context context) {
        super(context);
        this.mContext = context;
        mBinding = DialogPriceDescriptionLayoutBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_price_description_layout, null));
        init();
    }

    private void init() {
        addView(mBinding.getRoot());
        closeBtn(false);
        skinManager(QMUISkinManager.defaultInstance(mContext));

        mBinding.closeIv.setOnClickListener(view -> {
            dismiss();
        });

    }

}
