package com.xxjy.jyyh.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.entity.RespEntity;
import com.xxjy.jyyh.utils.eventbusmanager.EventBusManager;
import com.xxjy.jyyh.utils.eventbusmanager.events.WXLaunchMiniProgramEvent;

public class WXEntryActivity extends WXCallbackActivity  {

//    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;


//    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
//
//        try {
//            api.handleIntent(getIntent(), this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        setIntent(intent);
//        api.handleIntent(intent, this);
//    }

    @Override
    public void onReq(BaseReq req) {
        super.onReq(req);
//        switch (req.getType()) {
//            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                goToGetMsg();
//                break;
//            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) req);
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;

//        Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_SHORT).show();

//        switch (resp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                result = R.string.errcode_success;
//                if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM  && resp instanceof WXLaunchMiniProgram.Resp) {
//                    WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
//                    String extraData = launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
////                    Toast.makeText(this, extraData, Toast.LENGTH_LONG).show();
//                    RespEntity respEntity = GsonUtils.fromJson(extraData, RespEntity.class);
////                    Intent intent = new Intent(this, PaymentSuccessfulActivity.class);
////                    intent.putExtra("orderId", respEntity.getOrderId());
////                    startActivity(intent);
//
//                }
//
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                result = R.string.errcode_cancel;
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                result = R.string.errcode_deny;
//                break;
//            case BaseResp.ErrCode.ERR_UNSUPPORT:
//                result = R.string.errcode_unsupported;
//                break;
//            default:
//                result = R.string.errcode_unknown;
//                break;
//        }

//		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM
                && resp instanceof WXLaunchMiniProgram.Resp) {
            try {
                WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
                String extraData = launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
                LogUtils.w("extraData == " + extraData);
                WXLaunchMiniProgramEvent event = new WXLaunchMiniProgramEvent();
                event.setResp(launchMiniProResp);
                EventBusManager.post(event);
            } catch (Exception e) {
                LogUtils.e("ERROR : " + e.getMessage());
            }
            finish();
            return;
        }
        super.onResp(resp);

    }

    private void goToGetMsg() {
//		Intent intent = new Intent(this, GetFromWXActivity.class);
//		intent.putExtras(getIntent());
//		startActivity(intent);
//		finish();
    }

    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

        StringBuffer msg = new StringBuffer();
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("extInfo: ");
        msg.append(obj.extInfo);
        msg.append("\n");
        msg.append("filePath: ");
        msg.append(obj.filePath);

//		Intent intent = new Intent(this, ShowFromWXActivity.class);
//		intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
//		intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
//		intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
//		startActivity(intent);
//		finish();
    }
}