package com.xxjy.jyyh.constants;

import com.xxjy.jyyh.app.App;

import static com.xxjy.jyyh.constants.ApiService.CONFIG_BASE_URL;


/**
 * @author power
 * @date 12/1/20 1:38 PM
 * @project RunElephant
 * @description:
 */
public class Constants {

    public static final String USER_XIE_YI = CONFIG_BASE_URL + "found/articles/400221";
    public static final String YINSI_ZHENG_CE = CONFIG_BASE_URL + "found/articles/400222";
    public static final String INTEGRAL_EXPLANATION_URL = CONFIG_BASE_URL + "mall/introduction";

    //签到链接https://tcore.qqgyhk.com/oil/marketList?meet=1,
    // 营销活动聚合页https://tcore.qqgyhk.com/oil/marketList，
    // 购买月卡https://tcore.qqgyhk.com/oil/monthCardBuy，
    // 提前购买https://tcore.qqgyhk.com/oil/monthCard，
    // 新人好礼https://tcore.qqgyhk.com/oil/newUserGift
    public static final String SIGN_IN_URL = CONFIG_BASE_URL + "oil/marketList?meet=1";
    public static final String MARKET_ACTIVITIES_URL = CONFIG_BASE_URL + "oil/marketList";
    public static final String BUY_MONTH_CARD_URL = CONFIG_BASE_URL + "oil/monthCardBuy";
    public static final String BUY_IN_ADVANCE_MONTH_CARD_URL = CONFIG_BASE_URL + "oil/monthCard";
    public static final String NEW_USER_GIFT_URL = CONFIG_BASE_URL + "oil/newUserGift";
    public static final String VIP_URL = CONFIG_BASE_URL + "membership?id=1";


    /**
     * release服务器
     */
    public static final boolean URL_IS_DEBUG = true;   //测试用这个
//    public static final boolean URL_IS_DEBUG = false;   //正式上线用这个

    /**
     * 配置debug模式
     */


    public static final boolean IS_DEBUG = true;  //测试用这个
//    public static final boolean IS_DEBUG = false;   //正式上线用这个

    //微信配置
    public static final String WX_APP_ID = "wx3704434db8357ec1";
    public static final String WX_APP_SCRIPT = "787d5dcefab80f6bca272800e9bad139";//ab730ab00dd73986593da2ce6514ffe8     6b4edd26960e017c050f940210a99723

    //微信支付回调地址
    public static final String HTTP_CALL_BACK_URL = Constants.URL_IS_DEBUG ? "https://tcore.qqgyhk.com" : "https://core.qqgyhk.com";

    //fragment
    public static final String CURRENT_FRAGMENT_KEY = "current_fragment";
    public static final int TYPE_HOME = 0;
    public static final int TYPE_OIL = 1;
    public static final int TYPE_CAR_SERVE = 2;
    public static final int TYPE_INTEGRAL = 3;
    public static final int TYPE_MINE = 4;

    //回退事件
    public static final long DOUBLE_INTERVAL_TIME = 2000;

    //登录处理
    public static int LOGIN_FINISH = 1;         //finish掉
    //猎人码油站id
    public static String HUNTER_GAS_ID ;         //finish掉
    //是否点击关闭定位权限授权
    public static boolean OPEN_LOCATION_VISIBLE =true;         //finish掉

    //h5链接


    //入参key
    public static final String PAGE = "pageNum";
    public static final String PAGE_SIZE = "pageSize";
    public static final String LATITUDE = "appLatitude";
    public static final String LONGTIDUE = "appLongitude";
    public static final String GAS_STATION_ID = "gasId";    //油站id
    public static final String OIL_NUMBER_ID = "oilNo";     //油号
    public static final String TYPE = "type";
    public static final String NICK_NAME = "nickName";
    public static final String NICK_NAME_2 = "nickname";
    public static final String IMG_URL = "imgUrl";
    public static final String SEX = "sex";
    public static final String BIRTHDAY = "birthday";
    public static final String WX_ACCOUNT = "wxAccount";
    public static final String FILE = "file";

    //eventBus

    public static int PV_ID = 0;

}
