package com.ys.tvnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.ys.tvnews.R;
import com.ys.tvnews.application.MyApplication;
import com.ys.tvnews.bean.TimeNewsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class TimeNewsAdapter extends BaseAdapter{

    private List<TimeNewsBean> list_time_news;
    private Context context;
    private BitmapUtils bitmapUtils;

    public TimeNewsAdapter(Context context){
        list_time_news = new ArrayList<>();
        this.context = context;
        bitmapUtils = new BitmapUtils(context);
    }

    public void setList_time_news( List<TimeNewsBean> list_time_news){
        this.list_time_news = list_time_news;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list_time_news.size();
    }

    @Override
    public Object getItem(int position) {
        return list_time_news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.time_list_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_time.setText(list_time_news.get(position).getCreateTime().substring(0,8));
        holder.tv_title.setText(list_time_news.get(position).getDescription());
        bitmapUtils.display(holder.image2,list_time_news.get(position).getListImageUrl());
        return convertView;
    }

    class ViewHolder{
        private TextView tv_title,tv_time;
        private ImageView image1,image2;

        public ViewHolder(View view){
            tv_title = (TextView) view.findViewById(R.id.title);
            tv_time = (TextView) view.findViewById(R.id.show_time);
            image1 = (ImageView) view.findViewById(R.id.image);
            image2 = (ImageView) view.findViewById(R.id.image_1);
        }
    }
}
