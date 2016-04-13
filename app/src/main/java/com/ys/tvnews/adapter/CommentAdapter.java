package com.ys.tvnews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ys.tvnews.R;
import com.ys.tvnews.bean.CommentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public class CommentAdapter extends BaseAdapter{

    private Context mContext;
    private List<CommentBean> list_comment;

    public void addAllList(List<CommentBean> list_comment){
        this.list_comment = list_comment;
        notifyDataSetChanged();
    }
    public CommentAdapter(Context context){
        this.mContext = context;
        this.list_comment = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return list_comment.size();
    }

    @Override
    public Object getItem(int position) {
        return list_comment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null){
           convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_item_layout,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.userName.setText(list_comment.get(position).getUserName());
        holder.comment_time.setText(list_comment.get(position).getComment_time());
        holder.comment.setText(list_comment.get(position).getComment());
        //holder.head_image.setIm
        holder.like.setText(list_comment.get(position).getLike());
        holder.unLike.setText(list_comment.get(position).getUnLike());
        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time = Integer.parseInt(list_comment.get(position).getLike());
                time++;
                holder.like.setText(time+ "");
            }
        });
        holder.iv_unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time = Integer.parseInt(list_comment.get(position).getUnLike());
                time++;
                holder.unLike.setText(time+"");
            }
        });

        return convertView;
    }

    class ViewHolder{
        private ImageView head_image,iv_like,iv_unlike;
        private TextView userName,comment_time,like,unLike,comment;

        public ViewHolder(View view){
            head_image = (ImageView) view.findViewById(R.id.head_image_comment);
            userName = (TextView) view.findViewById(R.id.tv_user_name);
            comment_time = (TextView) view.findViewById(R.id.comment_time);
            like = (TextView) view.findViewById(R.id.tv_like);
            unLike = (TextView) view.findViewById(R.id.tv_unlike);
            comment = (TextView) view.findViewById(R.id.text_comment);
            iv_like = (ImageView) view.findViewById(R.id.iv_like);
            iv_unlike = (ImageView) view.findViewById(R.id.iv_unlike);
        }
    }
}
