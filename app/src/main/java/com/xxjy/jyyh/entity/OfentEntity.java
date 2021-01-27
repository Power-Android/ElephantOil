package com.xxjy.jyyh.entity;

/**
 * @author power
 * @date 1/27/21 10:40 AM
 * @project ElephantOil
 * @description:
 */
public class OfentEntity {

    private String gasName;
    private String gasId;

    public OfentEntity(String gasName) {
        this.gasName = gasName;
    }

    public String getGasName() {
        return gasName;
    }

    public void setGasName(String gasName) {
        this.gasName = gasName;
    }

    public String getGasId() {
        return gasId;
    }

    public void setGasId(String gasId) {
        this.gasId = gasId;
    }
}
