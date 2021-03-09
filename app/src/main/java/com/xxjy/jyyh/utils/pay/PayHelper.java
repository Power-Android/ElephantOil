package com.xxjy.jyyh.utils.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.entity.PayParamsBean;

import java.lang.ref.WeakReference;
import java.util.Map;

public class PayHelper {

    private static PayHelper mPayHelper = null;

    public static PayHelper getInstance() {
        if (mPayHelper == null) {
            mPayHelper = new PayHelper();
        }
        return mPayHelper;
    }

    public PayHelper() {

    }

    IWXAPI msgApi = null;

    public void WexPay(PayParamsBean data) {
        if (msgApi == null) {
            msgApi = WXAPIFactory.createWXAPI(App.getContext(), null);
            msgApi.registerApp(Constants.WX_APP_ID);// 将该app注册到微信
        }
        PayReq req = new PayReq();
        if (!msgApi.isWXAppInstalled()) {
            Toast.makeText(App.getContext(), "手机中没有安装微信客户端!", Toast.LENGTH_SHORT).show();

            return;
        }
        if (data != null) {

            req.appId = data.getAppId();
            req.partnerId = data.getPartnerId();
            req.prepayId = data.getPrepayId();
            req.nonceStr = data.getNonceStr();
            req.timeStamp = data.getTimeStamp();
            req.packageValue = data.getPackageX();
            req.sign = data.getSign();
            msgApi.sendReq(req);
        }
    }
public void unionPay(Context context,String payNo){
    UPPayAssistEx.startPay(context, null, null, payNo, "00");
}

    public void AliPay(Activity activity, final String orderInfo) {
        MyHandler myHandler = new MyHandler(activity);
        final PayTask alipay = new PayTask(activity);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {

                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;

                myHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }



    private  class MyHandler extends Handler {
        WeakReference<Activity> mActivity;
        public MyHandler(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            // 处理消息...
            switch (msg.what) {
                case 0:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        if (mIPayListener != null) {
//                            mIPayListener.onSuccess();
//                        }
                        PayListenerUtils.getInstance().addSuccess();
                    }else if(TextUtils.equals(resultStatus, "6001")){
                        PayListenerUtils.getInstance().addCancel();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                        if (mIPayListener != null) {
//                            mIPayListener.onFail();
//                        }
                        PayListenerUtils.getInstance().addFail();
                    }
                    break;
            }
        }
    }
//    public  IPayListener mIPayListener;
//
//    public void setIPayListener(IPayListener mIPayListener) {
//        this.mIPayListener = mIPayListener;
//    }

//    public interface IPayListener {
//        void onSuccess();
//
//        void onFail();
//    }
}
