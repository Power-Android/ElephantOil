package com.xxjy.jyyh.utils.pay;

import java.util.ArrayList;

public class PayListenerUtils {
    private static PayListenerUtils instance;

    private final static ArrayList<IPayListener> resultList = new ArrayList<>();
    private PayListenerUtils() {
    }

    public synchronized static PayListenerUtils getInstance() {
        if (instance == null) {
            instance = new PayListenerUtils();
        }
        return instance;
    }

    public void addListener(IPayListener listener) {
        if (!resultList.contains(listener)) {
            resultList.add(listener);
        }
    }

    public void removeListener(IPayListener listener) {
        resultList.remove(listener);
    }

    public void addSuccess() {
        for (IPayListener listener : resultList) {
            listener.onSuccess();
        }
    }

    public void addCancel() {
        for (IPayListener listener : resultList) {
            listener.onCancel();
        }
    }

    public void addFail() {
        for (IPayListener listener : resultList) {
            listener.onFail();
        }
    }
}
