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
    public static final String CONFIG_RELEASE_URL = "https://m.qqgyhk.com/";
    public static final String RELEASE_URL = CONFIG_RELEASE_URL+"server/";
    //默认测试服务器url
    public static final String CONFIG_DEBUG_URL = "https://tcore.qqgyhk.com/";
//    public static final String CONFIG_DEBUG_URL = "https://ccore.qqgyhk.com/";
    public static final String DEBUG_URL = CONFIG_DEBUG_URL+"server/";

    @DefaultDomain //默认域名
    public static String BASE_URL = Constants.URL_IS_DEBUG ? DEBUG_URL : RELEASE_URL;

    public static String CONFIG_BASE_URL = Constants.URL_IS_DEBUG ? CONFIG_DEBUG_URL : CONFIG_RELEASE_URL;



    //获取验证码
    public static final String GET_CODE = "api/v1/wx/sendSmsCode";
    //闪验、验证码登录
    public static final String VERIFY_LOGIN = "api/v1/user/flash/login";
    //移动端微信授权登录
    public static final String WECHAT_LOGIN = "api/v1/user/openId2Login";
    //app绑定手机号码
    public static final String APP_BIND_PHONE = "api/v1/user/flash/appBindPhone";
    //获取首页油站
    public static final String HOME_OIL = "api/gasPublic/getHomeOilStations";
    //加油订单滚动消息
    public static final String ORDER_NEWS = "api/gasPublic/getOrderNews";
    //获取油号列表
    public static final String OIL_NUMS = "api/gasPublic/getOilNums";
    //油站筛选
    public static final String OIL_STATIONS = "api/gasPublic/getOilStations";
    //加油签约油站列表
    public static final String SIGN_OIL_STATIONS = "api/gasPublic/getSignOilStations";
    //油站列表
    public static final String OIL_AND_SIGN_STATIONS = "api/gasPublic/getOilAndSignStations";
    //加油首页banner
    public static final String OIL_STATIONS_BANNERS = "api/gasPublic/getBanners";
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
    public static final String BUSINESS_COUPON_LIST = "api/coupon/v1/getBusinessCouponsList";
    //兑换优惠券
    public static final String EXCHANGE_COUPON = "api/coupon/v1/exchangeCoupon";
    //平台优惠券
    public static final String PLATFORM_COUPON = "api/coupon/v1/getPlatformCouponVOs";
    public static final String PLATFORM_COUPON_LIST = "api/coupon/v1/getMyCouponList";
    //优惠券数量
    public static final String GET_COUPON_NUM = "api/coupon/v1/getMyCouponNumber";
    //油站默认快捷价格
    public static final String OIL_PRICE_DEFAULT = "api/gasPublic/buyPriceDefault";
    //油站优惠券互斥
    public static final String OIL_MULTIPLE_PRICE = "api/tiein/v1/getPayPrices";
    //获取某个位置的banner
    public static final String GET_BANNER_OF_POSITION = "api/v1/banner/getBannerOfPostion";
    //获取加油订单列表
    public static final String REFUEL_ORDER_LIST = "api/gasPublic/refuelOrderList";
    //获取加油订单退单列表
    public static final String REFUND_ORDER_LIST = "api/gasPublic/orderRefundList";
    //获取订单列表
    public static final String INTEGRAL_ORDER_LIST = "api/product/v1/queryMallOrderList";
    //获取加油订单详情
    public static final String REFUEL_ORDER_DETAILS = "api/gasPublic/refuelOrderDetails";
    //退款订单详情接口
    public static final String ORDER_REFUND_DETAILS = "api/gasPublic/orderRefundDetail";
    //取消加油订单
    public static final String REFUEL_ORDER_CANCEL = "api/gasPublic/cancelOrder";
    //取消订单 积分商城订单
    public static final String PRODUCT_ORDER_CANCEL = "api/product/v1/cancelOrder";
    //申请退款
    public static final String REFUEL_APPLY_REFUND = "api/gasPublic/refuelApplyRefund";
    //查询余额
    public static final String QUERY_BALANCE = "api/user/queryBalance";
    //查询积分余额
    public static final String QUERY_INTEGRAL_BALANCE = "api/user/queryIntegralBalance";
    //下单温馨提示
    public static final String GET_ORDER_TIP = "api/gasPublic/getOrderTs";
    //创建加油订单
    public static final String CREATE_ORDER = "api/tiein/v1/refuelAndProductCreateOrder";
    //app版本更新检测
    public static final String CHECK_VERSION = "api/v1/common/checkVersion";
    //客服中心
    public static final String CALL_CENTER = "api/v1/common/getCallCenter";
    //退出登录
    public static final String USER_LOGOUT = "api/v1/wx/logout";
    //油站详情
    public static final String OIL_DETAIL = "api/gasPublic/getGasStationInfo";
    //首页积分豪礼
    public static final String HOME_PRODUCT = "api/product/v1/queryProductsByModule";
    //获取系统通知
    public static final String GET_ARTICLES = "api/v1/common/getArticles";
    //获取订单通知
    public static final String GET_NOTICES = "api/user/getNotices";
    //获取加油收银台
    public static final String GET_PAY_TYPE = "api/gasPublic/refuelCashierDesk";
    //收银台优惠标签
    public static final String GET_PAY_TAG = "api/gasPublic/payTag";
    //加油支付
    public static final String PAY_ORDER = "api/gasPublic/refuelPayOrder";
    //加油支付结果回调数据
    public static final String PAY_ORDER_RESULT = "/api/tiein/v1/queryPayOrderResult";
    //搜索权益
    public static final String QUERY_PRODUCTS_BY_NAME = "api/product/v1/queryProductsByName";
    //热门搜索
    public static final String HOT_SEARCH = "api/product/v1/hotSearch";
    //查询油站距离
    public static final String GET_OIL_DISTANCE = "api/gasPublic/getDistance";
    //是否隐藏权益相关内容
    public static final String GET_OS_OVERALL = "api/v1/common/getOsOverall";
    //是否隐藏余额
    public static final String GET_OS_BALANCE = "api/v1/common/getOsBalanceShow";
    //加油新老用户区分
    public static final String IS_NEW_USER = "api/gasPublic/isNewUser";
    //极光ID
    public static final String GET_JPUSH_ID_URL =  "api/v1/user/getJPushId";

    //本地生活 门店列表
    public static final String GET_STORE_LIST =  "api/localLife/getStoreList";
    //本地生活 门店详情
    public static final String GET_STORE_INFO =  "api/localLife/getStoreInfo";
    //本地生活 订单列表
    public static final String GET_STORE_ORDER_LIST =  "api/localLife/orderList";
    //获取油站邀请人专属油站
    public static final String GET_SPEC_GAS_ID =  "api/gasPublic/specGasId";
    //获取月度红包列表
    public static final String GET_MONTH_COUPON  = "api/coupon/v1/getPlatformMonthCouponVOs";
    //获取每周积分签到列表信息
    public static final String GET_INTEGRAL_INFO  = "api/product/v1/getIntegralInfo";
    //每周积分签到
    public static final String INTEGRAL_SIGN  = "api/product/v1/integralSign";
    //电子围栏新接口
    public static final String GET_PAY_DISTANCE  = "api/gasPublic/getPayDistance";
    //数据埋点
    public static final String TRACKING_ADD = "api/v1/clickData/add";
    //获取月度红包以及月度权益包购买条件信息(单独购买)
    public static final String GET_MONTH_EQUITY_INFO  = "api/monthCard/v1/getMonthEquityInfo";
    //新人礼包
    public static final String NEW_USER_STATUS  = "api/activeCommon/newUserStatus";
    //首页加油任务
    public static final String QUERY_REFUEL_JOB = "api/activeCommon/queryRefuelJob";
    //领取加油任务红包
    public static final String RECEIVE_OIL_JOB_COUPON = "api/activeCommon/receiveOilJobCoupon";
    //获取搭售列表
    public static final String QUERY_SALE_INFO = "api/tiein/v1/queryTieinSaleInfo";
    //获取搭售列表
    public static final String QUERY_SALE_INFO_CAR_SERVE = "api/tiein/v1/queryTieinSaleCfInfo";
    //获取会员卡信息
    public static final String GET_MEMBER_CARD_INFO = "api/v1/member/getMemberBuyIndex";
    //获取会员卡有效期
    public static final String GET_MEMBER_CARD = "api/v1/member/getMemberCard";
    //首页卡片
    public static final String HOME_CARD_INFO = "api/tiein/v1/queryHomeCardInfo";
    //首页菜单
    public static final String HOME_MENU_INFO = "api/tiein/v1/queryHomeKingKongDistrict";
    //浮窗接口
    public static final String DRAG_INFO = "api/v1/member/floatingWindow";
    //挽留活动
    public static final String OIL_USER_DISCOUNT = "api/activeCommon/queryOilUserDiscount";
    //挽留查询减免金额
    public static final String QUERY_AMOUNT_UPRIGHT = "api/activeCommon/queryAmountUpright";
    //优惠券升级——膨胀券
    public static final String COUPON_UPDATE = "api/v1/member/couponUpdate";
    //支持升级券的油站列表
    public static final String QUERY_COUPON_UPDATE_OIL = "api/v1/member/qryCouponCanUpdateOilStations";



}
