package com.xxjy.jyyh.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @author power
 * @date 12/10/20 2:36 PM
 * @project RunElephant
 * @description:
 */
public class OilNumBean implements MultiItemEntity {
    private int itemType;
    private String type;
    private List<OilListBean> oilList;

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<OilListBean> getOilList() {
        return oilList;
    }

    public void setOilList(List<OilListBean> oilList) {
        this.oilList = oilList;
    }


    public static class OilListBean {
        private Integer oilAliasId;
        private String oilNum;
        private String id;

        public Integer getOilAliasId() {
            return oilAliasId;
        }

        public void setOilAliasId(Integer oilAliasId) {
            this.oilAliasId = oilAliasId;
        }

        public String getOilNum() {
            return oilNum;
        }

        public void setOilNum(String oilNum) {
            this.oilNum = oilNum;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
