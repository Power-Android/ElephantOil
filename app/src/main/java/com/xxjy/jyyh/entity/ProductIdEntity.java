package com.xxjy.jyyh.entity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author power
 * @date 2021/5/27 1:50 下午
 * @project ElephantOil
 * @description:
 */
public class ProductIdEntity implements Serializable {
    private String id;
    private String number;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ProductIdEntity(String id, String number) {
        this.id = id;
        this.number = number;
    }

    @NotNull
    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
