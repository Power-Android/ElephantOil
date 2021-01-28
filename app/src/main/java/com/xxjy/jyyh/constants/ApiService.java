package com.xxjy.jyyh.constants;

import rxhttp.wrapper.annotation.DefaultDomain;

/**
 * @author power
 * @date 12/1/20 1:41 PM
 * @project RunElephant
 * @description: 网络请求专用
 */
public class ApiService {

    //默认正式服务器url
    public static final String RELEASE_URL = "https://core.qqgyhk.com/server/";
    //默认测试服务器url
    public static final String DEBUG_URL = "https://tcore.qqgyhk.com/server/";
//    public static final String DEBUG_URL = "https://ccore.qqgyhk.com/server/";

    @DefaultDomain //默认域名
    public static String BASE_URL = Constants.URL_IS_DEBUG ? DEBUG_URL : RELEASE_URL;


    //获取验证码
    public static final String GET_CODE = "api/v1/wx/sendSmsCode";

    //闪验、验证码登录
    public static final String VERIFY_LOGIN = "api/v1/user/flash/login";
    //移动端微信授权登录
    public static final String WECHAT_LOGIN ="api/v1/user/openId2Login";
    //app绑定手机号码
    public static final String APP_BIND_PHONE ="api/v1/user/flash/appBindPhone";

    //获取首页油站
    public static final String HOME_OIL = "api/gasPublic/getHomeOilStations";

    //加油订单滚动消息
    public static final String ORDER_NEWS = "api/gasPublic/getOrderNews";

    //获取油号列表
    public static final String OIL_NUMS = "api/gasPublic/getOilNums";

    //油站筛选
    public static final String OIL_STATIONS = "api/gasPublic/getOilStations";

    //加油首页签约油站列表
    public static final String SIGN_OIL_STATIONS = "api/gasPublic/getSignOilStations";
    //加油首页banner
    public static final String OIL_STATIONS_BANNERS = "api/gasPublic/getBanners";
    //获取某个位置的banner
    public static final String BANNER_OF_POSITION = "api/product/v1/getBannerOfPostion";
    //获取商品分类列表
    public static final String PRODUCT_CATEGORYS = "api/product/v1/queryProductCategorys";
    //根据商品分类获取商品列表
    public static final String QUERY_PRODUCTS = "api/product/v1/queryProducts";

    //查询个人账户信息
    public static final String USER_INFO = "api/user/queryUserInfo";

    //最近常去的油站
    public static final String OFTEN_OIL = "api/gasPublic/getPreferentialStations";

    //首页累计加油任务
    public static final String REFUEL_JOB = "api/gasPublic/getRefuelJob";

    //获取油号列表-筛选用
    public static final String GET_OIL_NUM = "api/gasPublic/getOilNums";

    //商户优惠券
    public static final String BUSINESS_COUPON = "api/coupon/v1/getBusinessCoupons";

    //平台优惠券
    public static final String PLATFORM_COUPON = "api/coupon/v1/getPlatformCouponVOs";

    //油站默认快捷价格
    public static final String OIL_PRICE_DEFAULT = "api/gasPublic/buyPriceDefault";

    //油站优惠券互斥
    public static final String OIL_MULTIPLE_PRICE = "api/gasPublic/getMultiplePrices";
    //获取某个位置的banner
    public static final String GET_BANNER_OF_POSITION = "api/v1/banner/getBannerOfPostion";


}
