package com.xxjy.jyyh.constants;

import rxhttp.wrapper.annotation.DefaultDomain;

public interface CarServeApiService {
    //默认正式服务器url
    String CONFIG_RELEASE_URL = "https://api-dev.xiaoxiangjiayou.com/";
    String RELEASE_URL = CONFIG_RELEASE_URL ;
    //默认测试服务器url
    String CONFIG_DEBUG_URL = "https://api-dev.xiaoxiangjiayou.com/";
    //    public static final String CONFIG_DEBUG_URL = "https://ccore.qqgyhk.com/";
    String DEBUG_URL = CONFIG_DEBUG_URL ;

    String BASE_URL = Constants.URL_IS_DEBUG ? DEBUG_URL : RELEASE_URL;
    String APP_ID = Constants.URL_IS_DEBUG ? "Orvay1rVsoU9nlpY" : "Orvay1rVsoU9nlpY";

    //门店省市区级联
    String GET_AREA = BASE_URL + "cs/api/v1/admin/area/";
    //客户渠道商品服务类型分类
    String GET_PRODUCT_CATEGORY = BASE_URL + "cs/api/v1/customer/product/category/product/category/list";
    //客户渠道门店
    String GET_STORE_LIST = BASE_URL + "cs/api/v1/customer/channel/store/list";

    //客户渠道门店信息详情/api/v1/customer/channel/store/{no}/{appid}
    String GET_STORE_DETAILS = BASE_URL + "cs/api/v1/customer/channel/store/";

    //当前可用优惠券分页列表-门店详情使用
    String COUPON_USABLE = BASE_URL + "cs/api/v1/customer/coupon/list/usable";
    //优惠券列表
    String COUPON_LIST = BASE_URL + "cs/api/v1/customer/coupon/list";

    //创建车服订单
    String COMMIT_ORDER = BASE_URL + "oil/api/v1/customer/car/createOrder";


    //车服订单列表
    String GET_ORDER_LIST = BASE_URL + "oil/api/v1/customer/car/order/list";
    //车服订单取消
    String CANCEL_ORDER = BASE_URL + "oil/api/v1/customer/car/order/cancel/tiein";

    //查询车服搭售加油卡信息：入口,商品
    // TODO: 2021/6/5
    String TYING_PRODUCT = BASE_URL + "api/tiein/v1/queryTieinSaleCfInfo";
//    String TYING_PRODUCT = "http://192.168.1.84:8833/api/tiein/v1/queryTieinSaleCfInfo";

    //收银台payment cashier
    String PAYMENT_CASHIER = BASE_URL + "oil/api/v1/customer/pay/method";

    //订单支付
    String PAYMENT_ORDER = BASE_URL + "oil/api/v1/customer/pay/payment";

    //获取订单支付结果
    String PAYMENT_RESULT = BASE_URL + "oil/api/v1/customer/pay/payment/result";
    //车服订单详情
    String ORDER_INFO = BASE_URL + "oil/api/v1/customer/car/order/detail/";
    //首页常去车服门店
    String OFENT_CAR_SERVE = BASE_URL + "cs/api/v1/customer/channel/store/use/list";

}
