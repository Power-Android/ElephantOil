package com.xxjy.jyyh.http;

import android.os.Looper;


import com.xxjy.jyyh.app.App;
import com.xxjy.jyyh.utils.toastlib.MyToast;

import java.io.IOException;
import java.lang.reflect.Type;

import rxhttp.wrapper.annotation.Parser;
import rxhttp.wrapper.entity.ParameterizedTypeImpl;
import rxhttp.wrapper.exception.ParseException;
import rxhttp.wrapper.parse.AbstractParser;

/**
 * @author power
 * @date 12/1/20 3:26 PM
 * @project RunElephant
 * @description: 这个解析器返回带有code的data，如需判断特殊code请用此解析器
 */

@Parser(name = "CodeResponse")
public class ResponseCodeParser<T> extends AbstractParser<Response<T>> {
    protected ResponseCodeParser() {
        super();
    }

    public ResponseCodeParser(Type type) {
        super(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response<T> onParse(okhttp3.Response response) throws IOException {
        final Type type = ParameterizedTypeImpl.get(Response.class, mType); //获取泛型类型
        Response<T> data = convert(response, type);

        if (data.getCode() != 1) {
            MyToast.showWarning(App.getContext(), data != null && data.getMsg() != null ? data.getMsg() : "网络请求错误");
            //code不等于 1，说明数据不正确，抛出异常
            throw new ParseException(String.valueOf(data.getCode()), data.getMsg(), response);
        }
        return data;
    }
}
