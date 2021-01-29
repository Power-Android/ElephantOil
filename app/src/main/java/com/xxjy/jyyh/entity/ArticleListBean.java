package com.xxjy.jyyh.entity;

import java.util.List;

public class ArticleListBean {
    private int pages;
    private List<ArticleBean> list;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<ArticleBean> getList() {
        return list;
    }

    public void setList(List<ArticleBean> list) {
        this.list = list;
    }
}
