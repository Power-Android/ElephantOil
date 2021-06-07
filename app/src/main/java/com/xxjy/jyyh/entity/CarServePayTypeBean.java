package com.xxjy.jyyh.entity;

public class CarServePayTypeBean {


    private String payImageUrl;
    private String payType;
    private String payTypeName;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPayImageUrl() {
        return payImageUrl;
    }

    public void setPayImageUrl(String payImageUrl) {
        this.payImageUrl = payImageUrl;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }
}
