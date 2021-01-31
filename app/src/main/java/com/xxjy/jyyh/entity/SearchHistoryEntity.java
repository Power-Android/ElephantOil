package com.xxjy.jyyh.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * @author power
 * @date 12/11/20 1:26 PM
 * @project RunElephant
 * @description:
 */

@Entity(tableName = "search_oil")
public class SearchHistoryEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String gasName;

    public SearchHistoryEntity(String gasName) {
        this.gasName = gasName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGasName() {
        return gasName;
    }

    public void setGasName(String gasName) {
        this.gasName = gasName;
    }
}
