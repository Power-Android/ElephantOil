package com.xxjy.jyyh.constants;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.bigkoo.pickerview.listener.ISelectTimeCallback;
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
    public static void setFirstOpen(boolean firstOpen){
        SPUtils.getInstance().put(SPConstants.FIRST_OPEN, firstOpen);
    }
    public static boolean getFirstOpen(){
       return SPUtils.getInstance().getBoolean(SPConstants.FIRST_OPEN);
    }
    public static void setAgreePrivacy(boolean isAgree){
        SPUtils.getInstance().put(SPConstants.AGREE_PRIVACY, isAgree);
    }
    public static boolean getAgreePrivacy(){
       return SPUtils.getInstance().getBoolean(SPConstants.AGREE_PRIVACY);
    }
    public static void setAppChannel(String appChannel){
        SPUtils.getInstance().put(SPConstants.APP_CHANNEL_KEY, appChannel);
    }
    public static String getAppChannel(){
       return SPUtils.getInstance().getString(SPConstants.APP_CHANNEL_KEY);
    }

}
