package com.xxjy.jyyh.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.xxjy.jyyh.base.BaseActivity;

import java.io.File;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2019/1/25.
 */
public class NaviActivityInfo {
    private static final String SAVE_PICTURE_ERROR_MSG = "图片保存失败，请提供存储卡读写权限";

    //一键加油
    public static final String NATIVE_RECHARGE_ADD_OIL = "native=rechargeAddOil";

    //消息中心
    public static final String NATIVE_TO_MSG_CENTER = "native=toMessageCenter";

    //商城的商品分类列表(商城二级界面)
    public static final String NATIVE_STORE_TYPE_LIST = "native=storeTypeList";

    //跳转加油支付结果界面
    public static final String NATIVE_TO_PAY_RESULT = "native=payResultAddOil";

    //显示客服帮助的弹窗
    public static final String NATIVE_SHOW_CALL_HELP = "native=customerService";

    //跳转到客服聊天页面
    public static final String NATIVE_TO_HELP_CHAT = "native=onlineService";

    //跳转到客服打电话页面
    public static final String NATIVE_TO_HELP_DIAL_PHONE = "native=dialPhoneService";

    //地址管理界面
    public static final String NATIVE_TO_ADDRESS_MANAGE = "native=addressManagement";

    //优惠券列表
    public static final String NATIVE_TO_MY_COUPON = "native=couponList";

    //跳转到首页
    public static final String NATIVE_TO_ONECARD_HOME = "native=toOnecardHome";

    //保存图片
    public static final String NATIVE_SAVE_PICTURE = "native=savePicture";

    /**
     * 从Url提取intent信息并分发
     *
     * @param activity
     * @param urlInfo
     * @return
     */
    public static void disPathIntentFromUrl(BaseActivity activity, String urlInfo) {
//        if (TextUtils.isEmpty(urlInfo)) return;
//        if (urlInfo.contains(NATIVE_RECHARGE_ADD_OIL)) {        //一键加油
////            if (Tool.htmlDatas != null && !TextUtils.isEmpty(Tool.htmlDatas.getOneKeyRefuel())) {
////                urlInfo = Tool.htmlDatas.getOneKeyRefuel();
////            }
////            WebViewCzbOilActivity.openCzbOilebActivity(activity, urlInfo);
//            String isOpenVip = getParams(urlInfo, "isOpenVip");
//            if (TextUtils.isEmpty(isOpenVip)) {
//                isOpenVip = null;
//            }
//            OilStationListActivity.openOilStationListAct(activity, isOpenVip);
//        }
////        else if (urlInfo.contains(NATIVE_RECHARGE_ONE_CARD)) {   //1号卡充值
////            RechargeOneCardActivity.openRechargeOneCardAct(activity);
////        }
//        else if (urlInfo.contains(NATIVE_STORE_TYPE_LIST)) {   //商城的商品分类列表
//            String mapClassId = getParams(urlInfo, "mapClassId");
//            String storeId = getParams(urlInfo, "storeId");
//            StoreTypeNewDetailActivity.openStoreProductDetail(activity, mapClassId, storeId);
//        } else if (urlInfo.contains(NATIVE_TO_MSG_CENTER)) {    //消息中心
//            if (activity instanceof MessageCenterActivity) return;
//            if (Tool.checkIsLogin(activity)) {
//                activity.startActivity(MessageCenterActivity.class);
//            }
//        } else if (urlInfo.contains(NATIVE_TO_PAY_RESULT)) {    //支付结果
//            String orderNo = getParams(urlInfo, "orderNo");
//            String orderId = getParams(urlInfo, "orderId");
//            PayResultNewActivity.openPayResultAct(activity, orderNo, orderId);
//        }
//        //客服相关
//        else if (urlInfo.contains(NATIVE_SHOW_CALL_HELP)) {
//            String phoneNumber = getParams(urlInfo, "phoneNumber");
//            Tool.showCallHelpDialog(activity, phoneNumber);
//        } else if (urlInfo.contains(NATIVE_TO_HELP_CHAT)) {
//            CallHelpDialog.toWebChatOnline(activity, "");
//        } else if (urlInfo.contains(NATIVE_TO_HELP_DIAL_PHONE)) {
//            String phoneNumber = getParams(urlInfo, "phoneNumber");
//            CallHelpDialog.toDialPhoneAct(activity, phoneNumber);
//        }
//        //客服
//        else if (urlInfo.contains(NATIVE_TO_ADDRESS_MANAGE)) {
//            GoodsAddressListActivity.openAddressListAct(activity);
//        } else if (urlInfo.contains(NATIVE_TO_MY_COUPON)) {
//            if (Tool.checkIsLogin(activity)) {
//                MyCouponsActivity.openMyCouponsListAct(activity);
//            }
//        } else if (urlInfo.contains(NATIVE_TO_ONECARD_HOME)) {
//            String index = getParams(urlInfo, "index");
//            if (!TextUtils.isEmpty(index)) {
//                if (index.equals("store")) {
//                    UiUtils.jumpToHome(activity, Tool.LOGIN_TO_STORE);
//                    return;
//                } else if (index.equals("mine")) {
//                    UiUtils.jumpToHome(activity, Tool.LOGIN_TO_MY);
//                    return;
//                } else if (index.equals("shoppingCart")) {
//                    UiUtils.jumpToHome(activity, Tool.LOGIN_TO_SHOPPING);
//                    return;
//                }
//            }
//            UiUtils.jumpToHome(activity, Tool.LOGIN_OUT);
//        } else if (urlInfo.contains(NATIVE_SAVE_PICTURE)) {
//            String imageString = getParams(urlInfo, "imageString");
//            if (!TextUtils.isEmpty(imageString)) {
//                try {
//                    int indexOf = imageString.indexOf(",");
//                    if (indexOf >= imageString.length() - 1) {
//                        activity.showToastWarning(SAVE_PICTURE_ERROR_MSG);
//                        return;
//                    }
//                    String result = imageString.substring(indexOf + 1);
////                    byte[] bytes = Base64.decode(result, Base64.DEFAULT);
//                    String startString = imageString.substring(0, indexOf);
//                    byte[] bytes = EncodeUtils.base64Decode(result);
//                    if (bytes != null) {
//                        Bitmap.CompressFormat formats = Bitmap.CompressFormat.JPEG;
//                        if (!TextUtils.isEmpty(startString)) {
//                            if (startString.contains("png")) {
//                                formats = Bitmap.CompressFormat.PNG;
//                            }
//                        }
//                        File file = ImageUtils.save2Album(ImageUtils.bytes2Bitmap(bytes), formats);
//                        if (file != null) {
//                            activity.showToastSuccess("保存成功，图片已保存至相册");
//                        } else {
//                            activity.showToastWarning(SAVE_PICTURE_ERROR_MSG);
//                        }
//                    }
//                } catch (Exception e) {
//                    activity.showToastWarning(SAVE_PICTURE_ERROR_MSG);
//                }
//            }
//        }
//        //默认的
//        else {
//            WebViewActivity.openRealUrlWebActivity(activity, urlInfo);
//        }
    }

