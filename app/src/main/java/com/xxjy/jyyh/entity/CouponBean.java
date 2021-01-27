package com.xxjy.jyyh.entity;

public class CouponBean {
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
    private int point;
    private int pointReduce;
    private String startTime;
    private int status;
    private String validTime;


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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPointReduce() {
        return pointReduce;
    }

    public void setPointReduce(int pointReduce) {
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
}
