package com.xxjy.jyyh.wight;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * create dd
 * date 2019-11-19
 * email dd@163.com
 * @author sunxiaokun
 */
public class SettingLayout extends AppCompatTextView implements IPagerTitleView {

    private int mSelectedColor;
    private int mNormalColor;

    public SettingLayout(Context context) {
        this(context, null);
    }

    public SettingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setGravity(17);
    }

    public void setmSelectedColor(int mSelectedColor) {
        this.mSelectedColor = mSelectedColor;
    }

    public void setmNormalColor(int mNormalColor) {
        this.mNormalColor = mNormalColor;
    }

    @Override
    public void onSelected(int index, int totalCount) {
        this.setTextColor(mSelectedColor);
        setTextSize(20);
        this.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        this.setTextColor(mNormalColor);
        setTextSize(17);
        this.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

    }
}
