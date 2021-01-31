package com.xxjy.jyyh.ui.broadcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.databinding.DialogHomeAdLayoutBinding;
import com.xxjy.jyyh.databinding.DialogReceiveRewardLayoutBinding;
import com.xxjy.jyyh.entity.BannerBean;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.GlideUtils;
import com.xxjy.jyyh.utils.NaviActivityInfo;

public class HomeAdDialog {
    private Context mContext;
    private QMUIFullScreenPopup mPopup;
    private final DialogHomeAdLayoutBinding mBinding;

private BannerBean mBannerBean;
    public HomeAdDialog(Context context, BannerBean bannerBean) {
        this.mContext = context;
        this.mBannerBean = bannerBean;
        mBinding = DialogHomeAdLayoutBinding.bind(
               LayoutInflater.from(context).inflate( R.layout.dialog_home_ad_layout, null));
        init();
    }

    private void init() {
        mPopup = QMUIPopups.fullScreenPopup(mContext)
                .addView(mBinding.getRoot())
                .closeBtn(false)
                .skinManager(QMUISkinManager.defaultInstance(mContext));

        mBinding.closeIv.setOnClickListener(v ->{
            dismiss();
        });
        GlideUtils.loadImage(mContext,mBannerBean.getImgUrl(),mBinding.adImg);
        mBinding.adImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NaviActivityInfo.disPathIntentFromUrl((BaseActivity) mContext,mBannerBean.getLink());
            }
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
