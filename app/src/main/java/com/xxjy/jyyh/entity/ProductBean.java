package com.xxjy.jyyh.entity;

public class ProductBean {

//    {
//        "redeemPoint":4850,
//            "productid":10558,
//            "productImg":"https://onecard.oss-cn-shanghai.aliyuncs.com/goods/202101/12211120heH.jpg",
//            "name":"爱奇艺黄金会员（季卡）",
//            "redeemPrice":0,
//            "salesNum":18
//    }

    private  int redeemPoint;
    private  int productid;
    private  String productImg;
    private  String name;
    private  int redeemPrice;
    private  int salesNum;
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getRedeemPoint() {
        return redeemPoint;
    }

    public void setRedeemPoint(int redeemPoint) {
        this.redeemPoint = redeemPoint;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRedeemPrice() {
        return redeemPrice;
    }

    public void setRedeemPrice(int redeemPrice) {
        this.redeemPrice = redeemPrice;
    }

    public int getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
    }
}
