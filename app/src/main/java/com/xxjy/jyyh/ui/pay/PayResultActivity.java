package com.xxjy.jyyh.ui.pay;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.databinding.ActivityPayResultBinding;
import com.xxjy.jyyh.utils.toastlib.Toasty;

public class PayResultActivity extends BindingActivity<ActivityPayResultBinding,PayResultViewModel> {


    @Override
    protected void initView() {
        mBinding.topLayout.setTitle("支付结果");
        mBinding.topLayout.addLeftImageButton(R.drawable.arrow_back_black,
                R.id.qmui_topbar_item_left_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.goHomeView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mBinding.goHomeView.getPaint().setAntiAlias(true);

        mBinding.copyView.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void initListener() {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.copy_view:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "这里是要复制的文字");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toasty.success(this,"复制成功").show();
                break;
        }

    }

    @Override
    protected void dataObservable() {

    }
}