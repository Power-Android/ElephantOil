package com.xxjy.jyyh.ui.setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.BarUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.databinding.ActivityAboutUsBinding;
import com.xxjy.jyyh.dialog.CheckVersionDialog;
import com.xxjy.jyyh.dialog.CustomerServiceDialog;
import com.xxjy.jyyh.ui.web.WebViewActivity;
import com.xxjy.jyyh.utils.Util;

public class AboutUsActivity extends BindingActivity<ActivityAboutUsBinding, AboutUsViewModel> {


    private CustomerServiceDialog customerServiceDialog;

    @Override
    protected void initView() {
//        mBinding.topLayout.setTitle("关于我们");
//        mBinding.topLayout.addLeftImageButton(R.drawable.arrow_back_black,
//                R.id.qmui_topbar_item_left_back).setOnClickListener(v -> finish());
        mBinding.titleLayout.tvTitle.setText("关于我们");
        mBinding.titleLayout.tbToolbar.setNavigationOnClickListener(v -> finish());
        BarUtils.addMarginTopEqualStatusBarHeight(mBinding.titleLayout.tbToolbar);
    }

    @Override
    protected void initListener() {
        mBinding.privacyPolicyLayout.setOnClickListener(this::onViewClicked);
        mBinding.userAgreementLayout.setOnClickListener(this::onViewClicked);
        mBinding.customerServiceLayout.setOnClickListener(this::onViewClicked);
        mBinding.checkVersionLayout.setOnClickListener(this::onViewClicked);
    }

    @Override
    protected void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_version_layout:
                checkVersion();
                break;
            case R.id.user_agreement_layout:
                WebViewActivity.openWebActivity(AboutUsActivity.this, Constants.USER_XIE_YI);
                break;
            case R.id.privacy_policy_layout:
                WebViewActivity.openWebActivity(AboutUsActivity.this, Constants.YINSI_ZHENG_CE);
                break;
            case R.id.customer_service_layout:
                if (customerServiceDialog == null) {
                    customerServiceDialog = new CustomerServiceDialog(this);
                }
                customerServiceDialog.show(view);
                break;
        }

    }

    @Override
    protected void dataObservable() {
        mViewModel.checkVersionLiveData.observe(this, data -> {
            int compare = Util.compareVersion(data.getLastVersion(), Util.getVersionName());
            if (compare == 1) {
                //是否强制更新，0：否，1：是
                CheckVersionDialog checkVersionDialog = new CheckVersionDialog(this,data);
                checkVersionDialog.show();
            }

        });
    }

    private void checkVersion() {
        mViewModel.checkVersion();
    }
}