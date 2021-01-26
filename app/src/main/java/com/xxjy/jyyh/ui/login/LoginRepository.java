package com.xxjy.jyyh.ui.login;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.xxjy.jyyh.base.BaseRepository;
import com.xxjy.jyyh.constants.ApiService;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.RxHttp;

/**
 * @author power
 * @date 1/21/21 11:40 AM
 * @project ElephantOil
 * @description:
 */
public class LoginRepository extends BaseRepository {

    public void verifyLogin(String twinklyToken, String jpushId, String inviteCode,
                            MutableLiveData<String> verifyLoginLiveData) {
        addDisposable(RxHttp.postForm(ApiService.VERIFY_LOGIN)
                .add("twinklyToken", twinklyToken)
                .add("jpushId", jpushId)
                .add("inviteCode", inviteCode, !TextUtils.isEmpty(inviteCode))
                .asResponse(String.class)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {

                    }
                })
        );
    }
}
