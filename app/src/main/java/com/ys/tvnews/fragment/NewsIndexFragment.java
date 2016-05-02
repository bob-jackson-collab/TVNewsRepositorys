package com.ys.tvnews.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.ys.tvnews.R;
import com.ys.tvnews.activity.NewsDetailActivity;
import com.ys.tvnews.adapter.AdvPagerAdapter;
import com.ys.tvnews.adapter.NewsAdapter;
import com.ys.tvnews.adapter.TopicNewsAdapter;
import com.ys.tvnews.bean.AdvBean;
import com.ys.tvnews.bean.NewsBean;
import com.ys.tvnews.bean.TopicNews;
import com.ys.tvnews.httpurls.HttpUrl;
import com.ys.tvnews.views.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by sks on 2015/12/8.
 */
public class NewsIndexFragment extends Fragment{

    /**
     * 下拉刷新和上拉刷新的标志
     * 1、下拉刷新
     * 2、上拉刷新
     */
    private  String pull_down_refresh_flag = "1";    //下拉刷新

    private  String pull_up_refresh_flag = "2";      //上啦加载

    private int toutuNum = 3;  //分页变量

    private Context mContext;
    private View view;
    private PullToRefreshListView listView;
    private View headerView;
    private LinearLayout linear_dots;

    private MyProgressDialog mDialog;

    private TextView tv_adv;  //广告标题栏
    private ViewPager adv_viewPager;

    private int type;   //通过判断传过来的参数类型来返回不同的视图

    private AdvPagerAdapter advPagerAdapter;   //广告适配器
    private NewsAdapter newsAdapter;           //新闻内容适配器
    private List<ImageView> list_adv_image;    //广告图的集合
    private List<ImageView> list_dots;         //广告dots集合

    private List<AdvBean> list_advBean;
    private List<NewsBean> list_newsBean;
    private List<NewsBean> list_all_newsBean;
    List<TopicNews> list_topic_news = new ArrayList<>();

    private BitmapUtils bitmapUtils;
    private HttpUtils httpUtils = new HttpUtils();

