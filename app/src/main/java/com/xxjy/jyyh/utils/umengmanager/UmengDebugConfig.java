package com.xxjy.jyyh.utils.umengmanager;

import android.content.Context;

import com.umeng.commonsdk.statistics.common.DeviceConfig;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2018/4/16.
 */
public class UmengDebugConfig {


    public static String getTestDeviceInfo(Context context) {
        String[] deviceInfo = new String[2];
        try {
            if (context != null) {
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context);
                deviceInfo[1] = DeviceConfig.getMac(context);
            }
        } catch (Exception e) {
        }
        return "{\"device_id\":\"" + deviceInfo[0] + "\",\"mac\":\"" + deviceInfo[1] + "\"}";
    }
}
