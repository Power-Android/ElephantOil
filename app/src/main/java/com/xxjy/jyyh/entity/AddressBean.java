package com.xxjy.jyyh.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2020/9/3.
 * <p>
 * 地址的bean
 */
public class AddressBean implements Serializable {

    /**
     * id : 4000021
     * isDelete : false
     * detail : 北京市可视对讲福克斯将对方立刻就拉克丝地方离开首都基辅
     * county : 宣武区
     * isDefault : false
     * name : 拉克丝
     * userId : 100002060
     * province : 北京市
     * city : 北京市
     * mobile : 18813928176
     */

    private String id;
    private boolean isDelete;
    private String detail;
    private String county;
    private boolean isDefault;
    private String name;
    private String userId;
    private String province;
    private String city;
    private String phone;
    private String addressCode;
    private String townShip;          //地址街道,用来标记第四级

    public String getTownShip() {
        return townShip == null ? "" : townShip;
    }

    public void setTownShip(String townShip) {
        this.townShip = townShip;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public String getId() {
        return TextUtils.isEmpty(id) ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvinceAddressCode() {
        return getAddressSingleCode(0);
    }

    public String getCityAddressCode() {
        return getAddressSingleCode(1);
    }

    public String getCountyAddressCode() {
        return getAddressSingleCode(2);
    }

    public String getTownAddressCode() {
        return getAddressSingleCode(3);
    }

    public String getAddressSingleCode(int index) {
        String result = "";
        if (!TextUtils.isEmpty(addressCode)) {
            String[] addressCodes = addressCode.split("_");
            if (addressCodes.length > index) {
                result = addressCodes[index];
            }
        }
        return result;
    }
}
