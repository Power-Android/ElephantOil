package com.xxjy.jyyh.entity;

/**
 * @author power
 * @date 12/3/20 9:30 PM
 * @project RunElephant
 * @description:
 */
public class LocationEntity {
    private double mLat;    //纬度
    private double mLng;    //经度

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLng() {
        return mLng;
    }

    public void setLng(double lng) {
        mLng = lng;
    }
}
