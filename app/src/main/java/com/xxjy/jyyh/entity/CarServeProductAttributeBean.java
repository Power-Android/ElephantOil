package com.xxjy.jyyh.entity;

public class CarServeProductAttributeBean {

    private int hasAppointment;
    private int expires;
    private int hasNowRefund;
    private String carTypeName;

    public int getHasAppointment() {
        return hasAppointment;
    }

    public void setHasAppointment(int hasAppointment) {
        this.hasAppointment = hasAppointment;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public int getHasNowRefund() {
        return hasNowRefund;
    }

    public void setHasNowRefund(int hasNowRefund) {
        this.hasNowRefund = hasNowRefund;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }
}
