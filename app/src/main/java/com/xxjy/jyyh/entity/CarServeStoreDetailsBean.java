package com.xxjy.jyyh.entity;

import java.util.List;
import java.util.Map;

public class CarServeStoreDetailsBean {
    private CardStoreInfoVoBean cardStoreInfoVo;
    private Map<String, List<CarServeProductsBean>> productCategory;

    public CardStoreInfoVoBean getCardStoreInfoVo() {
        return cardStoreInfoVo;
    }

    public void setCardStoreInfoVo(CardStoreInfoVoBean cardStoreInfoVo) {
        this.cardStoreInfoVo = cardStoreInfoVo;
    }

    public Map<String, List<CarServeProductsBean>> getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Map<String, List<CarServeProductsBean>> productCategory) {
        this.productCategory = productCategory;
    }
}
