package com.xxjy.jyyh.entity;

public class BannerBean {
    /**
     * id : 22
     * imgUrl : http://okche.oss-cn-shanghai.aliyuncs.com/tyb/20180513/134426Rcr.png
     * title : 新手优惠券
     * link : http://twx.yfq360.com/found/digitalSpecial
     */
    private int id;
    private String dateId;
    private String imgUrl;
    private String title;
    private String link;
    private String startTime;
    private String endTime;

    public String getDateId() {
        return dateId;
    }

    public void setDateId(String dateId) {
        this.dateId = dateId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
