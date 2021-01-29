package com.xxjy.jyyh.entity;

public class CallCenterBean {
//
//    {
//        "callOnline":"https://dev.qqgyhk.com:8889/adc29a8d6dbc9754?phone=15201061129",
//            "callPhone":"4008671777",
//            "callNotice":"工作日9:00-18:00"
//    }

    private String callOnline;
    private String callPhone;
    private String callNotice;

    public String getCallOnline() {
        return callOnline;
    }

    public void setCallOnline(String callOnline) {
        this.callOnline = callOnline;
    }

    public String getCallPhone() {
        return callPhone;
    }

    public void setCallPhone(String callPhone) {
        this.callPhone = callPhone;
    }

    public String getCallNotice() {
        return callNotice;
    }

    public void setCallNotice(String callNotice) {
        this.callNotice = callNotice;
    }
}
