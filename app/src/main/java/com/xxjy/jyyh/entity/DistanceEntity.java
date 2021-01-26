package com.xxjy.jyyh.entity;


public class DistanceEntity {
    private String title;
    private int distance;
    private boolean isChecked;

    public DistanceEntity(String title, int distance,boolean isChecked) {
        this.title = title;
        this.isChecked = isChecked;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
