package com.xxjy.jyyh.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.SPUtils;
import com.xxjy.jyyh.ui.MainActivity;
import com.xxjy.jyyh.constants.SPConstants;

/**
 * @author power
 * @date 12/4/20 3:31 PM
 * @project RunElephant
 * @description:
 */
public class LoginHelper {
    public static CallBack callBack;

    public static void login(Context context, CallBack loginCallBack) {
        if (!SPUtils.getInstance().getBoolean(SPConstants.LOGIN_STATUS)) {
            callBack = loginCallBack;
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        } else {
            if (loginCallBack != null) {
                loginCallBack.onLogin();
            }
        }
    }

    public static void loginOut(Activity activity) {
        SPUtils.getInstance().getBoolean(SPConstants.LOGIN_STATUS,false);
        activity.finish();
    }

    public interface CallBack {
        void onLogin();
    }
}
