package com.xxjy.jyyh.dialog;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.DialogHomeAdLayoutBinding;
import com.xxjy.jyyh.databinding.DialogHomeNewUserLayoutBinding;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.entity.HomeNewUserBean;
import com.xxjy.jyyh.utils.GlideUtils;
import com.xxjy.jyyh.utils.NaviActivityInfo;

public class HomeNewUserDialog {
    private Context mContext;
    private QMUIFullScreenPopup mPopup;
    private final DialogHomeNewUserLayoutBinding mBinding;
    private HomeNewUserBean mBean;
    //提前创建一个Singleton
    private static final HomeNewUserDialog instance = new HomeNewUserDialog();
    //有调用者直接就拿出来给了
    public static HomeNewUserDialog getInstance() {
        return instance;
    }
    private HomeNewUserDialog() {
        mBinding = DialogHomeNewUserLayoutBinding.bind(
                LayoutInflater.from(App.getContext()).inflate( R.layout.dialog_home_new_user_layout, null));
    }
//    public HomeNewUserDialog(Context context, HomeNewUserBean bean) {
//
//        mBinding = DialogHomeNewUserLayoutBinding.bind(
//               LayoutInflater.from(context).inflate( R.layout.dialog_home_new_user_layout, null));
//        init();
//    }
//
 public void setData(Context context, HomeNewUserBean bean){
     this.mContext = context;
     this.mBean = bean;
     init();
 }

    private void init() {
        mPopup = QMUIPopups.fullScreenPopup(mContext)
                .addView(mBinding.getRoot())
                .closeBtn(false)
                .skinManager(QMUISkinManager.defaultInstance(mContext));

        mBinding.closeIv.setOnClickListener(v ->{
            if(mOnItemClickedListener!=null){
                mOnItemClickedListener.onCloseClick(v);
            }
            dismiss();
        });
        GlideUtils.loadImage(mContext,mBean.getNewUrl(),mBinding.adImg);
        mBinding.adImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NaviActivityInfo.disPathIntentFromUrl((BaseActivity) mContext,mBean.getH5Link());
                dismiss();
            }
        });
    }

    public void show(View view) {
        if (mPopup != null) {
            if(!UserConstants.getShowNewUserRedPacket()){
                mPopup.show(view);
                UserConstants.setShowNewUserRedPacket();
            }

        }
    }

    public void dismiss(){
        if (mPopup != null){
            mPopup.dismiss();
        }
    }

    public interface OnItemClickedListener{
        void onCloseClick(View view);
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener){
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
