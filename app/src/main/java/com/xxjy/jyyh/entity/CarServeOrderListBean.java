package com.xxjy.jyyh.entity;

import java.util.List;

public class CarServeOrderListBean {

    private int pages;
    private List<CarServeOrderBean> list;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<CarServeOrderBean> getList() {
        return list;
    }

    public void setList(List<CarServeOrderBean> list) {
        this.list = list;
    }
}
