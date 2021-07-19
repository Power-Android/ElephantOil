package com.xxjy.jyyh.utils.eventtrackingmanager;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.rxjava.rxlife.RxLife;
import com.umeng.analytics.AnalyticsConfig;
import com.xxjy.jyyh.BuildConfig;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.entity.TrackingEntity;
import com.xxjy.jyyh.utils.locationmanger.MapLocationHelper;
import com.xxjy.jyyh.utils.umengmanager.UMengManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.RxHttp;

/**
 * @author power
 * @date 5/6/21 4:44 PM
 * @project ElephantOil
 * @description:
 */
public class EventTrackingManager {

    private static EventTrackingManager mInstance;
    private TrackingEntity mTrackingEntity;

    private EventTrackingManager() {
    }

    public static EventTrackingManager getInstance(){
        if (mInstance == null){
            synchronized (EventTrackingManager.class){
                if (mInstance == null){
                    mInstance = new EventTrackingManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * @param pvId 每次进入页面生成一个自增且唯一的pvId
     * @param pageId 页面的唯一id，用来区分不同的页面
     * @param path 路径
     */
//    public void tracking(Context context, BaseActivity activity, String pvId, String pageId, String path, String pageParam){
//        tracking( activity, pvId, pageId, path, pageParam, null, null);
//    }
    /**
     * @param pageId 页面的唯一id，用来区分不同的页面
     */
    public void tracking(BaseActivity activity, String pageId, String pageParam){
        tracking( activity, String.valueOf(++Constants.PV_ID), pageId, "", pageParam, null, null,null);
    }
    public void trackingEvent(BaseActivity activity, String pageId, String pageParam){
        tracking( activity, String.valueOf(++Constants.PV_ID), pageId, "", pageParam, null, null,"click");
    }

    /**
     * @param pvId
     * @param pageId
     * @param path
     * @param fromPageId    上级页面id
     * @param fromPageParam 上级页面参数
     */
    public void tracking( BaseActivity activity,String pvId, String pageId, String path, String pageParam,
                         String fromPageId, String fromPageParam,String clickType){
        setParams(activity);
        if (!TextUtils.isEmpty(pvId)){
            mTrackingEntity.setPvId(pvId);
        }
        if (!TextUtils.isEmpty(pageId)){
            mTrackingEntity.setPageId(pageId);
        }
        if (!TextUtils.isEmpty(path)){
            mTrackingEntity.setPath(path);
        }
        if (!TextUtils.isEmpty(pageParam)){
            mTrackingEntity.setPageParam(pageParam);
        }
        if (!TextUtils.isEmpty(fromPageId)){
            mTrackingEntity.setFromPageId(fromPageId);
        }
        if (!TextUtils.isEmpty(fromPageParam)){
            mTrackingEntity.setFromPageParam(fromPageParam);
        }
        if (!TextUtils.isEmpty(clickType)){
            mTrackingEntity.setClickType(clickType);
        }
        request(activity);
    }

    private void setParams(BaseActivity activity) {
        mTrackingEntity = new TrackingEntity();
        mTrackingEntity.setRequestUriCaPlatform("1");
        mTrackingEntity.setRequestUriCity(MapLocationHelper.getAdCode());
        mTrackingEntity.setRequestUriSid(String.valueOf(TimeUtils.getNowMills()));
        // 0：应用ICON 1：Push消息 2：页面外链唤起（运营页面、微信） 3：第三方APP
        mTrackingEntity.setRequestUriStartupFrom(UserConstants.getStartFrom());
        mTrackingEntity.setLon(UserConstants.getLongitude());
        mTrackingEntity.setLat(UserConstants.getLatitude());
        mTrackingEntity.setCreateTime(TimeUtils.getNowString());
        mTrackingEntity.setRequestUriToken(UserConstants.getToken());
        mTrackingEntity.setRequestUriIdfa(DeviceUtils.getUniqueDeviceId());
        mTrackingEntity.setRequestUriCaSource(UMengManager.getChannelName(activity));
        mTrackingEntity.setRequestUriDeviceMac(DeviceUtils.getMacAddress());
        mTrackingEntity.setRequestUriDeviceManufacturer(DeviceUtils.getManufacturer());
        mTrackingEntity.setRequestUriDeviceModel(DeviceUtils.getModel());
        mTrackingEntity.setRequestUriSystemVersion(DeviceUtils.getSDKVersionName());
    }

    public Map<String, String> getParams(BaseActivity activity, String pvId){
        Map<String, String> map = new HashMap<>();
        map.put("requestUriCaPlatform", "1");
        map.put("requestUriCity", MapLocationHelper.getAdCode());
        map.put("requestUriSid", String.valueOf(TimeUtils.getNowMills()));
        map.put("requestUriStartupFrom", UserConstants.getStartFrom());
        map.put("lon", UserConstants.getLongitude());
        map.put("lat", UserConstants.getLatitude());
        map.put("createTime", TimeUtils.getNowString());
        map.put("requestUriToken", UserConstants.getToken());
        map.put("requestUriIdfa", DeviceUtils.getUniqueDeviceId());
        map.put("requestUriCaSource", UMengManager.getChannelName(activity));
        map.put("requestUriDeviceMac", DeviceUtils.getMacAddress());
        map.put("requestUriDeviceManufacturer", DeviceUtils.getManufacturer());
        map.put("requestUriDeviceModel", DeviceUtils.getModel());
        map.put("requestUriSystemVersion", DeviceUtils.getSDKVersionName());

        if (!TextUtils.isEmpty(pvId)){
            map.put("pvId", pvId);
        }
//        if (!TextUtils.isEmpty(pageId)){
//            map.put("pageId", pageId);
//        }
//        if (!TextUtils.isEmpty(path)){
//            map.put("path", path);
//        }
//        if (!TextUtils.isEmpty(pageParam)){
//            map.put("pageParam", pageParam);
//        }
//        if (!TextUtils.isEmpty(fromPageId)){
//            map.put("fromPageId", fromPageId);
//        }
        if (!TextUtils.isEmpty(TrackingConstant.OIL_MAIN_TYPE)){
            map.put("fromPageParam", TrackingConstant.OIL_MAIN_TYPE);
        }
        return map;
    }

    private void request(BaseActivity activity) {
        //http://39.106.218.38:11000
        RxHttp.postJson(Constants.URL_IS_DEBUG?"http://39.106.218.38:11000/v1/clickData/add":"https://click.xiaoxiangjiayou.com/v1/clickData/add")
                .add("requestUriCaPlatform", mTrackingEntity.getRequestUriCaPlatform())
                .add("requestUriCity", mTrackingEntity.getRequestUriCity())
                .add("requestUriSid", mTrackingEntity.getRequestUriSid())
                .add("requestUriStartupFrom", mTrackingEntity.getRequestUriStartupFrom())
                .add("lon", mTrackingEntity.getLon())
                .add("lat", mTrackingEntity.getLat())
                .add("createTime", mTrackingEntity.getCreateTime())
                .add("requestUriToken", mTrackingEntity.getRequestUriToken())
                .add("requestUriIdfa", mTrackingEntity.getRequestUriIdfa())
                .add("requestUriCaSource", mTrackingEntity.getRequestUriCaSource())
                .add("requestUriDeviceMac", mTrackingEntity.getRequestUriDeviceMac())
                .add("requestUriDeviceManufacturer", mTrackingEntity.getRequestUriDeviceManufacturer())
                .add("requestUriDeviceModel", mTrackingEntity.getRequestUriDeviceModel())
                .add("requestUriSystemVersion", mTrackingEntity.getRequestUriSystemVersion())
                .add("pvId", mTrackingEntity.getPvId(),
                        !TextUtils.isEmpty(mTrackingEntity.getPvId()))
                .add("pageId", mTrackingEntity.getPageId(),
                        !TextUtils.isEmpty(mTrackingEntity.getPageId()))
                .add("path", mTrackingEntity.getPath(),
                        !TextUtils.isEmpty(mTrackingEntity.getPath()))
                .add("pageParam", mTrackingEntity.getPageParam(),
                        !TextUtils.isEmpty(mTrackingEntity.getPageParam()))
                .add("fromPageId", mTrackingEntity.getFromPageId(),
                        !TextUtils.isEmpty(mTrackingEntity.getFromPageId()))
                .add("fromPageParam", mTrackingEntity.getFromPageParam(),
                        !TextUtils.isEmpty(mTrackingEntity.getFromPageParam()))
                .add("clickType", mTrackingEntity.getClickType(),
                        !TextUtils.isEmpty(mTrackingEntity.getClickType()))
                .asString()
                .to(RxLife.toMain(activity))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {

                    }
                });
    }

}
