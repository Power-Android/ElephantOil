package com.xxjy.jyyh.entity;

/**
 * @author power
 * @date 1/29/21 8:24 PM
 * @project ElephantOil
 * @description:
 */
public class OilPayTypeEntity {
    private String title;
    private String id;
    private String actMes;//活动介绍
    private int img;
    private boolean isSelect;

    public String getActMes() {
        return actMes;
    }

    public void setActMes(String actMes) {
        this.actMes = actMes;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
