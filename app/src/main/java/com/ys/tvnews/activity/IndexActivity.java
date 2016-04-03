package com.ys.tvnews.activity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.RadioGroup;

import com.ys.tvnews.R;
import com.ys.tvnews.fragment.ListenTVFragment;
import com.ys.tvnews.fragment.NewsFragment;
import com.ys.tvnews.fragment.TVFragment;
import com.ys.tvnews.fragment.TimeFragment;

/**
 * Created by sks on 2015/12/8.
 */
public class IndexActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup radioGroup;
    private FragmentManager manager;
    private NewsFragment newsFragment;      //新闻模块
    private TimeFragment timeFragment;      //时间模块
    private TVFragment tvFragment;          //电视模块
    private ListenTVFragment listenTVFragment;  //听电视模块

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
        setOnClickListener();
        loadData();
    }

    private void initView(){
        radioGroup = (RadioGroup) findViewById(R.id.index_radioGroup);
    }

    private void setOnClickListener(){
        radioGroup.setOnCheckedChangeListener(this);
    }

    private void loadData(){
        newsFragment = new NewsFragment();
        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.linear_index,newsFragment).commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radio_news:
                if(newsFragment!=null){
                    manager.beginTransaction().hide(newsFragment).commit();
                }
                if(timeFragment!=null){
                    manager.beginTransaction().hide(timeFragment).commit();
                }
                if(tvFragment!=null){
                    manager.beginTransaction().hide(tvFragment).commit();
                }
                if(listenTVFragment!=null){
                    manager.beginTransaction().hide(listenTVFragment).commit();
                }
                manager.beginTransaction().show(newsFragment).commit();
                break;
            case R.id.radio_time:
                if(timeFragment==null){
                    timeFragment = new TimeFragment();
                    manager.beginTransaction().add(R.id.linear_index,timeFragment).commit();
                }
                if(newsFragment!=null){
                    manager.beginTransaction().hide(newsFragment).commit();
                }
                if(timeFragment!=null){
                    manager.beginTransaction().hide(timeFragment).commit();
                }
                if(tvFragment!=null){
                    manager.beginTransaction().hide(tvFragment).commit();
                }
                if(listenTVFragment!=null){
                    manager.beginTransaction().hide(listenTVFragment).commit();
                }
                manager.beginTransaction().show(timeFragment).commit();
                break;
            case R.id.radio_tv:
                if(tvFragment==null){
                    tvFragment = new TVFragment();
                    manager.beginTransaction().add(R.id.linear_index,tvFragment).commit();
                }
                if(newsFragment!=null){
                    manager.beginTransaction().hide(newsFragment).commit();
                }
                if(timeFragment!=null){
                    manager.beginTransaction().hide(timeFragment).commit();
                }
                if(tvFragment!=null){
                    manager.beginTransaction().hide(tvFragment).commit();
                }
                if(listenTVFragment!=null){
                    manager.beginTransaction().hide(listenTVFragment).commit();
                }
                manager.beginTransaction().show(tvFragment).commit();
                break;
            case R.id.radio_listen_tv:
                if(listenTVFragment==null){
                    listenTVFragment = new ListenTVFragment();
                    manager.beginTransaction().add(R.id.linear_index,listenTVFragment).commit();
                }
                if(newsFragment!=null){
                    manager.beginTransaction().hide(newsFragment).commit();
        }
                if(timeFragment!=null){
                    manager.beginTransaction().hide(timeFragment).commit();
                }
                if(tvFragment!=null){
                    manager.beginTransaction().hide(tvFragment).commit();
                }
                if(listenTVFragment!=null){
                    manager.beginTransaction().hide(listenTVFragment).commit();
                }
                manager.beginTransaction().show(listenTVFragment).commit();
                break;
            default:
                break;
        }
    }
}
