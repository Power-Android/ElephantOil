package com.xxjy.jyyh.entity;

public class UserBean {
//    {"code":1,"data":{"headImg":"https://onecard.oss-cn-shanghai.aliyuncs.com/goods/202101/26165443fSe.png","balance":0,"phone":"152****1129","couponsSize":0,"integralBalance":0},"serviceTime":1611741507956}

    private String headImg;
    private String balance;
    private String phone;
    private String couponsSize;
    private String integralBalance;

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
