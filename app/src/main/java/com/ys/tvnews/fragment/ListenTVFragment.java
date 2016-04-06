package com.ys.tvnews.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.ys.tvnews.R;
import com.ys.tvnews.activity.CollectActivity;
import com.ys.tvnews.activity.PushNewsActivity;
import com.ys.tvnews.application.MyApplication;
import com.ys.tvnews.bean.PushNews;

/**
 * Created by sks on 2015/12/8.
 */
public class ListenTVFragment extends Fragment{


    private NotificationManager manager;
    private Context mContext;
    private Button notify_btn;
    private PushNews pushNews;
    private DbUtils dbUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();
        dbUtils = MyApplication.getDbInstance();
        View view = inflater.inflate(R.layout.frag_listentv,container,false);
        notify_btn = (Button) view.findViewById(R.id.send_notify);
        pushNews = new PushNews();
        notify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationMethod();
            }
        });
        return view;
    }


    public void notificationMethod(){
        manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        NotificationCompat.Builder mBuidler = new NotificationCompat.Builder(getActivity());
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),R.layout.notification);
        remoteViews.setTextViewText(R.id.notify_title,"新闻推送");
        remoteViews.setTextViewText(R.id.notify_content, "Hello，Notification，I'm coming!!!");
       // remoteViews.setTextViewText(R.id.notify_time,System.currentTimeMillis()+"");
        remoteViews.setImageViewResource(R.id.notify_image, R.mipmap.notify_image);

        Intent intent = new Intent(mContext, PushNewsActivity.class);
        pushNews.setMessage("Hello，Notification，I'm coming!!!");
        try {
            dbUtils.save(pushNews);
        } catch (DbException e) {
            e.printStackTrace();
        }
        intent.putExtra("pushNews", pushNews);
        PendingIntent notify_intent = PendingIntent.getActivities(mContext, 0, new Intent[]{intent}, 0);

        mBuidler.setSmallIcon(R.mipmap.push_image).setTicker("您有新的消息推送哦").setContent(remoteViews).setShowWhen(true).setContentIntent(notify_intent);
        Notification notification = mBuidler.build();
        manager.notify(2, notification);
   //     remoteViews.removeAllViews();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.cancel(2);
    }

//    public void removeNotification(){
//        manager.cancel(2);
//    }
}
