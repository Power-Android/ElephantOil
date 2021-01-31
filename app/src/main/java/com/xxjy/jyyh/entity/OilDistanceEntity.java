package com.xxjy.jyyh.entity;

/**
 * @author power
 * @date 1/31/21 5:29 PM
 * @project ElephantOil
 * @description:
 */
public class OilDistanceEntity {
    private String distance;
    private String gasId;
    private String gasName;
    private String latitude;
    private String longitude;
    private boolean here;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getGasId() {
        return gasId;
    }

    public void setGasId(String gasId) {
        this.gasId = gasId;
    }

    public String getGasName() {
        return gasName;
    }

    public void setGasName(String gasName) {
        this.gasName = gasName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isHere() {
        return here;
    }

    public void setHere(boolean here) {
        this.here = here;
    }
}
