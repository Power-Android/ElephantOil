package com.xxjy.jyyh.entity;

public class SignInResultBean {
//    {
//        "couponAmount":0,
//            "excludeIntegral":0,
//            "integral":1,
//            "needDays":6,
//            "sendCouponFlag":false,
//            "signDays":1
//    }

    private int couponAmount;
    private int excludeIntegral;
    private int integral;
    private int needDays;
    private boolean sendCouponFlag;
    private int signDays;


    public int getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }

    public int getExcludeIntegral() {
        return excludeIntegral;
    }

    public void setExcludeIntegral(int excludeIntegral) {
        this.excludeIntegral = excludeIntegral;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getNeedDays() {
        return needDays;
    }

    public void setNeedDays(int needDays) {
        this.needDays = needDays;
    }

    public boolean isSendCouponFlag() {
        return sendCouponFlag;
    }

    public void setSendCouponFlag(boolean sendCouponFlag) {
        this.sendCouponFlag = sendCouponFlag;
    }

    public int getSignDays() {
        return signDays;
    }

    public void setSignDays(int signDays) {
        this.signDays = signDays;
    }
}
