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
    private String amountUpright;
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
    private String serviceChargeAmount;
    private int type;
    private String amountMonthCoupon;

    private String createTime;
    private String finalIntegral;
    private String finalPayment;
    private String link;

    private String refundReason;
    private int refundStatus;
    private String refundStatusName;
    private String id;
    private String applyTime;
    private String refundSuccessTime;
    private String refundFailTime;
    private String realRefundAmount;

    private String tips;
    private int trialType;

    public int getTrialType() {
        return trialType;
    }

    public void setTrialType(int trialType) {
        this.trialType = trialType;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getRealRefundAmount() {
        return realRefundAmount;
    }

    public void setRealRefundAmount(String realRefundAmount) {
        this.realRefundAmount = realRefundAmount;
    }

    public String getRefundSuccessTime() {
        return refundSuccessTime;
    }

    public void setRefundSuccessTime(String refundSuccessTime) {
        this.refundSuccessTime = refundSuccessTime;
    }

    public String getRefundFailTime() {
        return refundFailTime;
    }

    public void setRefundFailTime(String refundFailTime) {
        this.refundFailTime = refundFailTime;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public int getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(int refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRefundStatusName() {
        return refundStatusName;
    }

    public void setRefundStatusName(String refundStatusName) {
        this.refundStatusName = refundStatusName;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAmountMonthCoupon() {
        return amountMonthCoupon;
    }

    public void setAmountMonthCoupon(String amountMonthCoupon) {
        this.amountMonthCoupon = amountMonthCoupon;
    }

    public String getAmountUpright() {
        return amountUpright;
    }

    public void setAmountUpright(String amountUpright) {
        this.amountUpright = amountUpright;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(String serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }

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
