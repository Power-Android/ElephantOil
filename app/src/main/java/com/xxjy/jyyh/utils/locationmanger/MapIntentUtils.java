package com.xxjy.jyyh.utils.locationmanger;

import android.content.Intent;
import android.net.Uri;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.xxjy.jyyh.base.BaseActivity;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2018/8/7.
 * <p>
 * 主要用来处理跳转第三方地图的处理
 */
public class MapIntentUtils {

    public static final String BAIDU_PKG = "com.baidu.BaiduMap"; //百度地图的包名
    public static final String GAODE_PKG = "com.autonavi.minimap";//高德地图的包名
    public static final String TENCENT_PKG = "com.tencent.map";//腾讯地图的包名

    //参考高德地图app路径规划: https://lbs.amap.com/api/amap-mobile/guide/android/route
    public static void openGaoDe(BaseActivity context, double latitude, double longitude, String endName) {
        //这个是直接导航的
//        String uriStr = "androidamap://navi?sourceApplication=tuanyoubao&lat=" + latitude + "&lon=" + longitude + "&dev=0&style=2";
        //这个是进行路径规划的
        String uriStr = "amapuri://route/plan/?sname=我的位置&dlat=" + latitude + "&dlon=" +
                longitude + "&dname=" + endName + "&dev=0&t=0&sourceApplication=小象加油";
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(uriStr));
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setPackage("com.autonavi.minimap");
        startMapActivityForIntent(context, intent);
    }

    //参考百度地图app路径导航: http://lbsyun.baidu.com/index.php?title=uri/api/android
    public static void openBaidu(BaseActivity context, double latitude, double longitude, String endName) {
        Intent i1 = new Intent();
        //这是直接导航的
//        String uriStr = "baidumap://map/navi?location=" + latitude + "," + longitude + "&src=andr.czb.tuanyoubao";
        //这个是进行路径规划的
        String uriStr = "baidumap://map/direction?destination=name:" + endName + "|latlng:" +
                latitude + "," + longitude + "&coord_type=gcj02&src=andr.xxjy.xiaoxiangjiayou";
        i1.setData(Uri.parse(uriStr));
        startMapActivityForIntent(context, i1);
    }

    //参考腾讯地图调用app路径规划: http://lbs.qq.com/uri_v1/guide-mobile-navAndRoute.html
    public static void openTencent(BaseActivity context, double latitude, double longitude, String endName) {
        String uriStr = "qqmap://map/routeplan?type=drive&from=我的位置&to=" + endName + "&tocoord=" +
                latitude + "," + longitude + "&referer=com.xxjy.jyyh";
        Intent i1 = new Intent();
        i1.setData(Uri.parse(uriStr));
        startMapActivityForIntent(context, i1);
    }

    //参考高德地图调用h5路径规划: https://lbs.amap.com/api/uri-api/guide/travel/route
    public static void openGaoDeWeb(BaseActivity context, double latitude, double longitude, String endName) {
        String uriStr = "https://uri.amap.com/navigation?to=" + longitude + "," + latitude + "," +
                endName + "&mode=car&src=tuanyoubao&callnative=0";
        Uri uri = Uri.parse(uriStr);
        Intent i1 = new Intent(Intent.ACTION_VIEW, uri);
        startMapActivityForIntent(context, i1);
    }

    //参考腾讯地图调用h5路径规划: http://lbs.qq.com/uri_v1/guide-route.html
    public static void openTencentWeb(BaseActivity context, double latitude, double longitude, String endName) {
        String uriStr = "https://apis.map.qq.com/uri/v1/routeplan?type=drive&to=" + endName + "&tocoord=" +
                latitude + "," + longitude + "&policy=0&referer=com.xxjy.jyyh";
        Uri uri = Uri.parse(uriStr);
        Intent i1 = new Intent(Intent.ACTION_VIEW, uri);
        startMapActivityForIntent(context, i1);
    }

    /**
     * 根据形成的intent统一进行跳转处理
     *
     * @param context
     * @param intent
     */
    private static void startMapActivityForIntent(BaseActivity context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
            context.showToastError("打开软件失败,请您自行打开您的地图软件进行导航或者选择其他地图进行导航");
        }
    }

    /**
     * 检测地图应用是否安装
     *
     * @param packagename
     * @return
     */
    public static boolean checkMapAppsIsExist(String packagename) {
        boolean appInstalled = AppUtils.isAppInstalled(packagename);
        return appInstalled;
    }

    /**
     * 检查手机是否有默认导航功能
     *
     * @return
     */
    public static boolean isPhoneHasMapNavigation() {
        return isPhoneHasMapBaiDu() || isPhoneHasMapGaoDe() || isPhoneHasMapTencent();
    }

    /**
     * 检查手机是否有高德地图
     *
     * @return
     */
    public static boolean isPhoneHasMapGaoDe() {
        return checkMapAppsIsExist(GAODE_PKG);
    }

    /**
     * 检查手机是否有百度地图
     *
     * @return
     */
    public static boolean isPhoneHasMapBaiDu() {
        return checkMapAppsIsExist(BAIDU_PKG);
    }

    /**
     * 检查手机是否有腾讯地图
     *
     * @return
     */
    public static boolean isPhoneHasMapTencent() {
        return checkMapAppsIsExist(TENCENT_PKG);
    }
}
