package com.ys.tvnews.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by sks on 2015/11/23.
 */
public class TitlePagerAdapter extends PagerAdapter{

    private String [] mTitles;
    private Context mContext;

    public TitlePagerAdapter(String mTitles[],Context mContext){
        this.mTitles = mTitles;
        this.mContext = mContext;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        TextView tv_title = new TextView(mContext);
        tv_title.setText(mTitles[position]);
        container.addView(tv_title);
        return tv_title;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
