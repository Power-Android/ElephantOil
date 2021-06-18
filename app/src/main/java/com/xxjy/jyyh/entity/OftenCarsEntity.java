package com.xxjy.jyyh.entity;

import java.util.List;

/**
 * @author power
 * @date 2021/6/18 1:54 下午
 * @project ElephantOil
 * @description:
 */
public class OftenCarsEntity {

    private CardStoreInfoVoBean cardStoreInfoVo;
    private List<?> products;

    public CardStoreInfoVoBean getCardStoreInfoVo() {
        return cardStoreInfoVo;
    }

    public void setCardStoreInfoVo(CardStoreInfoVoBean cardStoreInfoVo) {
        this.cardStoreInfoVo = cardStoreInfoVo;
    }

    public List<?> getProducts() {
        return products;
    }

    public void setProducts(List<?> products) {
        this.products = products;
    }

    public static class CardStoreInfoVoBean {
        private Integer id;
        private String storeNo;
        private String thirdNo;
        private String supplierStoreNo;
        private String merchantId;
        private String merchantName;
        private Integer status;
        private String contact;
        private String mobile;
        private String phone;
        private Integer type;
        private String province;
        private String provinceCode;
        private String city;
        private String cityCode;
        private String area;
        private String areaCode;
        private String address;
        private String storeName;
        private String storePicture;
        private Integer channel;
        private Integer serviceType;
        private String supplierName;
        private Double longitude;
        private Double latitude;
        private String openStart;
        private String endStart;
        private String createPerson;
        private String createTime;
        private String updatePerson;
        private String updateTime;
        private String openHoliday;
        private String endHoliday;
        private String holidayReason;
        private Object deletedTime;
        private Integer isOnline;
        private Double distance;
        private Object categoryNameList;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getStoreNo() {
            return storeNo;
        }

        public void setStoreNo(String storeNo) {
            this.storeNo = storeNo;
        }

        public String getThirdNo() {
            return thirdNo;
        }

        public void setThirdNo(String thirdNo) {
            this.thirdNo = thirdNo;
        }

        public String getSupplierStoreNo() {
            return supplierStoreNo;
        }

        public void setSupplierStoreNo(String supplierStoreNo) {
            this.supplierStoreNo = supplierStoreNo;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStorePicture() {
            return storePicture;
        }

        public void setStorePicture(String storePicture) {
            this.storePicture = storePicture;
        }

        public Integer getChannel() {
            return channel;
        }

        public void setChannel(Integer channel) {
            this.channel = channel;
        }

        public Integer getServiceType() {
            return serviceType;
        }

        public void setServiceType(Integer serviceType) {
            this.serviceType = serviceType;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public String getOpenStart() {
            return openStart;
        }

        public void setOpenStart(String openStart) {
            this.openStart = openStart;
        }

        public String getEndStart() {
            return endStart;
        }

        public void setEndStart(String endStart) {
            this.endStart = endStart;
        }

        public String getCreatePerson() {
            return createPerson;
        }

        public void setCreatePerson(String createPerson) {
            this.createPerson = createPerson;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdatePerson() {
            return updatePerson;
        }

        public void setUpdatePerson(String updatePerson) {
            this.updatePerson = updatePerson;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getOpenHoliday() {
            return openHoliday;
        }

        public void setOpenHoliday(String openHoliday) {
            this.openHoliday = openHoliday;
        }

        public String getEndHoliday() {
            return endHoliday;
        }

        public void setEndHoliday(String endHoliday) {
            this.endHoliday = endHoliday;
        }

        public String getHolidayReason() {
            return holidayReason;
        }

        public void setHolidayReason(String holidayReason) {
            this.holidayReason = holidayReason;
        }

        public Object getDeletedTime() {
            return deletedTime;
        }

        public void setDeletedTime(Object deletedTime) {
            this.deletedTime = deletedTime;
        }

        public Integer getIsOnline() {
            return isOnline;
        }

        public void setIsOnline(Integer isOnline) {
            this.isOnline = isOnline;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public Object getCategoryNameList() {
            return categoryNameList;
        }

        public void setCategoryNameList(Object categoryNameList) {
            this.categoryNameList = categoryNameList;
        }
    }
}
