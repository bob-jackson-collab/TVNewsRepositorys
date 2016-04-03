package com.ys.tvnews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by sks on 2015/12/22.
 */
public class GuideAdapter extends PagerAdapter{

    private List<ImageView> list_guide_imageView;

    public GuideAdapter(List<ImageView> list_guide_imageView){
        this.list_guide_imageView = list_guide_imageView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list_guide_imageView.get(position));
        return list_guide_imageView.get(position);
    }

    @Override
    public int getCount() {
        return list_guide_imageView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list_guide_imageView.get(position));
    }
}
