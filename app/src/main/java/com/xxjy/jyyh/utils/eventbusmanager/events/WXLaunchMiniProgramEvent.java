package com.xxjy.jyyh.utils.eventbusmanager.events;

import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2020/5/26.
 */
public class WXLaunchMiniProgramEvent {

    private WXLaunchMiniProgram.Resp resp;

    public WXLaunchMiniProgram.Resp getResp() {
        return resp;
    }

    public void setResp(WXLaunchMiniProgram.Resp resp) {
        this.resp = resp;
    }
}
