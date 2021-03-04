package com.xxjy.jyyh.utils.pay;

public interface IPayListener {
    void onSuccess();
    void onFail();
    void onCancel();
}
