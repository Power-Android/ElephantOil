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
    public static final String DEBUG_URL = "https://ccore.qqgyhk.com/server/";

    @DefaultDomain //默认域名
    public static String BASE_URL = Constants.URL_IS_DEBUG ? DEBUG_URL : RELEASE_URL;


    //获取验证码
    public static final String GET_CODE = "api/v1/wx/sendSmsCode";

    //闪验、验证码登录
    public static final String VERIFY_LOGIN = "api/v1/user/flash/login";

    //获取首页油站
    public static final String HOME_OIL = "api/gasPublic/getHomeOilStations";
    //加油订单滚动消息
    public static final String ORDER_NEWS = "api/gasPublic/getOrderNews";
    //获取油号列表
    public static final String OIL_NUMS = "api/gasPublic/getOilNums";
    //油站筛选
    public static final String OIL_STATIONS = "api/gasPublic/getOilStations";
    //查询个人账户信息
    public static final String USER_INFO = "api/user/queryUserInfo";

    //最近常去的油站
    public static final String OFTEN_OIL = "api/gasPublic/getPreferentialStations";

    //首页累计加油任务
    public static final String REFUEL_JOB = "api/gasPublic/getRefuelJob";

    //获取油号列表-筛选用
    public static final String GET_OIL_NUM = "api/gasPublic/getOilNums";

}
