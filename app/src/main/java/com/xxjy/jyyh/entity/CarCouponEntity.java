package com.xxjy.jyyh.entity;

import java.util.List;

/**
 * @author power
 * @date 2021/6/11 11:48 上午
 * @project ElephantOil
 * @description:
 */
public class CarCouponEntity {

    private Integer total;
    private List<RecordsBean> records;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
        private Integer id;
        private Integer userId;
        private String phone;
        private String channel;
        private String orderId;
        private Integer couponTemplateId;
        private String couponType;
        private String productType;
        private String title;
        private Double couponValue;
        private String startTime;
        private String endTime;
        private Integer couponStatus;
        private String createTime;
        private String updateTime;
        private String deleteTime;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Integer getCouponTemplateId() {
            return couponTemplateId;
        }

        public void setCouponTemplateId(Integer couponTemplateId) {
            this.couponTemplateId = couponTemplateId;
        }

        public String getCouponType() {
            return couponType;
        }

        public void setCouponType(String couponType) {
            this.couponType = couponType;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Double getCouponValue() {
            return couponValue;
        }

        public void setCouponValue(Double couponValue) {
            this.couponValue = couponValue;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Integer getCouponStatus() {
            return couponStatus;
        }

        public void setCouponStatus(Integer couponStatus) {
            this.couponStatus = couponStatus;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getDeleteTime() {
            return deleteTime;
        }

        public void setDeleteTime(String deleteTime) {
            this.deleteTime = deleteTime;
        }
    }
}