    //从link中获取key值, 取值位置是 链接?之后的 key=value 的value值
    public static String getParams(String link, String key) {
        String result = "";
        if (TextUtils.isEmpty(link)) return result;

        if (link.contains("?")) {
            if (link.length() > link.indexOf("?") + 1) {
                link = link.substring(link.indexOf("?") + 1);
            }
        }
        String regex = key + "=";
        String[] split = link.split("&");
        for (int i = 0; i < split.length; i++) {
            if (split[i].startsWith(regex)) {
                result = split[i].substring(regex.length());
                break;
            }
        }
        return result;
    }

    /**
     * 获取link ? 和 & 符号之前中目标 tagStr 字符串之后 第 nextPosition 个值, 取值位置是 链接?之前的
     * link/result1/result2 中的 result1值或者result2值, 通过nextPosition来确定取哪个值
     *
     * @param link
     * @param tagStr
     * @param nextPosition
     * @return
     */
    private static String parseUrlParams(String link, String tagStr, int nextPosition) {
        String result = "";
        if (TextUtils.isEmpty(link)) return result;

        if (link.contains("?")) {
            if (link.length() > link.indexOf("?")) {
                link = link.substring(0, link.indexOf("?"));
            }
        }
        if (link.contains("&")) {
            if (link.length() > link.indexOf("&")) {
                link = link.substring(0, link.indexOf("&"));
            }
        }
        String[] split = link.split("\\/");
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals(tagStr)) {
                if (split.length > i + nextPosition) {
                    result = split[i + nextPosition];
                }
                break;
            }
        }
        return result;
    }
}
