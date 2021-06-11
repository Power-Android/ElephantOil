package com.xxjy.jyyh.entity;

public class UserBean {
//    {"code":1,"data":{"headImg":"https://onecard.oss-cn-shanghai.aliyuncs.com/goods/202101/26165443fSe.png","balance":0,"phone":"152****1129","couponsSize":0,"integralBalance":0},"serviceTime":1611741507956}

    private String headImg;
    private String balance;
    private String phone;
    private String couponsSize;
    private String integralBalance;
    private String walletUrl;
    private String integralBillUrl;
    private String totalDiscountPre;
    private String monthCardBuyUrl;
    private boolean hasBuyOldMonthCoupon;
    private String monthCardTotalDiscount;
    private String monthCardExpireDate;
    private boolean eVipOpenFlag;

    public boolean iseVipOpenFlag() {
        return eVipOpenFlag;
    }

    public void seteVipOpenFlag(boolean eVipOpenFlag) {
        this.eVipOpenFlag = eVipOpenFlag;
    }

    public String getMonthCardTotalDiscount() {
        return monthCardTotalDiscount;
    }

    public void setMonthCardTotalDiscount(String monthCardTotalDiscount) {
        this.monthCardTotalDiscount = monthCardTotalDiscount;
    }

    public String getMonthCardExpireDate() {
        return monthCardExpireDate;
    }

    public void setMonthCardExpireDate(String monthCardExpireDate) {
        this.monthCardExpireDate = monthCardExpireDate;
    }

    public boolean isHasBuyOldMonthCoupon() {
        return hasBuyOldMonthCoupon;
    }

    public void setHasBuyOldMonthCoupon(boolean hasBuyOldMonthCoupon) {
        this.hasBuyOldMonthCoupon = hasBuyOldMonthCoupon;
    }

    public String getMonthCardBuyUrl() {
        return monthCardBuyUrl;
    }

    public void setMonthCardBuyUrl(String monthCardBuyUrl) {
        this.monthCardBuyUrl = monthCardBuyUrl;
    }

    public String getTotalDiscountPre() {
        return totalDiscountPre;
    }

    public void setTotalDiscountPre(String totalDiscountPre) {
        this.totalDiscountPre = totalDiscountPre;
    }

    public String getWalletUrl() {
        return walletUrl;
    }

    public void setWalletUrl(String walletUrl) {
        this.walletUrl = walletUrl;
    }

    public String getIntegralBillUrl() {
        return integralBillUrl;
    }

    public void setIntegralBillUrl(String integralBillUrl) {
        this.integralBillUrl = integralBillUrl;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCouponsSize() {
        return couponsSize;
    }

    public void setCouponsSize(String couponsSize) {
        this.couponsSize = couponsSize;
    }

    public String getIntegralBalance() {
        return integralBalance;
    }

    public void setIntegralBalance(String integralBalance) {
        this.integralBalance = integralBalance;
    }
}
