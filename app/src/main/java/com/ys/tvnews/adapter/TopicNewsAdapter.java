package com.ys.tvnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.ys.tvnews.R;
import com.ys.tvnews.activity.NewsDetailActivity;
import com.ys.tvnews.bean.NewsBean;
import com.ys.tvnews.bean.TopicNews;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView适配器
 * Created by sks on 2015/12/14.
 */
public class TopicNewsAdapter extends BaseAdapter{

    private List<TopicNews> list_topic_news = new ArrayList<>();
    private Context mContext;
    private BitmapUtils bitmapUtils;

    public TopicNewsAdapter(Context mContext, BitmapUtils bitmapUtils){
        this.mContext = mContext;
        this.bitmapUtils = bitmapUtils;
    }

    public TopicNewsAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void addList(List<TopicNews> list_topic_news){
        this.list_topic_news.addAll(list_topic_news);
        notifyDataSetChanged();
    }

    public void setList(List<TopicNews> list_topic_news){
        this.list_topic_news = list_topic_news;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list_topic_news.size();
    }

    @Override
    public Object getItem(int position) {
        return list_topic_news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.news_list_item,parent,false);
            holder.image_news = (ImageView) convertView.findViewById(R.id.image_news);
            holder.tv_news = (TextView) convertView.findViewById(R.id.tv_news);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_news.setText(list_topic_news.get(position).getVotetitle());
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setLoadFailedDrawable(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
       // Log.e("imageUrl", list_newsBean.get(position).getImgUrl1() + "----");
        bitmapUtils.display(holder.image_news,list_topic_news.get(position).getVoteimage(),config);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("topicnews",list_topic_news.get(position));
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        private ImageView image_news;
        private TextView tv_news;
    }
}
