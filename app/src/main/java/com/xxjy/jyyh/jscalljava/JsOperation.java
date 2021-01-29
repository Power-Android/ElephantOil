package com.xxjy.jyyh.jscalljava;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.constants.SPConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.http.HttpManager;
import com.xxjy.jyyh.jscalljava.jscallback.OnJsCallListener;
import com.xxjy.jyyh.ui.web.WeChatWebPayActivity;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.ImageUtils;
import com.xxjy.jyyh.utils.StatusBarUtil;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.locationmanger.MapLocationHelper;
import com.xxjy.jyyh.utils.toastlib.Toasty;

import org.json.JSONObject;

import java.util.Map;

/**
 * 该类提供了 js 调用的 接口 , 可以增加方法的方式来进行跳转 , h5 调用端的代码为 :  window.benXiang.getAppInfo() 方法名字
 */
public class JsOperation implements JsOperationMethods {
    public static final String JS_USE_NAME = "littleElephant";
    private BaseActivity mActivity;
    private OnJsCallListener mListener;

    public JsOperation(BaseActivity activity) {
        mActivity = activity;
    }

    public void setOnJsCallListener(OnJsCallListener listener) {
        mListener = listener;
    }

    //获取appinfo
    @Override
    @JavascriptInterface
    public String getAppInfo() {
        Map<String, String> finalParams = HttpManager.getCommonParams(null);
        finalParams.put(SPConstants.APP_TOKEN, UserConstants.getToken());
        AMapLocation mapLocation = MapLocationHelper.getMapLocation();
        if (mapLocation != null) {
            finalParams.put("cityName", mapLocation.getCity());
        }
        JSONObject params = new JSONObject(finalParams);
        Log.e("getAppInfo",params.toString());
        return params.toString();
    }

    //跳转主页
    @Override
    @JavascriptInterface
    public void toBenXiangHome() {
//        if (mActivity instanceof MainActivity) {
//            MainActivity activity = (MainActivity) mActivity;
//            activity.toHome();
//        } else {
//            Intent intent = new Intent(mActivity, MainActivity.class);
//            MainActivity.stateFragment = Tool.LOGIN_OUT;
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            mActivity.startActivity(intent);
//        }
    }

    // 调起分享
    @Override
    @JavascriptInterface
    public void startShare(final String shardInfo) {
        LogUtils.e("获取到的数据为: " + shardInfo);
        if (mListener != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onJsCallListener(OnJsCallListener.CALL_TYPE_SHARE, shardInfo);
                }
            });
        }
    }

    @Override
    @JavascriptInterface
    public void showSharedIcon(final String shardInfo) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (mActivity instanceof WebViewActivity) {
//                    WebViewActivity webViewActivity = (WebViewActivity) mActivity;
//                    webViewActivity.showSharedIcon(shardInfo);
//                }
            }
        });
    }

    @Override
    @JavascriptInterface
    public void hideSharedIcon() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (mActivity instanceof WebViewActivity) {
//                    WebViewActivity webViewActivity = (WebViewActivity) mActivity;
//                    webViewActivity.hideSharedIcon();
//                }
            }
        });
    }

    @Override
    @JavascriptInterface
    public void showHelpIcon(final String phoneNumber) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (mActivity instanceof WebViewActivity) {
//                    WebViewActivity webViewActivity = (WebViewActivity) mActivity;
//                    webViewActivity.showHelpIcon(phoneNumber);
//                }
            }
        });
    }

    @Override
    @JavascriptInterface
    public void toRechargeOneCard() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                RechargeOneCardActivity.openRechargeOneCardAct(mActivity);
            }
        });
    }

    //拨打电话
    @Override
    @JavascriptInterface
    public void dialPhone(final String phoneNumber) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Tool.showCallHelpDialog(mActivity, phoneNumber);
            }
        });
    }

    //跳登录
    @Override
    @JavascriptInterface
    public void toLogin() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity instanceof WebViewActivity) {
                    WebViewActivity webViewActivity = (WebViewActivity) mActivity;
                    webViewActivity.setShouldLoadUrl(true);
                }
//                UiUtils.toLoginActivity(mActivity, Tool.LOGIN_FINISH);
                UiUtils.toLoginActivity(mActivity);
            }
        });
    }

    @Override
    @JavascriptInterface
    public void toWechatPay(final String url) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WeChatWebPayActivity.openWebPayAct(mActivity, null, "", url);
            }
        });
    }

    @Override
    @JavascriptInterface
    public void toAppletPay(final String params) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                WXSdkManager.newInstance().useWXLaunchMiniProgram(mActivity, params);
            }
        });
    }

    @Override
    @JavascriptInterface
    public void toAccountDetails(final String checkPosition) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                AccountDetailListActivity.openAccountDetailAct(mActivity, checkPosition);
            }
        });
    }

    //跳转本地方法
    @Override
    @JavascriptInterface
    public void nativeActivity(final String urlInfo) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                LogUtils.d("urlInfo = " + urlInfo);
