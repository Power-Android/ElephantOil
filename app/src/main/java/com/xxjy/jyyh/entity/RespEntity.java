package com.xxjy.jyyh.entity;

import java.io.Serializable;

/**
 * 小象加油
 * ---------------------------
 * <p>
 * Created by Power on 2020/11/27.
 */
public class RespEntity implements Serializable {
    private String orderId;
    private String outOrderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }
}
