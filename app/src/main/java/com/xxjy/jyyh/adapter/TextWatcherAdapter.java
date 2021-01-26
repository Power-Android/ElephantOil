package com.xxjy.jyyh.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 车主邦
 * ---------------------------
 * <p>
 * Created by zhaozh on 2018/4/25.
 *
 * TextWatcher的适配器, 对所有方法进行了空实现
 */

public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
