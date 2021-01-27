package com.xxjy.jyyh.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.NetworkUtils;
import com.xxjy.jyyh.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author power
 * @date 12/1/20 1:15 PM
 * @project RunElephant
 * @description:
 */
public abstract class BindingActivity<V extends ViewBinding, VM extends BaseViewModel> extends
        BaseActivity implements NetworkUtils.OnNetworkStatusChangedListener {
    protected V mBinding = null;
    protected VM mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (NetworkUtils.isConnected()) {
            initViewModel();
            initView();
            initListener();
            //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
            dataObservable();
        } else {
            initNoNetWorkView();
        }
        checkNetwork();
    }

    /**
     * 检测网络
     */
    private void checkNetwork() {
        NetworkUtils.registerNetworkStatusChangedListener(this);
    }

    @Override
    public void onDisconnected() {
        initNoNetWorkView();
    }

    @Override
    public void onConnected(NetworkUtils.NetworkType networkType) {
        switch (networkType) {
            case NETWORK_NO:
                initNoNetWorkView();
                break;
            default:
                initViewModel();
                initView();
                initListener();
                //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
                dataObservable();
                break;

        }
    }

    private void initNoNetWorkView() {
        View view = LayoutInflater.from(BindingActivity.this).inflate(R.layout.no_network_layout, null);
        setContentView(view);
        TextView refreshView = findViewById(R.id.refresh_view);
        refreshView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnected()) {
                    view.setVisibility(View.GONE);
                    initViewModel();
                    initView();
                    initListener();
                    //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
                    dataObservable();
                }
            }
        });
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
            setContentView(mBinding.getRoot());
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

    /**
     * 页面数据初始化方法
     */
    protected abstract void initView();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * @param view 处理点击事件
     */
    protected abstract void onViewClicked(View view);

    /**
     * 处理网络请求回调
     */
    protected abstract void dataObservable();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
        NetworkUtils.unregisterNetworkStatusChangedListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        NetworkUtils.registerNetworkStatusChangedListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetworkUtils.unregisterNetworkStatusChangedListener(this);
    }
}
