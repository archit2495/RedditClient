package com.example.architg.redditclientarchit.Adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.architg.redditclientarchit.Fragments.FeedFragment;

import java.util.List;

/**
 * Created by archit.g on 29/08/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    List<String> mFragmentType;
    List<String> mFragmentTitles;
    String mSubreddit = "";
    public ViewPagerAdapter(FragmentManager fm,List<String> fragmentType,List<String> fragmentTitles){
        super(fm);
        mFragmentType = fragmentType;
        mFragmentTitles = fragmentTitles;
    }

    @Override
    public FeedFragment getItem(int position) {
        String type = mFragmentType.get(position);
        FeedFragment feedFragment = FeedFragment.getInstance(type,mSubreddit);
        return feedFragment;
    }
    @Override
    public int getCount() {
        return mFragmentType.size();
    }
    @Override
    public CharSequence getPageTitle(int position){
        return mFragmentTitles.get(position);
    }
    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }

}
