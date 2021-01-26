package com.xxjy.jyyh.utils.symanager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.chuanglan.shanyan_sdk.listener.ShanYanCustomInterface;
import com.chuanglan.shanyan_sdk.tool.ShanYanUIConfig;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.constants.Constants;


public class SYConfigUtils {

    private static ConstraintLayout invitationLayout;
    private static boolean isDown = false;
    public static String inviteCode = "";
    public static String phoneNum = "";

    //沉浸式竖屏样式
    public static ShanYanUIConfig getCJSConfig(final Context context, ShanYanCustomInterface relativeLayoutClick,
                                               ShanYanCustomInterface thirdLoginClick) {
        /************************************************自定义控件**************************************************************/
        Drawable backgruond = new ColorDrawable(Color.WHITE);
        Drawable returnBg = context.getResources().getDrawable(R.drawable.icon_tb_close_bg);
        Drawable logoBg = context.getResources().getDrawable(R.drawable.logo);
        //登录按钮背景
        Drawable logBtnBg = context.getResources().getDrawable(R.drawable.selector_btn_login_one_key_bg_state);
//        Drawable checkBoxCheck = context.getResources().getDrawable(R.mipmap.icon_state_xieyi_checked_bg);
//        Drawable checkBoxUnCheck = context.getResources().getDrawable(R.mipmap.icon_state_xieyi_uncheck_bg);

        //loading自定义加载框
        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout view_dialog = (RelativeLayout) inflater.inflate(R.layout.shanyan_demo_dialog_layout, null);
        RelativeLayout.LayoutParams mLayoutParams3 =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        view_dialog.setLayoutParams(mLayoutParams3);
        view_dialog.setVisibility(View.GONE);

//        //号码栏背景
//        LayoutInflater numberinflater = LayoutInflater.from(context);
//        RelativeLayout numberLayout = (RelativeLayout) numberinflater.inflate(R.layout.shanyan_demo_phobackground, null);
//        RelativeLayout.LayoutParams numberParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        numberParams.setMargins(0, 0, 0, AbScreenUtils.dp2px(context, 250));
//        numberParams.width = AbScreenUtils.getScreenWidth(context, false) - AbScreenUtils.dp2px(context, 50);
//        numberParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        numberParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        numberLayout.setLayoutParams(numberParams);

        invitationLayout = (ConstraintLayout) LayoutInflater.from(context)
                .inflate(R.layout.invitation_layout, null);
        ConstraintLayout.LayoutParams invitationPrarms =
                new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        invitationPrarms.setMargins(0, SizeUtils.dp2px(290), 0, 0);
        invitationLayout.setLayoutParams(invitationPrarms);

        ImageView iv = invitationLayout.findViewById(R.id.iv1);
        View iv2 = invitationLayout.findViewById(R.id.iv2);
        EditText et = invitationLayout.findViewById(R.id.invitation_et);
        invitationLayout.findViewById(R.id.tv1).setOnClickListener(view -> {
            if (isDown){
                iv.animate().setDuration(200).rotation(90).start();
                et.setVisibility(View.VISIBLE);
                iv2.setVisibility(View.VISIBLE);
            }else {
                iv.animate().setDuration(200).rotation(0).start();
                et.setVisibility(View.GONE);
                iv2.setVisibility(View.GONE);
            }
            isDown = !isDown;
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inviteCode = editable.toString();
            }
        });

        //其他方式登录
        LayoutInflater otherLoginInflater1 = LayoutInflater.from(context);
        FrameLayout otherLoginLayout = (FrameLayout)
                otherLoginInflater1.inflate(R.layout.shanyan_sdk_other_login, null);
        RelativeLayout.LayoutParams otherParamsOther =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        otherParamsOther.addRule(RelativeLayout.CENTER_HORIZONTAL);
        otherParamsOther.setMargins(0, SizeUtils.dp2px(400), 0, 0);
        otherLoginLayout.setLayoutParams(otherParamsOther);

//        otherLoginLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (relativeLayoutClick != null) {
//                    relativeLayoutClick.onClick(context, v);
//                }
//            }
//        });

        FrameLayout thirdLogin = (FrameLayout)
                otherLoginInflater1.inflate(R.layout.shanyan_sdk_other_wx_login, null);
        RelativeLayout.LayoutParams layoutParamsOther =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsOther.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParamsOther.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        thirdLogin.setLayoutParams(layoutParamsOther);

        ImageView wxLoginView = thirdLogin.findViewById(R.id.login_for_wx);
        if (wxLoginView != null) {
            wxLoginView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (thirdLoginClick != null) {
                        thirdLoginClick.onClick(context, v);
                    }
                }
            });
        }

