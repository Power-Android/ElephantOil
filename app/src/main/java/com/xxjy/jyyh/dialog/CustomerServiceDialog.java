package com.xxjy.jyyh.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.popup.QMUIFullScreenPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.databinding.DialogCallHelpBinding;
import com.xxjy.jyyh.databinding.DialogOilTipsLayoutBinding;
import com.xxjy.jyyh.ui.setting.AboutUsViewModel;
import com.xxjy.jyyh.ui.web.WebChatActivity;

public class CustomerServiceDialog {
    private Context mContext;
    private QMUIFullScreenPopup mPopup;
    private final DialogCallHelpBinding mBinding;
    private String mPhone;
    private String mLink;
    private String mTime;

    private AboutUsViewModel aboutUsViewModel;


    public CustomerServiceDialog(Context context) {
        this.mContext = context;
        aboutUsViewModel = new ViewModelProvider((BaseActivity) context).get(AboutUsViewModel.class);
        mBinding = DialogCallHelpBinding.bind(
                LayoutInflater.from(context).inflate(R.layout.dialog_call_help, null));
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
            mBinding.callHelpWorkTime.setText(this.mTime);
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
     * ????????????????????????
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
        ((BaseActivity) activity).showToastWarning("??????????????????????????????");
        return false;
    }

    /**
     * ??????????????????
     *
     * @param activity
     * @param kFLink
     */
    public static void toWebChatOnline(Context activity, String kFLink) {
        if (TextUtils.isEmpty(kFLink)) {
            ((BaseActivity) activity).showToastWarning("???????????????????????????, ?????????????????????");
            return;
        }
        WebChatActivity.openWebChatActivity(activity, kFLink);
    }
}
