package com.xxjy.jyyh.constants;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.xxjy.jyyh.app.App;

/**
 * @author power
 * @date 12/4/20 2:47 PM
 * @project RunElephant
 * @description:
 */
public class UserConstants {

    public static void setH5Links(String links) {
        SPUtils.getInstance().put(SPConstants.WEB_LINKS, links);
    }


    public static String getToken() {
        return SPUtils.getInstance().getString(SPConstants.APP_TOKEN, "");
    }

    public static void setToken(String token) {
        SPUtils.getInstance().put(SPConstants.APP_TOKEN, token);
    }

    public static Integer getUserType() {
        return SPUtils.getInstance().getInt(SPConstants.USER_TYPE, -1);
    }

    public static void setUserType(int userType) {
        SPUtils.getInstance().put(SPConstants.USER_TYPE, userType);
    }


    public static String getMobile() {
        return SPUtils.getInstance().getString(SPConstants.MOBILE, "");
    }

    public static void setMobile(String mobile) {
        SPUtils.getInstance().put(SPConstants.MOBILE, mobile);
    }

    public static String getUuid() {
        String uniqueDeviceId = DeviceUtils.getUniqueDeviceId();
        if (StringUtils.isEmpty(uniqueDeviceId)) {
            return "";
        }
        return uniqueDeviceId;
    }

    public static Boolean getIsLogin() {
        return SPUtils.getInstance().getBoolean(SPConstants.LOGIN_STATUS);
    }

    public static void setIsLogin(Boolean isLogin) {
        SPUtils.getInstance().put(SPConstants.LOGIN_STATUS, true);
    }

    public static String getLongitude() {
        return SPUtils.getInstance().getString(SPConstants.LONGITUDE, "0");
    }

    public static void setLongitude(String longitude) {
        SPUtils.getInstance().put(SPConstants.LONGITUDE, longitude);
    }

    public static String getLatitude() {
        return SPUtils.getInstance().getString(SPConstants.LATITUDE, "0");
    }

    public static void setLatitude(String latitude) {
        SPUtils.getInstance().put(SPConstants.LATITUDE, latitude);
    }

    /**
     * 获取 app 中的 渠道 channel 值
     *
     * @return
     */
    public static String getAppMetaChannel() {
        return getAppMetaData(App.getContext(), "UMENG_CHANNEL");
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值 ， 或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String metaName) {
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(metaName);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }
}
