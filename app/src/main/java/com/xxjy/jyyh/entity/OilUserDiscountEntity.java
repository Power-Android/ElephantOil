package com.xxjy.jyyh.entity;

import java.io.Serializable;

/**
 * @author power
 * @date 2021/7/8 1:48 下午
 * @project ElephantOil
 * @description:
 */
public class OilUserDiscountEntity implements Serializable {
    private int flag;
    private String title;
    private String amountReduce;
    private int code;
    private String msg;

    public String getAmountReduce() {
        return amountReduce;
    }

    public void setAmountReduce(String amountReduce) {
        this.amountReduce = amountReduce;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
