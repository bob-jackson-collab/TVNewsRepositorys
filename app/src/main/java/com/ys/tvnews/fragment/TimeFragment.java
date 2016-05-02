package com.ys.tvnews.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Shader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ys.tvnews.R;
import com.ys.tvnews.activity.NewsDetailActivity;
import com.ys.tvnews.adapter.TimeAdapter;
import com.ys.tvnews.adapter.TimeNewsAdapter;
import com.ys.tvnews.bean.OilPriceBean;
import com.ys.tvnews.bean.TimeNewsBean;
import com.ys.tvnews.httpurls.HttpUrl;
import com.ys.tvnews.views.MyProgressDialog;
import com.ys.tvnews.views.PinnedHeaderListView;
import com.ys.tvnews.views.TitleBuilder;
import com.ys.tvnews.views.TitleViews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sks on 2015/12/8.
 */
public class TimeFragment extends  BaseFragment implements AdapterView.OnItemClickListener{

    private TitleBuilder titleBuilder;
    private TitleViews mTitleView;
    private HttpUtils httpUtils;
    private TimeNewsAdapter mAdapter;
    private MyProgressDialog mDialog;
    private List<TimeNewsBean> list_time_news;
    private TimeNewsBean timeNewsBean;
    private ListView listView;

    @Override
    protected int loadLayout() {
        return R.layout.frag_time;
    }

    @Override
    protected void findView(View view) {
      listView = (ListView) view.findViewById(R.id.time_listView);
        mTitleView = (TitleViews) view.findViewById(R.id.time_titles);
    }

    @Override
    protected void regListener() {
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void loadData() {
        mDialog = MyProgressDialog.createLoadingDialog(getActivity(),"正在加载哦");
        list_time_news = new ArrayList<>();
        mTitleView.setTitle("时间新闻");
        mTitleView.setLeftVisible(View.GONE);
        httpUtils = new HttpUtils(5000);
        RequestParams params = new RequestParams();
        httpUtils.send(HttpRequest.HttpMethod.GET, HttpUrl.TIMENEWSURL, params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                mDialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("info",responseInfo.result);
                if(responseInfo.result!=null) {
                    mDialog.dismiss();
                    packageData(responseInfo.result);
                }else{
                    showToast("网络连接失败！");
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mDialog.dismiss();
                Log.e("failure","网络请求失败");
            }
        });

        View view = View.inflate(getActivity(),R.layout.empty_view,null);
        listView.setEmptyView(view);

    }

    private void packageData(String content){
        try {
            JSONObject jsonObject = new JSONObject(content);
            if("0".equals(jsonObject.getString("code"))){
                JSONObject data  = jsonObject.getJSONObject("data");
                JSONArray dataArray = data.getJSONArray("data");
                Log.e("dataArray",dataArray.length()+"");
                list_time_news = JSON.parseArray(dataArray.toString(),TimeNewsBean.class);
                mAdapter = new TimeNewsAdapter(getActivity());
                listView.setAdapter(mAdapter);
                mAdapter.setList_time_news(list_time_news);
            }else{
                showToast("网络数据请求错误");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void requestServer() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        timeNewsBean = list_time_news.get(position);
        intent.putExtra("timeNewsBean",timeNewsBean);
        getActivity().startActivity(intent);
    }
}
