package com.xxjy.jyyh.entity;

import java.util.List;

/**
 * @author power
 * @date 2/1/21 9:38 PM
 * @project ElephantOil
 * @description:
 */
public class RefuelOilEntity {

    private Integer code;
    private Long serviceTime;
    private List<DataBean> data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Long getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Long serviceTime) {
        this.serviceTime = serviceTime;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private Integer redeemPoint;
        private Integer tOrderNum;
        private Integer productId;
        private String spName;
        private String gasName;
        private String link;
        private Integer salesNum;
        private Integer type;
        private Integer nOrderAmount;
        private String gasId;
        private String spImg;
        private Integer nOrderNum;
        private Integer redeemPrice;
        private Integer productType;
        private Boolean status;
        private String progress;


        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public Integer getRedeemPoint() {
            return redeemPoint;
        }

        public void setRedeemPoint(Integer redeemPoint) {
            this.redeemPoint = redeemPoint;
        }

        public Integer getTOrderNum() {
            return tOrderNum;
        }

        public void setTOrderNum(Integer tOrderNum) {
            this.tOrderNum = tOrderNum;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
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

        public Integer getNOrderAmount() {
            return nOrderAmount;
        }

        public void setNOrderAmount(Integer nOrderAmount) {
            this.nOrderAmount = nOrderAmount;
        }

        public String getGasId() {
            return gasId;
        }

        public void setGasId(String gasId) {
            this.gasId = gasId;
        }

        public String getSpImg() {
            return spImg;
        }

        public void setSpImg(String spImg) {
            this.spImg = spImg;
        }

        public Integer getNOrderNum() {
            return nOrderNum;
        }

        public void setNOrderNum(Integer nOrderNum) {
            this.nOrderNum = nOrderNum;
        }

        public Integer getRedeemPrice() {
            return redeemPrice;
        }

        public void setRedeemPrice(Integer redeemPrice) {
            this.redeemPrice = redeemPrice;
        }

        public Integer getProductType() {
            return productType;
        }

        public void setProductType(Integer productType) {
            this.productType = productType;
        }

        public Boolean isStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }
    }
}
