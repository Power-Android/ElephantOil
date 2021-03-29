package com.xxjy.jyyh.entity;

public class SignInDayBean {
//    {
//        "couponId":100003066,
//            "currentDayFlag":false,
//            "dayOfWeek":7,
//            "intelgral":6,
//            "signFlag":false
//    }

    private String couponId;
    private boolean currentDayFlag;
    private int dayOfWeek;
    private int intelgral;
    private boolean signFlag;
    private String weekStr;

    public String getWeekStr() {
        return weekStr;
    }

    public void setWeekStr(String weekStr) {
        this.weekStr = weekStr;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public boolean isCurrentDayFlag() {
        return currentDayFlag;
    }

    public void setCurrentDayFlag(boolean currentDayFlag) {
        this.currentDayFlag = currentDayFlag;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getIntelgral() {
        return intelgral;
    }

    public void setIntelgral(int intelgral) {
        this.intelgral = intelgral;
    }

    public boolean isSignFlag() {
        return signFlag;
    }

    public void setSignFlag(boolean signFlag) {
        this.signFlag = signFlag;
    }
}
