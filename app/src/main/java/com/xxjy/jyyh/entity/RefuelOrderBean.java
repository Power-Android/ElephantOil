package com.xxjy.jyyh.entity;

public class RefuelOrderBean {

//    {
//        "amount": "400.00",
//            "buyTime": "2021-01-28 18:04:12",
//            "orderId": "02210128180411zWphuI",
//            "payAmount": "368.15",
//            "productName": "阶梯直降油站3-null",
//            "status": 0,
//            "statusName": "待支付"
//    }
//{
//    "amount": "400.00",
//        "amountCoupon": "0.00",
//        "buyTime": "2021-01-28 18:04:12",
//        "czbCouponAmount": "0.00",
//        "czbCouponId": "-1",
//        "czbDepreciateAmount": "31.85",
//        "czbDiscountAmount": "31.85",
//        "litre": "74.07",
//        "orderId": "02210128180411zWphuI",
//        "payAmount": "368.15",
//        "payTypeName": "未知",
//        "productName": "阶梯直降油站3-null",
//        "status": 3,
//        "statusName": "支付失败",
//        "usedBalance": "0.00"
//}
    private String amount;
    private String buyTime;
    private String orderId;
    private String payAmount;
    private String productName;
    private int status;
    private String statusName;

    private String amountCoupon;
    private String czbCouponAmount;
    private String czbCouponId;
    private String czbDepreciateAmount;
    private String czbDiscountAmount;
    private String litre;
    private String usedBalance;
    private String payTypeName;


    public String getAmountCoupon() {
        return amountCoupon;
    }

    public void setAmountCoupon(String amountCoupon) {
        this.amountCoupon = amountCoupon;
    }

    public String getCzbCouponAmount() {
        return czbCouponAmount;
    }

    public void setCzbCouponAmount(String czbCouponAmount) {
        this.czbCouponAmount = czbCouponAmount;
    }

    public String getCzbCouponId() {
        return czbCouponId;
    }

    public void setCzbCouponId(String czbCouponId) {
        this.czbCouponId = czbCouponId;
    }

    public String getCzbDepreciateAmount() {
        return czbDepreciateAmount;
    }

    public void setCzbDepreciateAmount(String czbDepreciateAmount) {
        this.czbDepreciateAmount = czbDepreciateAmount;
    }

    public String getCzbDiscountAmount() {
        return czbDiscountAmount;
    }

    public void setCzbDiscountAmount(String czbDiscountAmount) {
        this.czbDiscountAmount = czbDiscountAmount;
    }

    public String getLitre() {
        return litre;
    }

    public void setLitre(String litre) {
        this.litre = litre;
    }

    public String getUsedBalance() {
        return usedBalance;
    }

    public void setUsedBalance(String usedBalance) {
        this.usedBalance = usedBalance;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
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