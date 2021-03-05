package com.xxjy.jyyh.entity;

/**
 * @author power
 * @date 1/29/21 9:19 PM
 * @project ElephantOil
 * @description:
 */
public class PayOrderEntity {

    private Integer discountAmount;
    private Integer integral;
    private Integer integralBalance;
    private String msg;
    private String orderNo;
    private String orderPayNo;
    private String payAmount;
    private String payType;
    private Integer result;
    private String type;
    private String url;
    private PayParamsBean payParams;
    private String stringPayParams;

    public String getStringPayParams() {
        return stringPayParams;
    }

    public void setStringPayParams(String stringPayParams) {
        this.stringPayParams = stringPayParams;
    }

    public PayParamsBean getPayParams() {
        return payParams;
    }

    public void setPayParams(PayParamsBean payParams) {
        this.payParams = payParams;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getIntegralBalance() {
        return integralBalance;
    }

    public void setIntegralBalance(Integer integralBalance) {
        this.integralBalance = integralBalance;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderPayNo() {
        return orderPayNo;
    }

    public void setOrderPayNo(String orderPayNo) {
        this.orderPayNo = orderPayNo;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class PayParams{

    }
}
