package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.View;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.databinding.DialogLocationTipsBinding;
import com.xxjy.jyyh.databinding.DialogTipsBinding;

/**
 * @author power
 * @date 1/21/21 8:26 PM
 * @project ElephantOil
 * @description:
 */
public class TipsDialog {
    private Context mContext;
    private QMUIFullScreenPopup mPopup;
    private DialogTipsBinding mBinding;
    private View mView;

    private String mContent;
    public TipsDialog(Context context, View view,String content) {
        this.mContext = context;
        this.mView = view;
        this.mContent = content;
        mBinding = DialogTipsBinding.bind(
                View.inflate(mContext,R.layout.dialog_tips, null));
        init();
    }

    private void init() {
        if (mPopup == null) {
            mPopup = QMUIPopups.fullScreenPopup(mContext)
                    .addView(mBinding.getRoot())
                    .closeBtn(false)
                    .skinManager(QMUISkinManager.defaultInstance(mContext));

        }
        mBinding.contentView.setText(mContent);
        mPopup.onBlankClick(popup -> popup.dismiss());

        mBinding.btView.setOnClickListener(view -> {
            if (mOnClickListener != null){
                mOnClickListener.onClick(view);
            }
            mPopup.dismiss();
        });
    }

public void setContent(String content){
        this.mContent=content;
        mBinding.contentView.setText(content);
}

    public void show(){
        mPopup.show(mView);
    }

    public interface OnClickListener{
        void onClick(View view);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }
}