//                NaviActivityInfo.disPathIntentFromUrl(mActivity, urlInfo);
            }
        });
    }

    @Override
    @JavascriptInterface
    public void toAliPay(final String intentInfo) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (mActivity instanceof WebViewActivity) {
//                    LogUtils.e(intentInfo);
//                    WebViewActivity webViewActivity = (WebViewActivity) mActivity;
//                    webViewActivity.startAliPay(intentInfo);
//                }
            }
        });
    }

    @JavascriptInterface
    @Override
    public void changeToolBarState(String bgColor) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity instanceof WebViewActivity) {
                    WebViewActivity webViewActivity = (WebViewActivity) mActivity;
                    webViewActivity.changeToolBarState(false, bgColor);
                }
            }
        });
    }

    @JavascriptInterface
    @Override
    public void changeToolBarDefault() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mActivity instanceof WebViewActivity) {
                    WebViewActivity webViewActivity = (WebViewActivity) mActivity;
                    webViewActivity.changeToolBarState(true, "");
                }
            }
        });
    }

    /**
     * 权益卡
     * @param parameter
     */
    @Override
    @JavascriptInterface
    public void goWeChatBuyEquityCard(String parameter) {
//
//        Log.e("goWeChatBuyEquityCard",parameter);
//        Gson gson = new Gson();
//        OrderBean orderEntity=gson.fromJson(parameter, OrderBean.class);
//        Log.e("goWeChatBuyEquityCard","orderId="+orderEntity.getOrderId());
//        if(mActivity instanceof WebViewActivity){
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    WXSdkManager.newInstance().useWXLaunchMiniProgramToPay(mActivity,orderEntity.getOrderId());
//                }
//            });
//        }
    }

    @Override
    @JavascriptInterface
    public void saveImage(String data) {
        Log.e("saveImage","data==>"+data);
        ImageUtils.saveImage(mActivity,data);
        PermissionUtils.permissionGroup(
                PermissionConstants.STORAGE
                )
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        ImageUtils.saveImage(mActivity,data);
                    }

                    @Override
                    public void onDenied() {
                        Toast.makeText(mActivity,"权限被拒绝，无法保存",Toast.LENGTH_SHORT);
                    }
                })
                .request();

    }
    @Override
    @JavascriptInterface
    public void shareImageToWeChat(String data) {
        Log.e("shareImageToWeChat","data==>"+data);
        if(mActivity instanceof WebViewActivity){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    WXSdkManager.newInstance().shareWX(mActivity,data);
                }
            });

        }
    }
    @Override
    @JavascriptInterface
    public void showSearch(boolean isShow) {

//        if(mActivity instanceof WebViewActivity){
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((WebViewActivity) mActivity).showSearchView(isShow);
//                }
//            });
//
//        }
    }

    @Override
    @JavascriptInterface
    public void showToolbar(boolean isShow) {
//        if(mActivity instanceof WebViewActivity){
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((WebViewActivity) mActivity).showToolbarView(isShow);
//                }
//            });
//
//        }
    }
    @Override
    @JavascriptInterface
    public void showPerformanceCenterView(boolean isShow) {
//        if(mActivity instanceof WebViewActivity){
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((WebViewActivity) mActivity).showPerformanceCenterView(isShow);
//                }
//            });
//
//        }
    }

    @Override
    @JavascriptInterface
    public void goBack() {
        if(mActivity instanceof WebViewActivity){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((WebViewActivity) mActivity).onBackClick();
                }
            });

        }
    }

    @Override
    @JavascriptInterface
    public int getStatusBarHeight() {
        return StatusBarUtil.getStatusBarHeight(mActivity);
    }

    @Override
    @JavascriptInterface
    public void showImmersiveToolBar(boolean isShow) {
//        if(mActivity instanceof WebViewActivity){
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((WebViewActivity) mActivity).showImmersiveToolBar(isShow);
//                }
//            });
//
//        }
    }

    @Override
    @JavascriptInterface
    public void showWithdrawalDetailsView(boolean isShow) {
//        if(mActivity instanceof WebViewActivity){
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ((WebViewActivity) mActivity).showWithdrawalDetailsView(isShow);
//                }
//            });
//
//        }
    }

    @Override
    @JavascriptInterface
    public void changeToolBarState(boolean isDefault, String bgColor) {
        if(mActivity instanceof WebViewActivity){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((WebViewActivity) mActivity).changeToolBarState(isDefault,bgColor);
                }
            });

        }
    }

    @Override
    @JavascriptInterface
    public void goWeChat() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            mActivity.startActivity(intent);
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toasty.error(mActivity,"启动微信失败，请手动打开微信").show();
                }
            });
        }

    }

    @Override
    @JavascriptInterface
    public void finishActivity() {
        if(mActivity instanceof WebViewActivity){
           mActivity.finish();

        }
    }

    @JavascriptInterface
    public void addCalendar(String result) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (mActivity instanceof WebViewActivity) {
//                    WebViewActivity webViewActivity = (WebViewActivity) mActivity;
//                    webViewActivity.addToCalendar(result);
//                }
            }
        });
    }

    @JavascriptInterface
    public void cancelCalendar(String result) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (mActivity instanceof WebViewActivity) {
//                    WebViewActivity webViewActivity = (WebViewActivity) mActivity;
//                    webViewActivity.deleteFromCalendar(result);
//                }
            }
        });
    }

    public void onDestory() {
        mActivity = null;
        mListener = null;
    }


}
