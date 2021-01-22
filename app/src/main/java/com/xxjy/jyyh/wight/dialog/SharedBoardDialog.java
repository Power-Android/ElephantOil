package com.xxjy.jyyh.wight.dialog;

import android.view.Gravity;
import android.view.View;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BaseDialog;


public class SharedBoardDialog extends BaseDialog {

    private OnShareCheckListener listener;


    private View shareWx,shareWxFriends;
    public SharedBoardDialog(BaseActivity context) {
        super(context, Gravity.BOTTOM, 0, AnimationDirection.VERTICLE, true, true);
        initListener();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.dialog_share_board;
    }

    private void initListener() {
        shareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onShareChecked(SHARE_MEDIA.WEIXIN);
                }
                dismiss();
            }
        });
        shareWxFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onShareChecked(SHARE_MEDIA.WEIXIN_CIRCLE);
                }
                dismiss();
            }
        });
    }

    public void setOnShareCheckListener(OnShareCheckListener listener) {
        this.listener = listener;
    }

    public interface OnShareCheckListener {
        void onShareChecked(SHARE_MEDIA checkType);
    }
}
