package com.xxjy.jyyh.entity;

public class ArticleBean {
//    {
//        "createTime":"2020-07-14 14:49:53",
//            "id":400209,
//            "imgUrl":"https://onecard.oss-cn-shanghai.aliyuncs.com/goods/202007/14144953cUa.png",
//            "source":"自己",
//            "subtitle":"测试",
//            "summary":"摘要",
//            "title":"20200714_测试",
//            "url":"http://"
//    },


    private String createTime;
    private int id;
    private String imgUrl;
    private String source;
    private String subtitle;
    private String summary;
    private String title;
    private String url;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
