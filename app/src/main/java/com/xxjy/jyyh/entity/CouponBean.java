package com.xxjy.jyyh.entity;

import android.text.TextUtils;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CouponBean implements MultiItemEntity {
//    {
//        "amount":"300.00",
//            "amountReduce":"10.00",
//            "description":"仅限加油使用",
//            "endTime":"2021.02.03",
//            "expireTime":"2021-02-03 15:45:32",
//            "id":"3983949",
//            "imgUrl":"https://onecard.oss-cn-shanghai.aliyuncs.com/goods/202101/21150817kQh.jpg",
//            "name":"测试加油1",
//            "point":0,
//            "pointReduce":0,
//            "startTime":"2021.01.27",
//            "status":0,
//            "validTime":"2021-01-27 15:45:30"
//    }

    private String amount;
    private String amountReduce;
    private String description;
    private String endTime;
    private String expireTime;
    private String id;
    private String imgUrl;
    private String name;
    private String point;
    private String pointReduce;
    private String startTime;
    private int status;
    private String validTime;
    private String excludeType;//0:不
    private String oilStations;
    private int rangeType;
    private String linkUrl;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getRangeType() {
        return rangeType;
    }

    public void setRangeType(int rangeType) {
        this.rangeType = rangeType;
    }

    public String getOilStations() {
        return oilStations;
    }

    public void setOilStations(String oilStations) {
        this.oilStations = oilStations;
    }

    public String getExcludeType() {
        return excludeType;
    }

    public void setExcludeType(String excludeType) {
        this.excludeType = excludeType;
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

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPointReduce() {
        return pointReduce;
    }

    public void setPointReduce(String pointReduce) {
        this.pointReduce = pointReduce;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    @Override
    public int getItemType() {
        return 0;
//        if(TextUtils.isEmpty(pointReduce)||TextUtils.equals(pointReduce,"0")){
//            return 0;
//        }else{
//            return 1;
//        }
    }
}
