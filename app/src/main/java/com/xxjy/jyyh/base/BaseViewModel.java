package com.xxjy.jyyh.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.OnLifecycleEvent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author power
 * @date 12/1/20 1:17 PM
 * @project RunElephant
 * @description:
 */
public class BaseViewModel<M extends BaseRepository> extends AndroidViewModel implements LifecycleObserver {
    protected M mRespository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mRespository = createModel();
    }

    private M createModel() {
        if (mRespository == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                //如果没有指定泛型参数，则默认使用BaseModel
                modelClass = BaseRepository.class;
            }
            try {
                mRespository = (M) modelClass.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return mRespository;
    }

    public M getRepository() {
        return mRespository;
    }

    public LiveData<Boolean> loadingView(){
        return mRespository.isShowLoading();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //ViewModel销毁时会执行，同时取消所有异步任务
        mRespository.unDisposable();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onAny(LifecycleOwner owner, Lifecycle.Event event) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {

    }
}
