package com.xxjy.jyyh.utils.umengmanager;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.umeng.analytics.MobclickAgent;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.UserConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * 方法:
 */

public class UMengOnEvent {

    private static final String EVENT_PUBLIC_PARAM_USER_ID = "user_id";   //公共参数userID

    public static final String EVENT_ID_FIRST_START = "first_start";   //首次进入

    public static final String EVENT_ID_WELCOME_AD = "home_advertisement";   //启动页广告

    public static final String EVENT_ID_BANNER = "top_banner";   //banner
    public static final String EVENT_ID_HOME_VIP_CENTER = "home_vip_center";   //banner
    public static final String EVENT_ID_NAVI_QY = "navi_quanyi";   //导航权益
    public static final String EVENT_ID_NAVI_TOP_QY = "navi_top_quanyi";   //导航栏切换
    public static final String EVENT_ID_PRODUCT = "home_product";   //商品
    public static final String EVENT_ID_HOME_VIP = "home_vip";   //会员入会礼
    public static final String EVENT_ID_ZERO_BUY = "zero_buy";   //0元购

    public static final String EVENT_ID_HOME_CAP = "home_cap";   //首页会员点击


    public static final String EVENT_ID_STORE_BANNER = "store_banner";   //商城banner

    public static final String EVENT_ID_SPECIAL = "special";   //专题



    private UMengOnEvent() {
    }

    /**
     * 首次进入时间事件
     */
    public static void onFirstStartEvent() {
        boolean firstIn = UserConstants.getFirstOpen();
        if (!firstIn) {
            return;
        }
        UserConstants.setFirstOpen(true);
        Map<String, String> params = new HashMap<>();
        params.put("app_store", App.getAppChannel());
        params.put("brand", DeviceUtils.getManufacturer());
        params.put("model", DeviceUtils.getModel());
        params.put("version", DeviceUtils.getSDKVersionName());
        params.put("app_version", AppUtils.getAppVersionName());
        onEvent(EVENT_ID_FIRST_START, params);
    }

    /**
     * banner点击事件
     *
     * @param position
     * @param id
     * @param title
     */
    public static void onBannerEvent(String position, String id, String title, String link) {
        onNormalBannerEvent(EVENT_ID_BANNER, position, id, title, link);
    }

    public static void onStoreBannerEvent(String position, String id, String title, String link) {
        onNormalBannerEvent(EVENT_ID_STORE_BANNER, position, id, title, link);
    }

    //0元购
    public static void onZeroBuyEvent(String position, String id, String title, String link) {
        onNormalBannerEvent(EVENT_ID_ZERO_BUY, position, id, title, link);
    }

    /**
     * 会员入会礼
     *
     * @param position
     * @param id
     * @param title
     * @param link
     */
    public static void onVIPEvent(String position, String id, String title, String link) {
        onNormalBannerEvent(EVENT_ID_HOME_VIP, position, id, title, link);
    }

    /**
     * 顶部导航tab切换
     *
     * @param position
     * @param id
     * @param title
     * @param link
     */
    public static void onNaviTopEvent(String position, String id, String title, String link) {
        onNormalBannerEvent(EVENT_ID_NAVI_TOP_QY, position, id, title, link);
    }

    /**
     * 导航事件
     *
     * @param position
     * @param id
     * @param title
     * @param link
     */
    public static void onNaviEvent(String position, String id, String title, String link) {
        onNormalBannerEvent(EVENT_ID_NAVI_QY, position, id, title, link);
    }


    /**
     * 首页充值会员添加
     */
    public static void onHomeCap(String position, String id, String title, String link) {
        onNormalBannerEvent(EVENT_ID_HOME_CAP, position, id, title, link);
    }

    /**
     * 专题
     */
    public static void onSpecial(String position, String id, String title, String link) {
        onNormalBannerEvent(EVENT_ID_SPECIAL, position, id, title, link);
    }

    /**
     * 首页商品
     */
    public static void onProduct(String position, String id, String title, String link) {
        onNormalBannerEvent(EVENT_ID_PRODUCT, position, id, title, link);
    }

    /**
     * 首页启动广告
     */
    public static void onWelcomAd(String link) {
        onNormalBannerEvent(EVENT_ID_WELCOME_AD, "", "", "", link);
    }

    public static void onNormalBannerEvent(String eventId, String position, String id, String title, String link) {
        Map<String, String> params = new HashMap<>();
        params.put("position", position);
        params.put("event_id", id);
        params.put("title", title);
        params.put("link", link);
        onEvent(eventId, params);
    }

    /**
     * 友盟自定义事件统计: 计数统计
     */
    public static void onEvent(String eventID, Map<String, String> params) {
        if (Constants.IS_DEBUG) return;

        if (params == null) {
            params = new HashMap<>();
        }
        addUserId(params);
        MobclickAgent.onEvent(App.getContext(), eventID, params);
//        MobclickAgent.onEventValue(AppContext.context, eventID, params, 1);

//        CountEvent jpushEvent = new CountEvent(eventID);
//        jpushEvent.addExtMap(params);
//        JAnalyticsInterface.onEvent(AppContext.context, jpushEvent);
    }

    /**
     * 为统计添加userId参数
     *
     * @param params
     */
    private static void addUserId(Map<String, String> params) {
//        if (!TextUtils.isEmpty(BaseResponse.getUserId())) {
//            params.put(EVENT_PUBLIC_PARAM_USER_ID, BaseResponse.getUserId());
//        }
    }
}
