package com.xxjy.jyyh.entity;

/**
 * 创建日期：2021/7/6 17:12
 *
 * @author jiangm
 * @version 1.0
 * 包名： com.xxjy.jyyh.entity
 * 类说明：
 */
public class CarTypeBean {

    private int value;
    private String name;
    private Object hasSelect;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getHasSelect() {
        return hasSelect;
    }

    public void setHasSelect(Object hasSelect) {
        this.hasSelect = hasSelect;
    }
}
