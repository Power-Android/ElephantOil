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
import com.xxjy.jyyh.databinding.DialogSignInSuccessLayoutBinding;

/**
 * @author power
 * @date 3/25/21 4:56 PM
 * @project ElephantOil
 * @description:
 */
public class SignInSuccessDialog extends QMUIFullScreenPopup {
    private Context mContext;
    private BaseActivity mActivity;
    private final DialogSignInSuccessLayoutBinding mBinding;

    public SignInSuccessDialog(Context context, BaseActivity activity) {
        super(context);
        this.mContext = context;
        this.mActivity = activity;
        mBinding = DialogSignInSuccessLayoutBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_sign_in_success_layout, null));
        init();
        initData();
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
    }

    private void initData() {
        getHotIV();
    }

    private void getHotIV() {
//        RxHttp.postForm(ApiService.GET_BANNER_OF_POSITION)
//                .add("position", BannerPositionConstants.OIL_HOT_TIP)
//                .asResponseList(BannerBean.class)
//                .to(RxLife.toMain(mActivity))
//                .subscribe(data -> Glide.with(mContext)
//                        .load(data.get(0).getImgUrl())
//                        .into(mBinding.hotIv));
    }

    public interface OnItemClickedListener {
        void onQueryClick();
    }

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }
}
