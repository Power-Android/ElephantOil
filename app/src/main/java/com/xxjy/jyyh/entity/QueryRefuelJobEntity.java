package com.xxjy.jyyh.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author power
 * @date 2021/5/12 2:05 下午
 * @project ElephantOil
 * @description:
 */
public class QueryRefuelJobEntity implements Serializable {

    private Integer amount;
    private List<CouponsBean> coupons;
    private Integer progress;
    private Integer totalProgress;
    private Integer usable;
    private String id;
    private String ruleUrl;
    private boolean button;

    public boolean isButton() {
        return button;
    }

    public void setButton(boolean button) {
        this.button = button;
    }

    public String getUrl() {
        return ruleUrl;
    }

    public void setUrl(String ruleUrl) {
        this.ruleUrl = ruleUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public List<CouponsBean> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponsBean> coupons) {
        this.coupons = coupons;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getTotalProgress() {
        return totalProgress;
    }

    public void setTotalProgress(Integer totalProgress) {
        this.totalProgress = totalProgress;
    }

    public Integer getUsable() {
        return usable;
    }

    public void setUsable(Integer usable) {
        this.usable = usable;
    }

    public static class CouponsBean {
        private Integer couponId;
        private String info;
        private Integer number;
        private Integer status;

        public Integer getCouponId() {
            return couponId;
        }

        public void setCouponId(Integer couponId) {
            this.couponId = couponId;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }
}
