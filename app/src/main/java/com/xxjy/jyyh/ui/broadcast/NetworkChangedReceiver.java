package com.xxjy.jyyh.ui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.xxjy.jyyh.ui.broadcast.callback.NetWorkChangeListener;


/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/9/5.
 */

public class NetworkChangedReceiver extends BroadcastReceiver {
    private static final int NET_WORK_NO_NTE = 1;
    private static final int NET_WORK_GPRS = 2;
    private static final int NET_WORK_WIFI = 3;

    private int mCurrentState = NET_WORK_NO_NTE;

    private final String TAG = "NetworkChangedReceiver";
    private NetWorkChangeListener netWorkChangeListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (netWorkChangeListener == null) return;

        //检测API是不是小于21，因为到了API 21之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected()) {
                refreshNetWork(NET_WORK_WIFI);
            } else if (dataNetworkInfo.isConnected()) {
                refreshNetWork(NET_WORK_GPRS);
            } else {
                refreshNetWork(NET_WORK_NO_NTE);
            }
        } else {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.isConnected()) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    refreshNetWork(NET_WORK_WIFI);
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    refreshNetWork(NET_WORK_GPRS);
                } else {
                    refreshNetWork(NET_WORK_NO_NTE);
                }
            } else {
                refreshNetWork(NET_WORK_NO_NTE);
            }
        }
    }

    private void refreshNetWork(int netStatus) {
        if (mCurrentState == netStatus) return;
        switch (netStatus) {
            case NET_WORK_WIFI:
                Log.i(TAG, "WIFI已连接");
                mCurrentState = NET_WORK_WIFI;
                netWorkChangeListener.useWifi();
                break;
            case NET_WORK_GPRS:
                Log.i(TAG, "移动数据已连接");
                mCurrentState = NET_WORK_GPRS;
                netWorkChangeListener.useGprs();
                break;
            default:
                Log.i(TAG, "WIFI已断开,移动数据已断开");
                mCurrentState = NET_WORK_NO_NTE;
                netWorkChangeListener.noNetWork();
                break;
        }
    }

    public void addNetWorkChangeListener(NetWorkChangeListener netWorkChangeListener) {
        this.netWorkChangeListener = netWorkChangeListener;
    }
}
