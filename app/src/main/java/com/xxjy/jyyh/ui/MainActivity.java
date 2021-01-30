package com.xxjy.jyyh.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.qmuiteam.qmui.util.QMUIDeviceHelper;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.rxjava.rxlife.RxLife;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.ApiService;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityMainBinding;
import com.xxjy.jyyh.dialog.CheckVersionDialog;
import com.xxjy.jyyh.dialog.VersionUpDialog;
import com.xxjy.jyyh.entity.EventEntity;
import com.xxjy.jyyh.ui.home.HomeFragment;
import com.xxjy.jyyh.ui.integral.IntegralFragment;
import com.xxjy.jyyh.ui.mine.MineFragment;
import com.xxjy.jyyh.ui.oil.OilFragment;
import com.xxjy.jyyh.ui.setting.AboutUsViewModel;
import com.xxjy.jyyh.utils.Util;
import com.xxjy.jyyh.utils.symanager.ShanYanManager;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.RxHttp;

public class MainActivity extends BindingActivity<ActivityMainBinding, MainViewModel> {
    private int mLastFgIndex = -1;
    private long clickTime;
    private HomeFragment mHomeFragment;
    private OilFragment mOilFragment;
    private IntegralFragment mIntergralFragment;
    private MineFragment mMineFragment;
    private FragmentTransaction mTransaction;

    //极光intent
    public static final String TAG_FLAG_INTENT_VALUE_INFO = "intentInfo";
private AboutUsViewModel aboutUsViewModel;


    @BusUtils.Bus(tag = EventConstants.EVENT_CHANGE_FRAGMENT)
    public void onEvent(@NotNull EventEntity event) {
        mBinding.navView.setSelectedItemId(R.id.navigation_oil);
    }

    @Override
    protected void initView() {
        BusUtils.register(this);
        initNavigationView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //未登录的时候尝试检查闪验是否支持
                if (!UserConstants.getIsLogin()) {
                    ShanYanManager.checkShanYanSupportState();
                }
            }
        }, 3000);
        int state = getIntent().getIntExtra("jumpState", -1);
        if(state!=-1){
            showFragment(state);
            switch (state){
                case 0:
                    mBinding.navView.setSelectedItemId(R.id.navigation_home);
                    break;
                case 1:
                    mBinding.navView.setSelectedItemId(R.id.navigation_oil);
                    break;
                case 2:
                    mBinding.navView.setSelectedItemId(R.id.navigation_integral);
                    break;
                case 3:
                    mBinding.navView.setSelectedItemId(R.id.navigation_mine);
                    break;
            }
        }
        aboutUsViewModel = new ViewModelProvider(this).get(AboutUsViewModel.class);
        checkVersion();
    }

    private void initNavigationView() {
        mBinding.navView.setItemIconTintList(null);
        adjustNavigationIcoSize(mBinding.navView);
        mBinding.navView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    showFragment(Constants.TYPE_HOME);
                    break;
                case R.id.navigation_oil:
                    showFragment(Constants.TYPE_OIL);
                    break;
                case R.id.navigation_integral:
                    showFragment(Constants.TYPE_INTEGRAL);
                    break;
                case R.id.navigation_mine:
                    showFragment(Constants.TYPE_MINE);
                    break;
            }
            return true;
        });
        mBinding.navView.setSelectedItemId(R.id.navigation_home);
    }

    private void adjustNavigationIcoSize(BottomNavigationView navigation) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
//            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            layoutParams.height = QMUIDisplayHelper.dp2px(this, 18);
            layoutParams.width = QMUIDisplayHelper.dp2px(this, 18);
            iconView.setLayoutParams(layoutParams);
        }
    }

    public void showFragment(int index) {
        mTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(mTransaction);
        mLastFgIndex = index;
        switch (index) {
            case Constants.TYPE_HOME:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.getInstance();
                    mTransaction.add(R.id.fragment_group, mHomeFragment);
                }
                mTransaction.show(mHomeFragment);
                break;
            case Constants.TYPE_OIL:
                if (mOilFragment == null) {
                    mOilFragment = OilFragment.getInstance();
                    mTransaction.add(R.id.fragment_group, mOilFragment);
                }
                mTransaction.show(mOilFragment);
                break;
            case Constants.TYPE_INTEGRAL:
                if (mIntergralFragment == null) {
                    mIntergralFragment = IntegralFragment.getInstance();
                    mTransaction.add(R.id.fragment_group, mIntergralFragment);
                }
                mTransaction.show(mIntergralFragment);
                break;
            case Constants.TYPE_MINE:
                if (mMineFragment == null) {
                    mMineFragment = MineFragment.getInstance();
                    mTransaction.add(R.id.fragment_group, mMineFragment);
                }
                mTransaction.show(mMineFragment);
                break;
        }
        mTransaction.commit();

    }

    private void hideFragment(FragmentTransaction transaction) {
        switch (mLastFgIndex) {
            case Constants.TYPE_HOME:
                if (mHomeFragment != null) {
                    transaction.hide(mHomeFragment);
                }
            case Constants.TYPE_OIL:
                if (mOilFragment != null) {
                    transaction.hide(mOilFragment);
                }
                break;
            case Constants.TYPE_INTEGRAL:
                if (mIntergralFragment != null) {
                    transaction.hide(mIntergralFragment);
                }
                break;
            case Constants.TYPE_MINE:
                if (mMineFragment != null) {
                    transaction.hide(mMineFragment);
                }
                break;
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt(Constants.CURRENT_FRAGMENT_KEY, mCurrentFgIndex);
    }

    /**
     * 处理回退事件
     */
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - clickTime) > Constants.DOUBLE_INTERVAL_TIME) {
            ToastUtils.showShort(getString(R.string.double_click_exit_toast));
            clickTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusUtils.unregister(this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {
        aboutUsViewModel.checkVersionLiveData.observe(this, data -> {
            int compare = Util.compareVersion(data.getLastVersion(), Util.getVersionName());
            if (compare == 1) {
                //是否强制更新，0：否，1：是
                VersionUpDialog checkVersionDialog = new VersionUpDialog(this,data);
                checkVersionDialog.show();
            }

        });
    }

    /**
     * 打开首页并清空栈
     *
     * @param activity
     */
    public static void openMainActAndClearTask(BaseActivity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
//        ActivityUtils.startHomeActivity();
    }

    private void checkVersion() {
        aboutUsViewModel.checkVersion();
    }
}