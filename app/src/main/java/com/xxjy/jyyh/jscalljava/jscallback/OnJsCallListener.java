package com.xxjy.jyyh.jscalljava.jscallback;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/12/12.
 */

public interface OnJsCallListener {
    public final int CALL_TYPE_SHARE = 1;

    void onJsCallListener(int callType, String content);
}
