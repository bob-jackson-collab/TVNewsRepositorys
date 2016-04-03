package com.ys.tvnews.service;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import com.ys.tvnews.R;
import com.ys.tvnews.application.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/3/22.
 */
public class UpdateService extends Service{

    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;

    private NotificationManager manager;
    private Notification updateNotification;
    private String download_url;
    private Intent updateIntent = null;// 下载完成
    private PendingIntent updatePendingIntent = null;// 在下载的时候

    RemoteViews remoteViews;
    private Dialog mDialog;

    private int download_max = 0;
    private int download_progress = 0;

    private View download_view;
    private ProgressBar progressBar;

    private File updateDir = null;// 文件目录
    private File updateFile = null;// 升级文件

    private Handler download_handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                progressBar.setProgress((Integer)msg.obj);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("Info","执行了服务中的Onstart方法");

       // download_url = intent.getStringExtra("download_url");
        download_url = "http://10.88.88.88:8080/gpay/Chamberlain.apk";
        new DownTask().execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class DownTask extends AsyncTask<Void,Void,Integer>{

        @Override
        protected void onPreExecute() {
            Log.e("-------","第一步");
            mDialog = new Dialog(UpdateService.this);
            mDialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
            mDialog.setTitle("下载APKing");
            download_view = LayoutInflater.from(UpdateService.this).inflate(R.layout.download_apk,null);
            progressBar = (ProgressBar) download_view.findViewById(R.id.download_progressBar);
            progressBar.setMax(100);
            mDialog.setContentView(download_view);
            mDialog.show();
//            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            updateNotification = new Notification();
//            updateNotification.tickerText= "有新的消息哦--";
//            updateNotification.when = System.currentTimeMillis();
//             remoteViews =  new RemoteViews(UpdateService.this.getPackageName(), R.layout.download_apk);
//            remoteViews.setImageViewResource(R.id.download_image,R.mipmap.app_icon);
//            remoteViews.setProgressBar(R.id.download_progressBar,100,0,false);
//            updateNotification.contentView = remoteViews;
//            updateIntent = new Intent(UpdateService.this, MyApplication.class);
//            updateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            updatePendingIntent = PendingIntent.getActivity(UpdateService.this,0,updateIntent,0);
//            updateNotification.contentIntent = updatePendingIntent;
//            manager.notify(100,updateNotification);
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Log.e("--------","第二部");
            //执行异步任务
            try {
                URL url = new URL(download_url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                if(connection.getResponseCode()==200){
                    download_max = connection.getContentLength();
                    InputStream inputStream = connection.getInputStream();
                    updateFile  = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"xinwen.apk");
                    FileOutputStream fos = new FileOutputStream(updateFile);
                    byte buffer[] = new byte[1024];
                    int length = 0;
                    int count = 0; //读取文件的大小
                    do{
                        length = inputStream.read();
                        if(length<0){
                            return DOWNLOAD_FINISH;
                        }
                        count = count + length;
                        download_progress = (count/ download_max)*100;
                        Log.e("download_progressss",download_progress+"");
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = download_progress;
                        download_handler.sendMessage(msg);
                    }while(true);
                }
            }catch(Exception e){
                e.printStackTrace();;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.e("-----------","第三部");
            if(integer.intValue()==DOWNLOAD_FINISH) {
                mDialog.dismiss();
                installAPK();
            }
            super.onPostExecute(integer);
        }

    }

       private void installAPK(){
           Intent i = new Intent(Intent.ACTION_VIEW);
           i.setDataAndType(Uri.parse("file://" + updateFile.toString()),
                   "application/vnd.android.package-archive");
           this.startActivity(i);
       }

    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();
    }

}
