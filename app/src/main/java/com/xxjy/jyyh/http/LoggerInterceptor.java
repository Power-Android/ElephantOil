package com.xxjy.jyyh.http;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.rxjava.BuildConfig;
import com.xxjy.jyyh.constants.Constants;

import okhttp3.Interceptor;
import okhttp3.internal.platform.Platform;

/**
 * @author power
 * @date 12/1/20 3:18 PM
 * @project RunElephant
 * @description:
 */
public class LoggerInterceptor {
    /**
     * 得到一个 Interceptor 类型的 logger 拦截器,用来进行 打印拦截
     *
     * @return
     */
    public static Interceptor getLoggingIntercaptor() {
        LoggingInterceptor.Builder loggingInterceptor = new LoggingInterceptor.Builder();
        loggingInterceptor
                .loggable(Constants.IS_DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .addHeader("version", BuildConfig.VERSION_NAME);
        return loggingInterceptor.build();
    }
}
