package com.xxjy.jyyh.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.utils.pay.PayListenerUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//			builder.show();
			int code = resp.errCode;
			String transaction = resp.transaction;
			String errStr = resp.errStr;
			if (code == 0) {
				//显示充值成功的页面和需要的操作
				//EventBus.getDefault().post("微信支付成功");//微信支付成功
//				Toast.makeText(aty, "充值成功了!", Toast.LENGTH_SHORT).show();
				PayListenerUtils.getInstance().addSuccess();
			}
			if (code == -1) {
				//错误    可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
//				Toast.makeText(aty, "充值失败!", Toast.LENGTH_SHORT).show();
				PayListenerUtils.getInstance().addFail();
			}

			if (code == -2) {
//				Toast.makeText(aty, "用户取消了!", Toast.LENGTH_SHORT).show();
				//用户取消
				PayListenerUtils.getInstance().addCancel();
			}
		}
	}
}