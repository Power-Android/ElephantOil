package com.xxjy.jyyh.dialog;

import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SpanUtils;
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
    private TextView mContentTv;
    private TextView mRefuseTv;
    private TextView mAgreeTv;


    public PrivacyAgreementDialog(BaseActivity context) {
        super(context, Gravity.CENTER, false, false);
        activity = context;
        initView();
        initListener();
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


        mContentTv = view.findViewById(R.id.content_tv);
        mRefuseTv = view.findViewById(R.id.refuse_tv);
        mAgreeTv = view.findViewById(R.id.agree_tv);

        SpanUtils.with(mContentTv)
                .append("亲爱的用户，感谢您信任并使用小象加油！\n" +
                        "\n" +
                        "我们依据相关法律制定了")
                .append("《用户协议》")
                .setForegroundColor(activity.getResources().getColor(R.color.colorAccent))
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        NaviActivityInfo.disPathIntentFromUrl(activity, USER_XIE_YI);
                    }
                })
                .append("和")
                .append("《隐私政策》")
                .setForegroundColor(activity.getResources().getColor(R.color.colorAccent))
                .setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        NaviActivityInfo.disPathIntentFromUrl(activity, YINSI_ZHENG_CE);
                    }
                })
                .append("，请您在点击同意之前仔细阅读并充分理解相关条款，其中的重点条款已为您标注，方便您了解自己的权利。\n" +
                        "\n")
                .append("我们将通过《隐私政策》向您说明：\n" +
                        "1.为了您可以更好地享受周边的加油及汽车服务，我们会根据您的授权内容，收集和使用对应的必要信息（例如您的位置信息等）。\n" +
                        "2.您可以对上述信息进行访问、更正、删除也将提供专门的个人信息保护联系方式。\n" +
                        "3.未经您的授权同意，我们不会将上述信息共享给第三方或用于您未授权的其他用途。")
                .setBold()
                .append("\n" +
                        "\n" +
                        "小象加油将一如既往坚守使命，帮大家获得更优质的加油和车服服务！")
                .create();

    }
    private void initListener() {
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);

        mRefuseTv.setOnClickListener(this);
        mAgreeTv.setOnClickListener(this);
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
        return mRefuseTv;
    }

    /**
     * 获取同意的引用
     *
     * @return
     */
    public View getConfirm() {
        return mAgreeTv;
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
