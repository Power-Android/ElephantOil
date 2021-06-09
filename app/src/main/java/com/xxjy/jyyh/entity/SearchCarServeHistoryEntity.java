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

@Entity(tableName = "search_car_serve")
public class SearchCarServeHistoryEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String storeName;

    public SearchCarServeHistoryEntity(String storeName) {
        this.storeName = storeName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
