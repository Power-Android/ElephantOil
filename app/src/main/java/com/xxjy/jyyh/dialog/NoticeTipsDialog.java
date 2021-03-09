package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.databinding.DialogNotificeTipsLayoutBinding;


/**
 * @author power
 * @date 1/22/21 5:59 PM
 * @project ElephantOil
 * @description:
 */
public class NoticeTipsDialog extends QMUIFullScreenPopup {
    private Context mContext;
    private final DialogNotificeTipsLayoutBinding mBinding;


    public NoticeTipsDialog(Context context) {
        super(context);
        this.mContext = context;
        mBinding = DialogNotificeTipsLayoutBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_notifice_tips_layout, null));
        init();
    }

    private void init() {
        addView(mBinding.getRoot());
        closeBtn(false);
        skinManager(QMUISkinManager.defaultInstance(mContext));

        mBinding.queryTv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null) {
                mOnItemClickedListener.onQueryClick();
            }
        });

        mBinding.closeIv.setOnClickListener(view -> {
//            if (mOnItemClickedListener != null) {
//                mOnItemClickedListener.onQueryClick();
//            }
            dismiss();
        });
        mBinding.noOpenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    public interface OnItemClickedListener {
        void onQueryClick();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
