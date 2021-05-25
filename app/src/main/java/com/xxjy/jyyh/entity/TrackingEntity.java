package com.xxjy.jyyh.entity;

import java.io.Serializable;

/**
 * @author power
 * @date 5/6/21 4:56 PM
 * @project ElephantOil
 * @description:
 */
public class TrackingEntity implements Serializable {

    private String requestUriCaPlatform;    //是，标识客户端平台信息 0：小程序；1：安卓 2：OS: 3：H5
    private String requestUriCity;          //是，用户定位城市
    private String requestUriSid;           //是，标识APP每次的启动和关闭 每次启动生成唯一不重复的sid
    private String requestUriStartupFrom;   //是，标识启动APP的方式 0：应用ICON 1：Push消息 2：页面外链唤起（运营页面、微信 3：第三方APP通过URD调起
    private String requestUriIsAbtest;      //是，是否abtest 0：否  1：是
    private String requestUriAbtestId;      //是，实验id
    private String requestUriAbtestName;    //是，实验名称 ab有关暂时传null
    private String pvId;                    //是，每次进入页面生成一个自增且唯一的pvId
    private String pageId;                  //是，页面的唯一id，用来区分不同的页面
    private String path;                    //是，路径
    private String lon;                     //是，经度
    private String lat;                     //是，纬度
    private String createTime;              //是，数据生成时间
    private String requestUriToken;          //是，用户token


    private String requestUriIdfa;          //否，用户设备信息，设备唯一标识
    private String requestUriCaSource;      //否，标识客户端渠道信息,上传格式为：渠道_版本号,例：qqstore01_v2.4.1
    private String requestUriDeviceMac;     //否，标识手机设备的MAC信息 需要加密上传
    private String requestUriDeviceImei;    //否，标识手机设备的imei 无法获取的上传空值“-”。需要加密
    private String requestUriDeviceManufacturer;    //否，标识手机设备制造商信息 需要加密上传
    private String requestUriDeviceModel;       //否，标识手机设备的型号信息 需要加密上传
    private String requestUriUa;            //否，用户代理
    private String requestUriSystemVersion; //否，系统版本
    private String pageParam;               //否，页面参数
    private String fromPageId;              //否，上级页面id
    private String fromPageParam;           //否，上级页面参数
    private String firstUtmSource;          //否，一级来源    建议增加，预留字段
    private String secondUtmSource;         //否，二级来源 建议增加，预留字段
    private String thirdUtmSource;          //否，三级来源 建议增加，预留字段

    public String getRequestUriToken() {
        return requestUriToken;
    }

    public void setRequestUriToken(String requestUriToken) {
        this.requestUriToken = requestUriToken;
    }

    public String getRequestUriCaPlatform() {
        return requestUriCaPlatform;
    }

    public void setRequestUriCaPlatform(String requestUriCaPlatform) {
        this.requestUriCaPlatform = requestUriCaPlatform;
    }

    public String getRequestUriCity() {
        return requestUriCity;
    }

    public void setRequestUriCity(String requestUriCity) {
        this.requestUriCity = requestUriCity;
    }

    public String getRequestUriSid() {
        return requestUriSid;
    }

    public void setRequestUriSid(String requestUriSid) {
        this.requestUriSid = requestUriSid;
    }

    public String getRequestUriStartupFrom() {
        return requestUriStartupFrom;
    }

    public void setRequestUriStartupFrom(String requestUriStartupFrom) {
        this.requestUriStartupFrom = requestUriStartupFrom;
    }

    public String getRequestUriIsAbtest() {
        return requestUriIsAbtest;
    }

    public void setRequestUriIsAbtest(String requestUriIsAbtest) {
        this.requestUriIsAbtest = requestUriIsAbtest;
    }

    public String getRequestUriAbtestId() {
        return requestUriAbtestId;
    }

    public void setRequestUriAbtestId(String requestUriAbtestId) {
        this.requestUriAbtestId = requestUriAbtestId;
    }

    public String getRequestUriAbtestName() {
        return requestUriAbtestName;
    }

    public void setRequestUriAbtestName(String requestUriAbtestName) {
        this.requestUriAbtestName = requestUriAbtestName;
    }

    public String getPvId() {
        return pvId;
    }

    public void setPvId(String pvId) {
        this.pvId = pvId;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRequestUriIdfa() {
        return requestUriIdfa;
    }

    public void setRequestUriIdfa(String requestUriIdfa) {
        this.requestUriIdfa = requestUriIdfa;
    }

    public String getRequestUriCaSource() {
        return requestUriCaSource;
    }

    public void setRequestUriCaSource(String requestUriCaSource) {
        this.requestUriCaSource = requestUriCaSource;
    }

    public String getRequestUriDeviceMac() {
        return requestUriDeviceMac;
    }

    public void setRequestUriDeviceMac(String requestUriDeviceMac) {
        this.requestUriDeviceMac = requestUriDeviceMac;
    }

    public String getRequestUriDeviceImei() {
        return requestUriDeviceImei;
    }

    public void setRequestUriDeviceImei(String requestUriDeviceImei) {
        this.requestUriDeviceImei = requestUriDeviceImei;
    }

    public String getRequestUriDeviceManufacturer() {
        return requestUriDeviceManufacturer;
    }

    public void setRequestUriDeviceManufacturer(String requestUriDeviceManufacturer) {
        this.requestUriDeviceManufacturer = requestUriDeviceManufacturer;
    }

    public String getRequestUriDeviceModel() {
        return requestUriDeviceModel;
    }

    public void setRequestUriDeviceModel(String requestUriDeviceModel) {
        this.requestUriDeviceModel = requestUriDeviceModel;
    }

    public String getRequestUriUa() {
        return requestUriUa;
    }

    public void setRequestUriUa(String requestUriUa) {
        this.requestUriUa = requestUriUa;
    }

    public String getRequestUriSystemVersion() {
        return requestUriSystemVersion;
    }

    public void setRequestUriSystemVersion(String requestUriSystemVersion) {
        this.requestUriSystemVersion = requestUriSystemVersion;
    }

    public String getPageParam() {
        return pageParam;
    }

    public void setPageParam(String pageParam) {
        this.pageParam = pageParam;
    }

    public String getFromPageId() {
        return fromPageId;
    }

    public void setFromPageId(String fromPageId) {
        this.fromPageId = fromPageId;
    }

    public String getFromPageParam() {
        return fromPageParam;
    }

    public void setFromPageParam(String fromPageParam) {
        this.fromPageParam = fromPageParam;
    }

    public String getFirstUtmSource() {
        return firstUtmSource;
    }

    public void setFirstUtmSource(String firstUtmSource) {
        this.firstUtmSource = firstUtmSource;
    }

    public String getSecondUtmSource() {
        return secondUtmSource;
    }

    public void setSecondUtmSource(String secondUtmSource) {
        this.secondUtmSource = secondUtmSource;
    }

    public String getThirdUtmSource() {
        return thirdUtmSource;
    }

    public void setThirdUtmSource(String thirdUtmSource) {
        this.thirdUtmSource = thirdUtmSource;
    }
}