//        RelativeLayout.LayoutParams layoutParamsOther =
//                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                        RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layoutParamsOther.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        otherLoginLayout.setLayoutParams(layoutParamsOther);

        ShanYanUIConfig uiConfig = new ShanYanUIConfig.Builder()
                //设置背景
                .setAuthBGImgPath(backgruond)

                //设置授权页进出场动画,使用默认
                .setActivityTranslateAnim("shanyan_login_in", "shanyan_login_out")

                //授权页状态栏,使用默认
                .setStatusBarHidden(false)

                //授权页导航栏：(导航栏就是标题栏)
                .setFullScreen(false)
                .setNavColor(Color.WHITE)  //设置导航栏颜色
                .setNavText("")  //设置导航栏标题文字
                .setNavReturnImgPath(returnBg)
                .setNavReturnBtnWidth(22)
                .setNavReturnBtnHeight(22)
                .setNavReturnBtnOffsetX(19)
                .setNavReturnBtnOffsetY(19)

                //授权页logo：
                .setLogoImgPath(logoBg)
                .setLogoWidth(70)
                .setLogoHeight(70)
                .setLogoOffsetY(40)
                .setLogoHidden(false)   //是否隐藏logo

                //授权页号码栏：
                .setNumberColor(Color.parseColor("#282828"))  //设置手机号码字体颜色
                .setNumberSize(27)
//                .setNumberBold(true)
                .setNumFieldOffsetY(170)    //设置号码栏相对于标题栏下边缘y偏移

                //授权页登录按钮：
                .setLogBtnText("本机号码一键登录")  //设置登录按钮文字
                .setLogBtnTextColor(Color.parseColor("#ffffff"))   //设置登录按钮文字颜色
                .setLogBtnImgPath(logBtnBg)   //设置登录按钮图片
                .setLogBtnOffsetY(230)
                .setLogBtnTextSize(15)
                .setLogBtnHeight(45)
                .setLogBtnWidth(getScreenWidth(context, true) - 56)

                //授权页隐私栏：
                .setAppPrivacyOne("服务协议", Constants.USER_XIE_YI)  //设置开发者隐私条款1名称和URL(名称，url)
                .setAppPrivacyTwo("隐私政策", Constants.YINSI_ZHENG_CE)  //设置开发者隐私条款2名称和URL(名称，url)
                .setPrivacySmhHidden(false)
                .setPrivacyTextSize(12)
                .setAppPrivacyColor(Color.parseColor("#999999"), Color.parseColor("#414141"))    //	设置隐私条款名称颜色(基础文字颜色，协议文字颜色)
                .setPrivacyOffsetY(500)
//                .setPrivacyOffsetBottomY(260)
                .setPrivacyOffsetGravityLeft(false)
                .setPrivacyState(true)
                .setPrivacyText("登录即同意遵守", "和", "、", "、", "以及个人敏感信息政策")
                .setPrivacyTextBold(false)
                .setPrivacyCustomToastText("请您查看并同意我们的服务协议等信息政策")
                .setPrivacyNameUnderline(false)
                .setPrivacyGravityHorizontalCenter(true)
                .setPrivacyOffsetX(67)
//                .setCheckBoxWH(30, 19)
                .setCheckBoxHidden(true)
//                .setCheckedImgPath(checkBoxCheck)
//                .setUncheckedImgPath(checkBoxUnCheck)
//                .setCheckBoxMargin(0, 0, 10, 0)


                //授权页slogan
                .setSloganHidden(false)
                .setSloganTextColor(Color.parseColor("#A6A6A6"))
                .setSloganTextSize(13)
                .setSloganOffsetY(130)

                //授权页创蓝slogan
                .setShanYanSloganHidden(true)

                //授权页loading
                .setLoadingView(view_dialog)

                //授权页相对控件设置（指定在登录按钮和协议栏之间）,使用默认
//                .setRelativeCustomView(otherLoginLayout, false, 0, 19, 0, 19, relativeLayoutClick)

                //协议页导航栏
//                .setPrivacyNavReturnImgHidden(true)

                //授权页遮盖层
                .setDialogDimAmount(0f)

                //隐私协议提示弹框
//                .addCustomPrivacyAlertView()

                //添加自定义控件
//                .addCustomView(numberLayout, false, false, null)
                .addCustomView(thirdLogin, false, false, null)
                .addCustomView(otherLoginLayout, false, false, relativeLayoutClick)
                .addCustomView(invitationLayout, false, false, null)

                .build();
        return uiConfig;

    }

    public static int getScreenWidth(Context context, boolean isDp) {
        int screenWidth = 0;
        int winWidth;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        if (point.x > point.y) {
            winWidth = point.y;
        } else {
            winWidth = point.x;
        }
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        if (!isDp) {
            return winWidth;
        }
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        screenWidth = (int) (winWidth / density);// 屏幕高度(dp)
        return screenWidth;
    }

    public static class ShanYanResultBean {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
