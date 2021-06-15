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
    private ProductParamsBean productParams;
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

    public ProductParamsBean getProductParams() {
        return productParams;
    }

    public void setProductParams(ProductParamsBean productParams) {
        this.productParams = productParams;
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

    public static class ProductParamsBean {
        //        "productAmount": 6.8,
//                "orderListH5Url": "123456678hkkkgfds",
//                "type": 2
        private String productAmount;
        private String orderListH5Url;
        private int type;
        private CarServeStoreBean storeRecordVo;
        private List<PayResultProductBean> productResult;
        private List<OilEntity.StationsBean> homeOilStations;

        public List<OilEntity.StationsBean> getHomeOilStations() {
            return homeOilStations;
        }

        public void setHomeOilStations(List<OilEntity.StationsBean> homeOilStations) {
            this.homeOilStations = homeOilStations;
        }

        public CarServeStoreBean getStoreRecordVo() {
            return storeRecordVo;
        }

        public void setStoreRecordVo(CarServeStoreBean storeRecordVo) {
            this.storeRecordVo = storeRecordVo;
        }

        public List<PayResultProductBean> getProductResult() {
            return productResult;
        }

        public void setProductResult(List<PayResultProductBean> productResult) {
            this.productResult = productResult;
        }

        public String getProductAmount() {
            return productAmount;
        }

        public void setProductAmount(String productAmount) {
            this.productAmount = productAmount;
        }

        public String getOrderListH5Url() {
            return orderListH5Url;
        }

        public void setOrderListH5Url(String orderListH5Url) {
            this.orderListH5Url = orderListH5Url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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
        private String oilName;
        private String authId;


        public String getOilName() {
            return oilName;
        }

        public void setOilName(String oilName) {
            this.oilName = oilName;
        }

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
