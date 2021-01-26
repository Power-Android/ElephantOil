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
    public static final String RELEASE_URL = "https://core.qqgyhk.com/server/api/";
    //默认测试服务器url
    public static final String DEBUG_URL = "https://tcore.qqgyhk.com/server/api/";

    @DefaultDomain //默认域名
    public static String BASE_URL = Constants.URL_IS_DEBUG ? DEBUG_URL : RELEASE_URL;


    //获取验证码
    public static final String GET_CODE = "v1/wx/sendSmsCode";

    //闪验、验证码登录
    public static final String VERIFY_LOGIN = "v1/user/flash/login";

    //获取首页油站
    public static final String HOME_OIL = "gasPublic/getHomeOilStations";

}
