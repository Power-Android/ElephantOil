package com.xxjy.jyyh.utils;

import android.text.TextUtils;

import com.xxjy.jyyh.entity.OilEntity;

/**
 * @author power
 * @date 2/7/21 7:03 PM
 * @project ElephantOil
 * @description:
 */
public class OilUtils {
    /**
     * 判断油号是否是汽油
     *
     * @param oilNumInfo
     * @return
     */
    public static boolean isOilNumGas(OilEntity.StationsBean.OilPriceListBean oilNumInfo) {
        int oilType = oilNumInfo.getOilType();
        if (oilType == 0) {
            return isOilNumGas(oilNumInfo.getOilName());
        }
        return oilType == 1;   //汽油
    }

    //汽油
    private static boolean isOilNumGas(String oilNum) {
        if (TextUtils.isEmpty(oilNum)) return false;
        String[] oilNumList = {"90#", "92#", "93#", "95#", "97#", "98#", "101#", "68#"};
        boolean result = false;
        for (int i = 0; i < oilNumList.length; i++) {
            result = oilNumList[i].equals(oilNum);
            if (result) {
                break;
            }
        }
        return result;
    }

    /**
     * 判断油号是否是柴油
     *
     * @param oilNumInfo
     * @return
     */
    public static boolean isOilNumDiesel(OilEntity.StationsBean.OilPriceListBean oilNumInfo) {
        int oilType = oilNumInfo.getOilType();
        if (oilType == 0) {
            return isOilNumDiesel(oilNumInfo.getOilName());
        }
        return oilType == 2;  //柴油
    }

    //柴油
    private static boolean isOilNumDiesel(String oilNum) {
        if (TextUtils.isEmpty(oilNum)) return false;
        String[] oilNumList = {"-40#", "-35#", "-30#", "-20#", "-10#", "国四0#", "0#"};
        boolean result = false;
        for (int i = 0; i < oilNumList.length; i++) {
            result = oilNumList[i].equals(oilNum);
            if (result) {
                break;
            }
        }
        return result;
    }

    /**
     * 判断油号是否是天然气
     *
     * @param oilNumInfo
     * @return
     */
    public static boolean isOilNumNatural(OilEntity.StationsBean.OilPriceListBean oilNumInfo) {
        int oilType = oilNumInfo.getOilType();
        if (oilType == 0) {
            return isOilNumNatural(oilNumInfo.getOilName());
        }
        return oilType == 3;  //天然气
    }

    //天然气
    private static boolean isOilNumNatural(String oilNum) {
        if (TextUtils.isEmpty(oilNum)) return false;
        String[] oilNumList = {"CNG", "LNG"};
        boolean result = false;
        for (int i = 0; i < oilNumList.length; i++) {
            result = oilNumList[i].equals(oilNum);
            if (result) {
                break;
            }
        }
        return result;
    }
}
