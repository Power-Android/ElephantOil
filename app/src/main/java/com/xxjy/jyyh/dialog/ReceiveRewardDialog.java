package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.databinding.DialogOilTipsLayoutBinding;
import com.xxjy.jyyh.databinding.DialogReceiveRewardLayoutBinding;

public class ReceiveRewardDialog {
    private Context mContext;
    private QMUIFullScreenPopup mPopup;
    private final DialogReceiveRewardLayoutBinding mBinding;


    public ReceiveRewardDialog(Context context) {
        this.mContext = context;
        mBinding = DialogReceiveRewardLayoutBinding.bind(
               LayoutInflater.from(context).inflate( R.layout.dialog_receive_reward_layout, null));
        init();
    }

    private void init() {
        mPopup = QMUIPopups.fullScreenPopup(mContext)
                .addView(mBinding.getRoot())
                .closeBtn(false)
                .skinManager(QMUISkinManager.defaultInstance(mContext));

        mBinding.queryTv.setOnClickListener(view -> {
            if (mOnItemClickedListener != null){
                mOnItemClickedListener.onQueryClick(view);
            }
        });
        mBinding.closeIv.setOnClickListener(v ->{
            dismiss();
        });
    }

    public void show(View view) {
        if (mPopup != null) {
            mPopup.show(view);
        }
    }

    public void dismiss(){
        if (mPopup != null){
            mPopup.dismiss();
        }
    }

    public interface OnItemClickedListener{
        void onQueryClick(View view);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener){
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
