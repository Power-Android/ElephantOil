package com.xxjy.jyyh.base;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import androidx.viewbinding.ViewBinding;

import com.xxjy.jyyh.R;

/**
 * Created by Administrator on 2016/5/5.
 */
public abstract class BaseDialog extends Dialog {

    public enum AnimationDirection {
        HORIZONTAL, VERTICLE, GROW
    }

    public View view;
    /**
     * 创建dialog 的方法, 省略了一些参数,只需要
     *
     * @param context           上下文
     * @param gravity           对齐方式
     * @param backCancelable    返回键是否可以取消
     * @param outsideCancelable 外部点击是否可以取消
     */
    protected BaseDialog(Context context, int gravity, boolean backCancelable, boolean outsideCancelable) {
        this(context, gravity, 0.0f, AnimationDirection.GROW, backCancelable, outsideCancelable);
    }

    /**
     * 创建dialog的方法
     *
     * @param context            上下文
     * @param gravity            对齐方式
     * @param marginVerticle     底部的距离,一般为 0
     * @param animationDirection 动画效果 {@link AnimationDirection}
     * @param backCancelable     返回键是否可以取消
     * @param outsideCancelable  外部点击是否可以取消
     */
    protected BaseDialog(Context context, int gravity, float marginVerticle,
                         AnimationDirection animationDirection, boolean backCancelable,
                         boolean outsideCancelable) {
        super(context, R.style.BaseDialog);
        init(gravity, marginVerticle, animationDirection, backCancelable, outsideCancelable);
       view=  LayoutInflater.from(context).inflate( getContentLayoutId(), null, false);
      setContentView(view);
    }

    private void init(int gravity, float marginVerticle, AnimationDirection animationDirection, boolean backCancelable, boolean outsideCancelable) {
        this.setCancelable(backCancelable);
        this.setCanceledOnTouchOutside(outsideCancelable);
        Window dialogWindow = this.getWindow();
        if (animationDirection == AnimationDirection.VERTICLE) {
            dialogWindow.setWindowAnimations(R.style.DialogVerticleWindowAnim);
        } else if (animationDirection == AnimationDirection.HORIZONTAL) {
            dialogWindow.setWindowAnimations(R.style.DialogRightHorizontalWindowAnim);
        } else if (animationDirection == AnimationDirection.GROW) {
            dialogWindow.setWindowAnimations(R.style.DialogGrowWindowAnim);
        }
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        layoutParams.verticalMargin = marginVerticle;
        initLayout(layoutParams);
        dialogWindow.setAttributes(layoutParams);
    }

    /**
     * 设置宽 ,高 ,位置, 距离底部的方法, 可以由子类进行重写
     *
     * @param layoutParams
     */
    protected void initLayout(WindowManager.LayoutParams layoutParams) {
        //todo default is :layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //todo default is :layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //todo can use :layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //todo can use :layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    public void dismiss() {
        releaseData();
        super.dismiss();
    }

    /**
     * 在弹窗即将关闭的时候释放数据操作,由子类进行操作
     */
    protected void releaseData() {
    }

    //设置 resoucelayoutid
    protected abstract int getContentLayoutId();
}
