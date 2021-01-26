package com.xxjy.jyyh.entity;

/**
 * @author power
 * @date 12/3/20 9:30 PM
 * @project RunElephant
 * @description:
 */
public class LocationEntity {
    private double mLat;        //纬度
    private double mLng;        //经度
    private String city;        //市
    private String district;    //区
    private String address;     //地址

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
