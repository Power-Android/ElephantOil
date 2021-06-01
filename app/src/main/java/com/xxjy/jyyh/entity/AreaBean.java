package com.xxjy.jyyh.entity;

import java.util.List;

public class AreaBean {
    private String area;
    private String areaCode;
    private boolean isChecked;

    public AreaBean(String area, String areaCode, boolean isChecked) {
        this.area = area;
        this.areaCode = areaCode;
        this.isChecked = isChecked;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
