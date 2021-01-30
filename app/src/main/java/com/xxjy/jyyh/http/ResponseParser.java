package com.xxjy.jyyh.http;


import android.app.Activity;

import com.blankj.utilcode.util.ActivityUtils;
import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.utils.LoginHelper;
import com.xxjy.jyyh.utils.UiUtils;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.io.IOException;
import java.lang.reflect.Type;

import rxhttp.wrapper.annotation.Parser;
import rxhttp.wrapper.entity.ParameterizedTypeImpl;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.parse.AbstractParser;

/**
 * Response<T> 数据解析器,解析完成对Response对象做判断,如果ok,返回数据 T
 * User: ljx
 * Date: 2018/10/23
 * Time: 13:49
 */
@Parser(name = "Response")
public class ResponseParser<T> extends AbstractParser<T> {

    protected ResponseParser() {
        super();
    }

    public ResponseParser(Type type) {
        super(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T onParse(okhttp3.Response response) throws IOException {
        final Type type = ParameterizedTypeImpl.get(Response.class, mType); //获取泛型类型
        Response<T> data = convert(response, type);
        T t = data.getData(); //获取data字段
        if (t == null) {
            /*
             * 考虑到有些时候服务端会返回：{"errorCode":0,"errorMsg":"关注成功"}  类似没有data的数据
             * 此时code正确，但是data字段为空，直接返回data的话，会报空指针错误，
             * 所以，判断泛型为String类型时，重新赋值，并确保赋值不为null
             */
            if (data.getCode() == 1){
                data.setMsg("暂无数据");
            }
            t = (T) data.getMsg();
        }

        if (data.getCode() != 1 ) {
            if (data.getCode() == 1007){
                LoginHelper.loginOut(null);
                UiUtils.toLoginActivity(App.getContext());
            }
            MyToast.showWarning(App.getContext(), data != null && data.getMsg() != null ? data.getMsg() : "网络请求错误");
            //code不等于 1，说明数据不正确，抛出异常
            throw new ParseException(String.valueOf(data.getCode()), data.getMsg(), response);
        }
        return t;
    }
}
