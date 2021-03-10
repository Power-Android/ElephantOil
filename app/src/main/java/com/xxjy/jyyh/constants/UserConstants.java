package com.xxjy.jyyh.constants;


import android.text.TextUtils;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.xxjy.jyyh.utils.Util;
import com.xxjy.jyyh.utils.locationmanger.MapLocationHelper;

import java.util.Date;

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

    public static void setOpenId(String openId) {
        SPUtils.getInstance().put(SPConstants.OPEN_ID, "");
    }

    public static String getOpenId() {
        return SPUtils.getInstance().getString(SPConstants.OPEN_ID);
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

    public static void setFirstOpen(boolean firstOpen) {
        SPUtils.getInstance().put(SPConstants.FIRST_OPEN, firstOpen);
    }

    public static boolean getFirstOpen() {
        return SPUtils.getInstance().getBoolean(SPConstants.FIRST_OPEN);
    }

    public static void setAgreePrivacy(boolean isAgree) {
        SPUtils.getInstance().put(SPConstants.AGREE_PRIVACY, isAgree);
    }

    public static boolean getAgreePrivacy() {
        return SPUtils.getInstance().getBoolean(SPConstants.AGREE_PRIVACY);
    }

    public static void setAppChannel(String appChannel) {
        SPUtils.getInstance().put(SPConstants.APP_CHANNEL_KEY, appChannel);
    }

    public static String getAppChannel() {
        return SPUtils.getInstance().getString(SPConstants.APP_CHANNEL_KEY);
    }

    public static void setGoneIntegral(boolean b) {
        SPUtils.getInstance().put(SPConstants.GONE_INTEGRAL, b);
    }

    public static boolean getGoneIntegral() {
        return SPUtils.getInstance().getBoolean(SPConstants.GONE_INTEGRAL, false);
    }

    public static void setNotificationRemind(boolean b) {
        String str = TimeUtils.date2String(new Date(), "yyyy-MM-dd") + "@_@" + b;
        SPUtils.getInstance().put(SPConstants.NOTIFICATION_REMIND, str);
    }

    public static boolean getNotificationRemind() {
        String content = SPUtils.getInstance().getString(SPConstants.NOTIFICATION_REMIND);
        if (TextUtils.equals(content, TimeUtils.date2String(new Date(), "yyyy-MM-dd") + "@_@" + true)) {
            return true;
        } else {
            return false;
        }
    }
    public static void setNotificationRemindVersion(boolean b) {
        String str = Util.getVersionName() + "@_@" + b;
        SPUtils.getInstance().put(SPConstants.NOTIFICATION_REMIND_VERSION, str);
    }

    public static boolean getNotificationRemindVersion() {
        String content = SPUtils.getInstance().getString(SPConstants.NOTIFICATION_REMIND_VERSION);
        if (TextUtils.equals(content, Util.getVersionName() + "@_@" + true)) {
            return true;
        } else {
            return false;
        }
    }

    public static void setNotificationRemindUserCenter(boolean b) {
        SPUtils.getInstance().put(SPConstants.NOTIFICATION_REMIND_USER_CENTER, b);
    }

    public static boolean getNotificationRemindUserCenter() {
        return SPUtils.getInstance().getBoolean(SPConstants.NOTIFICATION_REMIND_USER_CENTER,false);
    }


    //定位信息
    public static String getLocation() {
        if (MapLocationHelper.getLocationLatitude() != 0 && MapLocationHelper.getLocationLongitude() != 0) {
            return MapLocationHelper.getLocationLongitude() + "," + MapLocationHelper.getLocationLatitude();
        } else {
            return "";
        }
    }

    /**
     * @return 城市编码
     */
    public static String getCityCode() {
        if (MapLocationHelper.getCityCode() != null) {
            return MapLocationHelper.getCityCode();
        } else {
            return "";
        }
    }

    /**
     * @return 区域编码
     */
    public static String getAdCode() {
        if (MapLocationHelper.getAdCode() != null) {
            return MapLocationHelper.getAdCode();
        } else {
            return "";
        }
    }
}
