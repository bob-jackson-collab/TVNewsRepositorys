package com.ys.tvnews.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.ys.tvnews.R;
import com.ys.tvnews.adapter.PushNewsAdapter;
import com.ys.tvnews.application.MyApplication;
import com.ys.tvnews.bean.PushNews;
import com.ys.tvnews.views.FlabbyListView;
import com.ys.tvnews.views.TitleBuilder;
import com.ys.tvnews.views.TitleViews;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sks on 2016/1/26.
 */
public class PushNewsActivity extends Activity {

    private FlabbyListView flabbyListView;
    private List<PushNews> list_push_news;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PushNewsAdapter mAdapter;
    private TitleBuilder titleBuilder;
    private PushNews pushNews ;
    private DbUtils mDbUtils;    //数据库工具类
    private View empty_view;


    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1) {
                swipeRefreshLayout.setRefreshing(false);
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_news);
        initView();
        initData();
        initSwipeRefreshLayout();

        pushNews = (PushNews) getIntent().getSerializableExtra("pushNews");
        if(pushNews!=null) {
            Log.e("info", pushNews.getMessage());

            //list_items.add(pushNews.getMessage());
            mAdapter.notifyDataSetChanged();
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Message msg = Message.obtain();
                            msg.what = 1;
                            handler.sendMessage(msg);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

    }

    private void initView(){
        empty_view = LayoutInflater.from(PushNewsActivity.this).inflate(R.layout.empty_view,null);
        titleBuilder = new TitleBuilder(PushNewsActivity.this).setLeftImageRes(R.mipmap.base_header_back).setMiddleTitleText("推送消息").setRightText("删除");

        flabbyListView = (FlabbyListView) findViewById(R.id.flabby_listView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

    }

    private void initSwipeRefreshLayout(){
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED);
    }

    private void initData(){

        mDbUtils = MyApplication.getDbInstance();
        list_push_news = new ArrayList<>();

        try {
            list_push_news = mDbUtils.findAll(PushNews.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        if(list_push_news!=null){
            mAdapter = new PushNewsAdapter(PushNewsActivity.this,list_push_news);
            flabbyListView.setAdapter(mAdapter);
        }else{
            //设置listView展示空数据
            flabbyListView.setEmptyView(empty_view);
        }
    }
}
