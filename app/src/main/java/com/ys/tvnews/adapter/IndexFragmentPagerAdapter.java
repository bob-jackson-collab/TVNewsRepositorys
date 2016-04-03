package com.ys.tvnews.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ys.tvnews.fragment.NewsFragment;
import com.ys.tvnews.fragment.NewsIndexFragment;

/**
 * Created by sks on 2015/11/23.
 */
public class IndexFragmentPagerAdapter extends FragmentPagerAdapter {

    private String mTitles[];

    public IndexFragmentPagerAdapter(FragmentManager fm,String mTitles[]) {
        super(fm);
        this.mTitles = mTitles;
    }

    @Override
    public Fragment getItem(int position) {
        NewsIndexFragment news_indexFragment = new NewsIndexFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",position);
        //设置传递的参数
        news_indexFragment.setArguments(bundle);
       // bundle.putString("context",mContext);
        return news_indexFragment;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
