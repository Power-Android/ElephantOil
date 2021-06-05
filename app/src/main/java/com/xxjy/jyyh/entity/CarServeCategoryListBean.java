package com.xxjy.jyyh.entity;

import java.util.List;

public class CarServeCategoryListBean {
    private int total;
    private List<CarServeCategoryBean> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CarServeCategoryBean> getRecords() {
        return records;
    }

    public void setRecords(List<CarServeCategoryBean> records) {
        this.records = records;
    }
}
