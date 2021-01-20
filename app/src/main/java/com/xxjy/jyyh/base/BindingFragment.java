package com.xxjy.jyyh.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author power
 * @date 12/1/20 1:27 PM
 * @project RunElephant
 * @description:
 */
public abstract class BindingFragment<V extends ViewBinding, VM extends BaseViewModel> extends BaseFragment {
    protected V mBinding = null;
    protected VM mViewModel;
    protected Context mContext;
    private View contentView = null;

    //是否首次显示
    private boolean mIsFirstVisile = true;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //私有的初始化DataBinding和ViewModel方法
        initViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null){
            contentView = mBinding.getRoot();

        }
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        dataObservable();
    }

    /**
     * 初始化viewModel
     */
    private void initViewModel() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Class bindingClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            Method method = null;
            try {
                method = bindingClass.getMethod("inflate", LayoutInflater.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                mBinding = (V) method.invoke(null, getLayoutInflater());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if (mViewModel == null) {
            Class modelClass;
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            mViewModel = (VM) ViewModelProviders.of(this).get(modelClass);
        }
        //让ViewModel绑定View的生命周期
        getLifecycle().addObserver(mViewModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsFirstVisile) {
            mIsFirstVisile = false;
            onFirstVisible();
            onVisible();
        } else if (isVisible()){
            onNotFirstVisible();
            onVisible();
        } else {
            onInvisible();
        }
    }

    /**
     * 该界面首次可见
     */
    protected void onFirstVisible() {
    }

    /**
     * 该界面可见，单并非首次可见
     */
    protected void onNotFirstVisible() {
    }

    /**
     * 该界面可见
     */
    protected void onVisible() {
    }

    /**
     * 该界面不可见
     */
    protected void onInvisible() {
    }

    /**
     * 页面数据初始化方法
     */
    protected abstract void initView();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * @param view
     * 处理点击事件
     */
    protected abstract void onViewClicked(View view);

    /**
     * 处理网络请求回调
     */
    protected abstract void dataObservable();

    @Override
    public void onDestroyView() {
        mBinding = null;
        super.onDestroyView();
    }
}
