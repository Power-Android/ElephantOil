package com.xxjy.jyyh.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 小象加油
 * ---------------------------
 * <p>
 * Created by Power on 2020/11/13.
 */
public class MultiplePriceBean {
    private String depreciateAmount;//直降金额
    private String duePrice;// 应付金额
    private String liter;//加油升数
    private String sumDiscountPrice;//优惠金额
    private String amount;//用户加油金额
    private String balancePrice;//抵扣多少余额，费用详情使用
    private String serviceChargeAmount;//服务费
    private String totalDiscountAmount;//总优惠直降金额
    private String productAmount;//搭售金额
    private BestBusinessCouponBean bestBusinessCoupon;
    private BestuserCouponBean bestuserCoupon;

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(String totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public String getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(String serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }

    public String getDepreciateAmount() {
        return depreciateAmount;
    }

    public void setDepreciateAmount(String depreciateAmount) {
        this.depreciateAmount = depreciateAmount;
    }

    public String getDuePrice() {
        return duePrice;
    }

    public void setDuePrice(String duePrice) {
        this.duePrice = duePrice;
    }

    public String getLiter() {
        return liter;
    }

    public void setLiter(String liter) {
        this.liter = liter;
    }

    public String getSumDiscountPrice() {
        return sumDiscountPrice;
    }

    public void setSumDiscountPrice(String sumDiscountPrice) {
        this.sumDiscountPrice = sumDiscountPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalancePrice() {
        return balancePrice;
    }

    public void setBalancePrice(String balancePrice) {
        this.balancePrice = balancePrice;
    }

    public BestBusinessCouponBean getBestBusinessCoupon() {
        return bestBusinessCoupon;
    }

    public void setBestBusinessCoupon(BestBusinessCouponBean bestBusinessCoupon) {
        this.bestBusinessCoupon = bestBusinessCoupon;
    }

    public BestuserCouponBean getBestuserCoupon() {
        return bestuserCoupon;
    }

    public void setBestuserCoupon(BestuserCouponBean bestuserCoupon) {
        this.bestuserCoupon = bestuserCoupon;
    }

    public static class BestBusinessCouponBean {
        @SerializedName("amount")
        private String amountX;
        private String amountReduce;
        private String description;
        private String endTime;
        private String excludeTypeName;
        private String id;
        private String imgUrl;
        private String name;
        private String startTime;
        private Integer status;

        public String getAmountX() {
            return amountX;
        }

        public void setAmountX(String amountX) {
            this.amountX = amountX;
        }

        public String getAmountReduce() {
            return amountReduce;
        }

        public void setAmountReduce(String amountReduce) {
            this.amountReduce = amountReduce;
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

        public String getExcludeTypeName() {
            return excludeTypeName;
        }

        public void setExcludeTypeName(String excludeTypeName) {
            this.excludeTypeName = excludeTypeName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

    public static class BestuserCouponBean {
        @SerializedName("amount")
        private String amountX;
        private String amountReduce;
        private Integer couponId;
        private String description;
        private String endTime;
        private Integer excludeType;
        private String excludeTypeName;
        private String expireTime;
        private String id;
        private String imgUrl;
        private String name;
        private Integer point;
        private Integer pointReduce;
        private Integer sourceId;
        private String startTime;
        private Integer status;
        private String validTime;
        private CouponMapCzbVoBean couponMapCzbVo;

        public CouponMapCzbVoBean getCouponMapCzbVo() {
            return couponMapCzbVo;
        }

        public void setCouponMapCzbVo(CouponMapCzbVoBean couponMapCzbVo) {
            this.couponMapCzbVo = couponMapCzbVo;
        }

        public String getAmountX() {
            return amountX;
        }

        public void setAmountX(String amountX) {
            this.amountX = amountX;
        }

        public String getAmountReduce() {
            return amountReduce;
        }

        public void setAmountReduce(String amountReduce) {
            this.amountReduce = amountReduce;
        }

        public Integer getCouponId() {
            return couponId;
        }

        public void setCouponId(Integer couponId) {
            this.couponId = couponId;
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

        public Integer getExcludeType() {
            return excludeType;
        }

        public void setExcludeType(Integer excludeType) {
            this.excludeType = excludeType;
        }

        public String getExcludeTypeName() {
            return excludeTypeName;
        }

        public void setExcludeTypeName(String excludeTypeName) {
            this.excludeTypeName = excludeTypeName;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
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

        public Integer getSourceId() {
            return sourceId;
        }

        public void setSourceId(Integer sourceId) {
            this.sourceId = sourceId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getValidTime() {
            return validTime;
        }

        public void setValidTime(String validTime) {
            this.validTime = validTime;
        }
    }
}
