package com.xxjy.jyyh.entity;

/**
 * 小象加油
 * ---------------------------
 * <p>
 * Created by Power on 2020/11/13.
 */
public class MultiplePriceBean {
    private String depreciateAmount;//直降金额
    private String duePrice;// 应付金额
    private String liter;//加油升数
    private String sumDiscountPrice;//优惠金额
    private String amount;//用户加油金额
    private String balancePrice;//抵扣多少余额，费用详情使用
    private String serviceChargeAmount;//服务费
    private String totalDiscountAmount;//总优惠直降金额

    public String getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(String totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public String getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(String serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }

    public String getDepreciateAmount() {
        return depreciateAmount;
    }

    public void setDepreciateAmount(String depreciateAmount) {
        this.depreciateAmount = depreciateAmount;
    }

    public String getDuePrice() {
        return duePrice;
    }

    public void setDuePrice(String duePrice) {
        this.duePrice = duePrice;
    }

    public String getLiter() {
        return liter;
    }

    public void setLiter(String liter) {
        this.liter = liter;
    }

    public String getSumDiscountPrice() {
        return sumDiscountPrice;
    }

    public void setSumDiscountPrice(String sumDiscountPrice) {
        this.sumDiscountPrice = sumDiscountPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalancePrice() {
        return balancePrice;
    }

    public void setBalancePrice(String balancePrice) {
        this.balancePrice = balancePrice;
    }
}
