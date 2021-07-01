package com.xxjy.jyyh.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author power
 * @date 12/1/20 1:17 PM
 * @project RunElephant
 * @description:
 */
public class BaseRepository {
    //管理RxJava，主要针对RxJava异步操作造成的内存泄漏
    private CompositeDisposable mCompositeDisposable;
    private final MutableLiveData<Boolean> loadingLiveData;


    public BaseRepository() {
        loadingLiveData = new MutableLiveData<>();
    }

    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * viewmodel销毁时清除Rxjava
     */
    public void unDisposable() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }

    public void showLoading(boolean isShow) {
         loadingLiveData.postValue(isShow);
    }

    public LiveData<Boolean> isShowLoading() {
        return loadingLiveData;
    }

}