    private String news_url;  //新闻的url
/*
    private ScheduledExecutorService scheduledExecutorService;
    private int schedule_page;
    private Handler schedule_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==2){
                adv_viewPager.setCurrentItem((Integer)msg.obj);
            }
        }
    };*/


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_index_fragment, container, false);
        //初始化context对象
        mContext = getParentFragment().getActivity();
        listView = (PullToRefreshListView) view.findViewById(R.id.new_listView);
        loadData();
        setListViewProperties();
       // newsAdapter = new NewsAdapter(mContext,bitmapUtils);
        Bundle bundle = getArguments();
        if(bundle!=null){
            type = bundle.getInt("type");
            Log.e("type", type + "-------->");
                switch (type){
                    case 0:
                        //要闻
                        news_url =  HttpUrl.news_host+"controller=list&action=getHandDataInfoNew&handdata_id=TDAT1372928688333145&n1=3&n2=20&toutuNum=3";
                        break;
                    case 1:
                        //时政
                        news_url = HttpUrl.news_host+"controller=list&action=getHandDataInfoNew&handdata_id=TDAT1414661661499136&n1=1&n2=20&toutuNum=1";
                        break;
                    case 2:
                        //军事
                        news_url = HttpUrl.news_host+"controller=list&action=getHandDataInfoNew&handdata_id=TDAT1383125722766236&n1=1&n2=20&toutuNum=1";
                        break;
                    case 3:
                        //体育
                        news_url = HttpUrl.news_host+"controller=list&action=getHandDataInfoNew&handdata_id=TDAT1383126274316527&n1=1&n2=20&toutuNum=1";
                        break;
                    case 4:
                        //财经
                        news_url = HttpUrl.news_host+"controller=list&action=getHandDataInfoNew&handdata_id=TDAT1383126577835657&n1=1&n2=20&toutuNum=1";
                        break;
                    case 5:
                        //社会
                        news_url = HttpUrl.news_host+"controller=list&action=getHandDataInfoNew&handdata_id=TDAT1383126850500788&n1=1&n2=20&toutuNum=1";
                        break;
                    case 6:
                        //图解新闻
                        news_url = HttpUrl.news_host+"controller=list&action=morelist&handdata_id=TDAT1373943452289972&n=20";

                        break;
                    case 7:
                        //话题投票
                        news_url = "http://common.qr.cntv.cn/v2/NewsVote/getlist?pagenum=1&pagesize=20&app=news";
                        break;
                    default:
                        break;
                }
            setView(news_url);
            //设置listView的下拉刷新
            listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                @Override
                public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
                    if (refreshView.isHeaderShown()) {
                        Log.e("info", "执行了下拉刷新");
                        //setListViewLayoutProxy(true,false);
                        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getRefreshTime());
                        pull_down_refresh_flag = "3";
                        final Handler refresh_down_handler = new Handler() {

                            @Override
                            public void handleMessage(Message msg) {
                                if (msg.what == 1) {
                                    refreshView.onRefreshComplete();
                                }
                            }
                        };

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //Thread.sleep(3000);
                                    setView(news_url);
                                    Message msg = Message.obtain();
                                    msg.what = 1;
                                    msg.obj = 1;
                                    refresh_down_handler.sendMessage(msg);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    if (refreshView.isFooterShown()) {
                        Log.e("info", "执行了上啦加载");
                        pull_up_refresh_flag = "4";
                        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(getRefreshTime());
                        final Handler refresh_up_handler = new Handler() {

                            @Override
                            public void handleMessage(Message msg) {
                                if (msg.what == 1) {
                                    refreshView.onRefreshComplete();
                                }
                            }
                        };
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //Thread.sleep(3000);
                                    setView(news_url);
                                    Message msg = Message.obtain();
                                    msg.what = 1;
                                    msg.obj = 1;
                                    refresh_up_handler.sendMessage(msg);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                }
            });
        }
        return view;
    }

    public View getHeaderView(final List<AdvBean> list_advbean){

        headerView = LayoutInflater.from(mContext).inflate(R.layout.news_header, null);
        adv_viewPager = (ViewPager) headerView.findViewById(R.id.adv_viewPager);
        linear_dots = (LinearLayout) headerView.findViewById(R.id.linear_dots);
        final TextView tv_adv = (TextView) headerView.findViewById(R.id.tv_adv);
        /**
         * 当广告图只有一张图的时候 不实现轮播
         */
        if(list_advbean.size()==1){
           // linear_dots.setVisibility(View.GONE);
            ImageView adv_imageView = new ImageView(mContext);
            adv_imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            BitmapDisplayConfig config = new BitmapDisplayConfig();
            config.setLoadFailedDrawable(getResources().getDrawable(R.mipmap.icon_loading_text));
            config.setLoadingDrawable(getResources().getDrawable(R.mipmap.icon_loading_text));
            bitmapUtils.display(adv_imageView, list_advbean.get(0).getItemImage(), config);
            list_adv_image.add(adv_imageView);
            advPagerAdapter = new AdvPagerAdapter(list_adv_image);
            adv_viewPager.setAdapter(advPagerAdapter);
            tv_adv.setText(list_advbean.get(0).getItemTitle());
            return headerView;
        }else  if(list_advbean.size()>1){        //如果广告集合的数据大于1
            //设置广告轮播的监听
            adv_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    Log.e("info","执行了监听方法");
                    for(int i= 0;i<linear_dots.getChildCount();i++){
                        if(linear_dots.getChildAt(i)!=null) {
                            linear_dots.getChildAt(i).setEnabled(false);
                        }
                    }
                    linear_dots.getChildAt(position%list_adv_image.size()).setEnabled(true);
                    //设置广告的标题
                    tv_adv.setText(list_advbean.get(position % list_adv_image.size()).getItemTitle());

                }

                @Override
                public void onPageScrollStateChanged(int state) {
               }
            });

            //添加轮播点的图片
            for (int i = 0; i < list_advbean.size(); i++) {
                //tv_adv.setText(list_advbean.get(i).getItemTitle());
                final ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                BitmapDisplayConfig config = new BitmapDisplayConfig();
                config.setLoadFailedDrawable(getResources().getDrawable(R.mipmap.icon_loading_text));
                config.setLoadingDrawable(getResources().getDrawable(R.mipmap.icon_loading_text));
                bitmapUtils.display(imageView, list_advbean.get(i).getItemImage(), config);
                imageView.setTag(i);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdvBean advBean = list_advbean.get((Integer) imageView.getTag());
                        Intent intent = new Intent(mContext, NewsDetailActivity.class);
                        intent.putExtra("advNews",advBean);
                        mContext.startActivity(intent);
                    }
                });
                list_adv_image.add(imageView);
            }

                for (int i = 0; i < list_advbean.size(); i++) {
                    ImageView imageView = new ImageView(mContext);
                    imageView.setBackgroundResource(R.drawable.dots_selected);
                    imageView.setEnabled(false);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20,20);
                    params.setMargins(20,0,0,0);
                    imageView.setLayoutParams(params);
                    linear_dots.addView(imageView);
                   // list_dots.add(imageView);
                }
              //  list_dots.get(0).setEnabled(true);
                linear_dots.getChildAt(0).setEnabled(true);

            //给广告添加数据源
            Log.e("list_adv_image.size()",+list_adv_image.size()+"=======");
            if (list_adv_image.size() > 0) {
                advPagerAdapter = new AdvPagerAdapter(list_adv_image);
                adv_viewPager.setAdapter(advPagerAdapter);
            }
            adv_viewPager.setCurrentItem(list_adv_image.size()*100);
            return headerView;
        }else{
            return null;
        }
    }

    public void setView(String url){
        RequestParams params = new RequestParams();
        httpUtils.configRequestThreadPoolSize(3);
        httpUtils.configTimeout(5000);
        if(url.equals("http://common.qr.cntv.cn/v2/NewsVote/getlist?pagenum=1&pagesize=20&app=news")){
            Log.e("info", "执行到了话题投票这一块");
            httpUtils.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {

                @Override
                public void onStart() {
                    super.onStart();
                    mDialog.show();
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if (responseInfo.result != null) {
                        listView.onRefreshComplete();
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(responseInfo.result);
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray list = data.optJSONArray("list");
                            //  JSONArray bigImage = data.optJSONArray("bigImg");
                            if (list != null) {
                                list_topic_news = com.alibaba.fastjson.JSONArray.parseArray(list.toString(), TopicNews.class);
                                TopicNewsAdapter adapter = new TopicNewsAdapter(mContext, bitmapUtils);
                                listView.setAdapter(adapter);
                                adapter.setList(list_topic_news);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                }


            });
        }else {
            Log.e("---->", "接收到了参数");
        httpUtils.send(HttpRequest.HttpMethod.POST,url, params, new RequestCallBack<String>() {

            @Override
            public void onStart() {
                 //显示加载的对话框
                 mDialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("iiii","请求数据成功");
                if (responseInfo != null) {
                    Log.e("info", responseInfo.result);
                    if(mDialog.isShowing()){
                        mDialog.dismiss();
                    }
                    try {

                        JSONObject jsonObject = new JSONObject(responseInfo.result);
                        JSONArray itemList = null;
                        JSONObject data = null;
                        JSONArray bigImage = null;
                        if("6".equals(type+"")){
                             itemList = jsonObject.optJSONArray("itemList");
                        }else {
                             data = jsonObject.getJSONObject("data");
                             itemList = data.optJSONArray("itemList");
                             bigImage = data.optJSONArray("bigImg");
                        }

                        //分别得到itemList 和bigImage对象
                       // JSONArray bigImage = jsonObject.getJSONArray("bigImage");
                        if(itemList!=null) {
                            //得到新闻的结合
                            list_newsBean = JSON.parseArray(itemList.toString(), NewsBean.class);
                            //将刷新的数据添加到总list集合中
                            list_all_newsBean.addAll(list_newsBean);
                            Log.e("---------newsBean",list_newsBean.size()+"=====");
                            for(int i = 0;i<itemList.length();i++){
                                NewsBean newsBean = list_newsBean.get(i);
                                JSONObject object = itemList.getJSONObject(i);
                                JSONObject itemImage = object.getJSONObject("itemImage");
                                newsBean.setImgUrl1(itemImage.getString("imgUrl1"));
                            }

                            for(int i = 0;i<list_newsBean.size();i++){
                                if(list_newsBean.get(i).getItemTitle().equals("如您看到此提示，请升级客户端到最新版本")){
                                    list_newsBean.remove(i);
                                }
                            }
                            for(int i = 0;i<list_all_newsBean.size();i++){
                                if(list_all_newsBean.get(i).getItemTitle().equals("如您看到此提示，请升级客户端到最新版本")){
                                    list_all_newsBean.remove(i);
                                }
                            }
                        }
                        if(bigImage!=null) {
                            //得到广告的集合
                            list_advBean = JSON.parseArray(bigImage.toString(), AdvBean.class);
                            Log.e("------adbBean",list_advBean.size()+"=====");
                            //由于最后一个banner图是无效的 所以移除它
                            list_advBean.remove(list_advBean.get(list_advBean.size()-1));
                            Log.e("--------advBean",list_advBean.size()+"");
                            if(list_advBean.size()>0) {
                                headerView = getHeaderView(list_advBean);
                            }
                        }

                        //得到pullRefreshListView中的listView
                        ListView refreshableView = listView.getRefreshableView();
                        refreshableView.setAdapter(newsAdapter);

                        Log.e("pull_up_refresh_flag",pull_up_refresh_flag+"------------------------.>");
                        //如果是下拉刷新 则用setList方法更新数据源 如果是上啦加载，则用addList方法更新数据源
                        if("1".equals(pull_down_refresh_flag)||"3".equals(pull_down_refresh_flag)){
                            newsAdapter.setList(list_newsBean);
                        }

                        if("2".equals(pull_up_refresh_flag)||"4".equals(pull_up_refresh_flag)) {
                            Log.e("info","shiyongle addList方法");
                            //给新闻内容设置数据源
                            newsAdapter.addList(list_all_newsBean);
                        }
                        //ListView添加头部视图
                        if(headerView!=null) {
                            if("1".equals(pull_down_refresh_flag) && "2".equals(pull_up_refresh_flag)) {
                                Log.e("info","添加了头部");
                                refreshableView.addHeaderView(headerView);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(mContext,"请求数据失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(),"网络异常",Toast.LENGTH_SHORT).show();
                if(mDialog.isShowing()){
                    mDialog.dismiss();
                }
            }

        });
        }
    }

    private void loadData(){
        list_all_newsBean = new ArrayList<>();
        bitmapUtils = new BitmapUtils(mContext);
        newsAdapter = new NewsAdapter(mContext,bitmapUtils);
        list_adv_image = new ArrayList<>();
        list_dots = new ArrayList<>();
        mDialog = (MyProgressDialog) MyProgressDialog.createLoadingDialog(mContext,"loading");
    }
    private void setListViewProperties(){
        View empty_view = LayoutInflater.from(getActivity()).inflate(R.layout.empty_view, null);
        listView.setEmptyView(empty_view);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout loadingLayoutProx1 = listView.getLoadingLayoutProxy(true,false);
        loadingLayoutProx1.setRefreshingLabel("正在刷新哦");
        loadingLayoutProx1.setPullLabel("下拉刷新");
        loadingLayoutProx1.setReleaseLabel("释放刷新");
        loadingLayoutProx1.setLoadingDrawable(getResources().getDrawable(R.mipmap.xialashuaxin));

        ILoadingLayout loadingLayoutProx2 = listView.getLoadingLayoutProxy(false,true);
        loadingLayoutProx2.setRefreshingLabel("正在刷新哦");
        loadingLayoutProx2.setPullLabel("上啦加载");
        loadingLayoutProx2.setReleaseLabel("释放刷新");
        loadingLayoutProx2.setLoadingDrawable(getResources().getDrawable(R.mipmap.xialashuaxin));


        ILoadingLayout loadingLayoutProxy1 = listView.getLoadingLayoutProxy(true,false);
    }

    /**
     * 得到当前刷新的时间
     */

    private String getRefreshTime(){
        String label = DateUtils.formatDateTime(mContext, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        return  label;
    }
}
