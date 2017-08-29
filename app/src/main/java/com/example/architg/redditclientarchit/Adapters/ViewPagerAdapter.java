package com.example.architg.redditclientarchit.Adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by archit.g on 29/08/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    public void addFragment(android.support.v4.app.Fragment fragment,String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position){
        return mFragmentTitleList.get(position);
    }
}
