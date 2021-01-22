package com.xxjy.jyyh.utils.umengmanager;

import android.app.Activity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xxjy.jyyh.base.BaseActivity;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2020/4/10.
 */
public class UMengLoginWx {

    public static void loginFormWx(BaseActivity activity, UMAuthAdapter umAuthAdapter) {
        UMShareAPI.get(activity).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, umAuthAdapter);
    }

    public static void deleteOauthWx() {

    }

    public static abstract class UMAuthAdapter implements UMAuthListener {

        @Override
        public void onStart(SHARE_MEDIA share_media) {
            LogUtils.w("onStart oauth");
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Activity topActivity = ActivityUtils.getTopActivity();
            if (topActivity != null && topActivity instanceof BaseActivity) {
                BaseActivity activity = (BaseActivity) topActivity;
                activity.showToastError("出现错误");
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            LogUtils.w("用户取消授权");
        }
    }
}
