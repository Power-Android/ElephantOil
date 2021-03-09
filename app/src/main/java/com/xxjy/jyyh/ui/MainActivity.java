package com.xxjy.jyyh.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.BusUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.BannerPositionConstants;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityMainBinding;
import com.xxjy.jyyh.dialog.NoticeTipsDialog;
import com.xxjy.jyyh.dialog.VersionUpDialog;
import com.xxjy.jyyh.entity.EventEntity;
import com.xxjy.jyyh.ui.broadcast.HomeAdDialog;
import com.xxjy.jyyh.ui.home.HomeFragment;
import com.xxjy.jyyh.ui.integral.BannerViewModel;
import com.xxjy.jyyh.ui.integral.IntegralFragment;
import com.xxjy.jyyh.ui.mine.MineFragment;
import com.xxjy.jyyh.ui.oil.OilFragment;
import com.xxjy.jyyh.ui.setting.AboutUsViewModel;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.NotificationsUtils;
import com.xxjy.jyyh.utils.Util;
import com.xxjy.jyyh.utils.symanager.ShanYanManager;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends BindingActivity<ActivityMainBinding, MainViewModel> {
    private int mLastFgIndex = -1;
    private long clickTime;
    private HomeFragment mHomeFragment;
    private OilFragment mOilFragment;
    private IntegralFragment mIntergralFragment;
    private MineFragment mMineFragment;
    private FragmentTransaction mTransaction;
    private int isNewUser = -1;//新用户，未满3单跳油站列表，老用户满3单跳首页

    //极光intent
    public static final String TAG_FLAG_INTENT_VALUE_INFO = "intentInfo";
    private AboutUsViewModel aboutUsViewModel;
    private BannerViewModel bannerViewModel;


    @BusUtils.Bus(tag = EventConstants.EVENT_CHANGE_FRAGMENT)
    public void onEvent(@NotNull EventEntity event) {
        if (TextUtils.equals(event.getEvent(), EventConstants.EVENT_CHANGE_FRAGMENT)) {
            mBinding.navView.setSelectedItemId(R.id.navigation_oil);
        } else if (TextUtils.equals(event.getEvent(), EventConstants.EVENT_TO_INTEGRAL_FRAGMENT)) {
            mBinding.navView.setSelectedItemId(R.id.navigation_integral);
        }
    }

    @Override
    protected void initView() {
        BusUtils.register(this);

        mHomeFragment = null;
        mOilFragment = null;
        mIntergralFragment = null;
        mMineFragment = null;

        disPathIntentMessage(getIntent());
        //积分权益隐藏判断
        mViewModel.getOsOverAll().observe(this, b -> {
            if (!b) {
                UserConstants.setGoneIntegral(true);
                mBinding.navView.getMenu().removeItem(R.id.navigation_home);
                mBinding.navView.getMenu().removeItem(R.id.navigation_integral);
            } else {
                UserConstants.setGoneIntegral(false);
            }
        });
        //新老用户展示tab判断
        mViewModel.getIsNewUser().observe(this, aBoolean -> {
            if (aBoolean) {
                isNewUser = 1;
            } else {
                isNewUser = 0;
            }
            showDiffFragment(isNewUser);
        });
        initNavigationView();
        new Handler().postDelayed(() -> {
            if (!UserConstants.getIsLogin()) {
                ShanYanManager.checkShanYanSupportState();
            }
        }, 2000);

        int state = getIntent().getIntExtra("jumpState", -1);
        showDiffFragment(state);

        aboutUsViewModel = new ViewModelProvider(this).get(AboutUsViewModel.class);
        bannerViewModel = new ViewModelProvider(this).get(BannerViewModel.class);
        checkVersion();

        if(NotificationsUtils.isNotificationEnabled(this)){
            UserConstants.setNotificationRemindUserCenter(false);
        }else{
            UserConstants.setNotificationRemindUserCenter(true);
        }
    }

    private void showDiffFragment(int position) {
        if (position != -1) {
            showFragment(position);
            switch (position) {
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
    }

    private void initNavigationView() {
        mBinding.navView.setItemIconTintList(null);
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
            showToastInfo(getString(R.string.double_click_exit_toast));
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
                VersionUpDialog checkVersionDialog = new VersionUpDialog(this, data);
                checkVersionDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getAdData();
                    }
                });
                checkVersionDialog.show();
            } else {
                getAdData();
            }

        });
    }

    private void getAdData() {
        bannerViewModel.getBannerOfPostion(BannerPositionConstants.APP_OPEN_AD).observe(this, data -> {
            if (data != null && data.size() > 0) {
                HomeAdDialog homeAdDialog = new HomeAdDialog(this, data.get(0));
                homeAdDialog.show(mBinding.getRoot());
                homeAdDialog.setOnItemClickedListener(new HomeAdDialog.OnItemClickedListener() {
                    @Override
                    public void onCloseClick(View view) {
                        showNotification();
                    }
                });
            } else {
                showNotification();
            }
        });
    }

    private void showNotification() {
        if (!UserConstants.getNotificationRemind()) {

            if (!NotificationsUtils.isNotificationEnabled(this)) {
                NoticeTipsDialog noticeTipsDialog = new NoticeTipsDialog(this);
                noticeTipsDialog.show(mBinding.fragmentGroup);
                noticeTipsDialog.setOnItemClickedListener(new NoticeTipsDialog.OnItemClickedListener() {
                    @Override
                    public void onQueryClick() {
                        NotificationsUtils.requestNotify(MainActivity.this);
                    }
                });
                UserConstants.setNotificationRemind(true);
            }
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        disPathIntentMessage(intent);
        jump(intent);
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

    private void jump(Intent intent) {
        if (intent == null) {
            intent = getIntent();
        }
        int state = intent.getIntExtra("jumpState", -1);
        showDiffFragment(state);
    }

    private void disPathIntentMessage(Intent intent) {
        if (intent == null) {
            intent = getIntent();
        }
        String intentInfo = intent.getStringExtra(TAG_FLAG_INTENT_VALUE_INFO);
        if (TextUtils.isEmpty(intentInfo)) return;
        NaviActivityInfo.disPathIntentFromUrl(this, intentInfo);
        intent.removeExtra(TAG_FLAG_INTENT_VALUE_INFO);
    }

    private void checkVersion() {
        aboutUsViewModel.checkVersion();
    }

}