package com.ys.tvnews.receiver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ys.tvnews.R;
import com.ys.tvnews.activity.PersonalActivity;
import com.ys.tvnews.application.MyApplication;
import com.ys.tvnews.service.UpdateService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/3/22.
 */
public class UpdateVersionReceiver extends BroadcastReceiver{

    public static final String UPDATEACTION = "yangsong";
    private int  service_versionCode = 2;
    private int  client_verisonCode;
    private AlertDialog.Builder mDialog;
    private RemoteViews remoteViews;
    private NotificationManager manager;
    private int progress;  //下载的进度条
    private Notification notification;
    NotificationCompat.Builder mBuidler;
    private File download_fie;
    private FileOutputStream fos;
    private Handler updateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                Log.e("ooooo",msg.arg1+"");
                remoteViews.setProgressBar(R.id.download_progressBar,100,msg.arg1,false);
                manager.notify(100,notification);
            }
        }
    };;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("info","接受到了更新的通知");
        //开始比较app的版本
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
         //   PackageInfo packageInfo = MyApplication.getAppInstance().getPackageManager().getPackageInfo("com.ys.tvnews",0);
            client_verisonCode = packageInfo.versionCode;
            if(client_verisonCode<service_versionCode){ //当前客户端版本号小于服务器版本号
                showDialog(context);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showDialog(final Context context){
        mDialog = new AlertDialog.Builder(context);
        mDialog.setTitle("APP下载更新");
        mDialog.setMessage("您的版本过低，需要升级哦");
        mDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //推送开始下载最新的app版本
                startDownloadAPK(context);
//                Intent service_intent = new Intent(context, UpdateService.class);
//                service_intent.putExtra("download_url","http://10.88.88.88:8080/Chamberlain.apk");
//                context.startService(service_intent);
            }
        });
        mDialog.create().show();


    }

       private void startDownloadAPK(final Context context) {
           manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
           mBuidler = new NotificationCompat.Builder(context);
           remoteViews = new RemoteViews(context.getPackageName(), R.layout.download_apk);
           // remoteViews.setProgressBar(R.id.download_progressBar,100,0,true);
           remoteViews.setImageViewResource(R.id.download_image,R.mipmap.app_icon);
           Intent updateIntent = new Intent(context, PersonalActivity.class);
           PendingIntent updatePendingIntent = PendingIntent.getActivity(context, 0, updateIntent, 0);
           mBuidler.setSmallIcon(R.mipmap.icon_setting_my_fav).setTicker("您有新的app要下载哦").setContent(remoteViews).setContentIntent(updatePendingIntent);
           notification = mBuidler.build();
           manager.notify(100, notification);

           new Thread(new Runnable() {
               @Override
               public void run() {
                   try {
                       String directory = Environment.getExternalStorageDirectory().getAbsolutePath();
                       download_fie = new File(directory, "tv.apk");
                       if (!download_fie.isFile()) {
                           download_fie.createNewFile();
                       }
                       URL url = new URL("http://10.88.88.17:8081/gpay/360.apk");
                       //URL url = new URL("http://www.baidu.com");
                       HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                       connection.setDoInput(true);
                       connection.setDoOutput(true);
                       connection.setRequestProperty("Accept-Encoding", "identity");
                       connection.setRequestMethod("GET");
                       connection.setConnectTimeout(50000);
                       Log.e("==============",connection.getResponseCode()+"");
                       if (connection.getResponseCode() == 200) {
                           int download_max = connection.getContentLength();
                           Log.e("downloadMax",download_max+"");
                           InputStream inputStream = connection.getInputStream();
                           //  updateFile  = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"xinwen.apk");
                           fos = new FileOutputStream(download_fie);
                           byte buffer[] = new byte[1024];
                           int length = 0;
                           int count = 0; //读取文件的大小
                           do {
                               length = inputStream.read(buffer);
                               if (length <=0) {
                                   Intent intent = new Intent(Intent.ACTION_VIEW);
                                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                   intent.setDataAndType(Uri.parse("file://" + download_fie.getAbsolutePath()), "application/vnd.android.package-archive");
                                   context.startActivity(intent);
                                   break;
                               }
                               fos.write(buffer,0,length);
                               fos.flush();
                               count = count + length;
                               Log.e("count",count+"");
                               int download_progress = (int) (((double)count /(double) download_max) *  100);
                               Log.e("download_progressss", download_progress + "");
                               Message msg = Message.obtain();
                               msg.what = 1;
                               msg.arg1 = download_progress;
                               updateHandler.sendMessage(msg);
                           } while (true);
                       }
                   } catch (Exception e) {
                       Toast.makeText(context,"服务器出现了异常哦！",Toast.LENGTH_SHORT).show();
                       e.printStackTrace();
                   }finally{
                       if(fos!=null) {
                           try {
                               fos.close();
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }
                   }
               }
           }).start();
       }
}
