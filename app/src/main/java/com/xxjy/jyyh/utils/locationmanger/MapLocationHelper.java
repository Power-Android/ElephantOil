package com.xxjy.jyyh.utils.locationmanger;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.LogUtils;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.constants.UserConstants;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/9/15.
 * 高德地图的 帮助类
 */

public class MapLocationHelper {
    /**
     * 定位的有效时间
     */
    private static final long LOCATION_VALID_TIME = 60 * 1000;
    //            private static final long LOCATION_VALID_TIME = 1000;
    private static double mLocationLatitude = 0.0, mLocationLongitude = 0.0;  //标记当前的经纬度
    private static long mLastRefreshTime = 0;   //标记上次刷新的时间
    private static AMapLocation aMapLocation;   //保存当前的location
    private static boolean hasLocationPermission = true;   //标记当前是否有地址的使用权限, 根据实际获取到的位置来判断

    /**
     * 声明mlocationClient对象
     */
    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = null;

    // 单例对象
    private static MapLocationHelper instance;

    /**
     * 定位结果
     */
    private LocationResult mLocationResult;
    public static String cityCode;
    public static String adCode;

    private MapLocationHelper() {
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static MapLocationHelper getInstance() {
        if (instance == null) {
            synchronized (MapLocationHelper.class) {
                if (instance == null) {
                    instance = new MapLocationHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 获取定位坐标
     */
    private void initLocationClient() {
        mLocationClient = new AMapLocationClient(App.getContext());
        //设置定位监听
        mLocationClient.setLocationListener(locationListener);

        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
//        mLocationClient.startLocation();
    }

    private AMapLocationClientOption getSignInOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        mOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        mOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mOption.setHttpTimeOut(20000);
        //可选，设置是否使用缓存定位，默认为true
        mOption.setLocationCacheEnable(true);
        //主动刷新设备wifi模块，获取到最新鲜的wifi列表
//        mOption.setWifiActiveScan(true);
        return mOption;
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setGpsFirst(false);
        //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setHttpTimeOut(20000);
        //可选，设置定位间隔。默认为2秒
        mOption.setInterval(2000);
        //可选，设置是否返回逆地理地址信息。默认是true
        mOption.setNeedAddress(true);
        //可选，设置是否只定位一次,默认为false
        mOption.setOnceLocation(true);
        //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用 ,获取最近3s内精度最高的一次定位结果
        mOption.setOnceLocationLatest(true);
        //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
        //可选，设置是否使用传感器。默认是false
        mOption.setSensorEnable(false);
        //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setWifiScan(true);
        //可选，设置是否使用缓存定位，默认为true
        mOption.setLocationCacheEnable(true);
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    mLastRefreshTime = System.currentTimeMillis();
                    mLocationLatitude = location.getLatitude();
                    mLocationLongitude = location.getLongitude();
                    cityCode = location.getCityCode();
                    adCode = location.getAdCode();
                    LogUtils.i(cityCode + "---"+location.getLatitude() + "---" + location.getLongitude());
                    aMapLocation = location;
                    hasLocationPermission = true;
                    UserConstants.setLongitude(String.valueOf(location.getLongitude()));
                    UserConstants.setLatitude(String.valueOf(location.getLatitude()));

                    if (mLocationResult != null) {
                        mLocationResult.locationSuccess(location);
                    }

//                    sb.append("定位成功" + "\n");
//                    sb.append("定位类型: " + location.getLocationType() + "\n");
//                    sb.append("经    度    : " + location.getLongitude() + "\n");
//                    sb.append("纬    度    : " + location.getLatitude() + "\n");
//                    sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
//                    sb.append("提供者    : " + location.getProvider() + "\n");
//                    sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
//                    sb.append("角    度    : " + location.getBearing() + "\n");
//                    // 获取当前提供定位服务的卫星个数
//                    sb.append("星    数    : " + location.getSatellites() + "\n");
//                    sb.append("国    家    : " + location.getCountry() + "\n");
//                    sb.append("省            : " + location.getProvince() + "\n");
//                    sb.append("市            : " + location.getCity() + "\n");
//                    sb.append("城市编码 : " + location.getCityCode() + "\n");
//                    sb.append("区            : " + location.getDistrict() + "\n");
//                    sb.append("区域 码   : " + location.getAdCode() + "\n");
//                    sb.append("地    址    : " + location.getAddress() + "\n");
//                    sb.append("兴趣点    : " + location.getPoiName() + "\n");
//                    //定位完成的时间
//                    sb.append("定位时间: " + DateUtils.formatDate(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                } else {
                    //定位失败
//                    sb.append("定位失败" + "\n");
//                    sb.append("错误码:" + location.getErrorCode() + "\n");
//                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
//                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
                    if (location.getErrorCode() == 12) {
                        hasLocationPermission = false;
                    }

                    if (mLocationResult != null) {
                        mLocationResult.locationFiler();
                    }
                }
                //定位之后的回调时间
//                sb.append("回调时间: " + DateUtils.formatDate(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                //解析定位结果，
//                String result = sb.toString();
            } else {
                if (mLocationResult != null) {
                    mLocationResult.locationFiler();
                }
            }
        }
    };

    public interface LocationResult {
        void locationSuccess(AMapLocation location);

        void locationFiler();
    }

    /**
     * 刷新位置
     */
    public static void refreshLocation() {
        getInstance().getLocation(null);
    }

    /**
     * 判断当前的 location 位置是否有效
     * 如果无效会刷新位置
     *
     * @return  true 当前位置有效, false代表当前位置无效
     */
    public static boolean isLocationValid() {
        boolean isLocationValid = System.currentTimeMillis() - mLastRefreshTime < LOCATION_VALID_TIME;
        return isLocationValid;
    }

    public static boolean isHasLocationPermission() {
        return hasLocationPermission;
    }

    /**
     * 获取纬度
     *
     * @return
     */
    public static double getLocationLatitude() {
        return mLocationLatitude;
    }

    /**
     * 获取经度
     *
     * @return
     */
    public static double getLocationLongitude() {
        return mLocationLongitude;
    }

    public static String getCityCode(){
        return cityCode;
    }

    public static String getAdCode(){
        return adCode;
    }

    /**
     * 获取当前保存 的 AMapLocation 对象
     *
     * @return
     */
    public static AMapLocation getMapLocation() {
        return aMapLocation;
    }

    /**
     * 获取位置
     */
    public void getLocation(LocationResult locationResult) {
        this.mLocationResult = locationResult;
        if (mLocationClient == null) {
            initLocationClient();
        }
        //初始化定位参数
        if (mLocationOption == null) {
            mLocationOption = getSignInOption();
        }
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mLocationClient.stopLocation();
        mLocationClient.startLocation();
    }

    /**
     * 内部停止定位
     */
    private void stopInnerLocation() {
        this.mLocationResult = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    /**
     * 内部不在定位了
     */
    private void onInnerDestory() {
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(locationListener);
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    /**
     * 停止定位
     */
    public static void stopLocation() {
        getInstance().stopInnerLocation();
    }

    /**
     * 不再定位了
     */
    public static void onDestory() {
        getInstance().onInnerDestory();
    }
}
