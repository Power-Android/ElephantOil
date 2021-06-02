package com.xxjy.jyyh.entity;

import java.util.List;

public class CarServeStoreBean {

    private CardStoreInfoVoBean cardStoreInfoVo;
    private List<CarServeProductsBean> products;

    public CardStoreInfoVoBean getCardStoreInfoVo() {

        return cardStoreInfoVo;
    }

    public void setCardStoreInfoVo(CardStoreInfoVoBean cardStoreInfoVo) {
        this.cardStoreInfoVo = cardStoreInfoVo;
    }

    public List<CarServeProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<CarServeProductsBean> products) {
        this.products = products;
    }
}
