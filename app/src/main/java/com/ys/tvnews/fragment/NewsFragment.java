package com.ys.tvnews.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ys.tvnews.R;
import com.ys.tvnews.activity.PersonalActivity;
import com.ys.tvnews.adapter.IndexFragmentPagerAdapter;

/**
 * Created by sks on 2015/11/23.
 */
public class NewsFragment extends Fragment implements View.OnClickListener{


    private TabLayout tabLayout;  //标题栏
    private ViewPager index_viewPager;  //新闻ViewPager
    private IndexFragmentPagerAdapter mAdapter;  //新闻模块
    private String [] mTitles;
    private ImageView image_people;

//    private PullToRefreshListView news_listView;
//    private View view;
//    private View HeaderView;
//    private String data_type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.frag_news,container,false);
        findView(view);
        loadData();
        regListener();
        index_viewPager.setAdapter(mAdapter);
        initTabLayout();
        return view;

    }

    private void initTabLayout(){
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.setSelectedTabIndicatorHeight(2);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabTextColors(Color.parseColor("#F8F8FF"), Color.parseColor("#C71585"));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(index_viewPager);
        tabLayout.setTabsFromPagerAdapter(mAdapter);
    }


    protected void findView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.index_tabLayout);
        index_viewPager = (ViewPager) view.findViewById(R.id.index_viewPager);
        image_people = (ImageView) view.findViewById(R.id.image_people);
    }

    protected void regListener() {
        image_people.setOnClickListener(this);
    }

    protected void loadData() {
        mTitles = new String[]{"要闻","时政","军事","体育","财经","社会","图解新闻","话题投票"};
        /**
         * fragment嵌套里面不能用再用getActivity().getFragmentManager()
           要用getChildFragmentManager()
         */
        mAdapter = new IndexFragmentPagerAdapter(getChildFragmentManager(),mTitles);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_people:
                Intent intent = new Intent(getActivity(),PersonalActivity.class);
                startActivity(intent);
        }
    }
}
