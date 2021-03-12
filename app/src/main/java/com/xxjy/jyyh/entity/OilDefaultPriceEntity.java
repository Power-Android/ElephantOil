package com.xxjy.jyyh.entity;

import java.util.List;

/**
 * @author power
 * @date 1/27/21 4:55 PM
 * @project ElephantOil
 * @description:
 */
public class OilDefaultPriceEntity {

    private List<DefaultAmountBean> defaultAmount;

    public List<DefaultAmountBean> getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(List<DefaultAmountBean> defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public static class DefaultAmountBean {
        private String activityDisCountsAmount;
        private String amount;
        private String depreciateAmount;
        private String liter;
        private boolean isSelected;
        private String totalDiscountAmount;

        public String getTotalDiscountAmount() {
            return totalDiscountAmount;
        }

        public void setTotalDiscountAmount(String totalDiscountAmount) {
            this.totalDiscountAmount = totalDiscountAmount;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getActivityDisCountsAmount() {
            return activityDisCountsAmount;
        }

        public void setActivityDisCountsAmount(String activityDisCountsAmount) {
            this.activityDisCountsAmount = activityDisCountsAmount;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDepreciateAmount() {
            return depreciateAmount;
        }

        public void setDepreciateAmount(String depreciateAmount) {
            this.depreciateAmount = depreciateAmount;
        }

        public String getLiter() {
            return liter;
        }

        public void setLiter(String liter) {
            this.liter = liter;
        }
    }
}
