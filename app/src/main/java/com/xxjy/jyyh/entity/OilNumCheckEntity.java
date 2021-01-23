package com.xxjy.jyyh.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author power
 * @date 12/10/20 3:12 PM
 * @project RunElephant
 * @description:
 */
public class OilNumCheckEntity implements MultiItemEntity {
    public int key;
    public String type ;
    public OilNumBean.OilListBean oilListBeen;


    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OilNumBean.OilListBean getOilListBeen() {
        return oilListBeen;
    }

    public void setOilListBeen(OilNumBean.OilListBean oilListBeen) {
        this.oilListBeen = oilListBeen;
    }

    @Override
    public int getItemType() {
        return getKey();
    }
}
