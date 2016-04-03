package com.ys.tvnews.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ys.tvnews.R;
import com.ys.tvnews.bean.OilPriceBean;
import com.ys.tvnews.views.PinnedHeaderListView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/15.
 */
public class TimeAdapter extends BaseExpandableListAdapter{

    private List<String>list_city;
    private Map<String,List<String>> mMap;
    private Context context;

    public TimeAdapter(Context context,List<String>list_city,Map<String,List<String>>mMap){
        this.context = context;
        this.list_city = list_city;
        this.mMap = mMap;
    }
    @Override
    public int getGroupCount() {

        return list_city.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mMap.get(list_city.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list_city.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mMap.get(list_city.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView city_group = null;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.oilprice_group,parent,false);
        }
        city_group = (TextView) convertView.findViewById(R.id.city_group);
        Log.e("info",list_city.get(groupPosition));
        city_group.setText(list_city.get(groupPosition)+"油价");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
       TextView oilPrice_child = null;
       if(convertView==null){
           convertView = LayoutInflater.from(context).inflate(R.layout.oilprice_child,parent,false);
       }
        oilPrice_child = (TextView) convertView.findViewById(R.id.tv_oilprice_child);

        Log.e("info",mMap.get(list_city.get(groupPosition)).toString());
      //  Log.e("info",mMap.get(groupPosition).get(childPosition));
        if(childPosition==0){
            oilPrice_child.setText("B0:  "+mMap.get(list_city.get(groupPosition)).get(childPosition));
        }else if(childPosition == 1){
            oilPrice_child.setText("B90:  "+mMap.get(list_city.get(groupPosition)).get(childPosition));
        }else if(childPosition == 2){
            oilPrice_child.setText("B93:  "+mMap.get(list_city.get(groupPosition)).get(childPosition));
        }else if(childPosition == 3){
            oilPrice_child.setText("B97:  "+mMap.get(list_city.get(groupPosition)).get(childPosition));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
