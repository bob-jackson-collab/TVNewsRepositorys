package com.ys.tvnews.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.ys.tvnews.R;
import com.ys.tvnews.bean.CollectBean;
import com.ys.tvnews.sqlite.OperateDB;
import com.ys.tvnews.views.SwipeLayoutList;
import com.ys.tvnews.views.SwipeMenuLayout;

import java.util.List;

/**
 * Created by sks on 2016/1/26.
 */
public class CollectAdapter extends BaseAdapter implements SwipeMenuLayout.OnSlidingListener{

    private List<CollectBean> list_collect;
    private SwipeMenuLayout swipeMenuLayout;
    private Context mContext;
    private SwipeLayoutList swipe_list;
    private BitmapUtils bitmapUtils;
    private OperateDB odb;

    public CollectAdapter(OperateDB odb,Context mContext,SwipeLayoutList swipe_list,List<CollectBean> list_collect){
        this.mContext = mContext;
        this.swipe_list = swipe_list;
        this.list_collect = list_collect;
        bitmapUtils = new BitmapUtils(mContext);
        this.odb = odb;
    }

    public void setList_collect(List<CollectBean> list_collect){
        this.list_collect = list_collect;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(list_collect!=null) {
            return list_collect.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list_collect.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         swipeMenuLayout = (SwipeMenuLayout) convertView;
         ViewHolder holder = null;
         if(swipeMenuLayout==null){
             swipeMenuLayout = (SwipeMenuLayout) LayoutInflater.from(mContext).inflate(R.layout.collect_item,parent,false);
             holder = new ViewHolder(swipeMenuLayout);
             swipeMenuLayout.setTag(swipeMenuLayout);
             swipeMenuLayout.setOnSlidingListener(this);
         }else{
             holder = (ViewHolder) swipeMenuLayout.getTag();
         }
        bitmapUtils.display(holder.mIvImg,list_collect.get(position).getImageUrl1());
       // holder.mIvImg.setBackgroundResource(R.mipmap.ic_launcher);
        holder.mContent.setText(list_collect.get(position).getItemTitle());
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "删除 position ---> " + position + "数据", Toast.LENGTH_SHORT).show();
                shrinkListItem(position - swipe_list.getFirstVisiblePosition());
                list_collect.remove(position);
               //String sql = "delete collect where itemTitle = 'list_collect.get(position).getItemTitle()' ";
                //odb.updateData()
                notifyDataSetChanged();
            }
        });
        holder.mUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(mContext,"置顶 position ---> " + position + "数据",Toast.LENGTH_SHORT).show();
                CollectBean collectBean = list_collect.get(position);
                shrinkListItem(position - swipe_list.getFirstVisiblePosition());
                list_collect.remove(position);
                list_collect.add(0,collectBean);
                notifyDataSetChanged();
            }
        });
        return swipeMenuLayout;

    }

    @Override
    public void onSliding(View view, int state) {
        if (swipeMenuLayout != null && swipeMenuLayout != view)
        {
            swipeMenuLayout.shrink();
        }
        if (state == SwipeMenuLayout.SLIDE_STATUS_ON)
        {
            swipeMenuLayout = (SwipeMenuLayout) view;
        }
    }


    class ViewHolder
    {
        ImageView mIvImg;
        TextView mContent;
        TextView mDelete;
        TextView mUp;

        public ViewHolder(SwipeMenuLayout layout)
        {
            mIvImg = (ImageView) layout.findViewById(R.id.collect_img);
            mContent = (TextView) layout.findViewById(R.id.collect_title);
            mDelete = (TextView) layout.findViewById(R.id.tv_delete);
            mUp = (TextView) layout.findViewById(R.id.tv_up);
        }
    }
    /**
     * 关闭(还原侧滑出的菜单)
     * @param position
     */
    public void shrinkListItem(int position) {
        View item = swipe_list.getChildAt(position);

        if (item != null) {
            try {
                ((SwipeMenuLayout) item).shrink();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

}
