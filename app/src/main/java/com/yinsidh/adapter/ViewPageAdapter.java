package com.yinsidh.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by User on 2016/12/1.
 */

public class ViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public ViewPageAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;

    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
