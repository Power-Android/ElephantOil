package com.xxjy.jyyh.constants;

/**
 * @author power
 * @date 12/1/20 1:38 PM
 * @project RunElephant
 * @description:
 */
public class Constants {

    /**
     * release服务器
     */
//    public static final boolean URL_IS_DEBUG = true;   //测试用这个
    public static final boolean URL_IS_DEBUG = false;   //正式上线用这个

    /**
     * 配置debug模式
     */
//    public static final boolean IS_DEBUG = true;  //测试用这个
    public static final boolean IS_DEBUG = false;   //正式上线用这个

    //微信配置
    public static final String WX_APP_ID = "wx0984c939611e6f3d";


    //fragment
    public static final String CURRENT_FRAGMENT_KEY = "current_fragment";
    public static final int TYPE_HOME = 0;
    public static final int TYPE_OIL = 1;
    public static final int TYPE_INTEGRAL = 2;
    public static final int TYPE_MINE = 3;

    //回退事件
    public static final long DOUBLE_INTERVAL_TIME = 2000;


    //h5链接


    //入参key
    public static final String PAGE = "pageNum";
    public static final String PAGE_SIZE = "pageSize";
    public static final String LATITUDE = "appLatitude";
    public static final String LONGTIDUE = "appLongitude";
    public static final String GAS_STATION_ID = "gasId";    //油站id
    public static final String OIL_NUMBER_ID = "oilNo";     //油号
    public static final String TYPE = "type";
    public static final String NICK_NAME="nickName";
    public static final String NICK_NAME_2="nickname";
    public static final String IMG_URL="imgUrl";
    public static final String SEX="sex";
    public static final String BIRTHDAY="birthday";
    public static final String WX_ACCOUNT="wxAccount";
    public static final String FILE="file";

    //eventBus



}
