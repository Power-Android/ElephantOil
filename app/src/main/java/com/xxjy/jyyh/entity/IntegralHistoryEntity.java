package com.xxjy.jyyh.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * @author power
 * @date 1/31/21 11:11 AM
 * @project ElephantOil
 * @description:
 */
@Entity(tableName = "search_integral")
public class IntegralHistoryEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String integralName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIntegralName() {
        return integralName;
    }

    public void setIntegralName(String integralName) {
        this.integralName = integralName;
    }

    public IntegralHistoryEntity(String integralName) {
        this.integralName = integralName;
    }
}
