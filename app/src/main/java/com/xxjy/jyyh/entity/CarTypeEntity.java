package com.xxjy.jyyh.entity;

/**
 * @author power
 * @date 2021/6/7 3:11 下午
 * @project ElephantOil
 * @description:
 */
public class CarTypeEntity {
    private String title;
    private boolean isSelect;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public CarTypeEntity(String title, boolean isSelect) {
        this.title = title;
        this.isSelect = isSelect;
    }
}
