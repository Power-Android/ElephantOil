package com.xxjy.jyyh.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.Base64;

import com.blankj.utilcode.util.LogUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



public class StringUtils {

    /**
     * 将price 进行位数删除后 转换成合适的price  多于两位舍去后面部分 ,同时把小数点末位的0去掉
     *
     * @param price
     * @return
     */
    public static String getGreatPrice(double price) {
        String strPrice = String.valueOf(price);
        if (strPrice.contains("E") || strPrice.contains("e")) {
            return getGreatPrice(new BigDecimal(strPrice).toPlainString());
        }
        return getGreatPrice(String.valueOf(price));
    }

    /**
     * 默认保留2位
     *
     * @param price
     * @return
     */
    public static String[] getSplitPrice(String price) {
        return getSplitPriceEnd(price, ".00");
    }

    /**
     * 默认保留1位
     *
     * @param price
     * @return
     */
    public static String[] getSplitPriceEndOne(String price) {
        return getSplitPriceEnd(price, ".0");
    }

    /**
     * 默认保留0位
     *
     * @param price
     * @return
     */
    public static String[] getSplitPriceEndZero(String price) {
        return getSplitPriceEnd(price, "");
    }

    /**
     * @param price
     * @return
     */
    public static String[] getSplitPriceEnd(String price, String endValue) {
        String[] result = new String[2];
        if (!TextUtils.isEmpty(price)) {
            String[] priceSplit = price.split("\\.");
            result[0] = priceSplit[0];
            if (priceSplit.length == 2) {
                result[1] = "." + priceSplit[1];
            } else {
                result[1] = endValue;
            }
        } else {
            result[0] = "0";
            result[1] = endValue;
        }
        return result;
    }

    public static String getGreatPrice(String price) {
        try {
            String[] split = price.split("\\.");
            if (split.length == 1) {    //说明是整数
                return price;
            }
            String newPrice;
            if (split[1].length() > 2) {    //说明很长
                newPrice = split[1].substring(0, 2);
            } else {
                newPrice = split[1];
            }
            if (Integer.parseInt(newPrice) == 0) {
                return split[0];
            } else {
                newPrice = removeEndZero(newPrice);
                return split[0] + "." + newPrice;
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
            return price;
        }
    }

    //将字符串尾数为0 的字符去掉
    private static String removeEndZero(String newPrice) {
        if (newPrice.endsWith("0")) {
            newPrice = newPrice.substring(0, newPrice.length() - 1);
            return removeEndZero(newPrice);
        } else {
            return newPrice;
        }
    }

    /**
     * 将价格进行四舍五入最多保留两位进行处理, 截取规则为 多余两位四舍五入保留两位, 少于两位保留当前,同时把小数点末位的0去掉
     *
     * @param price
     * @return
     */
    public static String getGreatHalfUp(double price) {
        String discountHalfup = getDiscountHalfup(price, 2);
        double result = new BigDecimal(discountHalfup).doubleValue();
        return getGreatPrice(result);
    }

    public static String getGreatHalfUp(String price) {
        return getGreatHalfUp(new BigDecimal(price).doubleValue());
    }

    /**
     * 将价格进行四舍五入最多保留两位 , 最少保留一位 进行处理
     *
     * @param price
     * @return
     */
    public static String getGreatHalfUpEndOne(double price) {
//        String greatHalfUp = getGreatHalfUp(price);
//        if (!greatHalfUp.contains(".")) {
//            return greatHalfUp + ".0";
//        }
//        return greatHalfUp;

        //全部保留两位小数
        return getDiscountHalfup(price, 2);
    }

    public static String getGreatHalfUpEndOne(String price) {
        if (TextUtils.isEmpty(price)) return "";
        return getGreatHalfUpEndOne(new BigDecimal(price).doubleValue());
    }

    //向上取整
    public static String getDiscountUp(double d, int no) {
        //no代表保留no位小数
        BigDecimal b = new BigDecimal(d);
        double f1 = b.setScale(no, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (no == 2) {
            return new DecimalFormat("0.00").format(f1);
        }
        if (no == 1) {
            return new DecimalFormat("0.0").format(f1);
        }
        return f1 + "";
    }

    //四舍五入
    public static String getDiscountHalfup(double d, int no) {
        //no代表保留no位小数
        BigDecimal b = new BigDecimal(d);
        BigDecimal f1 = b.setScale(no, BigDecimal.ROUND_HALF_UP);
        if (no == 2) {
            return new DecimalFormat("0.00").format(f1);
        }
        if (no == 1) {
            return new DecimalFormat("0.0").format(f1);
        }
        if (no == 0) {
            return new DecimalFormat("0").format(f1);
        }
        return f1 + "";
    }

    /**
     * 一键加油中处理输入的数字
     *
     * @param price
     * @return
     */
    public static String checkPrice(String price) {
        if (TextUtils.isEmpty(price)) {
            return "";
        }
        String result;
        if (price.startsWith(".")) {
            result = "0" + price;
        } else {
            result = getGoodPrice(price);
        }

        double d = 0;
        try {
            d = Double.parseDouble(result);
            if (d > 100000) {
                result = "100000";
            }
        } catch (Exception e) {
            return "0";
        }
        if (!result.contains(".") && d == 0) {
            result = "0";
        }
        return result;
    }

    /**
     * 创建一个图片选择器
     *
     * @param selectedState        普通状态的图片
     * @param unSelectedStateState 选中的状态
     */
    public static StateListDrawable createSelector(Drawable selectedState, Drawable unSelectedStateState) {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_selected}, selectedState);
        bg.addState(new int[]{}, unSelectedStateState);
        return bg;
    }

