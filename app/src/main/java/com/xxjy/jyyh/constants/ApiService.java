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
    public static final String RELEASE_URL = "https://bx.qqgyhk.com/api/";
    //默认测试服务器url
    public static final String DEBUG_URL = "https://tbx.qqgyhk.com/api/";

    @DefaultDomain //默认域名
    public static String BASE_URL = Constants.URL_IS_DEBUG ? DEBUG_URL : RELEASE_URL;


    //获取验证码
    public static final String SEND_SMS_CODE = "v1/user/sendSmsCode";
    //登录
    public static final String LOGIN = "v1/user/login";
    //我的信息
    public static final String MINE_DATA = "settingOwnInfo/myBasicEarnings";
    //油站列表
    public static final String OIL_STATION_LIST = "v1/gas/getOilStations";
    //油站详情
    public static final String OIL_STATION_INFO = "v1/gas/getGasStationInfo";
    //油站默认快捷价格
    public static final String OIL_PRICE_DEFAULT = "v1/gas/buyPriceDefault";
    //油站详情查询价格
    public static final String OIL_INQUIRE_PRICE = "v1/gas/getMultiplePrices";
    //首页Banner
    public static final String INDEX_BANNER = "settingIndex/indexUnifyBanner";
    //首页滚动消息
    public static final String HOME_ORDER_NEWS = "v1/order/getOrderNews";
    //油站列表Banner
    public static final String OIL_LIST_BANNER = "v1/common/getBannersForRegion";
    //获取油号列表
    public static final String OIL_NUM_INFO = "v1/gas/getOilNums";
    //首页消息数目POST /api/settingIndex/messageCenterNum
    public static final  String MESSAGE_CENTER_NUM="settingIndex/messageCenterNum";
    //我的模块-我的团队
    public static final  String DIRECT_CUSTOMER="mycustomer/teamCustomer";
    //创建订单
    public static final  String CREATE_ORDER="v1/order/create";


    //获取h5链接
    public static final String GET_H5_LINKS = "/v1/common/getH5Links";
    //订单详情页面 POST /api/myIndex/myOrderDetails
    public static final String MY_ORDER_DETAILS = "myIndex/myOrderDetails";
    //获取支付结果页 v1/pay/getPaymentResult
    public static final String GET_PAYMENT_RESULT = "v1/pay/getPaymentResult";

    //编辑昵称POST /api/settingOwnInfo/editMyInfo
    public static final String EDIT_MY_INFO = "settingOwnInfo/editMyInfo";
    //编辑昵称POST /api/settingOwnInfo/editUserInfo
    public static final String EDIT_USER_INFO = "settingOwnInfo/editUserInfo";
    //个人基础信息POST /api/settingOwnInfo/myInfo
    public static final String MY_INFO = "settingOwnInfo/myInfo";
    //上传用户头像 POST /api/settingOwnInfo/uploadingImg
    public static final String UPLOADING_IMG = "settingOwnInfo/uploadingImg";


}
