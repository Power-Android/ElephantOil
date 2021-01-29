package com.xxjy.jyyh.entity;

import java.util.List;

/**
 * @author power
 * @date 1/29/21 3:59 PM
 * @project ElephantOil
 * @description:
 */
public class HomeProductEntity {

    private String className;
    private Integer id;
    private List<FirmProductsVoBean> firmProductsVo;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<FirmProductsVoBean> getFirmProductsVo() {
        return firmProductsVo;
    }

    public void setFirmProductsVo(List<FirmProductsVoBean> firmProductsVo) {
        this.firmProductsVo = firmProductsVo;
    }

    public static class FirmProductsVoBean {
        private Integer id;
        private String link;
        private String name;
        private Integer productId;
        private String productImg;
        private Integer productType;
        private Integer redeemPoint;
        private Integer redeemPrice;
        private Integer salesNum;
        private Integer type;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getProductImg() {
            return productImg;
        }

        public void setProductImg(String productImg) {
            this.productImg = productImg;
        }

        public Integer getProductType() {
            return productType;
        }

        public void setProductType(Integer productType) {
            this.productType = productType;
        }

        public Integer getRedeemPoint() {
            return redeemPoint;
        }

        public void setRedeemPoint(Integer redeemPoint) {
            this.redeemPoint = redeemPoint;
        }

        public Integer getRedeemPrice() {
            return redeemPrice;
        }

        public void setRedeemPrice(Integer redeemPrice) {
            this.redeemPrice = redeemPrice;
        }

        public Integer getSalesNum() {
            return salesNum;
        }

        public void setSalesNum(Integer salesNum) {
            this.salesNum = salesNum;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }
}
