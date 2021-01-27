package com.xxjy.jyyh.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author power
 * @date 1/26/21 3:51 PM
 * @project ElephantOil
 * @description:
 */
public class OilEntity implements Serializable {

    private List<StationsBean> stations;

    public List<StationsBean> getStations() {
        return stations;
    }

    public void setStations(List<StationsBean> stations) {
        this.stations = stations;
    }

    public static class StationsBean {
        private String cityName;
        private String countyName;
        private Double distance;
        private String gasAddress;
        private String gasId;
        private String gasName;
        private Integer gasType;
        private String gasTypeImg;
        private String gasTypeName;
        private Integer isInvoice;
        private boolean isSign;
        private String oilName;
        private String oilNo;
        private Integer polyOil;
        private String priceGun;
        private String priceOfficial;
        private String priceYfq;
        private String provinceName;
        private Double saveAmount;
        private Double stationLatitude;
        private Double stationLongitude;
        private List<CzbLabelsBean> czbLabels;
        private List<GunNosBean> gunNos;
        private List<OilPriceListBean> oilPriceList;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCountyName() {
            return countyName;
        }

        public void setCountyName(String countyName) {
            this.countyName = countyName;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public String getGasAddress() {
            return gasAddress;
        }

        public void setGasAddress(String gasAddress) {
            this.gasAddress = gasAddress;
        }

        public String getGasId() {
            return gasId;
        }

        public void setGasId(String gasId) {
            this.gasId = gasId;
        }

        public String getGasName() {
            return gasName;
        }

        public void setGasName(String gasName) {
            this.gasName = gasName;
        }

        public Integer getGasType() {
            return gasType;
        }

        public void setGasType(Integer gasType) {
            this.gasType = gasType;
        }

        public String getGasTypeImg() {
            return gasTypeImg;
        }

        public void setGasTypeImg(String gasTypeImg) {
            this.gasTypeImg = gasTypeImg;
        }

        public String getGasTypeName() {
            return gasTypeName;
        }

        public void setGasTypeName(String gasTypeName) {
            this.gasTypeName = gasTypeName;
        }

        public Integer getIsInvoice() {
            return isInvoice;
        }

        public void setIsInvoice(Integer isInvoice) {
            this.isInvoice = isInvoice;
        }

        public boolean isIsSign() {
            return isSign;
        }

        public void setIsSign(boolean isSign) {
            this.isSign = isSign;
        }

        public String getOilName() {
            return oilName;
        }

        public void setOilName(String oilName) {
            this.oilName = oilName;
        }

        public String getOilNo() {
            return oilNo;
        }

        public void setOilNo(String oilNo) {
            this.oilNo = oilNo;
        }

        public Integer getPolyOil() {
            return polyOil;
        }

        public void setPolyOil(Integer polyOil) {
            this.polyOil = polyOil;
        }

        public String getPriceGun() {
            return priceGun;
        }

        public void setPriceGun(String priceGun) {
            this.priceGun = priceGun;
        }

        public String getPriceOfficial() {
            return priceOfficial;
        }

        public void setPriceOfficial(String priceOfficial) {
            this.priceOfficial = priceOfficial;
        }

        public String getPriceYfq() {
            return priceYfq;
        }

        public void setPriceYfq(String priceYfq) {
            this.priceYfq = priceYfq;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public Double getSaveAmount() {
            return saveAmount;
        }

        public void setSaveAmount(Double saveAmount) {
            this.saveAmount = saveAmount;
        }

        public Double getStationLatitude() {
            return stationLatitude;
        }

        public void setStationLatitude(Double stationLatitude) {
            this.stationLatitude = stationLatitude;
        }

        public Double getStationLongitude() {
            return stationLongitude;
        }

        public void setStationLongitude(Double stationLongitude) {
            this.stationLongitude = stationLongitude;
        }

        public List<CzbLabelsBean> getCzbLabels() {
            return czbLabels;
        }

        public void setCzbLabels(List<CzbLabelsBean> czbLabels) {
            this.czbLabels = czbLabels;
        }

        public List<GunNosBean> getGunNos() {
            return gunNos;
        }

        public void setGunNos(List<GunNosBean> gunNos) {
            this.gunNos = gunNos;
        }

        public List<OilPriceListBean> getOilPriceList() {
            return oilPriceList;
        }

        public void setOilPriceList(List<OilPriceListBean> oilPriceList) {
            this.oilPriceList = oilPriceList;
        }

        public static class CzbLabelsBean {
            private String tagDescription;
            private String tagImageName;
            private String tagIndexDescription;
            private String tagName;
            private String tagType;

            public String getTagDescription() {
                return tagDescription;
            }

            public void setTagDescription(String tagDescription) {
                this.tagDescription = tagDescription;
            }

            public String getTagImageName() {
                return tagImageName;
            }

            public void setTagImageName(String tagImageName) {
                this.tagImageName = tagImageName;
            }

            public String getTagIndexDescription() {
                return tagIndexDescription;
            }

            public void setTagIndexDescription(String tagIndexDescription) {
                this.tagIndexDescription = tagIndexDescription;
            }

            public String getTagName() {
                return tagName;
            }

            public void setTagName(String tagName) {
                this.tagName = tagName;
            }

            public String getTagType() {
                return tagType;
            }

            public void setTagType(String tagType) {
                this.tagType = tagType;
            }
        }

        public static class GunNosBean {
            private Integer gunNo;

            public Integer getGunNo() {
                return gunNo;
            }

            public void setGunNo(Integer gunNo) {
                this.gunNo = gunNo;
            }
        }

        public static class OilPriceListBean {
            private String priceYfq;
            private Integer oilType;
            private String oilName;
            private String priceOfficial;
            private Integer oilNo;
            private String priceGun;
            private List<GunNosBean> gunNos;
            private boolean isSelected;

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public String getPriceYfq() {
                return priceYfq;
            }

            public void setPriceYfq(String priceYfq) {
                this.priceYfq = priceYfq;
            }

            public Integer getOilType() {
                return oilType;
            }

            public void setOilType(Integer oilType) {
                this.oilType = oilType;
            }

            public String getOilName() {
                return oilName;
            }

            public void setOilName(String oilName) {
                this.oilName = oilName;
            }

            public String getPriceOfficial() {
                return priceOfficial;
            }

            public void setPriceOfficial(String priceOfficial) {
                this.priceOfficial = priceOfficial;
            }

            public Integer getOilNo() {
                return oilNo;
            }

            public void setOilNo(Integer oilNo) {
                this.oilNo = oilNo;
            }

            public String getPriceGun() {
                return priceGun;
            }

            public void setPriceGun(String priceGun) {
                this.priceGun = priceGun;
            }

            public List<GunNosBean> getGunNos() {
                return gunNos;
            }

            public void setGunNos(List<GunNosBean> gunNos) {
                this.gunNos = gunNos;
            }

            public static class GunNosBean {
                private Integer gunNo;

                public Integer getGunNo() {
                    return gunNo;
                }

                public void setGunNo(Integer gunNo) {
                    this.gunNo = gunNo;
                }
            }
        }
    }
}
