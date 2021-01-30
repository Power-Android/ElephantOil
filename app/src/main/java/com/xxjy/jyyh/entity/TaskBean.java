package com.xxjy.jyyh.entity;

public class TaskBean {
//    {
//        "redeemPoint":9,
//            "tOrderNum":1,
//            "productId":10704,
//            "spName":"顺丰同城7折优惠券",
//            "gasName":"DHP测试油站-全选展示平台",
//            "link":"https://tcore.qqgyhk.com/mall/detail?id=10704",
//            "salesNum":99,
//            "type":1,
//            "nOrderAmount":0,
//            "gasId":"DH000011510",
//            "nOrderNum":0,
//            "redeemPrice":0,
//            "productType":3,
//            "status":false
//    }

    private int redeemPoint;
    private int tOrderNum;
    private int productId;
    private String spName;
    private String gasName;
    private String link;
    private int salesNum;
    private int type;
    private int nOrderAmount;
    private String gasId;
    private int nOrderNum;
    private int redeemPrice;
    private int productType;
    private boolean status;
    private String spImg;

    public String getSpImg() {
        return spImg;
    }

    public void setSpImg(String spImg) {
        this.spImg = spImg;
    }

    public int getRedeemPoint() {
        return redeemPoint;
    }

    public void setRedeemPoint(int redeemPoint) {
        this.redeemPoint = redeemPoint;
    }

    public int gettOrderNum() {
        return tOrderNum;
    }

    public void settOrderNum(int tOrderNum) {
        this.tOrderNum = tOrderNum;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getGasName() {
        return gasName;
    }

    public void setGasName(String gasName) {
        this.gasName = gasName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getnOrderAmount() {
        return nOrderAmount;
    }

    public void setnOrderAmount(int nOrderAmount) {
        this.nOrderAmount = nOrderAmount;
    }

    public String getGasId() {
        return gasId;
    }

    public void setGasId(String gasId) {
        this.gasId = gasId;
    }

    public int getnOrderNum() {
        return nOrderNum;
    }

    public void setnOrderNum(int nOrderNum) {
        this.nOrderNum = nOrderNum;
    }

    public int getRedeemPrice() {
        return redeemPrice;
    }

    public void setRedeemPrice(int redeemPrice) {
        this.redeemPrice = redeemPrice;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
