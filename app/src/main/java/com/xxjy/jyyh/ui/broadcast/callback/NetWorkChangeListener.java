package com.xxjy.jyyh.ui.broadcast.callback;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/9/5.
 */

public interface NetWorkChangeListener {
    void useWifi();

    void useGprs();

    void noNetWork();
}
