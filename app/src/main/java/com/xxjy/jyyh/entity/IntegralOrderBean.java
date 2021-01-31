package com.xxjy.jyyh.entity;

public class IntegralOrderBean {

//    {
//        "createTime": "2021-01-27 19:34:20",
//            "finalIntegral": "16500.00",
//            "finalPayment": "30.00",
//            "orderId": "03210127193420pSccag",
//            "productName": "爱奇艺黄金会员（年卡）",
//            "status": 0,
//            "statusName": "待支付"
//    }

    private String createTime;
    private String finalIntegral;
    private String finalPayment;
    private String orderId;
    private String productName;
    private int status;
    private String statusName;
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinalIntegral() {
        return finalIntegral;
    }

    public void setFinalIntegral(String finalIntegral) {
        this.finalIntegral = finalIntegral;
    }

    public String getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(String finalPayment) {
        this.finalPayment = finalPayment;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
