package com.ys.tvnews.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sks on 2015/12/10.
 */
public class AdvPagerAdapter extends PagerAdapter{

    private List<ImageView> list_adv_img = new ArrayList<>();

    public AdvPagerAdapter(List<ImageView> list_adv_img){
        this.list_adv_img = list_adv_img;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position %= list_adv_img.size();
        if (position<0){
            position = list_adv_img.size()+position;
        }
        ImageView view = list_adv_img.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp =view.getParent();
        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       // container.removeView(list_adv_img.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
