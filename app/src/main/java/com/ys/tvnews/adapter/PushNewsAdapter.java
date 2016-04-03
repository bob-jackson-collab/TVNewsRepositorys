package com.ys.tvnews.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ys.tvnews.R;
import com.ys.tvnews.bean.PushNews;
import com.ys.tvnews.views.FlabbyLayout;

import java.util.List;
import java.util.Random;

/**
 * Created by sks on 2016/1/27.
 */

public class PushNewsAdapter extends BaseAdapter{
    private Context mContext;
    private List<PushNews> list_push_news;
    private Random mRandomizer = new Random();

    public PushNewsAdapter(Context context, List<PushNews> list_push_news) {
        this.mContext = context;
        this.list_push_news = list_push_news;
    }

    @Override
    public int getCount() {
        return list_push_news.size();
    }

    @Override
    public Object getItem(int position) {
        return list_push_news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.push_news_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int color = Color.argb(255, mRandomizer.nextInt(256), mRandomizer.nextInt(256), mRandomizer.nextInt(256));
        ((FlabbyLayout)convertView).setFlabbyColor(color);
        holder.textView.setText(list_push_news.get(position).getMessage());
        return convertView;
    }

     class ViewHolder {
       private TextView textView;

        public ViewHolder(View view){
            textView = (TextView) view.findViewById(R.id.push_news_text);
        }
    }
}
