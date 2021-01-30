package com.xxjy.jyyh.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.constants.Constants;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * 微信sdk的相关工具类,来进行注册等功能.
 * 微信的分享和登录功能使用友盟sdk来实现, 其他功能自行施行
 */
public class WXSdkManager {

    private static WXSdkManager instance;

    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;
    private boolean isRegisterSuccess = false;

    /**
     * 不可创建对象
     */
    private WXSdkManager() {
    }

    /**
     * 将api注册到微信, 可以在程序入口 Activity 的 onCreate 回调函数处
     * 或其他合适的地方将你的应用 id 注册到微信。
     *
     * @param activity
     * @param appId
     */
    public void regToWx(BaseActivity activity, String appId) {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(activity, appId, true);

        // 将应用的appId注册到微信
        isRegisterSuccess = api.registerApp(appId);

        //建议动态监听微信启动广播进行注册到微信
        activity.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // 将该app注册到微信
                isRegisterSuccess = api.registerApp(appId);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));

        //将WX接收端的当前api对象进行更新
//        WXEntryActivity.setCurrentApi(api);
    }

    /**
     * 获取当前的wxapi,可能为空,需要先进行注册
     *
     * @return
     */
    public IWXAPI getCurrentWxApi() {
        return api;
    }

    /**
     * 检查wx的合法性,这里会给予统一的提示,包括检测是否安装微信, 微信版本是否过低无法处理,需要先进行注册api.
     *
     * @return true表示合法, 可以进行下一步, 当注册api成功并且合法会返回true, 所以此时应该先注册
     */
    public boolean checkWXIsInstall(BaseActivity activity) {
        IWXAPI currentWxApi = getCurrentWxApi();
        if (currentWxApi == null) {
            return true;
        }
        boolean result = true;
        if (!currentWxApi.isWXAppInstalled()) {
            activity.showToastWarning("未安装微信，请安装后重试");
            result = false;
        } else {
            boolean isPaySupported = currentWxApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
            if (!isPaySupported) {
                activity.showToastWarning("微信版本过低，请更新后重试");
                result = false;
            }
        }
        return result;
    }

    /**
     * 使用当前的api进行登录调用
     */
//    public void useWxLogin() {
//        SendAuth.Req req = new SendAuth.Req();
//        req.scope = "snsapi_userinfo";
//        String currentState = getCurrentState();
//        req.state = currentState;
//        api.sendReq(req);
//    }

    /**
     * 跳转微信小程序支付
     *
     * @param activity
     * @param params
     */
    public void useWXLaunchMiniProgramToPay(BaseActivity activity, String params) {
        if (!isRegisterToWx()) {
            regToWx(activity, getWxAppId());
        }
        if (!isRegisterToWx()) {
            activity.showToastWarning("注册微信发生错误,请联系客服");
            return;
        }
        if (!checkWXIsInstall(activity)) {
            return;
        }
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_b8fe0cd14d82"; // 填小程序原始id
        req.path = "pages/appPay/pay/index?orderId=" + params;                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        if (Constants.IS_DEBUG) {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW; // 可选打开 开发版，体验版和正式版
        } else {
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE; // 可选打开 开发版，体验版和正式版
        }
        if (getCurrentWxApi() != null) {
            getCurrentWxApi().sendReq(req);
        }
    }



    private static final int THUMB_SIZE = 150;

    public void shareWX(BaseActivity activity, String data) {
        activity.showLoadingDialog();
        if (!isRegisterToWx()) {
            regToWx(activity, getWxAppId());
        }
        if (!isRegisterToWx()) {
            activity.showToastWarning("注册微信发生错误,请联系客服");
            return;
        }
        if (!checkWXIsInstall(activity)) {
            return;
        }

        Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(@Nullable ObservableEmitter<byte[]> emitter) {
                emitter.onNext(Util.getHtmlByteArray(data));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())                // 切换上游线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<byte[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(byte[] value) {
                        activity.dismissLoadingDialog();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(value, 0, value.length);
                        WXImageObject imgObj = new WXImageObject(bitmap);
                        WXMediaMessage msg = new WXMediaMessage();
                        msg.mediaObject = imgObj;
//设置缩略图
                        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
                        bitmap.recycle();
                        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);
//                        msg.thumbData =value;
//构造一个Req
                        SendMessageToWX.Req req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("img");
                        req.message = msg;
                        req.scene = SendMessageToWX.Req.WXSceneSession;
//        req.userOpenId = getOpenId();
//调用api接口，发送数据到微信
                        if (getCurrentWxApi() != null) {
                            getCurrentWxApi().sendReq(req);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        activity.dismissLoadingDialog();
                        Log.e("onError", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private boolean isRegisterToWx() {
        return getCurrentWxApi() != null && isRegisterSuccess;
    }

    /**
     * 获取微信登录的appid
     *
     * @return
     */
    public String getWxAppId() {
        return Constants.WX_APP_ID;
    }

    public static WXSdkManager newInstance() {
        if (instance == null) {
            instance = new WXSdkManager();
        }
        return instance;
    }
}
