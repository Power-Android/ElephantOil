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
    private  String redeemPrice;
    private  int salesNum;
    private String link;
    private int id;
    private String productType;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getRedeemPrice() {
        return redeemPrice;
    }

    public void setRedeemPrice(String redeemPrice) {
        this.redeemPrice = redeemPrice;
    }

    public int getSalesNum() {
        return salesNum;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
    }
}
