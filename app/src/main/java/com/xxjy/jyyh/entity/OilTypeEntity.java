package com.xxjy.jyyh.entity;

import java.util.List;

/**
 * @author power
 * @date 2/7/21 7:35 PM
 * @project ElephantOil
 * @description:
 */
public class OilTypeEntity {
    private String OilTypeName;
    private boolean isSelect;
    private List<OilEntity.StationsBean.OilPriceListBean> oilPriceList;

    public List<OilEntity.StationsBean.OilPriceListBean> getOilPriceList() {
        return oilPriceList;
    }

    public void setOilPriceList(List<OilEntity.StationsBean.OilPriceListBean> oilPriceList) {
        this.oilPriceList = oilPriceList;
    }

    public String getOilTypeName() {
        return OilTypeName;
    }

    public void setOilTypeName(String oilTypeName) {
        OilTypeName = oilTypeName;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public OilTypeEntity(String oilTypeName) {
        OilTypeName = oilTypeName;
    }

    public OilTypeEntity(String oilTypeName, boolean isSelect) {
        OilTypeName = oilTypeName;
        this.isSelect = isSelect;
    }

    public OilTypeEntity(String oilTypeName, List<OilEntity.StationsBean.OilPriceListBean> oilPriceList) {
        OilTypeName = oilTypeName;
        this.oilPriceList = oilPriceList;
    }
}
