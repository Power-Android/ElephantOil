package com.xxjy.jyyh.entity;

import java.util.List;

public class CarServeCouponListBean {

    private int total;
    private List<CarServeCouponBean> records;
    private int pageIndex;
    private int pageSize;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CarServeCouponBean> getRecords() {
        return records;
    }

    public void setRecords(List<CarServeCouponBean> records) {
        this.records = records;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
