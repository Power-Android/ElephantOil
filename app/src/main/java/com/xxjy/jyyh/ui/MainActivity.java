package com.xxjy.jyyh.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.BusUtils;
import com.blankj.utilcode.util.SPUtils;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BaseActivity;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.BannerPositionConstants;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.constants.EventConstants;
import com.xxjy.jyyh.constants.SPConstants;
import com.xxjy.jyyh.constants.UserConstants;
import com.xxjy.jyyh.databinding.ActivityMainBinding;
import com.xxjy.jyyh.dialog.HomeNewUserDialog;
import com.xxjy.jyyh.dialog.NoticeTipsDialog;
import com.xxjy.jyyh.dialog.VersionUpDialog;
import com.xxjy.jyyh.entity.EventEntity;
import com.xxjy.jyyh.dialog.HomeAdDialog;
import com.xxjy.jyyh.ui.car.CarServeFragment;
import com.xxjy.jyyh.ui.home.HomeFragment;
import com.xxjy.jyyh.ui.integral.BannerViewModel;
import com.xxjy.jyyh.ui.integral.IntegralFragment;
import com.xxjy.jyyh.ui.mine.MineFragment;
import com.xxjy.jyyh.ui.oil.OilFragment;
import com.xxjy.jyyh.ui.setting.AboutUsViewModel;
import com.xxjy.jyyh.utils.AppManager;
import com.xxjy.jyyh.utils.JPushManager;
import com.xxjy.jyyh.utils.NaviActivityInfo;
import com.xxjy.jyyh.utils.NotificationsUtils;
import com.xxjy.jyyh.utils.Util;
import com.xxjy.jyyh.utils.eventtrackingmanager.TrackingConstant;
import com.xxjy.jyyh.utils.pay.PayListenerUtils;
import com.xxjy.jyyh.utils.shumeimanager.SmAntiFraudManager;
import com.xxjy.jyyh.utils.symanager.ShanYanManager;
import com.xxjy.jyyh.utils.umengmanager.UMengManager;
import com.xxjy.jyyh.utils.umengmanager.UMengOnEvent;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends BindingActivity<ActivityMainBinding, MainViewModel> {
    private int mLastFgIndex = -1;
    private long clickTime;
    private HomeFragment mHomeFragment;
    private OilFragment mOilFragment;
    private CarServeFragment mCarServeFragment;
    private IntegralFragment mIntergralFragment;
    private MineFragment mMineFragment;
    private FragmentTransaction mTransaction;
    private int isNewUser = -1;//新用户，未满3单跳油站列表，老用户满3单跳首页
    private static String appChannel = "";  //标记app的渠道

    //极光intent
    public static final String TAG_FLAG_INTENT_VALUE_INFO = "intentInfo";
    private AboutUsViewModel aboutUsViewModel;
    private BannerViewModel bannerViewModel;


    @BusUtils.Bus(tag = EventConstants.EVENT_CHANGE_FRAGMENT, sticky = true)
    public void onEvent(@NotNull EventEntity event) {
        if (TextUtils.equals(event.getEvent(), EventConstants.EVENT_CHANGE_FRAGMENT)) {
            TrackingConstant.OIL_MAIN_TYPE = "1";
            mBinding.navView.setSelectedItemId(R.id.navigation_oil);
        } else if (TextUtils.equals(event.getEvent(), EventConstants.EVENT_TO_INTEGRAL_FRAGMENT)) {
            mBinding.navView.setSelectedItemId(R.id.navigation_integral);
        }else if (TextUtils.equals(event.getEvent(), EventConstants.EVENT_TO_OIL_FRAGMENT)){
            mBinding.navView.setSelectedItemId(R.id.navigation_oil);
        }else if (TextUtils.equals(event.getEvent(), EventConstants.EVENT_TO_CAR_FRAGMENT)){
            mBinding.navView.setSelectedItemId(R.id.navigation_car_serve);
        }
    }

    private void initSdk() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appChannel =  UserConstants.getAppChannel();
                if (TextUtils.isEmpty(appChannel)) {
                    appChannel = AppManager.getAppMetaChannel();
                    UserConstants.setAppChannel(appChannel);
                }

                //初始化闪验sdk
                ShanYanManager.initShanYnaSdk(MainActivity.this);
                //标记首次进入app埋点
                UMengOnEvent.onFirstStartEvent();

                //极光推送配置
                JPushManager.initJPush(MainActivity.this);
                //友盟统计
                UMengManager.init(MainActivity.this);
                //数美风控
                SmAntiFraudManager.initSdk(MainActivity.this);
            }
        }).start();
    }

    @Override
    protected void initView() {
        initSdk();
        BusUtils.register(this);
        mHomeFragment = null;
        mOilFragment = null;
        mCarServeFragment = null;
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

        initNavigationView();
        new Handler().postDelayed(() -> {
            if (!UserConstants.getIsLogin()) {
                ShanYanManager.checkShanYanSupportState();
            }
        }, 3000);

        int state = getIntent().getIntExtra("jumpState", -1);
        showDiffFragment(state);

        aboutUsViewModel = new ViewModelProvider(this).get(AboutUsViewModel.class);
        bannerViewModel = new ViewModelProvider(this).get(BannerViewModel.class);
        if(state==-1){
            checkVersion();
            //新老用户展示tab判断
            mViewModel.getIsNewUser().observe(this, aBoolean -> {
                if (aBoolean) {
                    isNewUser = 1;
                } else {
                    if (UserConstants.getGoneIntegral()) {
                        isNewUser = 1;
                    } else {
                        isNewUser = 0;
                    }
                }
                if (TextUtils.isEmpty(Constants.HUNTER_GAS_ID)) {//猎人码绑定油站
                    showDiffFragment(isNewUser);
                }

            });
        }


        if (NotificationsUtils.isNotificationEnabled(this)) {
            UserConstants.setNotificationRemindUserCenter(false);
        } else {
            UserConstants.setNotificationRemindUserCenter(true);
        }
//        newUserStatus();
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
                    mBinding.navView.setSelectedItemId(R.id.navigation_car_serve);
                    break;
                case 3:
                    mBinding.navView.setSelectedItemId(R.id.navigation_integral);
                    break;
                case 4:
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
                case R.id.navigation_car_serve:
                    showFragment(Constants.TYPE_CAR_SERVE);
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
            case Constants.TYPE_CAR_SERVE:
                if (mCarServeFragment == null) {
                    mCarServeFragment = CarServeFragment.getInstance();
                    mTransaction.add(R.id.fragment_group, mCarServeFragment);
                }
                mTransaction.show(mCarServeFragment);
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
                break;
            case Constants.TYPE_OIL:
                if (mOilFragment != null) {
                    transaction.hide(mOilFragment);
                }
                break;
            case Constants.TYPE_CAR_SERVE:
                if (mCarServeFragment != null) {
                    transaction.hide(mCarServeFragment);
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
                checkVersionDialog.setOnDismissListener(dialog -> newUserStatus());
                checkVersionDialog.show();
            } else {
                newUserStatus();
            }

        });
    }

    private void getAdData() {
        bannerViewModel.getBannerOfPostion(BannerPositionConstants.APP_OPEN_AD).observe(this, data -> {
            if (data != null && data.size() > 0) {
                HomeAdDialog homeAdDialog = new HomeAdDialog(this, data.get(0));
                homeAdDialog.show(mBinding.getRoot());
                homeAdDialog.setOnItemClickedListener(view -> showNotification());
            } else {
                showNotification();
            }
        });
    }
    //新人礼包
    private void newUserStatus() {
        if(UserConstants.getIsLogin()){
            mViewModel.newUserStatus().observe(this, data -> {
                if (data != null && data.getStatus()==1) {
                    HomeNewUserDialog homeNewUserDialog = HomeNewUserDialog.getInstance();
                    homeNewUserDialog.setData(this,data);
                    homeNewUserDialog.show(mBinding.getRoot());
                    homeNewUserDialog.setOnItemClickedListener(view -> getAdData());
                } else {
                    getAdData();
                }
            });
        }else{
            getAdData();
        }

    }

    private void showNotification() {
        if (!UserConstants.getNotificationRemindVersion()) {
            if (!UserConstants.getNotificationRemind()) {
                if (!NotificationsUtils.isNotificationEnabled(this)) {
                    NoticeTipsDialog noticeTipsDialog = new NoticeTipsDialog(this);
                    noticeTipsDialog.show(mBinding.fragmentGroup);
                    noticeTipsDialog.setOnItemClickedListener(new NoticeTipsDialog.OnItemClickedListener() {
                        @Override
                        public void onQueryClick() {
                            NotificationsUtils.requestNotify(MainActivity.this);
                        }

                        @Override
                        public void onNoOpen() {
                            UserConstants.setNotificationRemindVersion(true);
                        }
                    });
                    UserConstants.setNotificationRemind(true);
                }
            }
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        disPathIntentMessage(intent);
        jump(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(UserConstants.getIsLogin()){
            mViewModel.newUserStatus().observe(this, data -> {
                if (data != null && data.getStatus()==1) {
//                    HomeNewUserDialog homeNewUserDialog = new HomeNewUserDialog(this, data);
//                    homeNewUserDialog.show(mBinding.getRoot());
//                    homeNewUserDialog.setOnItemClickedListener(view -> getAdData());
                    HomeNewUserDialog homeNewUserDialog = HomeNewUserDialog.getInstance();
                    homeNewUserDialog.setData(this,data);
                    homeNewUserDialog.show(mBinding.getRoot());
                    homeNewUserDialog.setOnItemClickedListener(view -> getAdData());
                }
            });
        }
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

    /**
     * 打开首页并清空栈
     *
     * @param activity
     */
    public static void openMainActAndClearTaskJump(BaseActivity activity, int jumpCode) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("jumpState", jumpCode);
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
        int startFrom = intent.getIntExtra("startFrom", 0);
        UserConstants.setStartFrom(String.valueOf(startFrom));

        String intentInfo = intent.getStringExtra(TAG_FLAG_INTENT_VALUE_INFO);
        if (TextUtils.isEmpty(intentInfo)) return;
        NaviActivityInfo.disPathIntentFromUrl(this, intentInfo);
        intent.removeExtra(TAG_FLAG_INTENT_VALUE_INFO);
    }

    private void checkVersion() {
        aboutUsViewModel.checkVersion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */

        if (data == null) {
            return;
        }
        String str = data.getExtras().getString("pay_result");
        if (!TextUtils.isEmpty(str)) {
            if (str.equalsIgnoreCase("success")) {
                //如果想对结果数据校验确认，直接去商户后台查询交易结果，
                //校验支付结果需要用到的参数有sign、data、mode(测试或生产)，sign和data可以在result_data获取到
                /**
                 * result_data参数说明：
                 * sign —— 签名后做Base64的数据
                 * data —— 用于签名的原始数据
                 *      data中原始数据结构：
                 *      pay_result —— 支付结果success，fail，cancel
                 *      tn —— 订单号
                 */
//            msg = "云闪付支付成功";
                PayListenerUtils.getInstance().addSuccess();
            } else if (str.equalsIgnoreCase("fail")) {
//            msg = "云闪付支付失败！";
                PayListenerUtils.getInstance().addFail();
            } else if (str.equalsIgnoreCase("cancel")) {
//            msg = "用户取消了云闪付支付";
                PayListenerUtils.getInstance().addCancel();

            }
        }
    }
}