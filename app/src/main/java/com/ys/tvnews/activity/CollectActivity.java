package com.ys.tvnews.activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.ys.tvnews.R;
import com.ys.tvnews.adapter.CollectAdapter;
import com.ys.tvnews.adapter.NewsAdapter;
import com.ys.tvnews.bean.CollectBean;
import com.ys.tvnews.bean.NewsBean;
import com.ys.tvnews.sqlite.OperateDB;
import com.ys.tvnews.views.CollectListView;
import com.ys.tvnews.views.SwipeLayoutList;
import com.ys.tvnews.views.TitleBuilder;
import com.ys.tvnews.views.TitleViews;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sks on 2015/12/31.
 */
public class CollectActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private SwipeLayoutList collect_listView;
    private List<CollectBean> collect_list_bean;
    private CollectAdapter mAdapter;
    private OperateDB operateDB;
    private TitleBuilder titleBuilder;

    @Override
    protected int loadLayout() {
        return R.layout.activity_collect;
    }

    @Override
    protected void findView() {
        collect_listView = (SwipeLayoutList) findViewById(R.id.swipe_list);
        titleBuilder = new TitleBuilder(CollectActivity.this).setTitleBackageGroundColor(Color.parseColor("#00B7CE")).setMiddleTitleText("我的收藏");
        titleBuilder.setLeftImageRes(R.mipmap.base_header_back);

        titleBuilder.setLeftTextOrImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectActivity.this.finish();
            }
        });
    }

    @Override
    protected void regListener() {
        collect_listView.setOnItemClickListener(this);
    }

    @Override
    protected void loadData() {
        operateDB = new OperateDB(CollectActivity.this);
        collect_list_bean =  new ArrayList<>();

/*        *//**
         * 模拟数据
         *//*
        collect_list_bean.add(new CollectBean("","","1111"));
        collect_list_bean.add(new CollectBean("","","2222"));
        collect_list_bean.add(new CollectBean("","","3333"));
        collect_list_bean.add(new CollectBean("","","4444"));*/

        mAdapter = new CollectAdapter(operateDB,CollectActivity.this,collect_listView,collect_list_bean);


        String sql = "select * from collect";
        //查询收藏表中所有的数据
         List<Map<String, Object>> news_data = operateDB.selectData(sql, null);
        for(int i =0;i<news_data.size();i++){
            CollectBean news = new CollectBean();
            Map<String,Object> hashMap = news_data.get(i);
            news.setItemTitle((String) hashMap.get("itemTitle"));
            news.setDetailUrl((String) hashMap.get("detailUrl"));
            news.setImageUrl1((String) hashMap.get("imgUrl1"));
            collect_list_bean.add(news);
        }
        collect_listView.setAdapter(mAdapter);
        mAdapter.setList_collect(collect_list_bean);

        //当没有收藏的记录时
        View empty_view = View.inflate(mContext,R.layout.empty_view,null);
        collect_listView.setEmptyView(empty_view);


    }

    @Override
    protected void reqServer() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(CollectActivity.this,NewsDetailActivity.class);
        intent.putExtra("collectBean",collect_list_bean.get(position));
        mContext.startActivity(intent);
    }
}
