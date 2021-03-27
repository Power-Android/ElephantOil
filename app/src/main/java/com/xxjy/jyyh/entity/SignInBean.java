package com.xxjy.jyyh.entity;

import java.util.List;

public class SignInBean {
    //    {
//        "couponAmount":7,
//            "signDays":0,
//            "signRule":"1.每日签到按1、2、2、3、3、5、6获取单日指定积分，当日重复签到不会重复计数；
//        2.积分按单日积分值获取，如周一1积分，周二2积分，不累计计数；
//        3.用户一周连续签到7日即可获得超值好礼，断签及未满7日连续签到的用户都将不会获得奖励；
//        4.连续签到以一周7日为准，每周一为新的签到起点日即重新自动计算连续天数；
//        5.签满并获得奖励的用户将重新计算天数,并在下一个连续7日签满时再次获得奖励；
//        6.本活动的最终解释权归小象加油所有；
//        7.若您在参与过程中有任何问题请联系客服400-6610-111；",
//        "list":[
//        {
//            "currentDayFlag":false,
//                "dayOfWeek":1,
//                "intelgral":1,
//                "signFlag":false
//        },
//        {
//            "currentDayFlag":false,
//                "dayOfWeek":2,
//                "intelgral":2,
//                "signFlag":false
//        },
//        {
//            "currentDayFlag":false,
//                "dayOfWeek":3,
//                "intelgral":2,
//                "signFlag":false
//        },
//        {
//            "currentDayFlag":false,
//                "dayOfWeek":4,
//                "intelgral":3,
//                "signFlag":false
//        },
//        {
//            "currentDayFlag":true,
//                "dayOfWeek":5,
//                "intelgral":3,
//                "signFlag":false
//        },
//        {
//            "currentDayFlag":false,
//                "dayOfWeek":6,
//                "intelgral":5,
//                "signFlag":false
//        },
//        {
//            "couponId":100003066,
//                "currentDayFlag":false,
//                "dayOfWeek":7,
//                "intelgral":6,
//                "signFlag":false
//        }
//        ]
//    }
    private int couponAmount;
    private int signDays;
    private String signRule;
    private List<SignInDayBean> list;

    public int getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }

    public int getSignDays() {
        return signDays;
    }

    public void setSignDays(int signDays) {
        this.signDays = signDays;
    }

    public String getSignRule() {
        return signRule;
    }

    public void setSignRule(String signRule) {
        this.signRule = signRule;
    }

    public List<SignInDayBean> getList() {
        return list;
    }

    public void setList(List<SignInDayBean> list) {
        this.list = list;
    }
}
