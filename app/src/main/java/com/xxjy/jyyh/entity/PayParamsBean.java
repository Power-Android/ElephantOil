package com.xxjy.jyyh.entity;

import com.google.gson.annotations.SerializedName;

public class PayParamsBean {
//     "timeStamp":"1614840804",
//             "package":"Sign=WXPay",
//             "appId":"wx3704434db8357ec1",
//             "sign":"79CCBB5F9DC531B05B08AA9F1FDDB0F0",
//             "partnerId":"1605297295",
//             "prepayId":"wx041453236272464e9ae024bddf2adb0000",
//             "nonceStr":"1614840804438"

    private String timeStamp;
    @SerializedName("package")
    private String packageX;
    private String appId;
    private String sign;
    private String partnerId;
    private String prepayId;
    private String nonceStr;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }
}
