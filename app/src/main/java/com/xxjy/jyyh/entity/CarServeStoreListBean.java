package com.xxjy.jyyh.entity;

import java.util.List;

public class CarServeStoreListBean {
    private int pageIndex;
    private int pageSize;
    private int total;
    private List<CarServeStoreBean> records;

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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CarServeStoreBean> getRecords() {
        return records;
    }

    public void setRecords(List<CarServeStoreBean> records) {
        this.records = records;
    }
}
