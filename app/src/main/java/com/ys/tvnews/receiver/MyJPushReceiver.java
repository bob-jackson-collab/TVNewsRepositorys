package com.ys.tvnews.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.ys.tvnews.activity.PushNewsActivity;
import com.ys.tvnews.adapter.PushNewsAdapter;
import com.ys.tvnews.application.MyApplication;
import com.ys.tvnews.bean.PushNews;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by sks on 2015/12/23.
 */
public class MyJPushReceiver extends BroadcastReceiver{

    public final static String TAG = "MyJPushReceiver";
    private PushNews pushNews;
    private String message;  //推送的消息内容

    private DbUtils dbUtils;
    @Override
    public void onReceive(Context context, Intent intent) {

        pushNews = new PushNews();
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            //SDK 向 JPush Server 注册所得到的注册 全局唯一的 ID ，可以通过此 ID 向对应的客户端发送消息和通知。
            message = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e("jpush registeration_id",message +"=============>>>");

        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

            Log.e("info","执行了这边,收到了自定义消息");

            //获得推送消息的标题
            String titlte = bundle.getString(JPushInterface.EXTRA_TITLE);
            //获得推送消息
            message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            pushNews.setMessage(message);
            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            if(pushNews.getMessage()!=null){
                //将信息存储到数据库表中
                Intent news_intent = new Intent(context, PushNewsActivity.class);  //自定义打开的界面
                context.startActivity(news_intent);
            }
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

            System.out.println("收到了通知"+bundle.getString(JPushInterface.EXTRA_MESSAGE));

            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            dbUtils = MyApplication.getDbInstance();
            Log.e("info", bundle.getString(JPushInterface.EXTRA_ALERT));

            pushNews.setMessage(bundle.getString(JPushInterface.EXTRA_ALERT));
            try {
                if(pushNews!=null)
                dbUtils.save(pushNews);
            } catch (DbException e) {
                e.printStackTrace();
            }
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent news_intent = new Intent(context, PushNewsActivity.class);  //自定义打开的界面
            news_intent.putExtra("pushNews",pushNews);
            news_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(news_intent);
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}
