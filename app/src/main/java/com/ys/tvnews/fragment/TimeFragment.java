package com.ys.tvnews.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ys.tvnews.R;
import com.ys.tvnews.adapter.TimeAdapter;
import com.ys.tvnews.bean.OilPriceBean;
import com.ys.tvnews.views.MyProgressDialog;
import com.ys.tvnews.views.PinnedHeaderListView;
import com.ys.tvnews.views.TitleBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sks on 2015/12/8.
 */
public class TimeFragment extends  BaseFragment{

    private TitleBuilder titleBuilder;
    private HttpUtils httpUtils;
    private String oil_url = "http://apis.juhe.cn/cnoil/oil_city";
    private String appkey= "9fab7e3d50bc6a25383c31a9e853326f";
    private OilPriceBean oilPriceBean;
    private ExpandableListView expandableListView;
    private TimeAdapter mAdapter;
    private MyProgressDialog mDialog;
    //private List<CityBean>();
    //首数据集
    private List<String> parent;
    private HashMap<String,List<String>> mMap;
    private List<OilPriceBean.CityBean> list_city;

    @Override
    protected int loadLayout() {
        return R.layout.frag_time;
    }

    @Override
    protected void findView(View view) {
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandelistView);
    }

    @Override
    protected void regListener() {

    }

    @Override
    protected void loadData() {
        mDialog = MyProgressDialog.createLoadingDialog(getActivity(),"正在加载哦");
        titleBuilder = new TitleBuilder(mContext).setLeftImageRes(R.mipmap.base_header_back).setMiddleTitleText("石油价钱");
        httpUtils = new HttpUtils(5000);
        oilPriceBean = new OilPriceBean();
        RequestParams params = new RequestParams();
        params.addBodyParameter("key",appkey);
        httpUtils.send(HttpRequest.HttpMethod.POST, oil_url, params, new RequestCallBack<String>() {

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
                    Type type = new TypeToken<OilPriceBean>() {
                    }.getType();
                    oilPriceBean = new Gson().fromJson(responseInfo.result, type);
                    if (oilPriceBean.getError_code().equals("0")) {
                        Log.e("info", oilPriceBean.getList_cityBean().size() + "");
                        list_city = oilPriceBean.getList_cityBean();
                        if(list_city!=null) {
                            packageData();
                            mAdapter = new TimeAdapter(getActivity(),parent,mMap);
                            expandableListView.setAdapter(mAdapter);
                        }
                    }else{
                        showToast(oilPriceBean.getReason());
                    }
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
        expandableListView.setEmptyView(view);

    }

    private void packageData(){
        parent = new ArrayList<>();
        for(int i=0;i<list_city.size();i++){
           parent.add(list_city.get(i).getCity());
        }
        mMap = new HashMap<>();
        for(int i = 0;i<parent.size();i++){
            List<String> child  = new ArrayList<>();
            if(parent.get(i).equals(list_city.get(i).getCity())) {
                child.add(list_city.get(i).getB0());
                child.add(list_city.get(i).getB90());
                child.add(list_city.get(i).getB93());
                child.add(list_city.get(i).getB97());
            }
            mMap.put(parent.get(i),child);
        }
    }
    @Override
    public void requestServer() {

    }
}
