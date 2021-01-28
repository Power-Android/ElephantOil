package com.xxjy.jyyh.entity;

/**
 * @author power
 * @date 1/27/21 5:46 PM
 * @project ElephantOil
 * @description:
 */
public class OilDiscountEntity {
    private float fallAmount;
    private String platformDesc;
    private String businessDesc;
    private float balance;//余额
    private float balanceDiscount;//余额抵扣金额
    private boolean isUseBill;//是否使用余额

    public boolean isUseBill() {
        return isUseBill;
    }

    public void setUseBill(boolean useBill) {
        isUseBill = useBill;
    }

    public float getBalanceDiscount() {
        return balanceDiscount;
    }

    public void setBalanceDiscount(float balanceDiscount) {
        this.balanceDiscount = balanceDiscount;
    }

    public float getFallAmount() {
        return fallAmount;
    }

    public void setFallAmount(float fallAmount) {
        this.fallAmount = fallAmount;
    }

    public String getPlatformDesc() {
        return platformDesc;
    }

    public void setPlatformDesc(String platformDesc) {
        this.platformDesc = platformDesc;
    }

    public String getBusinessDesc() {
        return businessDesc;
    }

    public void setBusinessDesc(String businessDesc) {
        this.businessDesc = businessDesc;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public OilDiscountEntity(float fallAmount, String platformDesc, String businessDesc, float balance) {
        this.fallAmount = fallAmount;
        this.platformDesc = platformDesc;
        this.businessDesc = businessDesc;
        this.balance = balance;
    }
}
