package com.xxjy.jyyh.entity;

import java.util.List;

/**
 * @author power
 * @date 1/30/21 12:31 PM
 * @project ElephantOil
 * @description:
 */
public class PayResultEntity {

    private ActiveParamsBean activeParams;
    private Integer discountAmount;
    private Integer integral;
    private Integer integralBalance;
    private String msg;
    private String payAmount;
    private Integer result;

    public ActiveParamsBean getActiveParams() {
        return activeParams;
    }

    public void setActiveParams(ActiveParamsBean activeParams) {
        this.activeParams = activeParams;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Integer discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getIntegralBalance() {
        return integralBalance;
    }

    public void setIntegralBalance(Integer integralBalance) {
        this.integralBalance = integralBalance;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public static class ActiveParamsBean {
        private List<BannerBean> banner;

        public List<BannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<BannerBean> banner) {
            this.banner = banner;
        }

        public static class BannerBean {
            private String imgUrl;
            private String link;
            private String title;

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
