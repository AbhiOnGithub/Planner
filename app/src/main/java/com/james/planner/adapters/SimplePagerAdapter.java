package com.james.planner.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.james.planner.fragments.SimpleFragment;

public class SimplePagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private SimpleFragment[] fragments;

    public SimplePagerAdapter(Context context, FragmentManager fm, SimpleFragment... fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public SimpleFragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments[position].getTitle(context);
    }
}
