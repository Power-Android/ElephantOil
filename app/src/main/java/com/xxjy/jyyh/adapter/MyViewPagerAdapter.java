package com.xxjy.jyyh.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * @author power
 * @date 2019/5/14 2:19 PM
 * @project BuildTalk
 * @description:
 */
public class MyViewPagerAdapter extends PagerAdapter {
    private String[] mTitles ;
    private List<View> mViews;

    public MyViewPagerAdapter(String[] titles, List<View> mViews) {
        this.mTitles = titles;
        this.mViews = mViews;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = mViews.get(position);
//        if (v.getParent() == null)
//        container.addView(v);

        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
