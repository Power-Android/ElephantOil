package com.xxjy.jyyh.entity;

import java.util.List;

/**
 * @author power
 * @date 3/18/21 1:45 PM
 * @project ElephantOil
 * @description:
 */
public class MonthCouponEntity {

    private String monthCouponRule;
    private Double monthCouponAmount;
    private String monthCouponImgUrl;

    public String getMonthCouponImgUrl() {
        return monthCouponImgUrl;
    }

    public void setMonthCouponImgUrl(String monthCouponImgUrl) {
        this.monthCouponImgUrl = monthCouponImgUrl;
    }

    private List<MonthCouponTemplatesBean> monthCouponTemplates;

    public String getMonthCouponRule() {
        return monthCouponRule;
    }

    public void setMonthCouponRule(String monthCouponRule) {
        this.monthCouponRule = monthCouponRule;
    }

    public Double getMonthCouponAmount() {
        return monthCouponAmount;
    }

    public void setMonthCouponAmount(Double monthCouponAmount) {
        this.monthCouponAmount = monthCouponAmount;
    }

    public List<MonthCouponTemplatesBean> getMonthCouponTemplates() {
        return monthCouponTemplates;
    }

    public void setMonthCouponTemplates(List<MonthCouponTemplatesBean> monthCouponTemplates) {
        this.monthCouponTemplates = monthCouponTemplates;
    }

    public static class MonthCouponTemplatesBean {
        private String amount;
        private String amountReduce;
        private String createTime;
        private String description;
        private String endTime;
        private Integer id;
        private String imgUrl;
        private Boolean monthCouponDefault;
        private String name;
        private Integer point;
        private Integer pointReduce;
        private Integer provideNumber;
        private Integer rangeType;
        private String startTime;
        private Integer type;
        private String updateTime;
        private Integer validDays;
        private boolean isSelected;

        public Boolean getMonthCouponDefault() {
            return monthCouponDefault;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAmountReduce() {
            return amountReduce;
        }

        public void setAmountReduce(String amountReduce) {
            this.amountReduce = amountReduce;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public Boolean isMonthCouponDefault() {
            return monthCouponDefault;
        }

        public void setMonthCouponDefault(Boolean monthCouponDefault) {
            this.monthCouponDefault = monthCouponDefault;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPoint() {
            return point;
        }

        public void setPoint(Integer point) {
            this.point = point;
        }

        public Integer getPointReduce() {
            return pointReduce;
        }

        public void setPointReduce(Integer pointReduce) {
            this.pointReduce = pointReduce;
        }

        public Integer getProvideNumber() {
            return provideNumber;
        }

        public void setProvideNumber(Integer provideNumber) {
            this.provideNumber = provideNumber;
        }

        public Integer getRangeType() {
            return rangeType;
        }

        public void setRangeType(Integer rangeType) {
            this.rangeType = rangeType;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public Integer getValidDays() {
            return validDays;
        }

        public void setValidDays(Integer validDays) {
            this.validDays = validDays;
        }
    }
}
