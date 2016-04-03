package com.ys.tvnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

/**
 * ListView适配器
 * Created by sks on 2015/12/14.
 */
public class NewsAdapter extends BaseAdapter{

    private List<NewsBean> list_newsBean = new ArrayList<>();
    private Context mContext;
    private BitmapUtils bitmapUtils;

    public NewsAdapter(Context mContext,BitmapUtils bitmapUtils){
        this.mContext = mContext;
        this.bitmapUtils = bitmapUtils;
    }

    public NewsAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void addList(List<NewsBean> list_newsBean){
        this.list_newsBean.addAll(list_newsBean);
        notifyDataSetChanged();
    }

    public void setList(List<NewsBean> list_newsBean){
        this.list_newsBean = list_newsBean;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list_newsBean.size();
    }

    @Override
    public Object getItem(int position) {
        return list_newsBean.get(position);
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

        holder.tv_news.setText(list_newsBean.get(position).getItemTitle());
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setLoadFailedDrawable(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
       // Log.e("imageUrl", list_newsBean.get(position).getImgUrl1() + "----");
        bitmapUtils.display(holder.image_news,list_newsBean.get(position).getImgUrl1(),config);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("news",list_newsBean.get(position));
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
