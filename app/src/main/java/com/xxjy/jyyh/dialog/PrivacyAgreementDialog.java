package com.xxjy.jyyh.dialog;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BaseDialog;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.utils.NaviActivityInfo;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/12/11.
 * <p>
 * 隐私协议dialog
 */

public class PrivacyAgreementDialog extends BaseDialog implements View.OnClickListener {
    private BaseActivity activity;
    private static final String USER_XIE_YI = Constants.USER_XIE_YI;
    private static final String YINSI_ZHENG_CE = Constants.YINSI_ZHENG_CE;


    private FrameLayout layout1,layout2,layout3,layoutCancel,layoutConfirm;


    public PrivacyAgreementDialog(BaseActivity context) {
        super(context, Gravity.CENTER, false, false);
        activity = context;
        initListener();
        initView();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.dialog_privacy_agreement;
    }

    private void initView(){
        layout1 =view.findViewById(R.id.layout_1);
        layout2 =view.findViewById(R.id.layout_2);
        layout3 =view.findViewById(R.id.layout_3);
        layoutConfirm =view.findViewById(R.id.layout_confirm);
        layoutCancel =view.findViewById(R.id.layout_cancel);
    }
    private void initListener() {
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
    }

    @Override
    protected void initLayout(WindowManager.LayoutParams layoutParams) {
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
    }

    /**
     * 获取取消的引用
     *
     * @return
     */
    public View getCancel() {
        return layoutCancel;
    }

    /**
     * 获取同意的引用
     *
     * @return
     */
    public View getConfirm() {
        return layoutConfirm;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_1:     //用户协议
                NaviActivityInfo.disPathIntentFromUrl(activity, USER_XIE_YI);
                break;
            case R.id.layout_2:     //隐私政策
            case R.id.layout_3:
                NaviActivityInfo.disPathIntentFromUrl(activity, YINSI_ZHENG_CE);
                break;
        }
    }

}
