package com.xxjy.jyyh.http;

import android.text.TextUtils;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncodeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author power
 * @date 12/9/20 11:52 AM
 * @project RunElephant
 * @description:
 */
public class HeaderUtils {

    public static Map<String, String> sortMapByKey(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        oriMap.put("rsign", getRandomString(15));
        oriMap.put("did", DeviceUtils.getUniqueDeviceId());

        Map<String, String> sortMap = new TreeMap<>((o1, o2) -> {
            // 升序排序
            return o1.compareTo(o2);

        });

        for (Map.Entry<String, String> entry : oriMap.entrySet()) {
            if (TextUtils.isEmpty(entry.getKey()) || TextUtils.isEmpty(entry.getValue()))
                continue;
            sortMap.put(entry.getKey(), entry.getValue());
        }
        return sortMap;
    }

    /**
     * @param length
     * @return 生成随机字符串
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String getSign(Map<String, String> map) {
        String sign = "";
        if (map != null) {
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry entry : entries) {
                sign += "&" + entry.getKey() + "=";
                sign += entry.getValue();
            }
        }
        String subSign = sign.substring(1);
//        LogUtils.e("加密前的字符串：" + subSign);
        final String ALGORITHM = "HmacSHA1";
        final String ENCODING = "UTF-8";
        String apiKey = "Orvay1rVsoU9nlpY";
        Mac mac = null;
        try {
            mac = Mac.getInstance(ALGORITHM);
            mac.init(new SecretKeySpec(apiKey.getBytes(ENCODING), ALGORITHM));
            byte[] signData = mac.doFinal(subSign.getBytes(ENCODING));
            sign = new String(EncodeUtils.base64Encode(signData));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LogUtils.e("加密后的字符串：" + sign);
        return percentEncode(sign);
    }

    private static final String ENCODING = "UTF-8";
    private static String percentEncode(String value) {
        String replace = "";
        try {
            replace = URLEncoder.encode(value, ENCODING)
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value != null ? replace : null;
    }
}
