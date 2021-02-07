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
    private GasParamsBean gasParams;
    private String discountAmount;
    private String integral;
    private String integralBalance;
    private String msg;
    private String payAmount;
    private Integer result;

    public GasParamsBean getGasParams() {
        return gasParams;
    }

    public void setGasParams(GasParamsBean gasParams) {
        this.gasParams = gasParams;
    }

    public ActiveParamsBean getActiveParams() {
        return activeParams;
    }

    public void setActiveParams(ActiveParamsBean activeParams) {
        this.activeParams = activeParams;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getIntegralBalance() {
        return integralBalance;
    }

    public void setIntegralBalance(String integralBalance) {
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
    public static class GasParamsBean {

//        {
//            "amount":100,
//                "gunNo":2,
//                "orderId":"02210207202202GZPQHp",
//                "phone":"15201061129",
//                "gasId":"JT000011456",
//                "oilNo":"92",
//                "authId":102021106
//        }
      private String amount;
      private String gunNo;
      private String orderId;
      private String phone;
      private String gasId;
      private String gasName;
      private String oilNo;
      private String authId;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getGunNo() {
            return gunNo;
        }

        public void setGunNo(String gunNo) {
            this.gunNo = gunNo;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGasId() {
            return gasId;
        }

        public void setGasId(String gasId) {
            this.gasId = gasId;
        }

        public String getGasName() {
            return gasName;
        }

        public void setGasName(String gasName) {
            this.gasName = gasName;
        }

        public String getOilNo() {
            return oilNo;
        }

        public void setOilNo(String oilNo) {
            this.oilNo = oilNo;
        }

        public String getAuthId() {
            return authId;
        }

        public void setAuthId(String authId) {
            this.authId = authId;
        }
    }
}
