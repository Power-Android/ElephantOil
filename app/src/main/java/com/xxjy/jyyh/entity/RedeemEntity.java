package com.xxjy.jyyh.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * @author power
 * @date 2021/5/27 10:55 上午
 * @project ElephantOil
 * @description:
 */
public class RedeemEntity implements Serializable {

    private List<EntranceListBean> entranceList;
    private List<EntranceListBean> entranceCfList;
    private Integer entranceListSize;
    private String gasName;
    private String h5url;
    private String priority;
    private String priorityName;
    private List<ProductOilGasListBean> productOilGasList;
    private Integer productOilGasListSize;
    private List<PurchaseQuantityListBean> purchaseQuantityList;
    private String totalPriority;
    private String totalPriorityName;

    public List<EntranceListBean> getEntranceCfList() {
        return entranceCfList;
    }

    public void setEntranceCfList(List<EntranceListBean> entranceCfList) {
        this.entranceCfList = entranceCfList;
    }

    public List<EntranceListBean> getEntranceList() {
        return entranceList;
    }

    public void setEntranceList(List<EntranceListBean> entranceList) {
        this.entranceList = entranceList;
    }

    public Integer getEntranceListSize() {
        return entranceListSize;
    }

    public void setEntranceListSize(Integer entranceListSize) {
        this.entranceListSize = entranceListSize;
    }

    public String getGasName() {
        return gasName;
    }

    public void setGasName(String gasName) {
        this.gasName = gasName;
    }

    public String getH5url() {
        return h5url;
    }

    public void setH5url(String h5url) {
        this.h5url = h5url;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public List<ProductOilGasListBean> getProductOilGasList() {
        return productOilGasList;
    }

    public void setProductOilGasList(List<ProductOilGasListBean> productOilGasList) {
        this.productOilGasList = productOilGasList;
    }

    public Integer getProductOilGasListSize() {
        return productOilGasListSize;
    }

    public void setProductOilGasListSize(Integer productOilGasListSize) {
        this.productOilGasListSize = productOilGasListSize;
    }

    public List<PurchaseQuantityListBean> getPurchaseQuantityList() {
        return purchaseQuantityList;
    }

    public void setPurchaseQuantityList(List<PurchaseQuantityListBean> purchaseQuantityList) {
        this.purchaseQuantityList = purchaseQuantityList;
    }

    public String getTotalPriority() {
        return totalPriority;
    }

    public void setTotalPriority(String totalPriority) {
        this.totalPriority = totalPriority;
    }

    public String getTotalPriorityName() {
        return totalPriorityName;
    }

    public void setTotalPriorityName(String totalPriorityName) {
        this.totalPriorityName = totalPriorityName;
    }

    public static class EntranceListBean {
        private String icon;
        private String status;
        private String subtitle;
        private String title;
        private Integer type;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }

    public static class ProductOilGasListBean implements MultiItemEntity {
        private String costPrice;
        private String gasId;
        private Integer id;
        private String name;
        private Integer platformProductId;
        private String productDetail;
        private Integer productId;
        private String productImg;
        private String productPics;
        private Integer productType;
        private Integer productsId;
        private Integer publishStatus;
        private String redeemPrice;
        private Integer stock;
        private String templateId;
        private Integer templateNum;
        private Integer trialType;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(String costPrice) {
            this.costPrice = costPrice;
        }

        public String getGasId() {
            return gasId;
        }

        public void setGasId(String gasId) {
            this.gasId = gasId;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPlatformProductId() {
            return platformProductId;
        }

        public void setPlatformProductId(Integer platformProductId) {
            this.platformProductId = platformProductId;
        }

        public String getProductDetail() {
            return productDetail;
        }

        public void setProductDetail(String productDetail) {
            this.productDetail = productDetail;
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

        public String getProductPics() {
            return productPics;
        }

        public void setProductPics(String productPics) {
            this.productPics = productPics;
        }

        public Integer getProductType() {
            return productType;
        }

        public void setProductType(Integer productType) {
            this.productType = productType;
        }

        public Integer getProductsId() {
            return productsId;
        }

        public void setProductsId(Integer productsId) {
            this.productsId = productsId;
        }

        public Integer getPublishStatus() {
            return publishStatus;
        }

        public void setPublishStatus(Integer publishStatus) {
            this.publishStatus = publishStatus;
        }

        public String getRedeemPrice() {
            return redeemPrice;
        }

        public void setRedeemPrice(String redeemPrice) {
            this.redeemPrice = redeemPrice;
        }

        public Integer getStock() {
            return stock;
        }

        public void setStock(Integer stock) {
            this.stock = stock;
        }

        public String getTemplateId() {
            return templateId;
        }

        public void setTemplateId(String templateId) {
            this.templateId = templateId;
        }

        public Integer getTemplateNum() {
            return templateNum;
        }

        public void setTemplateNum(Integer templateNum) {
            this.templateNum = templateNum;
        }

        public Integer getTrialType() {
            return trialType;
        }

        public void setTrialType(Integer trialType) {
            this.trialType = trialType;
        }

        @Override
        public int getItemType() {
            return getTrialType();
        }
    }

    public static class PurchaseQuantityListBean {
        private Integer num;
        private Integer type;

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }
}
