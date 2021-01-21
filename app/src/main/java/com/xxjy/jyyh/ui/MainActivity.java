package com.xxjy.jyyh.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.xxjy.jyyh.R;
import com.xxjy.jyyh.base.BindingActivity;
import com.xxjy.jyyh.constants.Constants;
import com.xxjy.jyyh.databinding.ActivityMainBinding;
import com.xxjy.jyyh.ui.home.HomeFragment;

public class MainActivity extends BindingActivity<ActivityMainBinding, MainViewModel> {
    private int mLastFgIndex = -1;
    private long clickTime;
    private HomeFragment mCounterFragment;
    private FragmentTransaction mTransaction;

    @Override
    protected void initView() {
        initNavigationView();
    }

    private void initNavigationView() {
        mBinding.navView.setItemIconTintList(null);
        adjustNavigationIcoSize(mBinding.navView);
        mBinding.navView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    showFragment(Constants.TYPE_COUNTER);
                    break;
                case R.id.navigation_order:
                    showFragment(Constants.TYPE_ORDER);
                    break;
                case R.id.navigation_oil:
                    showFragment(Constants.TYPE_OIL);
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
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }

    private void showFragment(int index) {
        mTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(mTransaction);
        mLastFgIndex = index;
//        switch (index) {
//            case Constants.TYPE_COUNTER:
//                if (mCounterFragment == null) {
//                    mCounterFragment = CounterFragment.getInstance();
//                    mTransaction.add(R.id.fragment_group, mCounterFragment);
//                }
//                mTransaction.show(mCounterFragment);
//                break;
//            case Constants.TYPE_ORDER:
//                if (mOrderFragment == null) {
//                    mOrderFragment = OrderFragment.getInstance();
//                    mTransaction.add(R.id.fragment_group, mOrderFragment);
//                }
//                mTransaction.show(mOrderFragment);
//                break;
//            case Constants.TYPE_OIL:
//                if (mOilFragment == null) {
//                    mOilFragment = OilFragment.getInstance();
//                    mTransaction.add(R.id.fragment_group, mOilFragment);
//                }
//                mTransaction.show(mOilFragment);
//                break;
//            case Constants.TYPE_MINE:
//                if (mMineFragment == null) {
//                    mMineFragment = MineFragment.getInstance();
//                    mTransaction.add(R.id.fragment_group, mMineFragment);
//                }
//                mTransaction.show(mMineFragment);
//                break;
//        }
        mTransaction.commit();

    }

    private void hideFragment(FragmentTransaction transaction) {
//        switch (mLastFgIndex) {
//            case Constants.TYPE_COUNTER:
//                if (mCounterFragment != null) {
//                    transaction.hide(mCounterFragment);
//                }
//            case Constants.TYPE_ORDER:
//                if (mOrderFragment != null) {
//                    transaction.hide(mOrderFragment);
//                }
//                break;
//            case Constants.TYPE_OIL:
//                if (mOilFragment != null) {
//                    transaction.hide(mOilFragment);
//                }
//                break;
//            case Constants.TYPE_MINE:
//                if (mMineFragment != null) {
//                    transaction.hide(mMineFragment);
//                }
//                break;
//        }
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
    protected void initListener() {

    }

    @Override
    protected void onViewClicked(View view) {

    }

    @Override
    protected void dataObservable() {

    }
}