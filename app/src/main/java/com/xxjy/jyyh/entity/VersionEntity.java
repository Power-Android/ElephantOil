package com.xxjy.jyyh.entity;

import java.io.Serializable;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2020/11/3.
 */
public class VersionEntity implements Serializable {
    private String lastVersion;
    private int forceUpdate;
    private String url;
    private String description;

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
