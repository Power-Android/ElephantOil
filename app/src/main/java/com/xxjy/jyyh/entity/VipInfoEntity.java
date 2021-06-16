package com.xxjy.jyyh.entity;

import java.util.List;

/**
 * @author power
 * @date 2021/6/11 2:47 下午
 * @project ElephantOil
 * @description:
 */
public class VipInfoEntity {

    private String adImgUrl;
    private String amount;
    private Boolean awardFlag;
    private Integer cardNum;
    private String userCardId;
    private String expire;
    private String id;
    private String inviteAmount;
    private String inviteNum;
    private String monthRemainStr;
    private String phone;
    private String refuelRemainCount;
    private String remainCount;
    private String terminusCardNum;
    private String usableCount;
    private String description;
    private String saveMoney;

    public String getSaveMoney() {
        return saveMoney;
    }

    public void setSaveMoney(String saveMoney) {
        this.saveMoney = saveMoney;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserCardId() {
        return userCardId;
    }

    public void setUserCardId(String userCardId) {
        this.userCardId = userCardId;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInviteAmount() {
        return inviteAmount;
    }

    public void setInviteAmount(String inviteAmount) {
        this.inviteAmount = inviteAmount;
    }

    public String getInviteNum() {
        return inviteNum;
    }

    public void setInviteNum(String inviteNum) {
        this.inviteNum = inviteNum;
    }

    public String getMonthRemainStr() {
        return monthRemainStr;
    }

    public void setMonthRemainStr(String monthRemainStr) {
        this.monthRemainStr = monthRemainStr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRefuelRemainCount() {
        return refuelRemainCount;
    }

    public void setRefuelRemainCount(String refuelRemainCount) {
        this.refuelRemainCount = refuelRemainCount;
    }

    public String getRemainCount() {
        return remainCount;
    }

    public void setRemainCount(String remainCount) {
        this.remainCount = remainCount;
    }

    public String getTerminusCardNum() {
        return terminusCardNum;
    }

    public void setTerminusCardNum(String terminusCardNum) {
        this.terminusCardNum = terminusCardNum;
    }

    public String getUsableCount() {
        return usableCount;
    }

    public void setUsableCount(String usableCount) {
        this.usableCount = usableCount;
    }

    private List<FirmVipCardEquityVoListBean> firmVipCardEquityVoList;

    public String getAdImgUrl() {
        return adImgUrl;
    }

    public void setAdImgUrl(String adImgUrl) {
        this.adImgUrl = adImgUrl;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Boolean getAwardFlag() {
        return awardFlag;
    }

    public void setAwardFlag(Boolean awardFlag) {
        this.awardFlag = awardFlag;
    }

    public Integer getCardNum() {
        return cardNum;
    }

    public void setCardNum(Integer cardNum) {
        this.cardNum = cardNum;
    }

    public List<FirmVipCardEquityVoListBean> getFirmVipCardEquityVoList() {
        return firmVipCardEquityVoList;
    }

    public void setFirmVipCardEquityVoList(List<FirmVipCardEquityVoListBean> firmVipCardEquityVoList) {
        this.firmVipCardEquityVoList = firmVipCardEquityVoList;
    }

    public static class FirmVipCardEquityVoListBean {
        private Integer cardId;
        private String description;
        private String equityDirectUrl;
        private String equityIconUrl;
        private String equityNumDesc;
        private Integer equitySeq;
        private String equitySubtitle;
        private String equityTitle;
        private Integer equityType;
        private Integer id;

        public Integer getCardId() {
            return cardId;
        }

        public void setCardId(Integer cardId) {
            this.cardId = cardId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEquityDirectUrl() {
            return equityDirectUrl;
        }

        public void setEquityDirectUrl(String equityDirectUrl) {
            this.equityDirectUrl = equityDirectUrl;
        }

        public String getEquityIconUrl() {
            return equityIconUrl;
        }

        public void setEquityIconUrl(String equityIconUrl) {
            this.equityIconUrl = equityIconUrl;
        }

        public String getEquityNumDesc() {
            return equityNumDesc;
        }

        public void setEquityNumDesc(String equityNumDesc) {
            this.equityNumDesc = equityNumDesc;
        }

        public Integer getEquitySeq() {
            return equitySeq;
        }

        public void setEquitySeq(Integer equitySeq) {
            this.equitySeq = equitySeq;
        }

        public String getEquitySubtitle() {
            return equitySubtitle;
        }

        public void setEquitySubtitle(String equitySubtitle) {
            this.equitySubtitle = equitySubtitle;
        }

        public String getEquityTitle() {
            return equityTitle;
        }

        public void setEquityTitle(String equityTitle) {
            this.equityTitle = equityTitle;
        }

        public Integer getEquityType() {
            return equityType;
        }

        public void setEquityType(Integer equityType) {
            this.equityType = equityType;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}
