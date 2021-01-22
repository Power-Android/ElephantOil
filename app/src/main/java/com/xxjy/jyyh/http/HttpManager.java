package com.xxjy.jyyh.http;

import android.app.Application;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.xxjy.jyyh.constants.UserConstants;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import rxhttp.RxHttp;
import rxhttp.RxHttpPlugins;
import rxhttp.wrapper.cahce.CacheMode;
import rxhttp.wrapper.cookie.CookieStore;
import rxhttp.wrapper.param.Method;
import rxhttp.wrapper.ssl.HttpsUtils;

/**
 * @author power
 * @date 12/1/20 2:18 PM
 * @project RunElephant
 * @description:
 */
public class HttpManager {
    public static void init(Application context){
        File file = new File(context.getExternalCacheDir(), "RxHttpCookie");
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieStore(file))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //添加信任证书
                .hostnameVerifier((hostname, session) -> true) //忽略host验证
                .addInterceptor(LoggerInterceptor.getLoggingIntercaptor())
//                .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
//                .addInterceptor(new RedirectInterceptor())
//                  .addInterceptor(new TokenInterceptor())
                .build();

        //RxHttp初始化，自定义OkHttpClient对象，非必须
        RxHttp.init(client);

        RxHttp.setDebug(true);

        //设置缓存策略，非必须
        File cacheFile = new File(context.getExternalCacheDir(), "RxHttpCache");
        RxHttpPlugins.setCache(cacheFile, 1000 * 100, CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        RxHttpPlugins.setExcludeCacheKeys("time"); //设置一些key，不参与cacheKey的组拼

        //设置数据解密/解码器，非必须
//        RxHttp.setResultDecoder(s -> s);

        //设置全局的转换器，非必须
//        RxHttp.setConverter(FastJsonConverter.create());

        //设置公共参数，非必须
        RxHttp.setOnParamAssembly(p -> {
            /*根据不同请求添加不同参数，子线程执行，每次发送请求前都会被回调
            如果希望部分请求不回调这里，发请求前调用Param.setAssemblyEnabled(false)即可
             */
            Method method = p.getMethod();
            if (method.isGet()) { //Get请求

            } else if (method.isPost()) { //Post请求

            }

            String token = UserConstants.getToken();
            if (!TextUtils.isEmpty(token)) {
                //添加公共请求头
                p.addHeader("Authorization", "Bearer " + token);
            }
            //添加公共参数
            p.addAll(getCommonParams());
            return p;
        });
    }

    /**
     * 生成最终的请求体的参数,主要用来添加默认的 参数
     *
     * @return 最终参数
     */
    public static Map<String, String> getCommonParams() {
        Map<String, String> finalParams = new HashMap<>();
        String t = System.currentTimeMillis() + "";     //时间戳
        String did = UserConstants.getUuid();              //设备标识
        // XIAOMI MI5 8.0
        String osv = DeviceUtils.getManufacturer() + " " + DeviceUtils.getModel() + " " +
                DeviceUtils.getSDKVersionName();        //操作系统版本号
        String cv = AppUtils.getAppVersionName();       //客户端版本号
        String location = "";                           //位置信息
        try {
//            location = UserConstants.getLocation();
        } catch (Exception e) {
            location = "";
        }
        String cityCode = "";
        String adCode = "";
        try {
//            cityCode = UserConstants.getCityCode();
//            adCode = UserConstants.getAdCode();
        } catch (Exception e){

        }

        String app_store = UserConstants.getAppChannel();

        finalParams.put("t", t);//时间戳
        finalParams.put("did", did);
        finalParams.put("os", "1");//操作系统
        finalParams.put("osv", osv);//手机系统版本
        finalParams.put("cv", cv);//客户端版本号
        finalParams.put("location", location);  //位置信息
        finalParams.put("cityCode", cityCode);//城市编码
        finalParams.put("region", cityCode);//banner用的城市编码
        finalParams.put("adCode", adCode);//区域编码
//        finalParams.put("appStore", app_store); //下载渠道
        return finalParams;
    }

}
