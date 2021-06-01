package com.xxjy.jyyh.constants;

import rxhttp.wrapper.annotation.DefaultDomain;

public interface CarServeApiService {
    //默认正式服务器url
    String CONFIG_RELEASE_URL = "http://api.dev.xiaoxiangjiayou.com/";
    String RELEASE_URL = CONFIG_RELEASE_URL + "cs/";
    //默认测试服务器url
    String CONFIG_DEBUG_URL = "http://api.dev.xiaoxiangjiayou.com/";
    //    public static final String CONFIG_DEBUG_URL = "https://ccore.qqgyhk.com/";
    String DEBUG_URL = CONFIG_DEBUG_URL + "cs/";

    String BASE_URL = Constants.URL_IS_DEBUG ? DEBUG_URL : RELEASE_URL;

    //门店省市区级联
    String GET_AREA = BASE_URL + "api/v1/admin/area/";
    //客户渠道商品服务类型分类
    String GET_PRODUCT_CATEGORY = BASE_URL + "api/v1/customer/product/category/product/category/list";
    //客户渠道门店
    String GET_STORE_LIST = BASE_URL + "api/v1/customer/channel/store/list";

}