    /**
     * 对字符串中的点进行处理, 最多保留两位小数
     *
     * @param price
     * @return
     */
    private static String getGoodPrice(String price) {
        if (TextUtils.isEmpty(price)) {
            return "";
        }
        String[] split = price.split("\\.");
        if (split.length == 2) {
            String newPrice = "";
            if (split[1].length() > 2) {    //说明很长
                newPrice = split[1].substring(0, 2);
            } else {
                newPrice = split[1];
            }
            if (Integer.parseInt(split[0]) == 0) {
                return "0." + newPrice;
            } else {
                return split[0] + "." + newPrice;
            }
        }
        return price;
    }

    /**
     * 通过url地址字符串获取值为key的参数内容
     *
     * @param key
     * @return
     */
    public static String getUrlDate(String url, String key) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        int firstIndex = url.indexOf("?");
        if (url.length() > firstIndex + 1) {
            url = url.substring(firstIndex + 1);
            return getUrlKeyDate(url, key);
        } else {
            return null;
        }
    }

    /**
     * 将base64加密过的内容进行解密
     *
     * @return
     */
    public static String decodePwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return pwd;
        }
        byte[] bytes = Base64.decode(pwd, Base64.DEFAULT);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ 0x01);
        }
        String result = null;
        try {
            result = new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        return result;
    }

    //通过url 和key 获取相应的数据
    private static String getUrlKeyDate(String url, String key) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String result = null;
        String[] arrSplit = url.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = strSplit.split("[=]");
            //解析出键值
            if (arrSplitEqual.length > 1 && arrSplitEqual[0].equals(key)) {
                //正确解析
                result = arrSplitEqual[1];
            }
        }
        return result;
    }

    /**
     * 对卡片进行格式化,分成四个四个一组的卡号
     *
     * @param cardNo
     * @return
     */
    public static String getFormatCardNo(String cardNo) {
        return getFormatCardNo(cardNo, 4);
    }

    /**
     * 对卡片进行格式化,分成everyFomatLength一组的卡号
     *
     * @param cardNo
     * @param everyFomatLength 每组的个数
     * @return
     */
    public static String getFormatCardNo(String cardNo, int everyFomatLength) {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(cardNo)) return sb.toString();
        int formatSize = cardNo.length() / everyFomatLength;
        if (formatSize == 0) return cardNo;
        for (int i = 0; i < formatSize; i++) {
            int startSplit = i * everyFomatLength;
            sb.append(cardNo.substring(startSplit, startSplit + everyFomatLength));
            if (i != formatSize - 1) {
                sb.append(" ");
            }
        }
        int tailValue = cardNo.length() % everyFomatLength;
        if (tailValue != 0) {
            sb.append(" " + cardNo.substring(cardNo.length() - tailValue, cardNo.length()));
        }
        return sb.toString();
    }


    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean isContainsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {     // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 对recycler列表的数据进行转换,数据给的是每 everyPageSize 个数据是横向的,因为recycler列表的排列顺序是竖向的,
     * 所以需要将数据进行转换来使竖向的排列达到横向的效果
     *
     * @param products
     * @param everyPageSize
     * @param <T>
     * @return
     */
    public static <T> List<T> convertRecyclerDatas(List<T> products, int everyPageSize) {
        List<List<T>> temp = new ArrayList<>();
        int pageSize = 1;
        if (products.size() > everyPageSize) {
            pageSize = products.size() / everyPageSize;
            int pageDetail = products.size() % everyPageSize;
            if (pageDetail != 0) {
                pageSize++;
            }
        }
        if (pageSize == 1) {
            temp.add(products);
        } else {
            List<T> currentData;
            for (int i = 0; i < pageSize; i++) {
                currentData = new ArrayList<>();
                for (int y = 0; y < everyPageSize; y++) {
                    int realPos = i * everyPageSize + y;
                    if (realPos < products.size()) {
                        currentData.add(products.get(realPos));
                    }
                }
                temp.add(currentData);
            }
        }
        List<T> result = new ArrayList<>();
        for (List<T> data : temp) {
            result.addAll(reverseToHorizontal(data));
        }
        return result;
    }

    private static <T> List<T> reverseToHorizontal(List<T> data) {
        List<T> result = new ArrayList<>();
        int realPos;
        for (int i = 0; i < data.size(); i++) {
            if (i % 2 == 0) {
                realPos = i / 2;
            } else {
                realPos = (data.size() + i) / 2;
            }
            if (realPos < data.size()) {
                result.add(data.get(realPos));
            }
        }
        return result;
    }

    public static <T> List<List<T>> disPathGroupDatas(List<T> products, int everyPageSize) {
        List<List<T>> result = new ArrayList<>();
        if (products == null || products.isEmpty()) {
            return result;
        }
        if (products.size() <= everyPageSize) {
            result.add(products);
            return result;
        }
        int pageSize = products.size() / everyPageSize;
        int pageReduce = products.size() % everyPageSize;
        if (pageReduce != 0) {
            pageSize += 1;
        }
        List<T> currentGroup;
        for (int i = 0; i < pageSize; i++) {
            currentGroup = new ArrayList<>();
            for (int y = 0; y < everyPageSize; y++) {
                int pos = i * everyPageSize + y;
                if (pos < products.size()) {
                    currentGroup.add(products.get(pos));
                }
            }
            result.add(currentGroup);
        }

        return result;
    }
}
