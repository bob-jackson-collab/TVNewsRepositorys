package com.ys.tvnews.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.ys.tvnews.R;
import com.ys.tvnews.application.MyApplication;
import com.ys.tvnews.bean.AdvBean;
import com.ys.tvnews.bean.CollectBean;
import com.ys.tvnews.bean.NewsBean;
import com.ys.tvnews.bean.TimeNewsBean;
import com.ys.tvnews.bean.TopicNews;
import com.ys.tvnews.sqlite.DBHelper;
import com.ys.tvnews.sqlite.OperateDB;
import com.ys.tvnews.utils.ShareUtils;


/**
 * Created by sks on 2015/12/24.
 */
public class NewsDetailActivity extends Activity implements View.OnClickListener{

    private WebView news_webView;
    private AdvBean advNews;
    private TopicNews topic_news;
    private NewsBean news;
    private CollectBean collectBean;
    private TimeNewsBean timeNewsBean;
    //private String url =  "http://m.news.cntv.cn/c/art/index.shtml?id=ARTI1450793452777103&isfromapp=1";
    private Intent intent;
    private OperateDB operateDB;
    private String detailUrl;
    private ProgressBar progressBar;
    private DBHelper dbHelper;

    private ImageView item_detail_back,item_detail_share,item_detail_fav,item_detail_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_item_detail);
        //初始化ShareSDK
       // ShareSDK.initSDK(this);
        intent = getIntent();
        news = (NewsBean) intent.getSerializableExtra("news");
        advNews = (AdvBean) intent.getSerializableExtra("advNews");
        topic_news = (TopicNews) intent.getSerializableExtra("topic_news");
        collectBean = (CollectBean) intent.getSerializableExtra("collectBean");
        timeNewsBean = (TimeNewsBean) intent.getSerializableExtra("timeNewsBean");
        if(news != null){
            detailUrl = news.getDetailUrl();
        }else if(advNews != null){
            detailUrl = advNews.getDetailUrl();
        }else if(topic_news!=null){
            detailUrl = topic_news.getWeburl();
        }else if(collectBean!=null){
            detailUrl = collectBean.getDetailUrl();
        }else if(timeNewsBean!=null){
            detailUrl = timeNewsBean.getLink();
        }
        initView();
        setWebSettings();
        setListener();
        loadData();
        news_webView.loadUrl(detailUrl);
        news_webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("info","网页加载错误");
                //网页加载错误的回调
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        news_webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // 加载中
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                    // 网页加载完成
                    Log.e("info","网页加载完成");

                } else if (View.INVISIBLE == progressBar.getVisibility()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                super.onProgressChanged(view,newProgress);
                }


        });
    }

    private void initView(){
        news_webView = (WebView) findViewById(R.id.news_webView);
        item_detail_back = (ImageView) findViewById(R.id.item_detail_back);
        item_detail_share = (ImageView) findViewById(R.id.item_detail_share);
        item_detail_fav = (ImageView) findViewById(R.id.item_detail_fav);
        item_detail_edit = (ImageView) findViewById(R.id.item_detail_edit);
        progressBar = (ProgressBar) findViewById(R.id.web_progress_bar);
        //item_detail_fav.setEnabled(false);

    }

    private void setWebSettings(){
        WebSettings settings = news_webView.getSettings();
        settings.setPluginState(WebSettings.PluginState.ON);
        news_webView.setVisibility(View.VISIBLE);
        news_webView.getSettings().setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        news_webView.requestFocus();
        //设置webView优先使用缓存
        news_webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

    }

    private void setListener(){
        item_detail_fav.setOnClickListener(this);
        item_detail_share.setOnClickListener(this);
        item_detail_back.setOnClickListener(this);
        item_detail_edit.setOnClickListener(this);
    }

    private void loadData(){
        dbHelper = new DBHelper(NewsDetailActivity.this);
        operateDB = MyApplication.getInstance();
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            if(news_webView.canGoBack())
            {
                news_webView.goBack();//返回上一页面
                return true;
            }
            else
            {
                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.item_detail_back:
               this.finish();
               break;
           case R.id.item_detail_edit:
               Intent edit_intent = new Intent(NewsDetailActivity.this,CommentActivity.class);
               if(ShareUtils.getUserName(NewsDetailActivity.this)!=null){
                   startActivity(edit_intent);
               }else{
                   Toast.makeText(NewsDetailActivity.this,"您还没有登录哦",Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(NewsDetailActivity.this,LoginActivity.class));
               }
               break;
           case R.id.item_detail_share:
               showShare();
               break;
           case R.id.item_detail_fav:
               String imgUrl1 = null;
               String detailUrl = null;
               String itemTitle = null;
               if(news!=null) {
                    imgUrl1 = news.getImgUrl1();
                    detailUrl = news.getDetailUrl();
                    itemTitle = news.getItemTitle();
               }
               if(timeNewsBean!=null){
                    imgUrl1 = timeNewsBean.getListImageUrl();
                    detailUrl = timeNewsBean.getLink();
                    itemTitle = timeNewsBean.getDescription();
               }
               Log.e("info", "点击了收藏");

               ContentValues values = new ContentValues();
               values.put("imgUrl1",imgUrl1);
               values.put("itemTitle",itemTitle);
               values.put("detailUrl",detailUrl);

               SQLiteDatabase db = dbHelper.getWritableDatabase();

               long flag = db.insert("collect",null,values);

               if(flag>0){
                   Toast.makeText(NewsDetailActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                   item_detail_fav.setEnabled(true);
               }
               //将收藏的数据添加到数据库中
               //String sql = "insert into collect vlaues(?,?,?)";
//               String sql = "insert into collect values('"+imgUrl1+"','"+itemTitle+"','"+detailUrl+"')";
//              if(operateDB.updateData(sql,new String[]{imgUrl1,itemTitle,detailUrl})){
//
//              }
               break;
           default:break;
       }
    }

    private void showShare(){
        Log.e("info", "执行了点击事件");

        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                };
        new ShareAction(this).setDisplayList( displaylist )
                .withText( "呵呵" )
                .withTitle("title")
                .withTargetUrl("http://www.baidu.com")
                .setListenerList(umShareListener,umShareListener)
                .setShareboardclickCallback(shareBoardlistener)
                .open();

    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(NewsDetailActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(NewsDetailActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(NewsDetailActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

        @Override
        public void onclick(SnsPlatform snsPlatform,SHARE_MEDIA share_media) {
            new ShareAction(NewsDetailActivity.this).setPlatform(share_media).setCallback(umShareListener)
                    .withText("多平台分享")
                    .share();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
