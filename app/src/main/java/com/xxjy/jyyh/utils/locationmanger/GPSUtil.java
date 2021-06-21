package com.xxjy.jyyh.utils.locationmanger;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.xxjy.jyyh.app.App;


/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/9/19.
 */

public class GPSUtil {

    /**
     * 查看是否可以定位
     *
     * @return
     */
    public static boolean isOpenGPS(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return enabled || network;
    }

    /**
     * 强制帮助用户打开gps定位
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param lat
     * @param lon
     */
    public static LatLng   gcj02_To_Bd09(double lat, double lon) {
        CoordinateConverter converter  = new CoordinateConverter(App.getContext());
// CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.BAIDU);
// sourceLatLng待转换坐标点 LatLng类型
        converter.coord(new LatLng(lat,lon));
// 执行转换操作
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }
}
