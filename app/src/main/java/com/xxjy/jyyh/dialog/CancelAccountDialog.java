package com.xxjy.jyyh.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.databinding.DialogCallHelpBinding;
import com.xxjy.jyyh.databinding.DialogCancelAccountBinding;
import com.xxjy.jyyh.ui.setting.AboutUsViewModel;
import com.xxjy.jyyh.ui.web.WebChatActivity;

public class CancelAccountDialog {
    private Context mContext;
    private QMUIFullScreenPopup mPopup;
    private final DialogCancelAccountBinding mBinding;
    private String mPhone;
    private String mLink;
    private String mTime;

    private AboutUsViewModel aboutUsViewModel;


    public CancelAccountDialog(Context context) {
        this.mContext = context;
        aboutUsViewModel = new ViewModelProvider((BaseActivity) context).get(AboutUsViewModel.class);
        mBinding = DialogCancelAccountBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_cancel_account, null));
        init();
    }

    private void init() {
        mPopup = QMUIPopups.fullScreenPopup(mContext)
                .addView(mBinding.getRoot())
                .closeBtn(false)
                .skinManager(QMUISkinManager.defaultInstance(mContext));
        mPopup.onBlankClick(popup -> {
            dismiss();
        });
        mBinding.callHelpOnline.setOnClickListener(v -> {
            dismiss();
            toWebChatOnline(mContext, mLink);
        });
        mBinding.callHelpPhone.setOnClickListener(v -> {
            dismiss();
            toDialPhoneAct(mContext, mPhone);
        });

        aboutUsViewModel.getCallCenter();
        aboutUsViewModel.callCenterLiveData.observe((BaseActivity) mContext, data -> {
            this.mLink = data.getCallOnline();
            this.mPhone = data.getCallPhone();
            this.mTime = data.getCallNotice();
        });

    }

    public void show(View view) {
        if (mPopup != null) {
            mPopup.show(view);
        }
    }

    public void dismiss() {
        if (mPopup != null) {
            mPopup.dismiss();
        }
    }

    /**
     * 跳转拨打电话界面
     *
     * @param activity
     * @param phoneNumber
     * @return
     */
    public static boolean toDialPhoneAct(Context activity, String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        }
        try {
            Uri phoneUri = Uri.parse("tel:" + phoneNumber);
            Intent intent = new Intent(Intent.ACTION_DIAL, phoneUri);
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                activity.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
        }
        ((BaseActivity) activity).showToastWarning("您的设备无法拨打电话");
        return false;
    }

    /**
     * 跳转在线客服
     *
     * @param activity
     * @param kFLink
     */
    public static void toWebChatOnline(Context activity, String kFLink) {
        if (TextUtils.isEmpty(kFLink)) {
            ((BaseActivity) activity).showToastWarning("在线客服暂无法连接, 请选择其他方式");
            return;
        }
        WebChatActivity.openWebChatActivity(activity, kFLink);
    }
}
