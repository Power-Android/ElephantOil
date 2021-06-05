package com.xxjy.jyyh.entity;

public class BusinessStatusBean {
    private String name;
    private int status;
    private boolean isChecked;

    public BusinessStatusBean(String name, int status, boolean isChecked) {
        this.name = name;
        this.status = status;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
